package com.example.core.context;

import com.example.core.db.DBConfig;
import com.example.core.db.DBManager;
import com.example.core.db.RedisDB;
import com.example.core.download.ua.UserAgentUtil;
import com.example.core.models.*;
import com.example.core.utils.Constant;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

public class SpiderApp {
    protected String name;
    private WorkManager workManager;
    protected DBManager dbManager;
    private Config config;

    protected void initName() {
    }

    protected Config config(Config config) {
        return config;
    }

    public void initInternal() {
        Config config = new Config();

        config.dbConfig = new DBConfig();
        config.dbConfig.appName = name;
        config.dbConfig.redis = new RedisConfig();
        config.dbConfig.redis.ip = "127.0.0.1";
        config.dbConfig.redis.port = 6379;
        this.config = config(config);

        dbManager = new RedisDB();
        dbManager.init(this.config.dbConfig);

        workManager = new WorkManager(dbManager, config);
        workManager.init(config.downThreadCount, config.extractThreadCount, config.resultThreadCount);

        if (config.breakpoint)
            initBreakpoint();
    }

    public void init() {

    }

    public boolean addTask(Task task) {
        return addTask(task, false);
    }

    public boolean addTask(Task task, boolean force) {
        if (task instanceof Request)
            checkRequest((Request) task);
        return dbManager.addTask(task, force);
    }

    public void checkRequest(Request request) {
        if (StringUtils.isEmpty(request.userAgent))
            request.userAgent = UserAgentUtil.getUserAgent();
    }

    public String getName() {
        return this.name;
    }

    public Result extract(Response response) {
        return null;
    }

    public boolean result(Result result) {
        return false;
    }

    private void initBreakpoint() {
        Map<String, Object> cache = dbManager.getAll();
        for (Map.Entry<String, Object> entry : cache.entrySet()) {
            if (entry.getValue() instanceof Request && entry.getKey().endsWith(Constant.REQUEST))
                addTask((Task) entry.getValue(), true);
            else if (entry.getValue() instanceof Response && entry.getKey().endsWith(Constant.EXTRACT))
                addTask((Task) entry.getValue(), true);
            else if (entry.getValue() instanceof Result && entry.getKey().endsWith(Constant.RESULT))
                addTask((Task) entry.getValue(), true);
            else
                dbManager.put(entry.getKey(), entry.getValue());
        }
    }

    public boolean duplicate(String url, boolean save) {
        return dbManager.duplicate(url, save);
    }

    /**
     * return not duplicate count
     */
    public int dupList(List<String> items) {
        int count = 0;
        for (Object str : items) {
            if (!duplicate(str.toString(), false))
                count++;
        }

        return count;
    }
}
