package com.example.core.utils;

import java.util.List;

public class CollectionUtils {
    public static <T> boolean isEmpty(List<T> list) {
        return list == null || list.isEmpty();
    }
}
