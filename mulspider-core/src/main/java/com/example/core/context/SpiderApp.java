package com.example.core.context;

import com.example.core.db.*;
import com.example.core.download.DownloadHandle;
import com.example.core.download.ua.UserAgentUtil;
import com.example.core.models.*;
import com.example.core.utils.Constant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import samples.wallpaper.WallHaven;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SpiderApp {
    private final static Logger appLogger = LoggerFactory.getLogger(SpiderApp.class);

    protected String name;
    private WorkManager workManager;
    protected DBManager dbManager;
    private Config config;

    protected void initName() {
    }

    protected Config config(Config config) {
        config.breakpoint = true;
        return config;
    }

    public void initInternal() {
        initDB();

        workManager = new WorkManager(this, config);
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

    public void restart() {
        appLogger.info("==>restart");
        init();
    }

    public String getName() {
        return this.name;
    }

    public Result extract(Response response) {
        return Result.makeIgnore();
    }

    public boolean result(Result result) {
        return false;
    }

    private void initBreakpoint() {
        Map<String, Object> cache = dbManager.getAll();
        for (Map.Entry<String, Object> entry : cache.entrySet()) {
            if (entry.getValue() instanceof Request && entry.getKey().endsWith(Constant.REQUEST_BK))
                addTask((Task) entry.getValue(), true);
            else if (entry.getValue() instanceof Response && entry.getKey().endsWith(Constant.EXTRACT_BK))
                addTask((Task) entry.getValue(), true);
            else if (entry.getValue() instanceof Result && entry.getKey().endsWith(Constant.RESULT_BK))
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
    public int dupList(List<String> items, boolean save) {
        return dupList("", items, save);
    }

    public int dupList(String prefix, List<String> items, boolean save) {
        int count = 0;
        for (Object str : items) {
            if (!duplicate(prefix + str.toString(), save))
                count++;
        }

        return count;
    }

    private void initDB() {
        Config config = new Config();

        config.dbConfig = new DBConfig();
        config.dbConfig.dbType = Constant.DB_ROCK;
        config.dbConfig.appName = name;
        config.dbConfig.redis = new RedisConfig();
        config.dbConfig.redis.ip = "127.0.0.1";
        config.dbConfig.redis.port = 6379;
        this.config = config(config);
        config.downloadTimeout = (request, e) -> {
            if (request.retryIndex < request.retryCount) {
                request.timeOut *= 1.5;
                addTask(request, true);
            } else
                requestTimeout(request, e);
        };

        switch (this.config.dbConfig.dbType) {
            case Constant.DB_MEMORY:
                dbManager = new MemoryDB();
                break;
            case Constant.DB_ROCK:
                dbManager = new RocksQueueDB();
                break;
            case Constant.DB_REDIS:
                dbManager = new RedisDB();
                break;
        }

        dbManager.init(this.config.dbConfig);
    }

    //ms
    protected boolean restartTime(long restart) {
        return dbManager.put(Constant.RESTART_DELAY_TAG, System.currentTimeMillis() + restart);
    }

    protected void requestTimeout(Request request, IOException e) {
        appLogger.error("requestTimeout==>" + request + "  err:" + e.getMessage());
    }

    public void checkThread() {
        workManager.checkThread(false);
    }
}
