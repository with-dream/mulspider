package samples.wallpaper;

import com.example.core.annotation.ExtractMethod;
import com.example.core.annotation.Spider;
import com.example.core.context.Config;
import com.example.core.download.DownloadWork;
import com.example.core.extract.ExtractUtils;
import com.example.core.models.Request;
import com.example.core.models.Response;
import com.example.core.models.Result;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

//找不到真正的首頁地址 直接返回403
@Spider(name = Pexels.NAME, enable = false)
public class Pexels extends WPTemp {
    public static final String NAME = "Pexels";

    public Pexels() {
        logger = LoggerFactory.getLogger(this.getClass());
        baseUrl = "https://www.pexels.com/new-photos/?page=";
        downType = DownloadWork.DownType.CLIENT_WEBDRIVER;
        infoMethods = new String[]{NAME + EXTRACT_INFO, WallPaperResult.WallPaperResult};
        itemMethods = new String[]{NAME + EXTRACT_ITEM};

        headers = new HashMap<>();
        headers.put("accept-encoding", "gzip, deflate, br");
        headers.put("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
    }

    @Override
    protected Config config(Config config) {
        config.downThreadCount = 1;
        config.breakpoint = true;
        return config;
    }

    @ExtractMethod(methods = {NAME + EXTRACT_ITEM})
    private Result extractItem(Response response) {
        List<String> urls = response.eval("//a[@class='js-photo-link photo-item__link']/@href");

        Result resTmp;
        if ((resTmp = duplicate(response, urls, true)) != null)
            return resTmp;

        List<String> tags = response.eval("//a[@class='js-photo-link photo-item__link']/@title");
        if (urls.size() != tags.size())
            throw new RuntimeException("获取数量错误 ==>" + response.request.url);

        int urlIndex = 0;
        for (String url : urls) {
            Request request = new Request(name);
            request.url = response.getSite() + url;
            request.method = infoMethods;

            String tag = tags.get(urlIndex);
            if (StringUtils.isNotEmpty(tag))
                request.meta.put(TAGS, tag);
            addTask(request);
            logger.debug("request==>" + count.incrementAndGet());
            urlIndex++;
        }
        return Result.makeIgnore();
    }

    @ExtractMethod(methods = {NAME + EXTRACT_INFO})
    private Result extractInfo(Response response) {
        Result result = Result.make(response.request);
        PexelsModel model = ExtractUtils.extract(response, PexelsModel.class);
        model.imgWrapUrl = response.request.url;

        if (response.request.meta.containsKey(TAGS)) {
            model.tags = Arrays.asList(response.request.removeMeta(TAGS));
        }
        model.imgW = model.imgW.replace("px", "");
        String[] wh = model.imgW.split("x");
        model.imgW = wh[0];
        model.imgH = wh[1];

        result.result.put(RESULT, model);
        logger.debug("result==>" + count.decrementAndGet());
        return result;
    }
}
