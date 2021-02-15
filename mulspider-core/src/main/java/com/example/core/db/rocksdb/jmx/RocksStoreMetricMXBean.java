package com.example.core.db.rocksdb.jmx;

public interface RocksStoreMetricMXBean {
    String getDatabaseName();
    String getRocksdbLocation();
    long getRocksDBDiskUsageInBytes();
    int getNumberOfQueueCreated();
    boolean getIsOpen();
    boolean getIsClosed();
    void reset();
}
