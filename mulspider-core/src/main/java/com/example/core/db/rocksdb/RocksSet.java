package com.example.core.db.rocksdb;

import org.rocksdb.ColumnFamilyHandle;

import java.util.Map;

public class RocksSet {
    private final String setName;

    private final ColumnFamilyHandle cfHandle;
    private final RocksStore store;

    public RocksSet(final String setName, final RocksStore store) {
        this.setName = setName;
        this.store = store;

        this.cfHandle = store.createColumnFamilyHandle(setName);
    }

    public void put(byte[] key, byte[] value) {
        this.store.put(cfHandle, key, value);
    }

    public byte[] get(byte[] key) {
        return this.store.getCF(key, cfHandle);
    }

    public void del(byte[] key) {
        this.store.del(key, cfHandle);
    }

    public Map<byte[], byte[]> getAll() {
        return this.store.getAll(cfHandle);
    }

    public boolean exist(byte[] key) {
        byte[] res = this.store.getCF(key, cfHandle);
        return res != null && res.length > 0;
    }

    public void close() {
        cfHandle.close();
    }

}
