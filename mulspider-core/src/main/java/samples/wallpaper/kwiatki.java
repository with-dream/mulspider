package samples.wallpaper;

import com.example.core.annotation.ExtractMethod;
import com.example.core.annotation.Spider;
import com.example.core.context.Config;
import com.example.core.extract.ExtractUtils;
import com.example.core.models.Request;
import com.example.core.models.Response;
import com.example.core.models.Result;
import org.slf4j.LoggerFactory;

import java.util.List;

@Spider(name = kwiatki.NAME, enable = false)
public class kwiatki extends WPTemp {
    public static final String NAME = "kwiatki";

    public kwiatki() {
        logger = LoggerFactory.getLogger(this.getClass());
        baseUrl = "https://www.kwiatki.org/kwiaty,,strona-%d.html";
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
        List<String> urls = response.soup("div.tekst > a", "href");

        Result resTmp;
        if ((resTmp = duplicate(response, urls, true, true)) != null)
            return resTmp;

        for (String url : urls) {
            Request request = new Request(name);
            request.url = response.getSite() + "/" + url;
            request.method = infoMethods;

            if (addTask(request))
                logger.debug("request==>" + count.incrementAndGet());
        }
        return Result.makeIgnore();
    }

    @ExtractMethod(methods = {NAME + EXTRACT_INFO})
    private Result extractInfo(Response response) {
        Result result = Result.make(response.request);

        kwiatkiModel model = ExtractUtils.extract(null, response.jsoup(), kwiatkiModel.class);
        model.imgWrapUrl = response.request.url;
        model.imgUrl = response.getSite() + "/" + model.imgUrl;

        WallPaperResultModel resModel = model.cover();
        result.put(RESULT, resModel);
        downFile(resModel, JPG);

        logger.debug("result==>" + count.decrementAndGet());

        return result;
    }

}
