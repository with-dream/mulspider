package com.example.core.db.bloomfilter;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import java.util.List;

public class JedisBitArray extends BitArray {
    @FunctionalInterface
    public interface JedisRunable {
        Object run(Jedis jedis);
    }

    private JedisPool jedisPool;

    private JedisBitArray() {
        super(null);
    }

    public JedisBitArray(JedisPool jedisPool, String prefix) {
        super(prefix);
        this.jedisPool = jedisPool;
    }

    private Object execute(JedisRunable runnable) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return runnable.run(jedis);
        } catch (Exception e) {
            throw e;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    @Override
    public List<Boolean> batchGet(List<Long> indexs) {
        return (List<Boolean>) execute(jedis -> {
            Pipeline pipeline = jedis.pipelined();
            indexs.forEach(index -> pipeline.getbit(key, index));
            return pipeline.syncAndReturnAll();
        });
    }

    @Override
    public List<Boolean> batchSet(List<Long> indexs) {
        return (List<Boolean>) execute(jedis -> {
            Pipeline pipeline = jedis.pipelined();
            indexs.forEach(index -> pipeline.setbit(key, index, true));
            return pipeline.syncAndReturnAll();
        });
    }
}
