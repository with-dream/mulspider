package com.example.core.utils;

import org.apache.commons.lang3.reflect.ConstructorUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectUtils {

    public static <T> T invoke(Object obj, Method md, Object[] param, boolean accessible) {
        if (accessible)
            md.setAccessible(true);
        try {
            return (T) md.invoke(obj, param);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static <T> T invokeDefault(Object obj, Method md, boolean accessible) {
        Class<?>[] clazz = md.getParameterTypes();
        Object[] param = new Object[clazz.length];
        for (int i = 0; i < clazz.length; i++)
            param[i] = ReflectUtils.getDefaultValue(clazz[i]);

        return invoke(obj, md, param, accessible);
    }

    public static <T> T instance(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getConstructor();
            if (constructor == null)
                throw new RuntimeException("class " + clazz.getName() + " has no default constructor");
            return (T) constructor.newInstance();
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getDefaultValue(Class<?> clazz) {
        if (clazz == byte.class || clazz == short.class || clazz == int.class || clazz == long.class
                || clazz == float.class || clazz == double.class)
            return 0;
        else if (clazz == boolean.class)
            return false;
        else if (clazz == char.class)
            return ' ';
        else if (clazz == String.class)
            return "";
        else return null;
    }
}
