package samples.wallpaper;

import com.example.core.annotation.ExtractMethod;
import com.example.core.annotation.Spider;
import com.example.core.context.Config;
import com.example.core.models.Request;
import com.example.core.models.Response;
import com.example.core.models.Result;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Spider(name = Papers.NAME, enable = false)
public class Papers extends WPTemp {
    public static final String NAME = "Papers";

    public Papers() {
        logger = LoggerFactory.getLogger(this.getClass());
        baseUrl = "https://papers.co/page/%d/";
        headers = new HashMap<>();
        headers.put("accept-encoding", "gzip");
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
        return String.format(baseUrl, index.getAndIncrement());
    }

    @ExtractMethod(methods = {NAME + EXTRACT_ITEM})
    private Result extractItem(Response response) {
        List<String> urls = response.soup("div.thumbs > a", "href");

        Result resTmp;
        if ((resTmp = duplicate(response, urls, false)) != null)
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
        PapersModel model = new PapersModel();
        model.imgWrapUrl = response.request.url;

        int maxSize = 0;
        int maxIndex = 0;
        List<String> sizes = response.soup("div.btnmain.downloadbtn.downloadmac", null);
        for (int i = 0; i < sizes.size(); i++) {
            String size = sizes.get(i);
            if (StringUtils.isNotEmpty(size) && size.contains("x")) {
                String[] wh = size.split("x");
                int resol = Integer.parseInt(wh[0].trim()) * Integer.parseInt(wh[1].trim());
                if (resol > maxSize) {
                    maxSize = resol;
                    maxIndex = i;
                }
            }
        }
        List<String> imgUrls = response.soup("div.downloadbox > a", "href");
        model.imgUrl = imgUrls.get(maxIndex);

        WallPaperResultModel resModel = model.cover();
        result.result.put(RESULT, resModel);
        downFile(resModel);

        logger.debug("result==>" + count.decrementAndGet());

        return result;
    }
}
