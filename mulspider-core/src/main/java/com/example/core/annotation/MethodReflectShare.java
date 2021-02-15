package com.example.core.annotation;

import java.util.HashMap;
import java.util.Map;

/**
 * wrap all share method
 * */
public class MethodReflectShare {
    public Map<String, MethodReflect.MethodMeta> emMap = new HashMap<>(); //@ExtractMethod
    public Map<String, MethodReflect.MethodMeta> eurMap = new HashMap<>();  //..
    public Map<String, MethodReflect.MethodMeta> rmMap = new HashMap<>();
    public Map<String, MethodReflect.MethodMeta> rurMap = new HashMap<>();
}
