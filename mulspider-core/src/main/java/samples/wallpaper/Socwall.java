package samples.wallpaper;

import com.example.core.annotation.ExtractMethod;
import com.example.core.annotation.Spider;
import com.example.core.context.Config;
import com.example.core.extract.ExtractUtils;
import com.example.core.models.Request;
import com.example.core.models.Response;
import com.example.core.models.Result;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Spider(name = Socwall.NAME, enable = false)
public class Socwall extends WPTemp {
    public static final String NAME = "Socwall";

    public Socwall() {
        logger = LoggerFactory.getLogger(this.getClass());
        baseUrl = "https://www.socwall.com/wallpapers/page:%d/";
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
        List<String> urls = response.eval("//*[@id=\"content\"]/ul[1]/li/div/a/@href");

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
        SocwallModel model = new SocwallModel();
        model.imgWrapUrl = response.request.url;

        Element downUrlEle = response.jsoup().select("a.download").first();
        if (downUrlEle != null)
            model.imgUrl = response.getSite() + downUrlEle.attr("href");
//3,954 views
        Element viewEle = response.jsoup().select("div.wallpaperMeta > p").first();
        if (viewEle != null) {
            String view = viewEle.text();
            if (StringUtils.isNotEmpty(view)) {
                view = view.replace("views", "");
                view = view.replace(",", "");
                model.views = view.trim();
            }
        }

        Elements tagsEle = response.jsoup().select("p.wallpaperTags > a");
        if (tagsEle != null && tagsEle.size() > 0) {
            model.tags = new ArrayList<>();
            for (Element e : tagsEle)
                model.tags.add(e.text());
        }


        WallPaperResultModel resModel = model.cover();
        result.result.put(RESULT, resModel);
        downFile(resModel);

        logger.debug("result==>" + count.decrementAndGet());

        return result;
    }
}
