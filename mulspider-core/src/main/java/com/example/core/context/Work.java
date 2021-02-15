package com.example.core.context;

import com.example.core.annotation.MethodReflect;
import com.example.core.db.DBManager;
import com.example.core.models.Request;
import com.example.core.models.Task;
import com.example.core.utils.Constant;
import com.example.core.utils.D;
import com.example.core.utils.ReflectUtils;
import com.example.core.utils.ThreadUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

public abstract class Work implements Runnable {
    public enum WorkType {
        request, extract, result
    }

    protected int threadIndex = -1;
    public AtomicInteger threadCount;
    public AtomicInteger threadCountCur;
    protected DBManager dbManager;
    protected long closeDelayTime = 10 * 60 * 1000;
    protected long currentDelayTime = 0;
    protected Config config;

    public Work(Config config) {
        this.config = config;
    }

    public void setDbManager(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    @Override
    public void run() {
        currentDelayTime = System.currentTimeMillis();
        threadCountCur.incrementAndGet();
        while (threadIndex < threadCount.get())
            if (!work())
                break;
        threadCountCur.decrementAndGet();
    }

    protected abstract boolean work();

    protected boolean handleTask(WorkType workType, Task task, Request request) {
        //invoke the SpiderApp not share method
        MethodReflect mr = Context.instance().methodList.get(request.name);
        MethodReflect.MethodMeta mdMeta;
        if (workType == WorkType.extract) {
            if ((mdMeta = mr.defaultMap.get(Constant.DEFAULT_EXTRACT)) != null) {
                D.d("test method", String.format("app:%s  %s methodName:%s", request.name, "extract", Constant.DEFAULT_EXTRACT));
                invoke(mr.obj, mdMeta, task, true);
            }
            if (request.method != null && request.method.length != 0) {
                for (String m : request.method)
                    if ((mdMeta = mr.emMap.get(m)) != null && !mdMeta.share) {
                        D.d("test method", String.format("app:%s  %s methodName:%s", request.name, "extract", m));
                        invoke(mr.obj, mdMeta, task, true);
                    }
            }
        } else if (workType == WorkType.result) {
            if ((mdMeta = mr.defaultMap.get(Constant.DEFAULT_RESULT)) != null) {
                D.d("test method", String.format("app:%s  %s methodName:%s", request.name, "result", Constant.DEFAULT_RESULT));
                invoke(mr.obj, mdMeta, task, false);
            }

            if (request.method != null && request.method.length != 0) {
                for (String m : request.method)
                    if ((mdMeta = mr.rmMap.get(m)) != null && !mdMeta.share) {
                        D.d("test method", String.format("app:%s  %s methodName:%s", request.name, "result", m));
                        invoke(mr.obj, mdMeta, task, false);
                    }
            }
        }

        //match method
        if (request.method != null && request.method.length != 0)
            if (workType == WorkType.extract) {
                for (String m : request.method)
                    if ((mdMeta = Context.instance().methodShare.emMap.get(m)) != null) {
                        D.d("test method", String.format("app:%s  %s methodName:%s", request.name, "extract", m));
                        invoke(mdMeta.mr.singleton ? mdMeta.mr.obj : invokeInit(mdMeta), mdMeta, task, true);
                    }
            } else if (workType == WorkType.result) {
                for (String m : request.method)
                    if ((mdMeta = Context.instance().methodShare.rmMap.get(m)) != null) {
                        D.d("test method", String.format("app:%s  %s methodName:%s", request.name, "result", m));
                        invoke(mdMeta.mr.singleton ? mdMeta.mr.obj : invokeInit(mdMeta), mdMeta, task, false);
                    }
            }
        //match url
        if (workType == WorkType.extract) {
            for (Map.Entry<String, MethodReflect.MethodMeta> entry : Context.instance().methodShare.eurMap.entrySet()) {
                if (Pattern.matches(entry.getKey(), request.url)) {
                    mdMeta = entry.getValue();
                    D.d("test method", String.format("app:%s  %s methodName:%s  %s", request.name, "extract", mdMeta.mr.clazz.getName(), mdMeta.method.getName()));
                    invoke(mdMeta.mr.singleton ? mdMeta.mr.obj : invokeInit(mdMeta), mdMeta, task, true);
                }
            }
        } else if (workType == WorkType.result) {
            for (Map.Entry<String, MethodReflect.MethodMeta> entry : Context.instance().methodShare.rurMap.entrySet()) {
                if (Pattern.matches(entry.getKey(), request.url)) {
                    mdMeta = entry.getValue();
                    D.d("test method", String.format("app:%s  %s methodName:%s  %s", request.name, "result", mdMeta.mr.clazz.getName(), mdMeta.method.getName()));
                    invoke(mdMeta.mr.singleton ? mdMeta.mr.obj : invokeInit(mdMeta), mdMeta, task, false);
                }
            }
        }

        return true;
    }

    private Object invokeInit(MethodReflect.MethodMeta mdMeta) {
        Object obj = ReflectUtils.instance(mdMeta.mr.clazz);
        if (mdMeta.mr.initMeta != null && !mdMeta.mr.isApp && !mdMeta.mr.singleton)
            ReflectUtils.invokeDefault(obj, mdMeta.mr.initMeta.method, mdMeta.mr.initMeta.accessible);
        return obj;
    }

    private void invoke(Object obj, MethodReflect.MethodMeta mdMeta, Task task, boolean store) {
        Class<?>[] clazz = mdMeta.method.getParameterTypes();
        Object[] param = new Object[clazz.length];
        for (int i = 0; i < clazz.length; i++)
            if (i == mdMeta.paramIndex)
                param[mdMeta.paramIndex] = task;
            else
                param[i] = ReflectUtils.getDefaultValue(clazz[i]);

        Task result = null;
//        D.d("invoke lock==>" + mdMeta.lock);
        if (mdMeta.lock == null)
            result = ReflectUtils.invoke(obj, mdMeta.method, param, mdMeta.accessible);
        else
            synchronized (mdMeta.lock) {
                result = ReflectUtils.invoke(obj, mdMeta.method, param, mdMeta.accessible);
            }

        if (store) {
            if (result != null) {
                if (!result.ignore) {
                    dbManager.addTask(result);
                }
            } else
                D.e(obj.getClass().getSimpleName() + "." + mdMeta.method.getName() + "  extract result is null");
        }

        if (mdMeta.mr.releaseMeta != null && !mdMeta.mr.isApp && !mdMeta.mr.singleton)
            ReflectUtils.invokeDefault(obj, mdMeta.mr.releaseMeta.method, mdMeta.mr.releaseMeta.accessible);
    }

    protected void delay(int delayTime) {
        ThreadUtils.sleep(delayTime);
    }
}
