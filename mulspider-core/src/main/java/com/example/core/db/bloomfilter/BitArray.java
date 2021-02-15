package com.example.core.db.bloomfilter;

import java.util.List;

/**
 * Created by jiangtiteng on 2020/4/22
 */
public abstract class BitArray {
    protected String key;

    protected long bitSize;

    public static final long MAX_REDIS_BIT_SIZE = 4294967296L;

    public static final String REDIS_PREFIX = "BLOOM_FILTER_";

    public BitArray(String prefix) {
        this.key = REDIS_PREFIX + prefix;
    }

    public void setBitSize(long bitSize) {
        if (bitSize > MAX_REDIS_BIT_SIZE)
            throw new IllegalArgumentException("Invalid redis bit size, must small than 2 to the 32");

        this.bitSize = bitSize;
    }

    public long bitSize() {
        return this.bitSize;
    }

    public abstract List<Boolean> batchGet(List<Long> indexs);

    public abstract List<Boolean> batchSet(List<Long> indexs);
}
