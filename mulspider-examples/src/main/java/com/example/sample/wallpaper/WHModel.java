package com.example.sample.wallpaper;

import com.example.core.annotation.SField;

import java.io.Serializable;
import java.util.List;

public class WHModel implements Serializable {
    private static final long serialVersionUID = 673293417482457120L;

    @SField(jsoup = "#thumbs > section > ul > li > figure > div > a.jsAnchor.overlay-anchor.wall-favs")
    List<String> urls;

    @SField(jsoup = "#thumbs > section > ul > li:nth-child(2) > figure > a", jsoupAttr = "href")
    String url;

    @Override
    public String toString() {
        return "WHModel{" +
                "urls=" + urls +
                ", url='" + url + '\'' +
                '}';
    }
}
