package samples.wallpaper;

import com.example.core.annotation.ExtractMethod;
import com.example.core.annotation.Spider;
import com.example.core.context.Config;
import com.example.core.download.DownloadWork;
import com.example.core.extract.ExtractUtils;
import com.example.core.models.Request;
import com.example.core.models.Response;
import com.example.core.models.Result;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Spider(name = Wallpaperup.NAME, enable = true)
public class Wallpaperup extends WPTemp {
    public static final String NAME = "Wallpaperup";

    public Wallpaperup() {
        logger = LoggerFactory.getLogger(this.getClass());
        baseUrl = "https://www.wallpaperup.com";
        infoMethods = new String[]{NAME + EXTRACT_INFO, WallPaperResult.WallPaperResult};
        listMethods = new String[]{NAME + EXTRACT_ITEM};
    }

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
        logger.info("init pindex==>" + pindex);
        Request request = new Request(name);
        if (downType == DownloadWork.DownType.CLIENT_POOL)
            request.httpPool();
        else if (downType == DownloadWork.DownType.CLIENT_WEBDRIVER)
            request.headless();
        request.method = listMethods;
        request.url = baseUrl;
        addTask(request, true);
    }

    @ExtractMethod(methods = {NAME + EXTRACT_ITEM})
    private Result extractItem(Response response) {
        List<String> urls = response.eval("//*[@id=\"pics-list\"]/p/a/@href");

        Result resTmp;
        if ((resTmp = duplicate(response, urls, true)) != null)
            return resTmp;

        List<String> tags = response.eval("//*[@id=\"pics-list\"]/p/a/img/@alt");
        if (urls.size() != tags.size())
            throw new RuntimeException("获取数量错误 ==>" + response.request.url);
        List<String> thumbnails = response.eval("//*[@id=\"pics-list\"]/p/a/img/@src");

        int urlIndex = 0;
        for (String url : urls) {
            Request request = new Request(name);
            request.url = response.request.getSite() + url;
            request.method = infoMethods;

            String tag = tags.get(urlIndex);
            if (StringUtils.isNotEmpty(tag))
                request.meta.put(TAGS, tag);
            if (!thumbnails.get(urlIndex).isEmpty())
                request.meta.put(THUM, response.request.getSite() + thumbnails.get(urlIndex));
            addTask(request);
            logger.debug("request==>" + count.incrementAndGet());
            urlIndex++;
        }
        return Result.makeIgnore();
    }

    @ExtractMethod(methods = {NAME + EXTRACT_INFO})
    private Result extractInfo(Response response) {
        Result result = Result.make(response.request);
        WP10Model model = ExtractUtils.extract(response, WP10Model.class);
        model.imgWrapUrl = response.request.url;

        if (response.request.meta.containsKey(TAGS)) {
            String tag = (String) response.request.meta.get(TAGS);
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
            response.request.meta.remove(TAGS);

            if (views != null) {
                model.views = views.split(":")[1];
            }
        }
        model.imgUrl = response.request.getSite() + model.imgUrl;
        String[] wh = model.imgW.split("x");
        model.imgW = wh[0];
        model.imgH = wh[1];
        model.thumbnail = (String) response.request.meta.get(THUM);
        model.thumbnailW = "400";
        model.thumbnailH = "225";
        response.request.meta.remove(THUM);

        result.result.put("result", model);
        logger.debug("result==>" + count.decrementAndGet());
        return result;
    }
}
