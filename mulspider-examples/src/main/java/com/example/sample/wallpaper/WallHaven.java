package com.example.sample.wallpaper;

import com.example.core.context.Config;
import com.example.core.context.SpiderApp;
import com.example.core.annotation.Spider;
import com.example.core.extract.ExtractUtils;
import com.example.core.models.Request;
import com.example.core.models.Response;
import com.example.core.models.Result;
import com.example.core.utils.D;

import java.util.concurrent.atomic.AtomicInteger;

@Spider(name = WallHaven.NAME)
public class WallHaven extends SpiderApp {
    public static final String NAME = "WallHaven";

    private static final String baseUrl = "https://wallhaven.cc/latest?page=";
    private AtomicInteger index = new AtomicInteger(1000);

    @Override
    protected Config config(Config config) {
        config.downThreadCount = 3;
        config.breakpoint = true;
        return config;
    }

    @Override
    public void init() {
        Integer pindex = dbManager.get("pageIndex");
        if (pindex != null && pindex > 0)
            index.set(pindex);
        D.d("init pindex==>" + pindex);
        Request request = new Request(name);
        request.url = baseUrl + index.get();
        request.method = new String[]{"default_savefile"};
        addTask(request, true);
    }

    @Override
    public Result extract(Response response) {
        Result result = Result.make(response.request);
        WHModel model = ExtractUtils.extract(response, WHModel.class);
        D.d(model);

        if (dupList(model.urls) != 0) {
            Request request = response.request.clone();
            request.url = baseUrl + index.incrementAndGet();
            dbManager.put("pageIndex", index.get());
            addTask(request);
        }
        result.result.put("111", index.get());

        D.d("extract index==>" + index.get());
        return result;
    }
}
