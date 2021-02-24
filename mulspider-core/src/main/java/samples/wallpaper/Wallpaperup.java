package samples.wallpaper;

import com.example.core.annotation.ExtractMethod;
import com.example.core.annotation.Spider;
import com.example.core.context.Config;
import com.example.core.extract.ExtractUtils;
import com.example.core.models.Request;
import com.example.core.models.Response;
import com.example.core.models.Result;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

//remote-loader
@Spider(name = Wallpaperup.NAME, enable = false)
public class Wallpaperup extends WPTemp {
    public static final String NAME = "Wallpaperup";
    private static final String urlEnd = "/date_added/desc";

    public Wallpaperup() {
        logger = LoggerFactory.getLogger(this.getClass());
        baseUrl = "https://www.wallpaperup.com/latest/";
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
    protected String getUrl() {
        return baseUrl + index.decrementAndGet() + urlEnd;
    }

    @ExtractMethod(methods = {NAME + EXTRACT_ITEM})
    private Result extractItem(Response response) {
        List<String> urls = response.eval("//figure[@class='black']/a/@href");

        Result resTmp;
        if ((resTmp = duplicate(response, urls, true)) != null)
            return resTmp;
        List<String> thumbnails = response.eval("//figure[@class='black']/a/img/@data-src");

        int urlIndex = 0;
        for (String url : urls) {
            Request request = new Request(name);
            request.url = response.request.getSite() + url;
            request.method = infoMethods;

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
