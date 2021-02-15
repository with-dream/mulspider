package com.example.core.download;

import com.example.core.models.Request;
import com.example.core.models.Response;

public abstract class DownloadHandle {
    public abstract Response down(Request request);
}
