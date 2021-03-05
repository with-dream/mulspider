package com.example.core.db;

import com.example.core.context.Context;
import com.example.core.models.Request;
import com.example.core.models.Response;
import com.example.core.models.Task;
import com.example.core.models.Result;
import com.example.core.utils.Constant;
import com.example.core.utils.MD5Utils;
import com.example.core.utils.SerializeUtils;
import com.google.common.hash.BloomFilter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.SerializationUtils;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RedisDB extends DBManager {
    private byte[] requestKey;
    private byte[] responseKey;
    private byte[] resultKey;
    private byte[] duplicateKey;
    private byte[] cacheKey;
    private BloomFilter<String> bloomFilter;

    private String ip;
    private int port;
    private String user;
    private String pwd;

    @Override
    public boolean init(DBConfig config) {
        super.init(config);
        requestKey = (config.appName + request).getBytes();
        responseKey = (config.appName + response).getBytes();
        resultKey = (config.appName + result).getBytes();
        cacheKey = (config.appName + "_cache").getBytes();
        duplicateKey = (config.appName + "_duplicate").getBytes();

        if (config.redis != null) {
            ip = config.redis.ip;
            port = config.redis.port;
            user = config.redis.user;
            pwd = config.redis.pwd;
        } else if (Context.instance().globalConfig != null && Context.instance().globalConfig.redis != null) {
            ip = Context.instance().globalConfig.redis.ip;
            port = Context.instance().globalConfig.redis.port;
            user = config.redis.user;
            pwd = config.redis.pwd;
        } else {
            throw new RuntimeException("redis ip and port is not config");
        }

        return true;
    }

    @Override
    public boolean put(String key, Object value) {
        Jedis jedis = getJedis();
        jedis.hset(cacheKey, key.getBytes(), SerializeUtils.serialize(value));
        jedis.close();
        return true;
    }

    @Override
    protected Object getReal(String key) {
        Jedis jedis = getJedis();
        byte[] res = jedis.hget(cacheKey, key.getBytes());
        jedis.close();
        if (ArrayUtils.isEmpty(res))
            return null;
        return SerializationUtils.deserialize(res);
    }

    @Override
    public boolean del(String key) {
        Jedis jedis = getJedis();
        jedis.hdel(cacheKey, key.getBytes());
        jedis.close();
        return true;
    }

    @Override
    public Map<String, Object> getAll() {
        Map<String, Object> map = new HashMap<>();
        Jedis jedis = getJedis();
        Map<byte[], byte[]> res = jedis.hgetAll(cacheKey);
        jedis.close();
        if (res != null && !res.isEmpty()) {
            for (Map.Entry<byte[], byte[]> entry : res.entrySet())
                map.put(new String(entry.getKey()), SerializationUtils.deserialize(entry.getValue()));
        }
        return map;
    }

    private Jedis getJedis() {
        return RedisPool.getRedisPool(ip, port, user, pwd);
    }

    @Override
    public boolean release() {
        return true;
    }

    @Override
    public boolean addTask(Task task, boolean force) {
        byte[] key = null;
        if (task instanceof Request) {
            if (!force && duplicate(((Request) task).url, true))
                return false;
            key = requestKey;
        } else if (task instanceof Response)
            key = responseKey;
        else if (task instanceof Result)
            key = resultKey;
        else
            throw new RuntimeException("unknown task type:" + task);

        Jedis jedis = getJedis();
        if (config.enablePriority)
            jedis.zadd(key, task.priority, SerializeUtils.serialize(task));
        else
            jedis.lpush(key, SerializeUtils.serialize(task));
        jedis.close();
        return false;
    }

    @Override
    public boolean duplicate(String url, boolean save) {
        if (config.duplicate == Constant.DUPLICATE_BF) {
            if (bloomFilter.mightContain(url))
                return true;
            if (save)
                bloomFilter.put(url);
        } else if (config.duplicate == Constant.DUPLICATE_MD5) {
            Jedis jedis = getJedis();

            byte[] md5 = MD5Utils.str2MD5(url).getBytes();
            if (jedis.exists(md5)) {
                jedis.close();
                return true;
            }
            if (save)
                jedis.sadd(duplicateKey, md5);
            jedis.close();
        }
        return false;
    }

    @Override
    public Request getRequest() {
        return get(requestKey);
    }

    @Override
    public long getRequestSize() {
        return len(requestKey);
    }

    @Override
    public Response getResponse() {
        return get(responseKey);
    }

    @Override
    public long getResponseSize() {
        return len(responseKey);
    }

    @Override
    public Result getResult() {
        return get(resultKey);
    }

    @Override
    public long getResultSize() {
        return len(resultKey);
    }

    private long len(byte[] key) {
        Jedis jedis = getJedis();
        long len;
        if (config.enablePriority)
            len = jedis.zcard(key);
        else
            len = jedis.llen(key);
        jedis.close();
        return len;
    }

    private <T> T get(byte[] key) {
        Jedis jedis = getJedis();
        byte[] res = null;
        if (config.enablePriority) {
            Set<byte[]> range = jedis.zrange(key, 0, 1);
            if (range != null && !range.isEmpty())
                res = range.iterator().next();
        } else
            res = jedis.rpop(key);

        jedis.close();

        if (ArrayUtils.isEmpty(res))
            return null;

        return SerializationUtils.deserialize(res);
    }
}
