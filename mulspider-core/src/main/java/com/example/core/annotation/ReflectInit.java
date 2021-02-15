package com.example.core.annotation;

import com.example.core.context.SpiderApp;
import com.example.core.models.AnnMeta;
import com.example.core.models.Response;
import com.example.core.models.Task;
import com.example.core.models.Result;
import com.example.core.utils.Constant;
import com.example.core.utils.ReflectUtils;
import com.example.core.utils.StrUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class ReflectInit {
    public void initAnnotation(Map<String, AnnMeta.AppMeta> appMap, Map<String, AnnMeta.WorkMeta> workMap
            , Map<String, MethodReflect> methodList, MethodReflectShare methodShare) {
        initAnnMeta(appMap, workMap);

        for (Map.Entry<String, AnnMeta.AppMeta> entity : appMap.entrySet()) {
            boolean share = entity.getValue().workMetaFlag == AnnMeta.AppWorkMeta.WORK_META_ENABLE;
            boolean enable = share || entity.getValue().enable;
            createMethodMap(methodList, entity.getKey(), entity.getValue().app, true, enable, share, false);
        }
        for (Map.Entry<String, AnnMeta.WorkMeta> entity : workMap.entrySet())
            createMethodMap(methodList, entity.getKey(), entity.getValue().object, entity.getValue().singleton, entity.getValue().enable, true, true);

        List<Map<String, MethodReflect.MethodMeta>> shareList = createMapList(methodShare.emMap, /*methodShare.emrMap, methodShare.euMap, */methodShare.eurMap, methodShare.rmMap, /*methodShare.rmrMap, methodShare.ruMap, */methodShare.rurMap, methodShare);
        for (MethodReflect mr : methodList.values()) {
            if (!mr.share)
                continue;

            List<Map<String, MethodReflect.MethodMeta>> mrList = createMapList(mr.emMap, /*mr.emrMap,mr.euMap, */mr.eurMap, mr.rmMap, /*mr.rmrMap, mr.ruMap,*/ mr.rurMap, methodShare);
            for (int i = 0; i < shareList.size(); i++) {
                if (mrList.get(i).isEmpty())
                    continue;

                for (Map.Entry<String, MethodReflect.MethodMeta> entry : mrList.get(i).entrySet()) {
                    if (!entry.getValue().share) continue;
                    shareList.get(i).put(entry.getKey(), entry.getValue());
                }
            }
        }

        invokeInit(methodList);
    }

    private void invokeInit(Map<String, MethodReflect> methodList) {
        for (MethodReflect meta : methodList.values())
            if (meta.obj != null && meta.initMeta != null && !meta.isApp)
                ReflectUtils.invokeDefault(meta.obj, meta.initMeta.method, meta.initMeta.accessible);
    }

    public void invokeRelease(Map<String, MethodReflect> methodList) {
        for (MethodReflect meta : methodList.values())
            if (meta.obj != null && meta.releaseMeta != null && !meta.isApp)
                ReflectUtils.invokeDefault(meta.obj, meta.releaseMeta.method, meta.releaseMeta.accessible);
    }

    private List<Map<String, MethodReflect.MethodMeta>> createMapList(Map<String, MethodReflect.MethodMeta> emMap, /*Map<String, MethodReflect.MethodMeta> emrMap, Map<String, MethodReflect.MethodMeta> euMap, */Map<String, MethodReflect.MethodMeta> eurMap, Map<String, MethodReflect.MethodMeta> rmMap, /*Map<String, MethodReflect.MethodMeta> rmrMap, Map<String, MethodReflect.MethodMeta> ruMap, */Map<String, MethodReflect.MethodMeta> rurMap, MethodReflectShare methodShare) {
        List<Map<String, MethodReflect.MethodMeta>> list = new ArrayList<>();
        list.add(emMap);
        list.add(eurMap);
        list.add(rmMap);
        list.add(rurMap);

        return list;
    }

    private void initAnnMeta(Map<String, AnnMeta.AppMeta> appMap, Map<String, AnnMeta.WorkMeta> workMap) {
        try {
            Reflections ref = new Reflections();
            Set<Class<?>> spiderCls = ref.getTypesAnnotatedWith(Spider.class);
            Set<Class<?>> spiderWorkCls = ref.getTypesAnnotatedWith(SpiderWork.class);

            Map<String, String> workNameClsMap = new HashMap<>();
            for (Class<?> clazz : spiderWorkCls) {
                SpiderWork annotation = clazz.getAnnotation(SpiderWork.class);
                if (!annotation.enable())
                    continue;

                String name = annotation.name();
                if (StrUtils.isEmpty(name))
                    name = clazz.getName();
                workNameClsMap.put(clazz.getName(), name);
                Object object = annotation.singleton() ? ReflectUtils.instance(clazz) : null;


                workMap.put(name, new AnnMeta.WorkMeta(name, annotation.singleton(), clazz, object, annotation.enable()));
            }

            for (Class<?> clazz : spiderCls) {
                if (!SpiderApp.class.isAssignableFrom(clazz))
                    throw new RuntimeException("@Spider can only be used on SpiderApp subclass, cannot used on class " + clazz.getName());
                Spider annotation = clazz.getAnnotation(Spider.class);
                SpiderApp app = ReflectUtils.instance(clazz);

                String keyName = annotation.name();
                if (StrUtils.isNotEmpty(keyName))
                    FieldUtils.writeField(app, "name", keyName, true);
                if (StrUtils.isEmpty(keyName))
                    keyName = app.getName();
                if (StrUtils.isEmpty(keyName)) {
                    Method md = MethodUtils.getAccessibleMethod(clazz, "initName");
                    if (md != null)
                        keyName = (String) MethodUtils.invokeMethod(app, true, "initName");
                }
                if (StrUtils.isEmpty(keyName)) {
                    keyName = clazz.getName();
                    FieldUtils.writeField(app, "name", keyName, true);
                }

                AnnMeta.AppWorkMeta type = AnnMeta.AppWorkMeta.WORK_META_NONE;
                if (workNameClsMap.containsKey(clazz.getName())) {
                    type = annotation.enable()
                            ? AnnMeta.AppWorkMeta.WORK_META_ENABLE : AnnMeta.AppWorkMeta.WORK_META_DISABLE;
                    workMap.remove(workNameClsMap.get(clazz.getName()));
                    workNameClsMap.remove(clazz.getName());
                }

                appMap.put(keyName, new AnnMeta.AppMeta(app, keyName, annotation.enable(), type));
            }

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param methodForceShare @SpiderWork force all share method  @Spider can config    both @Spider and @SpiderWork can config
     */
    private void createMethodMap(Map<String, MethodReflect> methodList, String workName, Object obj, boolean singleton, boolean enable, boolean share, boolean methodForceShare) {
        MethodReflect mr = new MethodReflect();
        methodList.put(workName, mr);

        mr.name = workName;
        mr.enable = enable;
        mr.share = share;
        mr.isApp = obj instanceof SpiderApp;
        mr.obj = singleton ? obj : null;
        mr.singleton = singleton;
        mr.clazz = obj.getClass();
        Method[] methods = mr.clazz.getDeclaredMethods();
        for (Method md : methods) {
            if (Constant.DEFAULT_EXTRACT_NAME.equals(md.getName())) {
                String returnType = md.getReturnType().getName();
                if (Result.class.getName().equals(returnType)) {
                    Class<?>[] typeParameters = md.getParameterTypes();
                    if (typeParameters.length == 1 && Response.class.getName().equals(typeParameters[0].getName())) {
                        MethodReflect.MethodMeta methodMeta = new MethodReflect.MethodMeta(
                                mr, false, Constant.DEFAULT_EXTRACT, md, md.isAccessible(), 0);
                        mr.defaultMap.put(Constant.DEFAULT_EXTRACT, methodMeta);
                    }
                }
            } else if (Constant.DEFAULT_RESULT_NAME.equals(md.getName())) {
                String returnType = md.getReturnType().getName();
                if (Boolean.class.getName().equals(returnType)) {
                    Class<?>[] typeParameters = md.getParameterTypes();
                    if (typeParameters.length == 1 && Result.class.getName().equals(typeParameters[0].getName())) {
                        MethodReflect.MethodMeta methodMeta = new MethodReflect.MethodMeta(
                                mr, false, Constant.DEFAULT_RESULT, md, md.isAccessible(), 0);
                        mr.defaultMap.put(Constant.DEFAULT_RESULT, methodMeta);
                    }
                }
            }

            for (Annotation ann : md.getAnnotations()) {
                if (ExtractMethod.class.equals(ann.annotationType())) {
                    ExtractMethod annotation = md.getAnnotation(ExtractMethod.class);
                    String[] names = annotation.methods();
                    MethodReflect.MethodMeta methodMeta = new MethodReflect.MethodMeta(
                            mr, methodForceShare || annotation.share(), annotation.lock() && singleton ? names : null, md, md.isAccessible(), checkExtractMethod(mr.clazz.getName(), md));
                    for (String name : names)
                        checkReflectKey(name, methodMeta, mr.emMap, annotation);
                } else if (ExtractUrlRegex.class.equals(ann.annotationType())) {
                    ExtractUrlRegex annotation = md.getAnnotation(ExtractUrlRegex.class);
                    String[] names = annotation.urls();
                    MethodReflect.MethodMeta methodMeta = new MethodReflect.MethodMeta(
                            mr, methodForceShare || annotation.share(), annotation.lock() && singleton ? names : null, md, md.isAccessible(), checkExtractMethod(mr.clazz.getName(), md));
                    for (String name : names)
                        checkReflectKey(name, methodMeta, mr.eurMap, annotation);
                } else if (ResultMethod.class.equals(ann.annotationType())) {
                    ResultMethod annotation = md.getAnnotation(ResultMethod.class);
                    String[] names = annotation.methods();
                    MethodReflect.MethodMeta methodMeta = new MethodReflect.MethodMeta(
                            mr, methodForceShare || annotation.share(), annotation.lock() && singleton ? names : null, md, md.isAccessible(), checkResultMethod(mr.clazz.getName(), md));
                    for (String name : names)
                        checkReflectKey(name, methodMeta, mr.rmMap, annotation);
                } else if (ResultUrlRegex.class.equals(ann.annotationType())) {
                    ResultUrlRegex annotation = md.getAnnotation(ResultUrlRegex.class);
                    String[] names = annotation.urls();
                    MethodReflect.MethodMeta methodMeta = new MethodReflect.MethodMeta(
                            mr, methodForceShare || annotation.share(), annotation.lock() && singleton ? names : null, md, md.isAccessible(), checkResultMethod(mr.clazz.getName(), md));
                    for (String name : names)
                        checkReflectKey(name, methodMeta, mr.rurMap, annotation);
                } else if (mr.initMeta == null && WorkInit.class.equals(ann.annotationType())) {
                    mr.initMeta = new MethodReflect.MethodMeta(
                            mr, false, null, md, md.isAccessible(), -1);
                } else if (mr.releaseMeta == null && WorkRelease.class.equals(ann.annotationType())) {
                    mr.releaseMeta = new MethodReflect.MethodMeta(
                            mr, false, null, md, md.isAccessible(), -1);
                }
            }
        }
    }

    private int checkExtractMethod(String className, Method md) {
        int paramIndex = -1;

        String returnType = md.getReturnType().getName();
        if (!Result.class.getName().equals(returnType) && !Task.class.getName().equals(returnType))
            throw new RuntimeException(String.format(Locale.ENGLISH, "%s method:%s return type must Result or Task", className, md.getName()));

        Class<?>[] typeParameters = md.getParameterTypes();
        for (int i = 0; i < typeParameters.length; i++) {
            Class<?> clazz = typeParameters[i];
            if (Response.class.getName().equals(clazz.getName()) || Task.class.getName().equals(clazz.getName())) {
                paramIndex = i;
                break;
            }
        }

        if (paramIndex == -1)
            throw new RuntimeException(String.format(Locale.ENGLISH, "%s method:%s param type must has Result or Task", className, md.getName()));
        return paramIndex;
    }

    private int checkResultMethod(String className, Method md) {
        int paramIndex = -1;

        Class<?>[] typeParameters = md.getParameterTypes();
        for (int i = 0; i < typeParameters.length; i++) {
            Class<?> clazz = typeParameters[i];
            if (Result.class.getName().equals(clazz.getName()) || Task.class.getName().equals(clazz.getName())) {
                paramIndex = i;
                break;
            }
        }

        if (paramIndex == -1)
            throw new RuntimeException(String.format(Locale.ENGLISH, "%s method:%s param type must has Result or Task", className, md.getName()));
        return paramIndex;
    }

    Set<String> retain = new HashSet<>();

    private void checkReflectKey(String key, MethodReflect.MethodMeta meta, Map<String, MethodReflect.MethodMeta> map, Annotation ann) {
        if (retain.contains(key))
            throw new RuntimeException("the share annotation name must be unique   repeat annotation name:" + key);
        retain.add(key);
        map.put(key, meta);
    }
}
