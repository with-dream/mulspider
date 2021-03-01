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
import java.util.Arrays;
import java.util.List;

@Spider(name = Cgwallpapers.NAME, enable = true)
public class Cgwallpapers extends WPTemp {
    public static final String NAME = "Cgwallpapers";
    private static final int PAGE_COUNT = 36;

    public Cgwallpapers() {
        logger = LoggerFactory.getLogger(this.getClass());
        baseUrl = "https://www.cgwallpapers.com/index.php?start=%d";
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
        return String.format(baseUrl, index.getAndIncrement() * PAGE_COUNT);
    }

    @ExtractMethod(methods = {NAME + EXTRACT_ITEM})
    private Result extractItem(Response response) {
        List<String> urls = response.eval("//h1[@class='title-index']/a/@href");
        List<String> tags = response.eval("//h1[@class='title-index']/a/text()");

        Result resTmp;
        if ((resTmp = duplicate(response, urls, true, true)) != null)
            return resTmp;

        if (urls.size() != tags.size())
            throw new RuntimeException("获取数量错误 ==>" + response.request.url);

        int urlIndex = 0;
        for (String url : urls) {
            Request request = new Request(name);
            request.url = response.getSite() + "/" + url;
            request.method = infoMethods;

            String tag = tags.get(urlIndex);
            if (StringUtils.isNotEmpty(tag))
                request.put(TAGS, tag);
            addTask(request);
            logger.debug("request==>" + count.incrementAndGet());
            urlIndex++;
        }
        return Result.makeIgnore();
    }

    @ExtractMethod(methods = {NAME + EXTRACT_INFO})
    private Result extractInfo(Response response) {
        Result result = Result.make(response.request);
        CgwallpapersModel model = ExtractUtils.extract(response, CgwallpapersModel.class);
        model.imgWrapUrl = response.request.url;
        //TODO
        model.tags = Arrays.asList(response.removeMeta(TAGS));

        model.imgUrl = response.getSite() + model.imgUrl;

        WallPaperResultModel resModel = model.cover();
        result.result.put(RESULT, resModel);
        downFile(resModel);

        logger.debug("result==>" + count.decrementAndGet());

        return result;
    }
}
