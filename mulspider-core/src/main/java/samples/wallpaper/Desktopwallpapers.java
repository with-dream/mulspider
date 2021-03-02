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
import java.util.HashMap;
import java.util.List;

@Spider(name = Desktopwallpapers.NAME, enable = false)
public class Desktopwallpapers extends WPTemp {
    public static final String NAME = "Desktopwallpapers";

    public Desktopwallpapers() {
        logger = LoggerFactory.getLogger(this.getClass());
        baseUrl = "http://desktopwallpapers.org.ua/page/%d";
        infoMethods = new String[]{NAME + EXTRACT_INFO, WallPaperResult.WallPaperResult};
        itemMethods = new String[]{NAME + EXTRACT_ITEM};
        headers = new HashMap<>();
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        headers.put("Accept-Encoding", "gzip, deflate");
    }

    @Override
    protected Config config(Config config) {
        config.downThreadCount = 1;
        config.breakpoint = true;
        return config;
    }

    @Override
    protected String getUrl() {
        return super.getUrl() + "/";
    }

    @ExtractMethod(methods = {NAME + EXTRACT_ITEM})
    private Result extractItem(Response response) {
        List<String> urls = response.eval("//*[@id=\"dle-content\"]/div[@class='photo-list-item']/a/@href");

        Result resTmp;
        if ((resTmp = duplicate(response, urls, false)) != null)
            return resTmp;

        List<String> thumbnails = response.eval("//*[@id=\"dle-content\"]/div/a/img/@src");

        int urlIndex = 0;
        for (String url : urls) {
            Request request = new Request(name);
            request.url = url;
            request.method = infoMethods;

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
        Result result = Result.make(response.request);
        DesktopwallpaperModel model = ExtractUtils.extract(response, DesktopwallpaperModel.class);
        model.imgWrapUrl = response.request.url;

        model.imgUrl = response.getSite() + model.imgUrl;
        if (StringUtils.isNotEmpty(model.views)) {
            model.views = model.views.split(":")[1];
            if (StringUtils.isNotEmpty(model.views))
                model.views = model.views.trim();
        }

        if (StringUtils.isNotEmpty(model.fav)) {
            model.fav = model.fav.split(":")[1];
            if (StringUtils.isNotEmpty(model.fav)) {
                model.fav = model.fav.split("\\(")[0];
                model.fav = model.fav.trim();
            }
        }

        String[] wh = model.imgW.split("x");
        model.imgW = wh[0];
        model.imgH = wh[1];
        model.thumbnail = response.request.removeMeta(THUM);
        model.thumbnailW = "290";
        model.thumbnailH = "200";

        WallPaperResultModel resModel = model.cover();
        result.result.put(RESULT, resModel);
        downFile(resModel);

        logger.debug("result==>" + count.decrementAndGet());
        return result;
    }
}
