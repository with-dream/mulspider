package com.example.core.annotation;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MethodReflect {
    public String name;
    //the SpiderApp is enable
    //@SpiderWork is all true
    public boolean enable;
    public boolean share;
    public boolean isApp;
    public boolean singleton;
    public Class<?> clazz;
    public Object obj;
    public Map<String, MethodMeta> emMap = new HashMap<>(); //@ExtractMethod
    public Map<String, MethodMeta> eurMap = new HashMap<>();
    public Map<String, MethodMeta> rmMap = new HashMap<>();
    public Map<String, MethodMeta> rurMap = new HashMap<>();
    public MethodMeta initMeta, releaseMeta;
    public Map<String, MethodMeta> defaultMap = new HashMap<>();

    public static class MethodMeta {
        public MethodReflect mr;
        public boolean share;
        public Object lock;
        public Method method;
        public boolean accessible;
        public int paramIndex;

        public MethodMeta(MethodReflect mr, boolean share, Object lock, Method method, boolean accessible, int paramIndex) {
            this.mr = mr;
            this.share = share;
            this.lock = lock;
            this.method = method;
            this.accessible = accessible;
            this.paramIndex = paramIndex;
        }

        @Override
        public String toString() {
            return "MethodMeta{" +
                    "share=" + share +
                    ", method=" + method +
                    ", accessible=" + accessible +
                    ", paramIndex=" + paramIndex +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "MethodReflect{" +
                "name='" + name + '\'' +
                ", enable=" + enable +
                ", share=" + share +
                ", clazz=" + clazz +
                ", obj=" + obj +
                ", emMap=" + emMap +
                ", eurMap=" + eurMap +
                ", rmMap=" + rmMap +
                ", rurMap=" + rurMap +
                ", defaultMap=" + defaultMap +
                '}';
    }
}
