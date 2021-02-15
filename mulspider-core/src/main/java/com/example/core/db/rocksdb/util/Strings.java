package com.example.core.db.rocksdb.util;

public class Strings {
    public static boolean nullOrEmpty(String directory) {
        return directory == null || directory.trim().isEmpty();
    }
}
