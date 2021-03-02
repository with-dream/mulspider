package samples.wallpaper;

import com.example.core.annotation.ExtractMethod;
import com.example.core.annotation.Spider;
import com.example.core.context.Config;
import com.example.core.models.Request;
import com.example.core.models.Response;
import com.example.core.models.Result;
import com.example.core.utils.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Spider(name = Konachan.NAME, enable = false)
public class Konachan extends WPTemp {
    public static final String NAME = "Konachan";

    public Konachan() {
        logger = LoggerFactory.getLogger(this.getClass());
        baseUrl = "https://konachan.net/post?page=%d&tags=";
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
        List<String> urls = response.soup("a.thumb", "href");

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
        Result result = Result.make(response.request);
        KonachanModel model = new KonachanModel();
        model.imgWrapUrl = response.request.url;
        model.imgUrl = response.soupFirst("#right-col > div > img", "src");
        model.tags = response.soup("#tag-sidebar > li > a:eq(1)", null);

        WallPaperResultModel resModel = model.cover();
        result.result.put(RESULT, resModel);
        downFile(resModel);

        logger.debug("result==>" + count.decrementAndGet());

        return result;
    }
}
