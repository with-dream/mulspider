package com.example.core.download;

import com.example.core.models.Request;
import com.example.core.models.Response;

import java.io.IOException;

public abstract class DownloadHandle {
    public abstract Response down(Request request);
    protected DownloadTimeout downloadTimeout;

    public interface DownloadTimeout {
        void timeOut(Request request, IOException e);
    }
}
