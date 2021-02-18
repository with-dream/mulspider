package samples.wallpaper;

import com.example.core.annotation.ExtractMethod;
import com.example.core.annotation.Spider;
import com.example.core.context.Config;
import com.example.core.context.SpiderApp;
import com.example.core.download.DownloadWork;
import com.example.core.extract.ExtractUtils;
import com.example.core.models.Request;
import com.example.core.models.Response;
import com.example.core.models.Result;
import com.example.core.utils.D;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Spider(name = WP10.NAME)
public class WP10 extends SpiderApp {
    public static final String NAME = "WP10";
    private static final String EXTRACT_INFO = "WP10.extractInfo";

    private static final String baseUrl = "https://10wallpaper.com/List_wallpapers/page/";
    private AtomicInteger index = new AtomicInteger(1);
    private String[] infoMethods = new String[]{EXTRACT_INFO, WallPaperResult.WallPaperResult};
    private String[] listMethods = new String[]{"WP10.extractItem"};
    AtomicInteger count = new AtomicInteger(0);

    @Override
    protected Config config(Config config) {
        config.downThreadCount = 1;
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

    @ExtractMethod(methods = {"WP10.extractItem"})
    private Result extractItem(Response response) {
        List<String> urls = response.eval("//*[@id=\"pics-list\"]/p/a/@href");
        if (urls == null || urls.isEmpty()) {
            D.e("err==>" + response.request.url);
            return Result.makeIgnore();
        }
        if (dupList(response.request.getSite(), urls) != 0) {
            Request request = response.request.clone();
            int indexTmp = index.incrementAndGet();
            if (indexTmp != 10) {
                request.url = baseUrl + indexTmp;
                request.method = listMethods;
                dbManager.put("pageIndex", index.get());
                addTask(request);
            }
        }

        List<String> tags = response.eval("//*[@id=\"pics-list\"]/p/a/img/@alt");
        if (urls.size() != tags.size())
            throw new RuntimeException("获取数量错误 ==>" + response.request.url);

        int urlIndex = 0;
        for (String url : urls) {
            Request request = new Request(name);
            request.url = response.request.getSite() + url;
            request.method = infoMethods;

            String tag = tags.get(urlIndex++);
            if (StringUtils.isNotEmpty(tag))
                request.meta.put("tags", tag);
            addTask(request);
            D.d("request==>" + count.incrementAndGet());
        }
        return Result.makeIgnore();
    }

    @ExtractMethod(methods = {EXTRACT_INFO})
    private Result extractInfo(Response response) {
        Result result = Result.make(response.request);
        WP10Model model = ExtractUtils.extract(response, WP10Model.class);
        model.imgWrapUrl = response.request.url;

        if (response.request.meta.containsKey("tags")) {
            String tag = (String) response.request.meta.get("tags");
            String[] tagList = tag.split(" ");
            if (tagList.length < 3)
                tagList = tag.split("-");
            if (tagList.length < 3)
                throw new RuntimeException("tag错误==>" + tag);
            model.tags = new ArrayList<>();
            String views = null;
            if (tagList.length != 0) {
                for (String t : tagList) {
                    if (t.toLowerCase().contains("views:")) {
                        views = t.toLowerCase();
                        continue;
                    }
                    model.tags.add(t);
                }
            }
            response.request.meta.remove("tags");

            if (views != null) {
                model.views = views.split(":")[1];
            }
        }
        model.imgUrl = response.request.getSite() + model.imgUrl;

        result.result.put("result", model);
        return result;
    }

    @Override
    protected void requestTimeout(Request request, IOException e) {
        super.requestTimeout(request, e);
        D.e("requestTimeout==>" + e.getMessage());
    }
}
