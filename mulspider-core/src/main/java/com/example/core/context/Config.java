package com.example.core.context;

import com.example.core.db.DBConfig;

public class Config {
    public DBConfig dbConfig;
    public int downThreadCount = 1;
    public int extractThreadCount = 1;
    public int resultThreadCount = 1;
    public boolean breakpoint;
}
