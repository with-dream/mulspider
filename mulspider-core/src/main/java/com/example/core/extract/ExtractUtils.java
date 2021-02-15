package com.example.core.extract;

import com.example.core.annotation.SField;
import com.example.core.models.Response;
import org.apache.commons.lang3.StringUtils;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ExtractUtils {

    public static <T> T extract(Response response, Class<?> clazz) {
        return extract(response.xpath(), response.jsoup(), clazz);
    }

    public static <T> T extract(TagNode tagNode, Document document, Class<?> clazz) {
        T instance = null;
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                SField ann = null;
                if ((ann = field.getAnnotation(SField.class)) != null) {
                    List<String> obj = null;
                    String xpath = ann.xpath();
                    String jsoup = ann.jsoup();
                    String attr = ann.jsoupAttr();

                    //create data from html
                    if (StringUtils.isNotEmpty(xpath) && tagNode != null) {
                        if (tagNode == null)
                            throw new RuntimeException("annotation SField xpath param  tagNode must not null");

                        obj = new ArrayList<>();
                        for (Object item : tagNode.evaluateXPath(xpath))
                            obj.add((String) item);
                    } else if (StringUtils.isNotEmpty(jsoup)) {
                        if (document == null)
                            throw new RuntimeException("annotation SField jsoup param  document must not null");

                        Elements elements = document.select(jsoup);
                        if (StringUtils.isNotEmpty(attr))
                            obj = elements.eachAttr(attr);
                        else
                            obj = elements.eachText();
                    } else {
                        throw new RuntimeException("annotation SField param err");
                    }

                    if (!field.isAccessible())
                        field.setAccessible(true);

                    if (instance == null) {
                        try {
                            instance = (T) clazz.newInstance();
                        } catch (InstantiationException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }

                    List<Class<?>> list = null;
                    if (field.getType().equals(List.class))
                        list = new ArrayList<>();

                    if (obj != null && !obj.isEmpty()) {
                        if (list == null) {
                            coverType(field, instance, list, field.getType(), obj);
                        } else {
                            Type genericType = field.getGenericType();
                            if (genericType instanceof ParameterizedType) {
                                ParameterizedType pt = (ParameterizedType) genericType;
                                Class<?> actualTypeArgument = (Class<?>) pt.getActualTypeArguments()[0];
                                coverType(field, instance, list, actualTypeArgument, obj);
                            }
                            field.set(instance, list);
                        }
                    }
                }
            }
        } catch (XPatherException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return instance;
    }

    private static void coverType(Field field, Object object, List list, Class<?> clazz, List<String> info) throws IllegalAccessException {
        if (clazz == String.class) {
            if (list != null) {
                list.addAll(info);
            } else {
                field.set(object, info.get(0));
            }
        } else if (clazz.equals(Integer.TYPE) || clazz.equals(Integer.class)) {
            if (list != null) {
                for (String item : info)
                    list.add(Integer.parseInt(item));
            } else {
                field.setInt(object, Integer.parseInt(info.get(0)));
            }
        } else if (clazz.equals(Byte.TYPE) || clazz.equals(Byte.class)) {
            if (list != null) {
                for (String item : info)
                    list.add(Byte.parseByte(item));
            } else {
                field.setByte(object, Byte.parseByte(info.get(0)));
            }
        } else if (clazz.equals(Short.TYPE) || clazz.equals(Short.class)) {
            if (list != null) {
                for (String item : info)
                    list.add(Short.parseShort(item));
            } else {
                field.setShort(object, Short.parseShort(info.get(0)));
            }
        } else if (clazz.equals(Long.TYPE) || clazz.equals(Long.class)) {
            if (list != null) {
                for (String item : info)
                    list.add(Long.parseLong(item));
            } else {
                field.setLong(object, Long.parseLong(info.get(0)));
            }
        } else if (clazz.equals(Float.TYPE) || clazz.equals(Float.class)) {
            if (list != null) {
                for (String item : info)
                    list.add(Float.parseFloat(item));
            } else {
                field.setFloat(object, Float.parseFloat(info.get(0)));
            }
        } else if (clazz.equals(Double.TYPE) || clazz.equals(Double.class)) {
            if (list != null) {
                for (String item : info)
                    list.add(Double.parseDouble(item));
            } else {
                field.setDouble(object, Double.parseDouble(info.get(0)));
            }
        } else if (clazz.equals(Boolean.TYPE) || clazz.equals(Boolean.class)) {
            if (list != null) {
                for (String item : info)
                    list.add(Boolean.parseBoolean(item));
            } else {
                field.setBoolean(object, Boolean.parseBoolean(info.get(0)));
            }
        } else {
            throw new RuntimeException("not support class type");
        }
    }
}
