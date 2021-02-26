package samples.wallpaper;

import com.example.core.annotation.ExtractMethod;
import com.example.core.annotation.Spider;
import com.example.core.context.Config;
import com.example.core.extract.ExtractUtils;
import com.example.core.models.Request;
import com.example.core.models.Response;
import com.example.core.models.Result;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Spider(name = Wallpaperim.NAME, enable = false)
public class Wallpaperim extends WPTempCate {
    public static final String NAME = "Wallpaperim";
    public static final int PAGE_COUNT = 30;

    public Wallpaperim() {
        logger = LoggerFactory.getLogger(this.getClass());
        cateMethods = new String[]{NAME + EXTRACT_CATE};
        infoMethods = new String[]{NAME + EXTRACT_INFO};
        itemMethods = new String[]{NAME + EXTRACT_ITEM};
        homeUrl = "https://wallpaperim.net/";
    }

    @Override
    protected Config config(Config config) {
        config.downThreadCount = 1;
        config.breakpoint = true;
        return config;
    }

    @Override
    protected String getCateUrl(String cate, int index) {
        return String.format("%s%s/start-%d", homeUrl, cate, index * PAGE_COUNT);
    }

    @ExtractMethod(methods = {NAME + EXTRACT_CATE})
    private Result extractCate(Response response) {
        List<String> cates = response.eval("//*[@id=\"sidebarmenu1\"]/li/a/@href");
        for (String cate : cates) {
            int cateIndex = initCateIndex(cate, 0);
            createCateReq(cate, getCateUrl(cate, cateIndex));
        }
        return Result.makeIgnore();
    }

    @ExtractMethod(methods = {NAME + EXTRACT_ITEM})
    private Result extractItem(Response response) {
        List<String> urls = response.eval("//*[@id=\"thumbnails\"]/li/div/div/a/@href");
        for (int i = 0; i < urls.size(); i++)
            urls.set(i, urls.get(i).substring(urls.get(i).lastIndexOf("../") + 2));

        if (dupUrls(response, urls, true, false, false)) {
            for (String url : urls) {
                addRequest(response, url);
            }
        }

        return Result.makeIgnore();
    }

    @ExtractMethod(methods = {NAME + EXTRACT_INFO})
    private Result extractInfo(Response response) {
        response.request.method = new String[]{WallPaperResult.WallPaperResult};
        Result result = Result.make(response.request);
        WallpaperimModel model = ExtractUtils.extract(response, WallpaperimModel.class);
        model.imgUrl = response.request.getSite() + model.imgUrl.substring(model.imgUrl.lastIndexOf("../") + 2);
        model.imgUrl = model.imgUrl.replace("amp;", "");
        model.imgWrapUrl = response.request.url;

        WallPaperResultModel resModel = model.cover();
        result.result.put(RESULT, resModel);
        downFile(resModel);

        return result;
    }
}
