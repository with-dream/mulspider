package samples.wallpaper;

import com.example.core.annotation.ExtractMethod;
import com.example.core.annotation.Spider;
import com.example.core.context.Config;
import com.example.core.extract.ExtractUtils;
import com.example.core.models.Request;
import com.example.core.models.Response;
import com.example.core.models.Result;
import com.example.core.utils.CollectionUtils;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

@Spider(name = hdwallsource.NAME, enable = true)
public class hdwallsource extends WPTemp {
    public static final String NAME = "hdwallsource";

    public hdwallsource() {
        logger = LoggerFactory.getLogger(this.getClass());
        baseUrl = "https://hdwallsource.com/home/%d";
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
        List<String> urls = response.soup("div.details > a", "href");

        Result resTmp;
        if ((resTmp = duplicate(response, urls, false)) != null)
            return resTmp;

        for (String url : urls) {
            Request request = new Request(name);
            request.url = url;
            request.method = infoMethods;

            if (addTask(request))
                logger.debug("request==>" + count.incrementAndGet());
        }
        return Result.makeIgnore();
    }

    @ExtractMethod(methods = {NAME + EXTRACT_INFO})
    private Result extractInfo(Response response) {
        Result result = Result.make(response.request);

        hdwallsourceModel model = ExtractUtils.extract(null, response.jsoup(), hdwallsourceModel.class);
        model.imgWrapUrl = response.request.url;
        if (!CollectionUtils.isEmpty(model.tags))
            model.tags = Arrays.asList(model.tags.get(0).split(" "));

        WallPaperResultModel resModel = model.cover();
        result.put(RESULT, resModel);
        downFile(resModel);

        logger.debug("result==>" + count.decrementAndGet());

        return result;
    }
}
