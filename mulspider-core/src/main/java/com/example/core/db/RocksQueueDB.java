package com.example.core.db;

import com.example.core.db.rocksdb.*;
import com.example.core.db.rocksdb.exception.RocksQueueException;
import com.example.core.models.Request;
import com.example.core.models.Response;
import com.example.core.models.Result;
import com.example.core.models.Task;
import com.example.core.utils.*;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.SerializationUtils;

import java.util.HashMap;
import java.util.Map;

public class RocksQueueDB extends DBManager {
    private static final String prefix = "rock_";
    public static final int priority_high = 0;
    public static final int priority_middle = 1;
    public static final int priority_low = 2;
    private static final int[] priority_list = {priority_high, priority_middle, priority_low};
    private RocksStore rocksStore;
    private RocksQueue[] requestQ, responseQ, resultQ;
    private RocksSet duplicateSet, cacheSet;

    private int coverPriority(int priority) {
        if (!config.enablePriority)
            return 1;

        if (priority < 300)
            return priority_high;
        else if (priority < 600)
            return priority_middle;
        else
            return priority_low;
    }

    @Override
    public boolean init(DBConfig config) {
        super.init(config);
        StoreOptions storeOptions = StoreOptions.builder()
                .directory("./file/rockdb")
                .database(prefix + config.appName)
                .build();
        rocksStore = new RocksStore(storeOptions);

        int count = 3;
        requestQ = new RocksQueue[count];
        responseQ = new RocksQueue[count];
        resultQ = new RocksQueue[count];

        if (config.duplicate == Constant.DUPLICATE_BF)
            System.err.println("rocksDB do not support bloomfilter, use md5");
        if (config.duplicate == Constant.DUPLICATE_MD5
                || config.duplicate == Constant.DUPLICATE_BF)
            duplicateSet = rocksStore.createSet("duplicate");
        cacheSet = rocksStore.createSet("cacheSet");

        if (count == 1)
            initQ(priority_middle);
        else
            for (int i = 0; i < count; i++)
                initQ(i);

        return true;
    }

    @Override
    public boolean put(String key, Object value) {
        cacheSet.put(key.getBytes(), SerializeUtils.serialize(value));
        return true;
    }

    @Override
    protected Object getReal(String key) {
        byte[] res = cacheSet.get(key.getBytes());
        if (ArrayUtils.isEmpty(res))
            return null;
        return SerializationUtils.deserialize(res);
    }

    @Override
    public boolean del(String key) {
        cacheSet.del(key.getBytes());
        return false;
    }

    @Override
    public Map<String, Object> getAll() {
        Map<String, Object> map = new HashMap<>();
        synchronized (config.appName) {
            Map<byte[], byte[]> res = cacheSet.getAll();
            if (res != null && !res.isEmpty()) {
                for (Map.Entry<byte[], byte[]> entry : res.entrySet())
                    map.put(new String(entry.getKey()), SerializationUtils.deserialize(entry.getValue()));
            }
        }
        return map;
    }

    private void initQ(int i) {
        requestQ[i] = rocksStore.createQueue(request + priority_list[i]);
        responseQ[i] = rocksStore.createQueue(response + priority_list[i]);
        resultQ[i] = rocksStore.createQueue(result + priority_list[i]);
    }

    @Override
    public boolean release() {
        rocksStore.close();
        return true;
    }

    @Override
    public boolean addTask(Task task, boolean force) {
        try {
            if (task instanceof Request) {
                if (!force && duplicate(((Request) task).url, true))
                    return false;
                requestQ[coverPriority(task.priority)].enqueue(SerializeUtils.serialize(task));
            } else if (task instanceof Response)
                responseQ[coverPriority(task.priority)].enqueue(SerializeUtils.serialize(task));
            else if (task instanceof Result)
                resultQ[coverPriority(task.priority)].enqueue(SerializeUtils.serialize(task));
            else
                throw new RuntimeException("unknown task type:" + task);
        } catch (RocksQueueException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean duplicate(String url, boolean save) {
        if (config.duplicate == Constant.DUPLICATE_MD5
                || config.duplicate == Constant.DUPLICATE_BF) {
            byte[] key = MD5Utils.str2MD5(url).getBytes();
            if (duplicateSet.exist(key))
                return true;
            if (save) {
                byte[] value = new byte[1];
                duplicateSet.put(key, value);
            }
        }
        return false;
    }

    @Override
    public Request getRequest() {
        return getTask(requestQ);
    }

    @Override
    public long getRequestSize() {
        return getSize(requestQ);
    }

    @Override
    public Response getResponse() {
        return getTask(responseQ);
    }

    @Override
    public long getResponseSize() {
        return getSize(responseQ);
    }

    @Override
    public Result getResult() {
        return getTask(resultQ);
    }

    @Override
    public long getResultSize() {
        return getSize(resultQ);
    }

    private <T> T getTask(RocksQueue[] queue) {
        try {
            if (config.enablePriority) {
                for (int i = 0; i < 3; i++)
                    if (!queue[i].isEmpty()) {
                        QueueItem item = null;
                        item = queue[i].dequeue();
                        if (item != null && item.getValue() != null && item.getValue().length != 0)
                            return SerializationUtils.deserialize(item.getValue());
                    }
            } else {
                QueueItem item = queue[priority_middle].dequeue();
                if (item != null && item.getValue() != null && item.getValue().length != 0)
                    return SerializationUtils.deserialize(item.getValue());
            }
        } catch (RocksQueueException e) {
            e.printStackTrace();
        }
        return null;
    }

    private long getSize(RocksQueue[] queue) {
        long size = 0;
        if (config.enablePriority) {
            for (RocksQueue q : queue)
                size += q.getSize();
        } else
            size = queue[priority_middle].getSize();
        return size;
    }
}
