package com.example.core.utils;

public class CustomUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
//        D.e("uncaughtException==>" + e.getMessage());
        e.printStackTrace();
    }
}
