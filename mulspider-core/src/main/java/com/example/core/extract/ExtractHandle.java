package com.example.core.extract;

import com.example.core.models.Response;
import com.example.core.models.Result;

/**
 * 调用注解的解析方法
 * 调用xml文件的解析类
 * 调用默认的解析方法
 *
 * 优先级 从高到低
 * */
public abstract class ExtractHandle {
    public abstract Result extract(Response response);
}
