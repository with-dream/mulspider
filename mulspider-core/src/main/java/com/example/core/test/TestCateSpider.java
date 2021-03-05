package com.example.core.test;

import com.example.core.annotation.ExtractMethod;
import com.example.core.annotation.Spider;
import com.example.core.context.Config;
import com.example.core.models.Request;
import com.example.core.models.Response;
import com.example.core.models.Result;
import com.google.gson.Gson;
import org.slf4j.LoggerFactory;
import samples.wallpaper.WallPaperResult;

import java.util.List;

@Spider(name = TestCateSpider.NAME, enable = true)
public class TestCateSpider extends TempCateSpider {
    public static final String NAME = "TestCateSpider";
    private Gson gson = new Gson();

    public TestCateSpider() {
        logger = LoggerFactory.getLogger(this.getClass());
        homeUrl = "http://127.0.0.1:8080/test/cate";
        cateUrl = "http://127.0.0.1:8080/test/item/%s/%d";
    }

    @Override
    protected Config config(Config config) {
        config.downThreadCount = 10;
        config.extractThreadCount = 1;
        config.breakpoint = true;
        return config;
    }

    @ExtractMethod(methods = {NAME + EXTRACT_CATE})
    private Result extractCate(Response response) {
        List<String> cates = gson.fromJson(response.body, List.class);

        logger.info("cates==>" + cates);

        for (String cate : cates) {
            int cateIndex = initCateIndex(cate);
            createCateReq(cate, getCateUrl(cate, cateIndex));
        }
        return Result.makeIgnore();
    }

    @ExtractMethod(methods = {NAME + EXTRACT_ITEM})
    private Result extractItem(Response response) {
        List<String> urls = gson.fromJson(response.body, List.class);

        logger.info("==>" + urls.size() + "  " + urls.get(0));

        if (!dupUrls(response, urls, true))
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
