package samples.wallpaper;

import com.example.core.annotation.ExtractMethod;
import com.example.core.annotation.Spider;
import com.example.core.context.Config;
import com.example.core.download.DownloadWork;
import com.example.core.extract.ExtractUtils;
import com.example.core.models.Request;
import com.example.core.models.Response;
import com.example.core.models.Result;
import org.slf4j.LoggerFactory;

import java.util.List;

@Spider(name = Tt98.NAME, enable = true)
public class Tt98 extends WPTempCate {
    public static final String NAME = "Tt98";
    private String[] cates = {"desk", "ipad", "phone"};
    private String[] cateType = {"1", "2", "3"};

    public Tt98() {
        logger = LoggerFactory.getLogger(this.getClass());
        baseUrl = "https://www.tt98.com/list-%s-0-0-0-0-%d.html";
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

    @Override
    public void init() {

        for (int i = 0; i < cates.length; i++) {
            int index = initCateIndex(cates[i]);

        }
    }

    @ExtractMethod(methods = {NAME + EXTRACT_ITEM})
    private Result extractItem(Response response) {
        List<String> urls = response.soup("dl.egeli_pic_dl > dd > a", "href");

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
        EnterdeskModel model = ExtractUtils.extract(response, EnterdeskModel.class);
        model.imgWrapUrl = response.request.url;

        String url = "https:" + model.imgUrl;
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
        EnterdeskModel model = response.request.removeMeta(RESULT);
        model.imgUrl = response.soupFirst("#images_show_downa", "href");

        WallPaperResultModel resModel = model.cover();
        result.result.put(RESULT, resModel);
        downFile(resModel);

        logger.debug("result==>" + count.decrementAndGet());
        return result;
    }
}
