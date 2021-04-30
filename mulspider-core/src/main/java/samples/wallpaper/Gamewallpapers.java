//package samples.wallpaper;
//
//import com.example.core.annotation.ExtractMethod;
//import com.example.core.annotation.Spider;
//import com.example.core.context.Config;
//import com.example.core.models.Request;
//import com.example.core.models.Response;
//import com.example.core.models.Result;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.LoggerFactory;
//
//import java.util.Arrays;
//import java.util.List;
//
///**
// * //FAIL 有水印 草
// * */
//@Spider(name = Gamewallpapers.NAME, enable = false)
//public class Gamewallpapers extends WPTemp {
//    public static final String NAME = "Gamewallpapers";
//    private static final int PAGE_COUNT = 36;
//
//    public Gamewallpapers() {
//        index.set(0);
//        logger = LoggerFactory.getLogger(this.getClass());
//        baseUrl = "https://www.gamewallpapers.com/index.php?start=%d&page=";
//        infoMethods = new String[]{NAME + EXTRACT_INFO};
//        itemMethods = new String[]{NAME + EXTRACT_ITEM};
//        infoMethods_1 = new String[]{NAME + EXTRACT_INFO_1, WallPaperResult.WallPaperResult};
//    }
//
//    @Override
//    protected Config config(Config config) {
//        config.downThreadCount = 1;
//        config.breakpoint = true;
//        return config;
//    }
//
//    @Override
//    protected String getUrl() {
//        return String.format(baseUrl, index.getAndIncrement() * PAGE_COUNT);
//    }
//
//    @ExtractMethod(methods = {NAME + EXTRACT_ITEM})
//    private Result extractItem(Response response) {
//        List<String> urls = response.eval("//h1[@class='title-index']/a/@href");
//        List<String> tags = response.eval("//h1[@class='title-index']/a/text()");
//
//        Result resTmp;
//        if ((resTmp = duplicate(response, urls, true, true)) != null)
//            return resTmp;
//
//        if (urls.size() != tags.size())
//            throw new RuntimeException("获取数量错误 ==>" + response.request.url);
//
//        int urlIndex = 0;
//        for (String url : urls) {
//            Request request = new Request(name);
//            request.url = response.getSite() + "/" + url;
//            request.method = infoMethods;
//
//            String tag = tags.get(urlIndex);
//            if (StringUtils.isNotEmpty(tag))
//                request.put(TAGS, tag);
//            addTask(request);
//            logger.debug("request==>" + count.incrementAndGet());
//            urlIndex++;
//        }
//        return Result.makeIgnore();
//    }
//
//    @ExtractMethod(methods = {NAME + EXTRACT_INFO})
//    private Result extractInfo(Response response) {
//        List<String> urls = response.soup("#thumbnails-left-container > div > a.zoom4", "href");
//
//        for (String url : urls) {
//            Request request = response.request.clone();
//            request.url = response.getSite() + "/" + url;
//            request.method = infoMethods_1;
//            addTask(request);
//        }
//
//        return Result.makeIgnore();
//    }
//
//    @ExtractMethod(methods = {NAME + EXTRACT_INFO_1})
//    private Result extractInfo1(Response response) {
//        Result result = Result.make(response.request);
//        CgwallpapersModel model = new CgwallpapersModel();
//        model.imgWrapUrl = response.request.url;
//        String tag = response.request.removeMeta(TAGS);
//        if (StringUtils.isNotEmpty(tag))
//            model.tags = Arrays.asList(tag.split(","));
//
//        model.imgUrl = response.soupFirst("body > center > img", "src");
//
//        WallPaperResultModel resModel = model.cover();
//        result.put(RESULT, resModel);
//        downFile(resModel, ".jpg");
//
//        logger.debug("result==>" + count.decrementAndGet());
//
//        return result;
//    }
//}
