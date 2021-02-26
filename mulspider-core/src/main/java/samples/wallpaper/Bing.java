package samples.wallpaper;

import com.example.core.annotation.ExtractMethod;
import com.example.core.annotation.Spider;
import com.example.core.context.Config;
import com.example.core.extract.ExtractUtils;
import com.example.core.models.Request;
import com.example.core.models.Response;
import com.example.core.models.Result;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Spider(name = Bing.NAME, enable = false)
public class Bing extends WPTemp {
    public static final String NAME = "Bing";

    public Bing() {
        logger = LoggerFactory.getLogger(this.getClass());
        baseUrl = "https://bing.ioliu.cn/?p=";
        infoMethods = new String[]{NAME + EXTRACT_INFO, WallPaperResult.WallPaperResult};
        itemMethods = new String[]{NAME + EXTRACT_ITEM};
    }

    @Override
    protected Config config(Config config) {
        config.downThreadCount = 1;
        config.breakpoint = true;
        return config;
    }

    @ExtractMethod(methods = {NAME + EXTRACT_ITEM})
    private Result extractItem(Response response) {
        List<String> urls = response.eval("//div[@class='item']/div/a/@href");

        Result resTmp;
        if ((resTmp = duplicate(response, urls, true)) != null)
            return resTmp;

        List<String> tags = response.eval("//div[@class='description']/h3/text()");
        if (urls.size() != tags.size())
            throw new RuntimeException("获取数量错误 ==>" + response.request.url);
        List<String> thumbnails = response.eval("//img[@class='progressive__img progressive--not-loaded']/@src");
        List<String> views = response.eval("//p[@class='view']/em/text()");
        List<String> favs = response.eval("//span[@class='ctrl heart']/em/text()");

        int urlIndex = 0;
        for (String url : urls) {
            Request request = new Request(name);
            request.url = response.request.getSite() + url;
            request.method = infoMethods;

            String tag = tags.get(urlIndex);
            if (StringUtils.isNotEmpty(tag))
                request.meta.put(TAGS, tag);
            if (!thumbnails.get(urlIndex).isEmpty())
                request.meta.put(THUM, thumbnails.get(urlIndex));
            request.meta.put("view", views.get(urlIndex));
            request.meta.put("fav", favs.get(urlIndex));
            addTask(request);
            logger.debug("request==>" + count.incrementAndGet());
            urlIndex++;
        }
        return Result.makeIgnore();
    }

    @ExtractMethod(methods = {NAME + EXTRACT_INFO})
    private Result extractInfo(Response response) {
        Result result = Result.make(response.request);
        BingModel model = new BingModel();
        model.imgWrapUrl = response.request.url;

        if (response.request.meta.containsKey(TAGS)) {
            String tag = response.request.removeMeta(TAGS);
            String[] tagList = tag.split("，");
            if (!ArrayUtils.isEmpty(tagList) && tagList[tagList.length - 1].contains("("))
                tagList[tagList.length - 1] = tagList[tagList.length - 1].substring(0, tagList[tagList.length - 1].indexOf("(")).trim();
            model.tags = Arrays.asList(tagList);
        }
        model.views = response.request.removeMeta("view");
        model.fav = response.request.removeMeta("fav");

        model.imgUrl = response.evalSingle("//img[@class='target progressive__img progressive--not-loaded']/@data-progressive");
        model.imgW = "1920";
        model.imgH = "1080";
        model.thumbnail = (String) response.request.meta.get(THUM);
        model.thumbnailW = "427";
        model.thumbnailH = "320";
        response.request.meta.remove(THUM);

        result.result.put("result", model);
        logger.debug("result==>" + count.decrementAndGet());
        return result;
    }
}
