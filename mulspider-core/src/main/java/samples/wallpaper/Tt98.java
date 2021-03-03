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

@Spider(name = Tt98.NAME, enable = false)
public class Tt98 extends WPTempCate {
    public static final String NAME = "Tt98";
    private String[] cates = {"desk", "ipad", "phone"};
    private String[] cateType = {"1", "2", "3"};

    public Tt98() {
        logger = LoggerFactory.getLogger(this.getClass());
        baseUrl = "https://www.tt98.com/list-%s-0-0-0-0-%d.html";
        cateMethods = new String[]{NAME + EXTRACT_CATE};
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
    protected void initUrl() {
        for (int i = 0; i < cateType.length; i++) {
            if (i != 1)
                continue;
            int index = initCateIndex(cateType[i]);
            createCateReq(cateType[i], getCateUrl(cateType[i], index));
        }
    }

    @Override
    protected String getCateUrl(String cate, int index) {
        return String.format(baseUrl, cate, index);
    }

    @ExtractMethod(methods = {NAME + EXTRACT_ITEM})
    private Result extractItem(Response response) {
        List<String> urls = response.soup("li > div.listbox > a", "href");

        if (dupUrls(response, urls, true, false, false)) {
            for (String url : urls) {
                Request request = new Request(name);
                request.url = response.getSite() + url;
                request.httpPool();
                request.method = infoMethods;
                if (addTask(request))
                    logger.debug("request==>" + count.incrementAndGet());
            }
        }
        return Result.makeIgnore();
    }

    @ExtractMethod(methods = {NAME + EXTRACT_INFO})
    private Result extractInfo(Response response) {
        String count = response.soupFirst("a.photo-a", "zong");
        String urls = response.soupFirst("div.page > a[href]", "href");

        for (int i = 0; i < Integer.parseInt(count); i++) {
            String url = urls.replaceAll("-\\d+\\.html", "-" + i + ".html");
            Request request = response.request.clone();
            request.url = response.getSite() + url;
            request.method = infoMethods_1;
            addTask(request);
        }

        return Result.makeIgnore();
    }

    @ExtractMethod(methods = {NAME + EXTRACT_INFO_1})
    private Result extractInfo1(Response response) {
        Result result = Result.make(response.request);
        String ff = response.soupFirst("#diggnum", null);
        Tt98Model model = ExtractUtils.extract(response, Tt98Model.class);
        model.imgWrapUrl = response.request.url;
        model.imgUrl = model.imgUrl.replace("edpic", "edpic_source");

        WallPaperResultModel resModel = model.cover();
        result.result.put(RESULT, resModel);
        downFile(resModel);

        logger.debug("result==>" + count.decrementAndGet());
        return result;
    }
}
