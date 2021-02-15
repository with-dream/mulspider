package com.example.core.models;

import org.apache.commons.lang3.ArrayUtils;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Response extends Task {
    public Request request;

    public Map<String, String> headers;
    public String body;
    public String resCharset;
    private transient Document document;
    private transient TagNode tagNode;

    public int resCode;
    public String resMsg;

    public Document jsoup() {
        if (document == null)
            document = Jsoup.parse(body);
        return document;
    }

    public TagNode xpath() {
        if (tagNode == null) {
            HtmlCleaner hc = new HtmlCleaner();
            tagNode = hc.clean(body);
        }
        return tagNode;
    }

    public <T> List<T> eval(String xpath) {
        List<T> list = new ArrayList<>();
        try {
            Object[] res = xpath().evaluateXPath(xpath);
            if (ArrayUtils.isEmpty(res))
                return null;
            for (Object obj : res)
                list.add((T) obj);
        } catch (XPatherException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static Response make(Request request, int resCode) {
        Response response = new Response();
        response.request = request;
        response.resCode = resCode;
        return response;
    }

    public static Response make(Request request, String body, int resCode) {
        Response response = Response.make(request, resCode);
        response.body = body;
        return response;
    }

}
