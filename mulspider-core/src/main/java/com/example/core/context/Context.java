package com.example.core.context;

import com.example.core.annotation.*;
import com.example.core.download.DownloadWork;
import com.example.core.extract.ExtractWork;
import com.example.core.models.AnnMeta;
import com.example.core.models.GlobalConfig;
import com.example.core.result.ResultWork;
import com.example.core.utils.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.ho.yaml.Yaml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class Context implements Runnable {
    private final static Logger logger = LoggerFactory.getLogger(SpiderApp.class);

    private static final String CONFIG_FILE = "./config_enable.txt";
    public Map<String, AnnMeta.AppMeta> appMap = new HashMap<>();
    public Map<String, AnnMeta.WorkMeta> workMap = new HashMap<>();
    public GlobalConfig globalConfig;
    public Map<String, MethodReflect> methodList = new HashMap<>();
    public MethodReflectShare methodShare = new MethodReflectShare();

    private Executor downExecutor, extractExecutor, resultExecutor, executor;
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

        Map<String, ConfigItem> conItems = getFileConfig();
        for (Map.Entry<String, MethodReflect> entry : methodList.entrySet()) {
            String clazz = entry.getValue().clazz.getName();
            if (conItems.containsKey(clazz)) {
                appMap.get(entry.getValue().name).enable =
                        entry.getValue().enable =
                                conItems.get(clazz).enable;
            }
        }

        initThreadPool();

        for (Map.Entry<String, MethodReflect> entry : methodList.entrySet())
            logger.info("reflectInit  k:{} v:{}", entry.getKey(), entry.getValue());
    }

    public void release() {
        reflectInit.invokeRelease(methodList);
    }

    public void initConfig() {
        String path = FileUtil.getResourcePath("./config_global.yml");
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
        downExecutor = new ThreadPoolExecutor(2, Integer.MAX_VALUE, 60
                , TimeUnit.SECONDS, new SynchronousQueue<>(), new ExceptionThreadFactory(handler), (r, executor) -> {
            logger.warn("downExecutor work overflow");
        });
        extractExecutor = new ThreadPoolExecutor(2, Integer.MAX_VALUE, 60
                , TimeUnit.SECONDS, new SynchronousQueue<>(), new ExceptionThreadFactory(handler), (r, executor) -> {
            logger.warn("extractExecutor work overflow");
        });
        resultExecutor = new ThreadPoolExecutor(2, Integer.MAX_VALUE, 60
                , TimeUnit.SECONDS, new SynchronousQueue<>(), new ExceptionThreadFactory(handler), (r, executor) -> {
            logger.warn("resultExecutor work overflow");
        });
        executor = new ThreadPoolExecutor(1, Integer.MAX_VALUE, 60
                , TimeUnit.SECONDS, new SynchronousQueue<>(), new ExceptionThreadFactory(handler), (r, executor) -> {
            logger.warn("resultExecutor work overflow");
        });
    }

    private Map<String, ConfigItem> getFileConfig() {
        File file = new File(CONFIG_FILE);
        logger.debug("config path:" + file.getAbsolutePath());
        Map<String, ConfigItem> conItems = new HashMap<>();
        if (file.exists()) {
            try {
                List<String> cons = FileUtils.readLines(file, "utf-8");
                for (String c : cons) {
                    if (StringUtils.isNotEmpty(c) && c.contains(":")) {
                        String s[] = c.split(":");
                        ConfigItem item = new ConfigItem();
                        item.clazz = s[0].trim();
                        item.enable = Boolean.parseBoolean(s[1].trim());
                        conItems.put(item.clazz, item);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return conItems;
    }

    public void prepare(String path[]) {
        Map<String, AnnMeta.AppMeta> appMap = new HashMap<>();
        Map<String, AnnMeta.WorkMeta> workMap = new HashMap<>();

        reflectInit.initAnnMeta(appMap, workMap);
        Map<String, ConfigItem> items = new HashMap<>();
        for (AnnMeta.AppMeta meta : appMap.values()) {
            ConfigItem item = new ConfigItem();
            item.clazz = meta.app.getClass().getName();
            item.enable = meta.enable;
            items.put(item.clazz, item);
        }

        Map<String, ConfigItem> conItems = getFileConfig();

        if (!conItems.isEmpty()) {
            for (Map.Entry<String, ConfigItem> item : items.entrySet()) {
                if (!conItems.containsKey(item.getKey())) {
                    conItems.put(item.getKey(), item.getValue());
                }
            }
        } else {
            conItems = items;
        }

        Map<String, List<String>> cate = new HashMap<>();
        for (ConfigItem ci : conItems.values()) {
            for (String p : path) {
                if (ci.clazz.contains(p)) {
                    if (cate.get(p) == null)
                        cate.put(p, new ArrayList<>());
                    cate.get(p).add(ci.clazz + "\t:" + ci.enable);
                    break;
                }
            }
        }
        try {
            File file = new File(CONFIG_FILE);
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file), "utf-8");
            osw.write(" ", 0, 1);
            osw.flush();

            for (Map.Entry<String, List<String>> item : cate.entrySet()) {
                FileUtils.write(file, "\n\n" + item.getKey() + "\n", "utf-8", true);
                FileUtils.writeLines(file, item.getValue(), true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class ConfigItem {
        public String clazz;
        public boolean enable;
    }

    public void add(Work work) {
        if (work instanceof DownloadWork)
            this.downExecutor.execute(work);
        else if (work instanceof ExtractWork)
            this.extractExecutor.execute(work);
        else if (work instanceof ResultWork)
            this.resultExecutor.execute(work);
    }

    @Override
    public void run() {
        for (AnnMeta.AppMeta appMeta : appMap.values()) {
            if (!appMeta.enable)
                continue;

            appMeta.app.initInternal();
            appMeta.app.init();
        }

        while (true) {
            for (AnnMeta.AppMeta appMeta : appMap.values()) {
                if (!appMeta.enable)
                    continue;

                appMeta.app.checkThread();
                ThreadUtils.sleep(10 * 1000);
            }
        }
    }

    public void start() {
        executor.execute(this);
    }
}
