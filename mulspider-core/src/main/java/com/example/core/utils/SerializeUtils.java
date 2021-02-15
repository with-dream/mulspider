package com.example.core.utils;

import java.io.*;

public class SerializeUtils {
    public static byte[] serialize(Object object) {
        try {
            ByteArrayOutputStream byam = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(byam);
            oos.writeObject(object);
            return byam.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
