package samples.wallpaper;

import com.example.core.annotation.ExtractMethod;
import com.example.core.annotation.Spider;
import com.example.core.context.Config;
import com.example.core.download.DownloadWork;
import com.example.core.extract.ExtractUtils;
import com.example.core.models.Request;
import com.example.core.models.Response;
import com.example.core.models.Result;
import com.example.core.utils.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Spider(name = Goodfon.NAME, enable = false)
public class Goodfon extends WPTemp {
    public static final String NAME = "Goodfon";

    public Goodfon() {
        logger = LoggerFactory.getLogger(this.getClass());
        baseUrl = "https://www.goodfon.com/index-%d.html";
        itemMethods = new String[]{NAME + EXTRACT_ITEM};
        infoMethods = new String[]{NAME + EXTRACT_INFO};
        infoMethods_1 = new String[]{NAME + EXTRACT_INFO_1, WallPaperResult.WallPaperResult};
        downType = DownloadWork.DownType.CLIENT_POOL;
    }

    @Override
    protected Config config(Config config) {
        config.downThreadCount = 1;
        config.breakpoint = true;
        return config;
    }

    @ExtractMethod(methods = {NAME + EXTRACT_ITEM})
    private Result extractItem(Response response) {
        List<String> urls = response.soup("div.wallpapers__item > div.wallpapers__item__wall > a", "href");

        Result resTmp;
        if ((resTmp = duplicate(response, urls, false)) != null)
            return resTmp;

        for (String url : urls) {
            Request request = new Request(name);
            request.url = url;
            request.httpPool();
            request.method = infoMethods;
            addTask(request);
            logger.debug("request==>" + count.incrementAndGet());
        }
        return Result.makeIgnore();
    }

    @ExtractMethod(methods = {NAME + EXTRACT_INFO})
    private Result extractInfo(Response response) {
        GoodfonModel model = ExtractUtils.extract(response, GoodfonModel.class);
        model.imgWrapUrl = response.request.url;
        if (StringUtils.isNotEmpty(model.fav)) {
            String[] fav = model.fav.split(" ");
            String f = fav[fav.length - 1];
            if (f.matches("[0-9]+"))
                model.fav = f;
            else
                model.fav = "";
        }
        //Photo wallpaper street, home, art, Los Angeles, gta, San Andreas, Grove Street
        if (!CollectionUtils.isEmpty(model.tags)) {
            String[] t = model.tags.get(0).split(",");
            model.tags.clear();
            if (ArrayUtils.isNotEmpty(t))
                for (String tag : t)
                    if (StringUtils.isNotEmpty(tag.trim()))
                        model.tags.add(tag.trim());
        }

        String url = response.getSite() + model.imgUrl;
        Request request = response.request;
        request.reset();
        request.put(RESULT, model);
        request.url = url;
        request.method = infoMethods_1;
        addTask(request);

        return Result.makeIgnore();
    }

    @ExtractMethod(methods = {NAME + EXTRACT_INFO_1})
    private Result extractInfo1(Response response) {
        Result result = Result.make(response.request);
        GoodfonModel model = response.request.removeMeta(RESULT);
        model.imgUrl = response.soupFirst("#im", "href");

        WallPaperResultModel resModel = model.cover();
        result.result.put(RESULT, resModel);
        downFile(resModel, JPG);

        logger.debug("result==>" + count.decrementAndGet());
        return result;
    }
}
