package com.example.core.utils;

public class Constant {
    public static final boolean DEBUG = true;
    public static final String DEFAULT_EXTRACT_NAME = "extract";
    public static final String DEFAULT_RESULT_NAME = "result";
    public static final String DEFAULT_EXTRACT = "default_" + DEFAULT_EXTRACT_NAME;
    public static final String DEFAULT_RESULT = "default_" + DEFAULT_RESULT_NAME;

    public static final String REQUEST = "request";
    public static final String EXTRACT = "extract";
    public static final String RESULT = "result";
    public static final String DOWN_FILE = "__down_file__";
    public static final String DOWN_FILE_RES = "__down_file_response__";

    public static final int DB_ROCK = 0;
    public static final int DB_MEMORY = 1;
    public static final int DB_REDIS = 2;

    public static final int DUPLICATE_NONE = -1;
    public static final int DUPLICATE_MD5 = 0;
    public static final int DUPLICATE_BF = 1;

    public static final int MIN_DOWN_DELAY_TIME = 800;
    public static final int EMPTY_DELAY_TIME = 2000;

    public static final String RESULT_FILE_PATH = "./file";
}
