package samples.wallpaper;

import com.example.core.annotation.ExtractMethod;
import com.example.core.annotation.Spider;
import com.example.core.context.Config;
import com.example.core.download.DownloadWork;
import com.example.core.extract.ExtractUtils;
import com.example.core.models.Request;
import com.example.core.models.Response;
import com.example.core.models.Result;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Spider(name = Onlyhdwallpapers.NAME, enable = true)
public class Onlyhdwallpapers extends WPTemp {
    public static final String NAME = "Onlyhdwallpapers";
    private Map<String, String> downHeader = new HashMap<>();

    public Onlyhdwallpapers() {
        logger = LoggerFactory.getLogger(this.getClass());
        preReq = true;
        preUrl = "https://onlyhdwallpapers.com/";
        baseUrl = "https://onlyhdwallpapers.com/api_json/new/new/%d";
        headers = new HashMap<>();
        headers.put("content-encoding", "gzip");
        downHeader.put("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        downHeader.put("accept-encoding", "gzip");
        downHeader.put("accept-language", "zh-CN,zh;q=0.9");
        downHeader.put("cache-control", "max-age=0");
        itemMethods = new String[]{NAME + EXTRACT_ITEM};
        infoMethods = new String[]{NAME + EXTRACT_INFO};
        downType = DownloadWork.DownType.CLIENT_WEBDRIVER;
        responseCookie = true;
    }

    @Override
    protected Config config(Config config) {
        config.downThreadCount = 1;
        config.breakpoint = true;
        return config;
    }

    @Override
    protected String getUrl() {
        return String.format(baseUrl, index.getAndIncrement() * 60);
    }

    @Override
    protected void preReq(Response response) {
        super.preReq(response);
    }

    @ExtractMethod(methods = {NAME + EXTRACT_ITEM})
    private Result extractItem(Response response) {
        Result result = Result.make(response.request);
        JsonArray arr = response.gson().getAsJsonArray();
        List<String> urls = new ArrayList<>();

        for (int i = 0; i < arr.size(); i++) {
            JsonObject obj = arr.get(i).getAsJsonObject();
            String title = obj.get("title").getAsString();
            title = title.replaceAll("[ ]+", "-");
            title = title + "-" + obj.get("id").getAsString();
            urls.add(title);
        }

        Result resTmp;
        if ((resTmp = duplicate(response, urls, "https://onlyhdwallpapers.com/wallpaper/")) != null)
            return resTmp;

        List<WallPaperResultModel> resultModels = new ArrayList<>();
        for (String url : urls) {
            String imgUrl = "https://onlyhdwallpapers.com/wallpaper/" + url + ".jpg";
            OnlyhdwallpapersModel model = new OnlyhdwallpapersModel();
            model.imgWrapUrl = response.request.url;
            model.imgUrl = imgUrl;

            WallPaperResultModel resModel = model.cover();
            resultModels.add(resModel);
            downFile(resModel);
        }

        result.put(RESULT_LIST, resultModels);
        return result;
    }
}
