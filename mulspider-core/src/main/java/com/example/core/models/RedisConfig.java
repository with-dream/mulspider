package com.example.core.models;

public class RedisConfig {
    public String ip;
    public int port;
    public String user;
    public String pwd;

    @Override
    public String toString() {
        return "Redis{" +
                "ip='" + ip + '\'' +
                ", port='" + port + '\'' +
                '}';
    }
}
