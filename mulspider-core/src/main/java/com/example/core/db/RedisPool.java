package com.example.core.db;

import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.*;

public class RedisPool {
    private static Map<String, Jedis> redisCache = new WeakHashMap<>();
    private static Map<String, JedisPool> redisPoolCache = new WeakHashMap<>();

    public static Jedis getRedis(String ip, int port) {
        return getRedis(ip, port, null, null);
    }

    public static Jedis getRedis(String ip, int port, String user, String pwd) {
        String key = ip + port;
        Jedis jedis = redisCache.get(key);
        if (jedis == null)
            synchronized (RedisPool.class) {
                if (jedis == null)
                    jedis = new Jedis(ip, port);
                redisCache.put(key, jedis);
            }
        auth(jedis, user, pwd);
        return jedis;
    }

    public static Jedis getRedisPool(String ip, int port) {
        return getRedisPool(ip, port, null, null);
    }

    public static Jedis getRedisPool(String ip, int port, String user, String pwd) {
        String key = ip + port;
        JedisPool jedisPool = redisPoolCache.get(key);
        if (jedisPool == null)
            synchronized (RedisPool.class) {
                if (jedisPool == null)
                    jedisPool = new JedisPool(ip, port);
                redisPoolCache.put(key, jedisPool);
            }
        Jedis jedis = jedisPool.getResource();
        auth(jedis, user, pwd);
        return jedis;
    }

    public static void releaseRedis() {
        for (Jedis jedis : redisCache.values()) {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public static void releaseRedisPool() {
        for (JedisPool jedisPool : redisPoolCache.values()) {
            if (jedisPool != null) {
                jedisPool.close();
                jedisPool.destroy();
            }
        }
    }

    private static void auth(Jedis jedis, String user, String pwd) {
        if (StringUtils.isNotEmpty(user) && StringUtils.isNotEmpty(pwd))
            jedis.auth(user, pwd);
        else if (StringUtils.isNotEmpty(pwd))
            jedis.auth(pwd);
    }
}
