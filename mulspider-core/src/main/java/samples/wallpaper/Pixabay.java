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

@Spider(name = Pixabay.NAME, enable = false)
public class Pixabay extends WPTemp {
    public static final String NAME = "Pixabay";

    public Pixabay() {
        logger = LoggerFactory.getLogger(this.getClass());
        baseUrl = "https://pixabay.com/zh/photos/search/?order=latest&pagi=";
        infoMethods = new String[]{NAME + EXTRACT_INFO, WallPaperResult.WallPaperResult};
        listMethods = new String[]{NAME + EXTRACT_ITEM};
    }

    @Override
    protected Config config(Config config) {
        config.downThreadCount = 1;
        config.breakpoint = true;
        return config;
    }

    @ExtractMethod(methods = {NAME + EXTRACT_ITEM})
    private Result extractItem(Response response) {
        List<String> urls = response.eval("//*div[@class='credits']/div[@class='item']/a/@href");

        Result resTmp;
        if ((resTmp = duplicate(response, urls, true)) != null)
            return resTmp;

        List<String> thumbnails = response.eval("//*div[@class='credits']/div[@class='item']/a/img/@src");
        List<String> thumW = response.eval("//*div[@class='credits']/div[@class='item']/@data-w");
        List<String> thumH = response.eval("//*div[@class='credits']/div[@class='item']/@data-h");
        if (thumbnails.size() != thumW.size() || thumbnails.size() != thumH.size())
            throw new RuntimeException("缩略图与尺寸数量不对应");

        int urlIndex = 0;
        for (String url : urls) {
            Request request = new Request(name);
            request.url = response.request.getSite() + url;
            request.method = infoMethods;

            if (!thumbnails.get(urlIndex).isEmpty())
                request.meta.put(THUM, thumbnails.get(urlIndex));
            if (!thumW.get(urlIndex).isEmpty())
                request.meta.put(THUMW, thumW.get(urlIndex));
            if (!thumH.get(urlIndex).isEmpty())
                request.meta.put(THUMH, thumH.get(urlIndex));
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

        model.imgUrl = response.request.getSite() + model.imgUrl;
        String[] wh = model.imgW.split("x");
        model.imgW = wh[0];
        model.imgH = wh[1];
        model.thumbnail = response.request.removeMeta(THUM);
        model.thumbnailW = "400";
        model.thumbnailH = "225";

        result.result.put("result", model);
        logger.debug("result==>" + count.decrementAndGet());
        return result;
    }
}
