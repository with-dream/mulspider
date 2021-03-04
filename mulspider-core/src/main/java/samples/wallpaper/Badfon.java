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

@Spider(name = Badfon.NAME, enable = false)
public class Badfon extends WPTemp {
    public static final String NAME = "Badfon";

    public Badfon() {
        logger = LoggerFactory.getLogger(this.getClass());
        baseUrl = "https://www.badfon.ru/index-%d.html";
        infoMethods = new String[]{NAME + EXTRACT_INFO};
        infoMethods_1 = new String[]{NAME + EXTRACT_INFO_1, WallPaperResult.WallPaperResult};
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
        List<String> urls = response.eval("//div[@class='w']/a/@href");

        Result resTmp;
        if ((resTmp = duplicate(response, urls, true)) != null)
            return resTmp;

        for (String url : urls) {
            Request request = new Request(name);
            request.url = response.getSite() + url;
            request.method = infoMethods;

            if (addTask(request))
                logger.debug("request==>" + count.incrementAndGet());
        }
        return Result.makeIgnore();
    }

    @ExtractMethod(methods = {NAME + EXTRACT_INFO})
    private Result extractInfo(Response response) {
        BadfonModel model = ExtractUtils.extract(response, BadfonModel.class);
        model.imgWrapUrl = response.request.url;
        String url = response.getSite() + model.imgUrl;

        Request request = response.request;
        request.reset();
        request.url = url;
        request.method = infoMethods_1;
        request.put(RESULT, model);
        addTask(request);

        return Result.makeIgnore();
    }

    @ExtractMethod(methods = {NAME + EXTRACT_INFO_1})
    private Result extractInfo1(Response response) {
        Result result = Result.make(response.request);
        BadfonModel model = response.request.removeMeta(RESULT);
        model.imgUrl = response.evalFirst("//*[@id=\"im\"]/@href");

        WallPaperResultModel resModel = model.cover();
        result.put(RESULT, resModel);
        downFile(resModel, JPG);

        logger.debug("result==>" + count.decrementAndGet());

        return result;
    }
}
