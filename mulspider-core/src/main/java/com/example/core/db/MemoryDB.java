package com.example.core.db;

import com.example.core.models.Request;
import com.example.core.models.Response;
import com.example.core.models.Result;
import com.example.core.models.Task;
import com.example.core.utils.MD5Utils;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class MemoryDB extends DBManager {
    private BlockingQueue<Request> requestQueue;
    private BlockingQueue<Response> responseQueue;
    private BlockingQueue<Result> resultQueue;
    private BloomFilter<String> bloomFilter;
    private Set<String> duplicateSet;
    private Map<String, Object> cacheMap = new HashMap<>();

    @Override
    public boolean init(DBConfig config) {
        super.init(config);
        if (!config.enablePriority) {
            requestQueue = new PriorityBlockingQueue<>();
            responseQueue = new PriorityBlockingQueue<>();
            resultQueue = new PriorityBlockingQueue<>();
        } else {
            requestQueue = new LinkedBlockingQueue<>();
            responseQueue = new LinkedBlockingQueue<>();
            resultQueue = new LinkedBlockingQueue<>();
        }

        if (config.duplicate == DBConfig.DUPLICATE_BF)
            bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()), 100_0000, 0.000_0001);
        else if (config.duplicate == DBConfig.DUPLICATE_MD5)
            duplicateSet = new HashSet<>();
        return true;
    }

    @Override
    public boolean put(String key, Object value) {
        cacheMap.put(key, value);
        return true;
    }

    @Override
    public <T> T get(String key) {
        return (T) cacheMap.get(key);
    }

    @Override
    public boolean del(String key) {
        cacheMap.remove(key);
        return true;
    }

    @Override
    public Map<String, Object> getAll() {
        Map<String, Object> map = new HashMap<>();
        for (Map.Entry<String, Object> entry : cacheMap.entrySet())
            map.put(entry.getKey(), entry.getValue());
        cacheMap.clear();
        return map;
    }

    @Override
    public boolean release() {
        if (requestQueue != null)
            requestQueue.clear();
        requestQueue = null;
        if (responseQueue != null)
            responseQueue.clear();
        responseQueue = null;
        if (resultQueue != null)
            resultQueue.clear();
        resultQueue = null;
        return true;
    }

    @Override
    public boolean addTask(Task task, boolean force) {
        if (task instanceof Request) {
            if (!force && duplicate(((Request) task).url, true))
                return false;
            requestQueue.add((Request) task);
        } else if (task instanceof Response)
            responseQueue.add((Response) task);
        else if (task instanceof Result)
            resultQueue.add((Result) task);
        else
            throw new RuntimeException("unknown task type:" + task);

        return true;
    }

    @Override
    public boolean duplicate(String url, boolean save) {
        if (config.duplicate == DBConfig.DUPLICATE_BF) {
            if (bloomFilter.mightContain(url))
                return true;
            if (save)
                bloomFilter.put(url);
        } else if (config.duplicate == DBConfig.DUPLICATE_MD5) {
            if (duplicateSet.contains(MD5Utils.str2MD5(url)))
                return true;
            if (save)
                duplicateSet.add(MD5Utils.str2MD5(url));
        }
        return false;
    }

    @Override
    public Request getRequest() {
        return requestQueue.poll();
    }

    @Override
    public long getRequestSize() {
        return requestQueue.size();
    }

    @Override
    public Response getResponse() {
        return responseQueue.poll();
    }

    @Override
    public long getResponseSize() {
        return responseQueue.size();
    }

    @Override
    public Result getResult() {
        return resultQueue.poll();
    }

    @Override
    public long getResultSize() {
        return resultQueue.size();
    }
}
