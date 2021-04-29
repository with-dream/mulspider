package samples.wallpaper;

import com.example.core.annotation.ExtractMethod;
import com.example.core.annotation.Spider;
import com.example.core.context.Config;
import com.example.core.extract.ExtractUtils;
import com.example.core.models.Request;
import com.example.core.models.Response;
import com.example.core.models.Result;
import com.example.core.utils.Constant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * //FAIL 下载需要cookie 需要先完善webdriver
 * */
@Spider(name = Wallpapervortex.NAME, enable = false)
public class Wallpapervortex extends WPTemp {
    public static final String NAME = "Wallpapervortex";

    public Wallpapervortex() {
        logger = LoggerFactory.getLogger(this.getClass());
        baseUrl = "https://www.wallpapervortex.com/new_wallpapers_page_%d.html#.YEBXzGgzaUk";
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
        List<String> urls = response.eval("//div[@class='newWallpaper1']//td[@align='center']/a/@href");

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
        WallpapervortexModel model = new WallpapervortexModel();
        model.imgWrapUrl = response.request.url;
        model.imgUrl = "https://www.wallpapervortex.com/downloadwallpaper.php";

        Map<String, String> params = new HashMap<>();
        String file = response.evalFirst("//input[@name='file']/@value");
        String cat = response.evalFirst("//input[@name='cat']/@value");
        String subcat = response.evalFirst("//input[@name='subcat']/@value");
        params.put("file", file);
        params.put("cat", cat);
        params.put("subcat", subcat);

        WallPaperResultModel resModel = model.cover();
        resModel.params = params;
        result.put(RESULT, resModel);
        downFile(resModel, JPG);

        logger.debug("result==>" + count.decrementAndGet());

        return result;
    }
}
