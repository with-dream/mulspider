package com.example.core.db.rocksdb;

import com.example.core.db.rocksdb.exception.RocksQueueDequeueException;
import com.example.core.db.rocksdb.exception.RocksQueueEnqueueException;
import com.example.core.db.rocksdb.exception.RocksQueueException;
import com.example.core.db.rocksdb.exception.RocksQueueRemoveHeadException;
import com.example.core.db.rocksdb.jmx.RocksQueueMetric;
import com.example.core.db.rocksdb.util.Bytes;
import com.example.core.utils.D;
import org.rocksdb.ColumnFamilyHandle;
import org.rocksdb.RocksDBException;
import org.rocksdb.WriteBatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

public class RocksQueue {
    private static final Logger log = LoggerFactory.getLogger(RocksQueue.class);

    private static final byte[] HEAD = Bytes.stringToBytes("head");
    private static final byte[] TAIL = Bytes.stringToBytes("tail");
    private static final long MAX_SIZE = Long.MAX_VALUE - 10;
//    private static final long MAX_SIZE = 800000;

    private static final Object TAIL_LOCK = new Object();
    private static final Object HEAD_LOCK = new Object();

    private final String queueName;

    private final AtomicLong head = new AtomicLong();
    private final AtomicLong tail = new AtomicLong();

    private final ColumnFamilyHandle cfHandle;
    private final ColumnFamilyHandle indexCfHandle;
    private final RocksStore store;
    private final RocksQueueMetric rocksQueueMetric;

    public RocksQueue(final String queueName, final RocksStore store) {
        this.queueName = queueName;
        this.store = store;

        this.cfHandle = store.createColumnFamilyHandle(queueName);
        this.indexCfHandle = store.createColumnFamilyHandle(getIndexColumnFamilyName(queueName));

        this.head.set(getIndexId(HEAD, 0));
        this.tail.set(getIndexId(TAIL, 0));

        this.rocksQueueMetric = new RocksQueueMetric(this, this.store.getDatabase());
        this.rocksQueueMetric.register();
        this.rocksQueueMetric.onInit();
    }

    public long getIndexId(byte[] key, long defaultValue) {
        byte[] value = store.getCF(key, indexCfHandle);

        if (value == null) {
            return defaultValue;
        }
        return Bytes.byteToLong(value);
    }

    public long enqueue(byte[] value) throws RocksQueueException {
        if (isFull())
            throw new RuntimeException("this queue is full:" + queueName);
        long index;
        if (tail.get() + 1 >= MAX_SIZE) {
            synchronized (TAIL_LOCK) {
                if (tail.get() + 1 >= MAX_SIZE)
                    tail.set(0);
                else
                    tail.incrementAndGet();
                index = tail.get();
            }
        } else
            index = tail.incrementAndGet();

        try (final WriteBatch writeBatch = new WriteBatch()) {
            final byte[] indexId = Bytes.longToByte(index);
            writeBatch.put(cfHandle, indexId, value);
            writeBatch.put(indexCfHandle, TAIL, indexId);
            store.write(writeBatch);

            this.rocksQueueMetric.onEnqueue(value.length);
        } catch (RocksDBException e) {
            tail.decrementAndGet();
            log.error("Enqueue {} fails", index, e);
            throw new RocksQueueEnqueueException(store.getRockdbLocation(), e);
        }

        return index;
    }

    /**
     * Get the head and remove it from queue
     *
     * @return
     */
    public QueueItem dequeue() throws RocksQueueException {
        if (isEmpty())
            return null;

        long index;
        if (head.get() + 1 >= MAX_SIZE)
            synchronized (HEAD_LOCK) {
                if (head.get() + 1 >= MAX_SIZE)
                    head.set(0);
                else
                    head.incrementAndGet();
                index = head.get();
            }
        else
            index = head.incrementAndGet();

        QueueItem item = consume(index);
        try {
            removeHead();
        } catch (RocksQueueException e) {
            throw new RocksQueueDequeueException(store.getRockdbLocation(), e);
        }
        if (item != null && item.getValue() != null) {
            this.rocksQueueMetric.onDequeue(item.getValue().length);
        }
        return item;
    }

    /**
     * Get the head of queue, in case there will have many deleted tombstones,
     * the final return index maybe bigger than the startId.
     *
     * @return
     */
    public QueueItem consume(long index) {
        byte[] key = Bytes.longToByte(index);
        byte[] res = store.getCF(key, cfHandle);

        this.rocksQueueMetric.onConsume();

        return new QueueItem(index, res);
    }

    /**
     * Remove the head from queue
     *
     * @return
     */
    public void removeHead() throws RocksQueueException {
        if (this.getSize() <= 0) {
            return;
        }

        try (final WriteBatch writeBatch = new WriteBatch()) {
            writeBatch.delete(cfHandle, Bytes.longToByte(head.get()));
            writeBatch.put(indexCfHandle, HEAD, Bytes.longToByte(head.get()));
            store.write(writeBatch);
        } catch (RocksDBException e) {
            log.error("Remove head {} failed.", head.get());
            throw new RocksQueueRemoveHeadException(store.getRockdbLocation(), e);
        }
    }

    public void close() {
        cfHandle.close();
        indexCfHandle.close();

        this.rocksQueueMetric.onClose();
    }

    private String getIndexColumnFamilyName(String queueName) {
        return new StringBuilder()
                .append("_")
                .append(queueName)
                .toString();
    }

    public boolean isEmpty() {
        return head.get() == tail.get();
    }

    public boolean isFull() {
        return head.get() == (tail.get() + 1) % MAX_SIZE;
    }

    public long getSize() {
        if (tail.get() > head.get())
            return tail.get() - head.get();
        else if (tail.get() < head.get())
            return MAX_SIZE - head.get() + tail.get();
        return 0;
    }

    public long getHeadIndex() {
        return head.get();
    }

    public long getTailIndex() {
        return tail.get();
    }

    public long getTailIndexReal() {
        return getIndexId(TAIL, -1);
    }

    public long getHeadIndexReal() {
        return getIndexId(HEAD, -1);
    }

    public long approximateSize() {
        return getIndexId(TAIL, 0) - getIndexId(HEAD, 0);
    }

    public String getQueueName() {
        return this.queueName;
    }

    public RocksQueueMetric getRocksQueueMetric() {
        return this.rocksQueueMetric;
    }
}
