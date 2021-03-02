package samples.wallpaper;

import com.example.core.annotation.ExtractMethod;
import com.example.core.annotation.SField;
import com.example.core.annotation.Spider;
import com.example.core.context.Config;
import com.example.core.extract.ExtractUtils;
import com.example.core.models.Request;
import com.example.core.models.Response;
import com.example.core.models.Result;
import com.example.core.utils.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Spider(name = Brodyaga.NAME, enable = false)
public class Brodyaga extends WPTemp {
    public static final String NAME = "Brodyaga";
    private static final String urlPrex = "http://www.brodyaga.com/pages/";

    public Brodyaga() {
        logger = LoggerFactory.getLogger(this.getClass());
        baseUrl = "http://www.brodyaga.com/pages/w_europeg.php?count=All Countries&page=%d&order=Date Added";
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
        List<String> urls = response.eval("//tbody/tr/td/a[@target='_blank']/@href");

        Result resTmp;
        if ((resTmp = duplicate(response, urls, urlPrex)) != null)
            return resTmp;

        for (String url : urls) {
            Request request = new Request(name);
            request.url = urlPrex + url;
            request.method = infoMethods;

            addTask(request);
            logger.debug("request==>" + count.incrementAndGet());
        }
        return Result.makeIgnore();
    }

    @ExtractMethod(methods = {NAME + EXTRACT_INFO})
    private Result extractInfo(Response response) {
        Result result = Result.make(response.request);
        BrodyagaModel model = ExtractUtils.extract(response, BrodyagaModel.class);
        model.imgWrapUrl = response.request.url;
        model.imgUrl = urlPrex + model.imgUrl;

        //( Image gallery:   Burglauer, Germany)
        String tags = response.evalFirst("//p[@class='Header']/font/b/text()");
        if (StringUtils.isNotEmpty(tags)) {
            tags = tags.replace("( Image gallery:", "");
            tags = tags.replace(")", "").trim();
            String[] t = tags.split(",");
            if (ArrayUtils.isNotEmpty(t)) {
                model.tags = new ArrayList<>();
                for (String tt : t)
                    if (StringUtils.isNotEmpty(tt.trim()))
                        model.tags.add(tt);
            }
        }
//BurglauerGermany Author: pilot_micha      Viewed: 40 (3)      Comments: 0
        String views = response.evalFirst("//div/table/tbody/tr/td/p/font/text()");
        if (StringUtils.isNotEmpty(views)) {
            String[] v = views.split("  ");
            for (String view : v)
                if (view.contains("Viewed")) {
                    String vv = view.substring(view.indexOf(":") + 1, view.indexOf("(")).trim();
                    if (StringUtils.isNotEmpty(vv))
                        model.views = vv;
                    break;
                }
        }

        WallPaperResultModel resModel = model.cover();
        result.result.put(RESULT, resModel);
        downFile(resModel);

        logger.debug("result==>" + count.decrementAndGet());

        return result;
    }
}
