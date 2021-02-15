package com.example.core.models;

import java.util.HashMap;
import java.util.Map;

public class Result extends Task {
    public Request request;
    public Map<String, Object> result = new HashMap<>();

    public static Result make(Request request) {
        Result result = new Result();
        result.request = request;
        return result;
    }

    public static Result makeIgnore() {
        Result response = new Result();
        response.ignore = true;
        return response;
    }
}
