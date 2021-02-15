package com.example.core.utils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class ExceptionThreadFactory implements ThreadFactory {
    private Thread.UncaughtExceptionHandler handler;

    public ExceptionThreadFactory(Thread.UncaughtExceptionHandler handler) {
        this.handler = handler;
    }

    ThreadFactory tf = Executors.defaultThreadFactory();

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = tf.newThread(r);
        thread.setUncaughtExceptionHandler(this.handler);
        return thread;
    }
}
