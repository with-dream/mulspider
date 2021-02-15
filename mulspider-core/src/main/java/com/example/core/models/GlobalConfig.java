package com.example.core.models;

public class GlobalConfig {
    public RedisConfig redis;

    @Override
    public String toString() {
        return "GlobalConfig{" +
                "redis=" + redis +
                '}';
    }
}
