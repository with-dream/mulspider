package com.example.core.utils;

import com.example.core.context.Context;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;

public class FileUtils {
    public static String getResourcePath(String filePath) {
        URL url = Context.instance().getClass().getClassLoader().getResource(filePath);
        if (url != null && StringUtils.isNotEmpty(url.getPath()))
            return url.getPath();
        return null;
    }
}
