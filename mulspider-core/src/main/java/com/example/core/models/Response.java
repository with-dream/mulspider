package com.example.core.models;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Response extends Task {
    public Request request;

    public Map<String, String> headers;
    public Map<String, String> cookie;
    public String body;
    public String resCharset;
    private transient Document document;
    private transient TagNode tagNode;
    private transient JsonElement jsonElement;

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

    public JsonElement gson() {
        if (jsonElement == null)
            jsonElement = JsonParser.parseString(body);
        return jsonElement;
    }

    public List<String> soup(String jsoup, String attr) {
        List<String> list = new ArrayList<>();
        Elements eles = jsoup().select(jsoup);
        if (eles.isEmpty())
            return list;
        for (Element e : eles)
            if (StringUtils.isNotEmpty(attr))
                list.add(e.attr(attr));
            else
                list.add(e.text());
        return list;
    }

    public String soupFirst(String jsoup, String attr) {
        Elements eles = jsoup().select(jsoup);
        if (eles.isEmpty())
            return null;
        for (Element e : eles)
            if (StringUtils.isNotEmpty(attr))
                return e.attr(attr);
            else
                return e.text();
        return null;
    }

    public List<String> eval(String xpath) {
        List<String> list = new ArrayList<>();
        try {
            Object[] res = xpath().evaluateXPath(xpath);
            if (ArrayUtils.isEmpty(res))
                return list;
            for (Object obj : res)
                list.add(obj == null ? null : String.valueOf(obj).trim());
        } catch (XPatherException e) {
            e.printStackTrace();
        }

        return list;
    }

    public String evalFirst(String xpath) {
        try {
            Object[] res = xpath().evaluateXPath(xpath);
            if (ArrayUtils.isEmpty(res))
                return null;
            return String.valueOf(res[0]);
        } catch (XPatherException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getSite() {
        return request.getSite();
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
