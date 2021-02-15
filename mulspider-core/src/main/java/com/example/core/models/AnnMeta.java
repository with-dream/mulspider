package com.example.core.models;

import com.example.core.context.SpiderApp;

public class AnnMeta {
    public enum AppWorkMeta {
        WORK_META_NONE, WORK_META_ENABLE, WORK_META_DISABLE
    }

    public static class AppMeta {
        public SpiderApp app;
        public String name;
        public boolean enable;
        /**
         * SpiderApp has annotation Spider and SpiderWork
         * than ReflectInit.appMap record the SpiderApp
         * if SpiderWork.enable==true or Spider.enable==true the MethodReflect methods is take effect
         */
        public AppWorkMeta workMetaFlag;

        public AppMeta() {
        }

        public AppMeta(SpiderApp app, String name, boolean enable, AppWorkMeta workFlag) {
            this.app = app;
            this.name = name;
            this.enable = enable;
            this.workMetaFlag = workFlag;
        }

        @Override
        public String toString() {
            return "AppMeta{" +
                    "name='" + name + '\'' +
                    ", enable=" + enable +
                    ", workMetaFlag=" + workMetaFlag +
                    ", app=" + app +
                    '}';
        }
    }

    public static class WorkMeta {
        public String name;
        public boolean singleton;
        public boolean enable;
        public Object object;
        public Class<?> clazz;

        public WorkMeta() {
        }

        public WorkMeta(String name, boolean singleton, Class<?> clazz, Object object, boolean enable) {
            this.name = name;
            this.clazz = clazz;
            this.object = object;
            this.singleton = singleton;
            this.enable = enable;
        }

        @Override
        public String toString() {
            return "WorkMeta{" +
                    "name='" + name + '\'' +
                    ", clazz=" + clazz +
                    ", singleton=" + singleton +
                    ", enable=" + enable +
                    '}';
        }
    }
}
