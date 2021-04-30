package com.example.core.models;

public class GlobalConfig {
    public RedisConfig redis;
    public boolean proxy_pool;

    @Override
    public String toString() {
        return "GlobalConfig{" +
                "redis=" + redis +
                '}';
    }
}
