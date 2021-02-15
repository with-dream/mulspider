package com.example.core.db;

import com.example.core.models.Request;
import com.example.core.models.Response;
import com.example.core.models.Result;
import com.example.core.models.Task;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public abstract class DBManager {
    protected static final String request = "request";
    protected static final String response = "response";
    protected static final String result = "result";

    protected DBConfig config;

    public boolean init(DBConfig config) {
        this.config = config;
        if (config == null || StringUtils.isEmpty(config.appName))
            throw new RuntimeException("appName must associate with ReptileApp.name");
        return true;
    }

    public abstract boolean put(String key, Object value);

    public abstract <T> T get(String key);

    public abstract boolean del(String key);

    public abstract Map<String, Object> getAll();

    public abstract boolean release();

    public abstract boolean addTask(Task task, boolean force);

    public boolean addTask(Task task) {
        return addTask(task, false);
    }

    public abstract boolean duplicate(String url, boolean save);

    public abstract Request getRequest();

    public abstract long getRequestSize();

    public abstract Response getResponse();

    public abstract long getResponseSize();

    public abstract Result getResult();

    public abstract long getResultSize();

}
