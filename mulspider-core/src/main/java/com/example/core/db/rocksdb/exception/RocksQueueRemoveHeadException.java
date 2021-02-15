package com.example.core.db.rocksdb.exception;

public class RocksQueueRemoveHeadException extends RocksQueueException {
    public RocksQueueRemoveHeadException(String rocksDBLocation, Throwable cause) {
        super("Rocks queue remove head exception, please check rocksdb located in " + rocksDBLocation, cause);
    }
}
