package samples.wallpaper;

import com.example.core.annotation.ExtractMethod;
import com.example.core.annotation.Spider;
import com.example.core.context.Config;
import com.example.core.extract.ExtractUtils;
import com.example.core.models.Request;
import com.example.core.models.Response;
import com.example.core.models.Result;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

@Spider(name = WallpaperMaiden.NAME, enable = false)
public class WallpaperMaiden extends WPTemp {
    public static final String NAME = "WallpaperMaiden";
    private static final String EXTRACT_IMG = NAME + ".extractImg";

    public WallpaperMaiden() {
        logger = LoggerFactory.getLogger(this.getClass());
        baseUrl = "https://www.wallpapermaiden.com/?page=";
        infoMethods = new String[]{NAME + EXTRACT_INFO};
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
        List<String> urls = response.eval("//div[@class='wallpaperList']/div[@class='wallpaperBg']/a/@href");

        Result resTmp;
        if ((resTmp = duplicate(response, urls, false)) != null)
            return resTmp;

        List<String> tags = response.eval("//div[@class='wallpaperList']/div[@class='wallpaperBg']/a/@title");
        if (urls.size() != tags.size())
            throw new RuntimeException("获取数量错误 ==>" + response.request.url);
        List<String> thumbnails = response.eval("//div[@class='wallpaperList']/div[@class='wallpaperBg']/a/div[@class='wallpaperBgImage']/img/@src");

        int urlIndex = 0;
        for (String url : urls) {
            Request request = new Request(name);
            request.url = url;
            request.method = infoMethods;

            String tag = tags.get(urlIndex);
            if (StringUtils.isNotEmpty(tag))
                request.meta.put(TAGS, tag);
            if (!thumbnails.get(urlIndex).isEmpty())
                request.meta.put(THUM, response.getSite() + thumbnails.get(urlIndex));
            addTask(request);
            logger.debug("request==>" + count.incrementAndGet());
            urlIndex++;
        }
        return Result.makeIgnore();
    }

    @ExtractMethod(methods = {NAME + EXTRACT_INFO})
    private Result extractInfo(Response response) {
        WallpaperMaidenModel model = ExtractUtils.extract(response, WallpaperMaidenModel.class);
        model.imgWrapUrl = response.request.url;

        if (response.request.meta.containsKey(TAGS)) {
            String tag = response.request.removeMeta(TAGS);
            model.tags = Arrays.asList(tag.split(","));
            response.request.meta.remove(TAGS);
        }

        String[] wh = model.imgW.split("x");
        model.imgW = wh[0];
        model.imgH = wh[1];
        model.thumbnail = response.request.removeMeta(THUM);
        model.thumbnailW = "270";
        model.thumbnailH = "170";

        response.request.meta.put(RESULT, model);
        response.request.url = model.imgUrl;
        response.request.method = new String[]{EXTRACT_IMG, WallPaperResult.WallPaperResult};
        addTask(response.request);

        return Result.makeIgnore();
    }

    @ExtractMethod(methods = {EXTRACT_IMG})
    private Result extractImage(Response response) {
        Result result = Result.make(response.request);
        WallpaperMaidenModel model = response.request.removeMeta(RESULT);
        model.imgUrl = response.evalFirst("//div[@class='wpBig wpBigFull']/a/img/@src");

        WallPaperResultModel resModel = model.cover();
        result.result.put(RESULT, resModel);
        downFile(resModel);

        logger.debug("result==>" + count.decrementAndGet());
        return result;
    }
}
