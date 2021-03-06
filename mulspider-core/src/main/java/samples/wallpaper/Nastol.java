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

@Spider(name = Nastol.NAME, enable = false)
public class Nastol extends WPTemp {
    public static final String NAME = "Nastol";

    public Nastol() {
        logger = LoggerFactory.getLogger(this.getClass());
        baseUrl = "https://www.nastol.com.ua/page/%d";
        infoMethods = new String[]{NAME + EXTRACT_INFO, WallPaperResult.WallPaperResult};
        itemMethods = new String[]{NAME + EXTRACT_ITEM};
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
        List<String> urls = response.eval("//a[@class='screen-link']/@href");

        Result resTmp;
        if ((resTmp = duplicate(response, urls, true)) != null)
            return resTmp;

        for (String url : urls) {
            Request request = new Request(name);
            request.url = url;
            request.method = infoMethods;

            addTask(request);
            logger.debug("request==>" + count.incrementAndGet());
        }
        return Result.makeIgnore();
    }

    @ExtractMethod(methods = {NAME + EXTRACT_INFO})
    private Result extractInfo(Response response) {
        Result result = Result.make(response.request);
        NastolModel model = ExtractUtils.extract(response, NastolModel.class);
        model.imgWrapUrl = response.request.url;

        model.imgUrl = response.getSite() + model.imgUrl;
//        String[] wh = model.imgW.split("x");
//        model.imgW = wh[0];
//        model.imgH = wh[1];
//        model.thumbnail = response.request.removeMeta(THUM);
//        model.thumbnailW = "400";
//        model.thumbnailH = "225";
        if (StringUtils.isNotEmpty(model.fav) && model.fav.contains(":")) {
            model.fav = model.fav.split(":")[1];
            model.fav = model.fav.trim();
        }

        WallPaperResultModel resModel = model.cover();
        result.result.put(RESULT, resModel);
        downFile(resModel);

        logger.debug("result==>" + count.decrementAndGet());

        return result;
    }
}
