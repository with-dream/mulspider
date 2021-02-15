package com.example.core.db;

import com.example.core.models.RedisConfig;

public class DBConfig {
    public static final int DUPLICATE_NONE = -1;
    public static final int DUPLICATE_MD5 = 0;
    public static final int DUPLICATE_BF = 1;
    public String appName;
    public RedisConfig redis;
    public boolean enablePriority;
    public int duplicate;
}
