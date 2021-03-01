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

import java.util.ArrayList;
import java.util.List;

@Spider(name = Wallpaperfx.NAME, enable = false)
public class Wallpaperfx extends WPTemp {
    public static final String NAME = "Wallpaperfx";

    public Wallpaperfx() {
        logger = LoggerFactory.getLogger(this.getClass());
        baseUrl = "https://wallpaperfx.com/latest_wallpapers/page-";
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
        List<String> urls = response.eval("//ul[@class='wallpapers']/li/a/@href");

        Result resTmp;
        if ((resTmp = duplicate(response, urls, true)) != null)
            return resTmp;

        for (String url : urls) {
            Request request = new Request(name);
            request.url = response.getSite() + url;
            request.method = infoMethods;

            addTask(request);
            logger.debug("request==>" + count.incrementAndGet());
        }
        return Result.makeIgnore();
    }

    @ExtractMethod(methods = {NAME + EXTRACT_INFO})
    private Result extractInfo(Response response) {
        WallpaperfxModel model = ExtractUtils.extract(response, WallpaperfxModel.class);
        model.imgWrapUrl = response.request.url;

        List<String> resolutions = response.eval("//ul[@class='wallpaper-resolutions']/li/a/text()");
        int maxSize = 0;
        int resIndex = 0;
        for (int i = 0; i < resolutions.size(); i++) {
            String res = resolutions.get(i);
            if (res.contains("x")) {
                String r[] = res.split("x");
                int tmp = Integer.parseInt(r[0]) * Integer.parseInt(r[1]);
                if (tmp > maxSize) {
                    resIndex = i;
                    maxSize = tmp;
                }
            }
        }

        List<String> tmpUrls = response.eval("//ul[@class='wallpaper-resolutions']/li/a/@href");

        String imgUrl = response.getSite() + tmpUrls.get(resIndex);
        Request req = response.request.clone();
        req.url = imgUrl;
        req.method = new String[]{NAME + EXTRACT_INFO_1};
        req.put(RESULT, model);
        addTask(req);

        return Result.makeIgnore();
    }

    @ExtractMethod(methods = {NAME + EXTRACT_INFO_1})
    private Result extractInfo1(Response response) {
        Result result = Result.make(response.request);
        result.request.method = new String[]{WallPaperResult.WallPaperResult};

        WallpaperfxModel model = response.request.removeMeta(RESULT);
        model.imgUrl = response.getSite() + response.evalFirst("//*[@id=\"wallpaper\"]/@src");

        WallPaperResultModel resModel = model.cover();
        result.result.put(RESULT, resModel);
        downFile(resModel);

        logger.debug("result==>" + count.decrementAndGet());

        return result;
    }
}
