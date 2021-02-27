package com.example.core.context;

import com.example.core.annotation.*;
import com.example.core.download.DownloadWork;
import com.example.core.extract.ExtractWork;
import com.example.core.models.AnnMeta;
import com.example.core.models.GlobalConfig;
import com.example.core.result.ResultWork;
import com.example.core.utils.*;
import org.ho.yaml.Yaml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.*;

public class Context {
    private final static Logger logger = LoggerFactory.getLogger(SpiderApp.class);

    public Map<String, AnnMeta.AppMeta> appMap = new HashMap<>();
    public Map<String, AnnMeta.WorkMeta> workMap = new HashMap<>();
    public GlobalConfig globalConfig;
    public Map<String, MethodReflect> methodList = new HashMap<>();
    public MethodReflectShare methodShare = new MethodReflectShare();

    private Executor downExecutor, extractExecutor, resultExecutor;
    private ExceptionThreadFactory threadFactory;
    private ReflectInit reflectInit = new ReflectInit();

    private Context() {
    }

    private static class ContextHolder {
        private static Context INSTANCE = new Context();
    }

    public static Context instance() {
        return ContextHolder.INSTANCE;
    }

    public void init() {
        initConfig();
        reflectInit.initAnnotation(appMap, workMap, methodList, methodShare);
        initThreadPool();

        for (Map.Entry<String, MethodReflect> entry : methodList.entrySet())
            logger.info("reflectInit  k:{} v:{}", entry.getKey(), entry.getValue());
    }

    public void release() {
        reflectInit.invokeRelease(methodList);
    }

    public void initConfig() {
        String path = FileUtils.getResourcePath("./config_global.yml");
        File dumpFile = new File(path);
        try {
            globalConfig = Yaml.loadType(dumpFile, GlobalConfig.class);
            logger.info("config==>" + globalConfig);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initThreadPool() {
        CustomUncaughtExceptionHandler handler = new CustomUncaughtExceptionHandler();
        threadFactory = new ExceptionThreadFactory(handler);
        downExecutor = new ThreadPoolExecutor(2, Integer.MAX_VALUE, 60
                , TimeUnit.SECONDS, new SynchronousQueue<>(), threadFactory, (r, executor) -> {
            logger.warn("downExecutor work overflow");
        });
        extractExecutor = new ThreadPoolExecutor(2, Integer.MAX_VALUE, 60
                , TimeUnit.SECONDS, new SynchronousQueue<>(), threadFactory, (r, executor) -> {
            logger.warn("extractExecutor work overflow");
        });
        resultExecutor = new ThreadPoolExecutor(2, Integer.MAX_VALUE, 60
                , TimeUnit.SECONDS, new SynchronousQueue<>(), threadFactory, (r, executor) -> {
            logger.warn("resultExecutor work overflow");
        });
    }

    public void add(Work work) {
        if (work instanceof DownloadWork)
            this.downExecutor.execute(work);
        else if (work instanceof ExtractWork)
            this.extractExecutor.execute(work);
        else if (work instanceof ResultWork)
            this.resultExecutor.execute(work);
    }

    public void run() {
        for (AnnMeta.AppMeta appMeta : appMap.values()) {
            if (!appMeta.enable)
                continue;

            appMeta.app.initInternal();
            appMeta.app.init();
        }
    }
}
