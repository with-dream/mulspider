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

import java.util.List;

@Spider(name = Wallpaperswide.NAME, enable = false)
public class Wallpaperswide extends WPTemp {
    public static final String NAME = "Wallpaperswide";

    public Wallpaperswide() {
        logger = LoggerFactory.getLogger(this.getClass());
        baseUrl = "http://wallpaperswide.com/latest_wallpapers/page/";
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
        List<String> urls = response.eval("//li[@class='wall']/div[@class='thumb']/a/@href");
        List<String> urls1 = response.eval("//div[@itemprop='image']/a/@href");
        urls.addAll(urls1);
        List<String> infos = response.eval("//*[@id=\"hudvisits\"]/em/text()");

        if (urls.size() != infos.size())
            throw new RuntimeException("urls infos数量对不上");

        Result resTmp;
        if ((resTmp = duplicate(response, urls, true)) != null)
            return resTmp;

        int urlIndex = 0;
        for (String url : urls) {
            Request request = new Request(name);
            request.url = response.request.getSite() + url;
            request.method = infoMethods;
            request.put("info", infos.get(urlIndex));

            addTask(request);
            logger.debug("request==>" + count.incrementAndGet());
            urlIndex++;
        }
        return Result.makeIgnore();
    }

    @ExtractMethod(methods = {NAME + EXTRACT_INFO})
    private Result extractInfo(Response response) {
        Result result = Result.make(response.request);
        WallpaperswideModel model = ExtractUtils.extract(response, WallpaperswideModel.class);
        model.imgWrapUrl = response.request.url;

        List<String> resolutions = response.eval("//*[@id=\"wallpaper-resolutions\"]/a/text()");
        int maxSize = 0;
        int maxIndex = 0;
        for (int i = 0; i < resolutions.size(); i++) {
            String res = resolutions.get(i);
            if (res.contains("x")) {
                String[] sp = res.split("x");
                int tmpSize = Integer.parseInt(sp[0]) * Integer.parseInt(sp[1]);
                if (tmpSize > maxSize) {
                    maxIndex = i;
                    maxSize = tmpSize;
                }
            }
        }
        List<String> resUrls = response.eval("//*[@id=\"wallpaper-resolutions\"]/a/@href");

        model.imgUrl = response.request.getSite() + resUrls.get(maxIndex);
        String info = response.request.removeMeta("info");
        if (StringUtils.isNotEmpty(info) && info.contains("|")) {
            String[] views = info.split("|");
            model.views = views[0].split(" ")[0];
            model.fav = views[1].trim().split(" ")[0];
        }

        WallPaperResultModel resModel = model.cover();
        result.result.put(RESULT, resModel);
        downFile(resModel);

        logger.debug("result==>" + count.decrementAndGet());

        return result;
    }
}
