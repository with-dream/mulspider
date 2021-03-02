package samples.wallpaper;

import com.example.core.annotation.ExtractMethod;
import com.example.core.annotation.Spider;
import com.example.core.context.Config;
import com.example.core.extract.ExtractUtils;
import com.example.core.models.Request;
import com.example.core.models.Response;
import com.example.core.models.Result;
import com.example.core.utils.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Spider(name = Alphacoders.NAME, enable = false)
public class Alphacoders extends WPTemp {
    public static final String NAME = "Alphacoders";

    public Alphacoders() {
        logger = LoggerFactory.getLogger(this.getClass());
        baseUrl = "https://wall.alphacoders.com/newest_wallpapers.php?page=%d";
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
        List<String> urls = response.eval("//*[@class='thumb-container-big']/div/div[@class='boxgrid']/a/@href");

        Result resTmp;
        if ((resTmp = duplicate(response, urls, true, true)) != null)
            return resTmp;

        List<String> thumbnails = response.eval("//*[@class='thumb-container-big']/div/div[@class='boxgrid']/a//img/@src");

        int urlIndex = 0;
        for (String url : urls) {
            Request request = new Request(name);
            request.url = response.getSite() + "/" + url;
            request.method = infoMethods;

            if (!thumbnails.get(urlIndex).isEmpty())
                request.meta.put(THUM, response.getSite() + thumbnails.get(urlIndex));
            addTask(request);
            logger.debug("request==>" + count.incrementAndGet());
            urlIndex++;
        }
        return Result.makeIgnore();
    }

    @ExtractMethod(methods = {NAME + EXTRACT_INFO})
    private Result extractInfo(Response response) {
        Result result = Result.make(response.request);
        AlphacoderModel model = ExtractUtils.extract(response, AlphacoderModel.class);
        model.imgWrapUrl = response.request.url;
//        model.thumbnail = response.request.removeMeta(THUM);
//        model.thumbnailW = "600";
//        model.thumbnailH = "375";

        if (StringUtils.isNotEmpty(model.fav))
            model.fav = model.fav.trim();
        if (StringUtils.isNotEmpty(model.views))
            model.views = model.views.trim();

//        if (!CollectionUtils.isEmpty(model.sizeContent)) {
//            for (String c : model.sizeContent)
//                if (c.contains("MB") || c.contains("kB")) {
//                    int index = c.indexOf("MB");
//                    if (index <= 0)
//                        index = c.indexOf("kB");
//                    model.size = c.substring(0, index + 2).trim();
//                    break;
//                }
//            model.sizeContent.clear();
//        }

//        if (model.colors != null && !model.colors.isEmpty()) {
//            List<String> colorList = new ArrayList<>(model.colors.size());
//            for (String c : model.colors)
//                colorList.add(c.split(":")[1]);
//            model.colors = colorList;
//        }

        WallPaperResultModel resModel = model.cover();
        result.result.put(RESULT, resModel);
        downFile(resModel);

        logger.debug("result==>" + count.decrementAndGet());
        return result;
    }
}
