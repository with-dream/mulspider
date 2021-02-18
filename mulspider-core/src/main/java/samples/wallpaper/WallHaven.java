package samples.wallpaper;

import com.example.core.annotation.ExtractMethod;
import com.example.core.context.Config;
import com.example.core.context.SpiderApp;
import com.example.core.annotation.Spider;
import com.example.core.extract.ExtractUtils;
import com.example.core.models.Request;
import com.example.core.models.Response;
import com.example.core.models.Result;
import com.example.core.utils.D;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Spider(name = WallHaven.NAME, enable = false)
public class WallHaven extends SpiderApp {
    public static final String NAME = "WallHaven";
    private static final String EXTRACT_INFO = "WallHaven.extractInfo";

    private static final String baseUrl = "https://wallhaven.cc/latest?page=";
    private AtomicInteger index = new AtomicInteger(1);
    private String[] infoMethods = new String[]{EXTRACT_INFO, WallPaperResult.WallPaperResult};
    private String[] listMethods = new String[]{"WallHaven.extractItem"};
    AtomicInteger count = new AtomicInteger(0);

    @Override
    protected Config config(Config config) {
        config.downThreadCount = 2;
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
        request.method = listMethods;
        request.url = baseUrl + index.get();
        addTask(request, true);
    }

    @ExtractMethod(methods = {"WallHaven.extractItem"})
    private Result extractItem(Response response) {
        List<String> urls = response.eval("//*[@id=\"thumbs\"]/section[1]/ul/li/figure/a/@href");
        if (urls == null || urls.isEmpty()) {
            D.e("err==>" + response.request.url);
            return Result.makeIgnore();
        }
        if (dupList(urls) != 0) {
            Request request = response.request.clone();
            int indexTmp = index.incrementAndGet();
            if (indexTmp != 10) {
                request.url = baseUrl + indexTmp;
                request.method = listMethods;
                dbManager.put("pageIndex", index.get());
                addTask(request);
            }
        }

        for (String url : urls) {
            Request request = new Request(name);
            request.url = url;
            request.method = infoMethods;
            addTask(request);
            D.d("request==>" + count.incrementAndGet());
        }
        return Result.makeIgnore();
    }

    @ExtractMethod(methods = {EXTRACT_INFO})
    private Result extractInfo(Response response) {
        Result result = Result.make(response.request);
        WHModel model = ExtractUtils.extract(response, WHModel.class);
        model.imgWrapUrl = response.request.url;
        result.result.put("result", model);
        return result;
    }

    @Override
    protected void requestTimeout(Request request, IOException e) {
        super.requestTimeout(request, e);
        D.e("requestTimeout==>" + e.getMessage());
    }
}
