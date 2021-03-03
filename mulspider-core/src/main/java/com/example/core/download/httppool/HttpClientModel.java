package com.example.core.download.httppool;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.CloseableHttpClient;

public class HttpClientModel {
    public CloseableHttpClient client;
    public CookieStore cookieStore;
}
