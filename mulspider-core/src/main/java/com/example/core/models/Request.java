package com.example.core.models;

import com.example.core.download.DownloadWork;
import com.example.core.download.selenium.SeleniumDownloader;
import com.example.core.utils.StrUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class Request extends Task {
    public final static String POST = "POST";
    public final static String GET = "GET";

    public enum ParamType {
        PARAM_JSON("application/json"), PARAM_XML("text/xml"), PARAM_FORM("application/x-www-form-urlencoded"), PARAM_MULTIPART("multipart/form-data");

        private String type;

        ParamType(String type) {
        }

        public String getType() {
            return type;
        }
    }

    public String name;
    public String url;
    public String[] method;
    private String site;
    public String reqCharset = "utf-8";
    public String reqType;
    public Map<String, String> headers;
    public Map<String, String> params;
    public ParamType paramType;
    public Map<String, String> cookie;
    public String userAgent;

    public String proxyUrl;
    public int proxyPort;
    public String proxyUserName;
    public String proxyPassword;

    public DownloadWork.DownType downType = DownloadWork.DownType.CLIENT_POOL;
    public int delayTime = 500;
    public SeleniumDownloader.DriverType driver;

    public boolean force;   //是否去重

    //失败重试
    public int retryCount = 5;
    public int timeOut = 30 * 1000;

    public Request(String name) {
        this.name = name;
    }

    public String getSite() {
        return StringUtils.isEmpty(site) ? site = StrUtils.getBaseUrl(url) : site;
    }

    public void reset() {
        url = null;
        site = null;
    }

    public Request clone() {
        Request request = SerializationUtils.clone(this);
        request.reset();
        return request;
    }
}
