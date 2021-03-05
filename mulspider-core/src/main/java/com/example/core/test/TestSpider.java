package com.example.core.test;

import com.example.core.annotation.ExtractMethod;
import com.example.core.annotation.Spider;
import com.example.core.context.Config;
import com.example.core.extract.ExtractUtils;
import com.example.core.models.Request;
import com.example.core.models.Response;
import com.example.core.models.Result;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.slf4j.LoggerFactory;
import samples.wallpaper.BadfonModel;
import samples.wallpaper.WPTemp;
import samples.wallpaper.WallPaperResult;
import samples.wallpaper.WallPaperResultModel;

import java.util.ArrayList;
import java.util.List;

@Spider(name = TestSpider.NAME, enable = false)
public class TestSpider extends TempSpider {
    public static final String NAME = "TestSpider";
    private Gson gson = new Gson();

    public TestSpider() {
        logger = LoggerFactory.getLogger(this.getClass());
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
        Result result = Result.make(response.request);
        String body = response.body;

//        logger.info("body  end==>" + body);
        result.put(RESULT, body);
        logger.info("extractInfo==>" + count.decrementAndGet());

//        return result;
        return Result.makeIgnore();
    }
}
