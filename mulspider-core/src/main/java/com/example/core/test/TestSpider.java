package com.example.core.test;

import com.example.core.annotation.ExtractMethod;
import com.example.core.annotation.Spider;
import com.example.core.context.Config;
import com.example.core.download.DownloadWork;
import com.example.core.extract.ExtractUtils;
import com.example.core.models.Request;
import com.example.core.models.Response;
import com.example.core.models.Result;
import com.example.core.utils.Constant;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.slf4j.LoggerFactory;
import samples.wallpaper.BadfonModel;
import samples.wallpaper.WPTemp;
import samples.wallpaper.WallPaperResult;
import samples.wallpaper.WallPaperResultModel;

import java.util.ArrayList;
import java.util.List;

@Spider(name = TestSpider.NAME, enable = true)
public class TestSpider extends TempSpider {
    public static final String NAME = "TestSpider";
    private Gson gson = new Gson();

    public TestSpider() {
        logger = LoggerFactory.getLogger(this.getClass());
        downType = DownloadWork.DownType.CLIENT_WEBDRIVER;
        baseUrl = "http://127.0.0.1:8080/test/item/c1/%d";
        itemMethods = new String[]{NAME + EXTRACT_ITEM};
        infoMethods = new String[]{NAME + EXTRACT_INFO, WallPaperResult.WallPaperResult};
    }

    @Override
    protected Config config(Config config) {
        config.downThreadCount = 30;
        config.extractThreadCount = 3;
        config.breakpoint = true;
        return config;
    }

    @ExtractMethod(methods = {NAME + EXTRACT_ITEM}, lock = false)
    private Result extractItem(Response response) {
        if (response.body.contains("<pre"))
            response.body = response.evalFirst("//pre/text()");
        List<String> urls = gson.fromJson(response.body, List.class);

        logger.info("==>" + urls.size() + "  " + urls.get(0));

        if (!duplicate(response, urls, response.getSite(), false))
            return Result.makeIgnore();

        logger.info("end==>" + urls.size());
        for (String url : urls) {
            Request request = new Request(name);
            request.url = response.getSite() + url;
            request.method = infoMethods;

//            addTask(request);
            if (addTask(request))
                logger.info("extractItem==>" + count.incrementAndGet());
        }

        return Result.makeIgnore();
    }

    @ExtractMethod(methods = {NAME + EXTRACT_INFO})
    private Result extractInfo(Response response) {
//        logger.info("body  end==>" + body);

        logger.info("extractInfo==>" + count.decrementAndGet());
        String url = response.request.url;
        url = url.substring(url.lastIndexOf("/") + 1);

        downFile("http://127.0.0.1:8080/download/" + url, url, ".mp4");

//        return result;
        return Result.makeIgnore();
    }

    @ExtractMethod(methods = {NAME + DOWN_FILE})
    private Result extractDownfile(Response response) {
        logger.info("donw==>{}  res:{}", response.request.getMeta(Constant.DOWN_FILE_PATH), response.getMeta(Constant.DOWN_FILE_RES));
        return Result.makeIgnore();
    }
}
