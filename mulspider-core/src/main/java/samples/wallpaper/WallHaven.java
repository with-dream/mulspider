package samples.wallpaper;

import com.example.core.annotation.ExtractMethod;
import com.example.core.context.Config;
import com.example.core.annotation.Spider;
import com.example.core.download.DownloadWork;
import com.example.core.extract.ExtractUtils;
import com.example.core.models.Request;
import com.example.core.models.Response;
import com.example.core.models.Result;
import org.slf4j.LoggerFactory;

import java.util.List;

@Spider(name = WallHaven.NAME, enable = false)
public class WallHaven extends WPTemp {
    public static final String NAME = "WallHaven";

    public WallHaven() {
        logger = LoggerFactory.getLogger(this.getClass());
        baseUrl = "https://wallhaven.cc/latest?page=%d";
        infoMethods = new String[]{NAME + EXTRACT_INFO, WallPaperResult.WallPaperResult};
        itemMethods = new String[]{NAME + EXTRACT_ITEM};
        downType = DownloadWork.DownType.CLIENT_POOL;
    }

    @Override
    protected Config config(Config config) {
        config.downThreadCount = 1;
        config.breakpoint = true;
        return config;
    }

    @ExtractMethod(methods = {NAME + EXTRACT_ITEM})
    private Result extractItem(Response response) {
        List<String> urls = response.eval("//*[@id=\"thumbs\"]/section[1]/ul/li/figure/a/@href");

        Result resTmp;
        if ((resTmp = duplicate(response, urls, false)) != null)
            return resTmp;

        List<String> thumbnails = response.eval("//*[@id=\"thumbs\"]/section/ul/li/figure/img/@data-src");
        int thIndex = 0;
        for (String url : urls) {
            Request request = new Request(name);
            request.url = url;
            if (downType == DownloadWork.DownType.CLIENT_POOL)
                request.httpPool();
            else if (downType == DownloadWork.DownType.CLIENT_WEBDRIVER)
                request.headless();
            request.method = infoMethods;
            request.meta.put(THUM, thumbnails.get(thIndex));
            addTask(request);
            logger.debug("request==>" + count.incrementAndGet());
            thIndex++;
        }
        return Result.makeIgnore();
    }

    @ExtractMethod(methods = {NAME + EXTRACT_INFO})
    private Result extractInfo(Response response) {
        Result result = Result.make(response.request);
        WallHavenModel model = ExtractUtils.extract(response, WallHavenModel.class);
        model.imgWrapUrl = response.request.url;
//        String[] wh = model.imgW.split("x");
//        model.imgW = wh[0];
//        model.imgH = wh[1];

//        model.thumbnail = (String) response.request.meta.get(THUM);
//        model.thumbnailW = "300";
//        model.thumbnailH = "200";

        WallPaperResultModel resModel = model.cover();
        result.result.put(RESULT, resModel);
        downFile(resModel);

        logger.debug("result==>" + count.decrementAndGet());
        return result;
    }
}
