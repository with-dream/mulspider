package samples.wallpaper;

import com.example.core.annotation.ExtractMethod;
import com.example.core.annotation.Spider;
import com.example.core.context.Config;
import com.example.core.extract.ExtractUtils;
import com.example.core.models.Request;
import com.example.core.models.Response;
import com.example.core.models.Result;
import com.example.core.utils.Constant;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 下载需要cookie 卒
 */
@Spider(name = pakutaso.NAME, enable = false)
public class pakutaso extends WPTempCate {
    public static final String NAME = "pakutaso";

    public pakutaso() {
        logger = LoggerFactory.getLogger(this.getClass());
        homeUrl = "https://www.pakutaso.com/";
        cateMethods = new String[]{NAME + EXTRACT_CATE};
        itemMethods = new String[]{NAME + EXTRACT_ITEM};
        infoMethods = new String[]{NAME + EXTRACT_INFO, WallPaperResult.WallPaperResult};
    }

    @Override
    protected Config config(Config config) {
        config.downThreadCount = 1;
        config.breakpoint = true;
        return config;
    }

    @Override
    protected String getCateUrl(String cate, int index) {
        return String.format("https://www.pakutaso.com%sindex_%d.html", cate, index);
    }

    @ExtractMethod(methods = {NAME + EXTRACT_CATE})
    private Result extractCate(Response response) {
        List<String> cates = response.eval("//div[@class='photo']/a/@href");

        String prefix = ".com";
        for (String cate : cates) {
            if (cate.contains(prefix))
                cate = cate.substring(cate.indexOf(prefix) + prefix.length());
            int cateIndex = initCateIndex(cate);
            createCateReq(cate, getCateUrl(cate, cateIndex));
        }
        return Result.makeIgnore();
    }

    @ExtractMethod(methods = {NAME + EXTRACT_ITEM})
    private Result extractItem(Response response) {
        List<String> urls = response.eval("//li[@class='photos__item']/div[@class='photoEntries']/a/@href");

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

        pakutasoModel model = ExtractUtils.extract(response, pakutasoModel.class);
        model.imgWrapUrl = response.request.url;
        model.imgUrl = response.getSite() + model.imgUrl;

        WallPaperResultModel resModel = model.cover();
        result.put(RESULT, resModel);
        downFile(resModel);

        logger.debug("result==>" + count.decrementAndGet());

        return result;
    }

}
//https://www.pakutaso.com/animal/cat/index_2.html
//https://www.pakutaso.com/animal/cat/index_1.html