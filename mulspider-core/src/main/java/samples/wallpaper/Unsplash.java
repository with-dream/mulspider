package samples.wallpaper;

import com.example.core.annotation.ExtractMethod;
import com.example.core.annotation.Spider;
import com.example.core.context.Config;
import com.example.core.models.Response;
import com.example.core.models.Result;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Spider(name = Unsplash.NAME, enable = false)
public class Unsplash extends WPTempCate {
    public static final String NAME = "Unsplash";

    public Unsplash() {
        logger = LoggerFactory.getLogger(this.getClass());
        cateMethods = new String[]{NAME + EXTRACT_CATE};
        infoMethods = new String[]{NAME + EXTRACT_INFO};
        itemMethods = new String[]{NAME + EXTRACT_ITEM};
        homeUrl = "https://unsplash.com";
    }

    @Override
    protected Config config(Config config) {
        config.downThreadCount = 1;
        config.breakpoint = true;
        return config;
    }

    @Override
    protected String getUrl() {
        return String.format("https://unsplash.com/napi/landing_pages/wallpapers?page=%d&per_page=20", index.getAndIncrement());
    }

    @Override
    protected String getCateUrl(String cate, int index) {
        return String.format("https://unsplash.com/napi/topics/%s/photos?page=%d&per_page=20", cate, index);
    }

    @ExtractMethod(methods = {NAME + EXTRACT_CATE})
    private Result extractCate(Response response) {
        List<String> cates = response.eval("//li/a[@class='qvEaq _1CBrG']/@href");
        for (String cate : cates) {
            cate = cate.substring(cate.lastIndexOf("/") + 1);
            int cateIndex = initCateIndex(cate);
            createCateReq(cate, getCateUrl(cate, cateIndex));
        }
        return Result.makeIgnore();
    }

    @ExtractMethod(methods = {NAME + EXTRACT_ITEM})
    private Result extractItem(Response response) {
        Result result = Result.make(response.request);
        response.request.method = new String[]{WallPaperResult.WallPaperResult};

        List<WallPaperResultModel> list = new ArrayList<>();
        List<String> imgSign = new ArrayList<>();
        JsonArray photos = response.gson().getAsJsonArray();
        for (int i = 0; i < photos.size(); i++) {
            UnsplashModel model = new UnsplashModel();
            JsonObject photo = photos.get(i).getAsJsonObject();
            String id = photo.get("id").getAsString();
            if (duplicate(response.getSite() + "/" + id, false))
                continue;

            imgSign.add(id);
            model.imgWrapUrl = response.request.url;
            model.imgUrl = photo.get("urls").getAsJsonObject()
                    .get("raw").getAsString();
            model.imgW = photo.get("width").getAsString();
            model.imgH = photo.get("height").getAsString();
            model.fav = photo.get("likes").getAsString();
            model.tags = new ArrayList<>();
            JsonArray tagArr = photo.getAsJsonArray("tags");
            if (tagArr != null && tagArr.size() > 0)
                for (int j = 0; j < tagArr.size(); j++)
                    model.tags.add(tagArr.get(j).getAsJsonObject().get("title").getAsString());

            WallPaperResultModel resModel = model.cover();
            downFile(resModel);

            list.add(resModel);
        }
        result.put(RESULT_LIST, list);

        dupUrls(response, imgSign, true, true, true);

        return result;
    }
}