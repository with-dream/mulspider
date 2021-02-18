package com.example.core.context;

import com.example.core.db.DBConfig;
import com.example.core.download.DownloadHandle;

public class Config {
    public DBConfig dbConfig;
    public int downThreadCount = 1;
    public int extractThreadCount = 1;
    public int resultThreadCount = 1;
    public boolean breakpoint;

    public DownloadHandle.DownloadTimeout downloadTimeout;
}
