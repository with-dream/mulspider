package samples.wallpaper;

import com.example.core.annotation.ExtractMethod;
import com.example.core.annotation.Spider;
import com.example.core.context.Config;
import com.example.core.download.DownloadWork;
import com.example.core.models.Request;
import com.example.core.models.Response;
import com.example.core.models.Result;
import com.example.core.utils.Constant;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Spider(name = Unsplash.NAME, enable = true)
public class Unsplash extends WPTemp {
    public static final String NAME = "Unsplash";
    private Map<String, AtomicInteger> indexMap = new HashMap<>();
    private static final String DUP = "dup_";
    private static final String CATE = "cater";

    public Unsplash() {
        logger = LoggerFactory.getLogger(this.getClass());
        infoMethods = new String[]{NAME + EXTRACT_INFO, WallPaperResult.WallPaperResult};
        listMethods = new String[]{NAME + EXTRACT_ITEM};
    }

    @Override
    protected Config config(Config config) {
        config.downThreadCount = 1;
        config.breakpoint = true;
        return config;
    }

    @Override
    public void init() {
        Request request = new Request(name);
        request.httpPool();
        request.method = listMethods;
        request.url = "https://unsplash.com";
        addTask(request, true);
    }

    @Override
    protected String getUrl() {
        return String.format("https://unsplash.com/napi/landing_pages/wallpapers?page=%d&per_page=20", index.getAndIncrement());
    }

    @ExtractMethod(methods = {NAME + EXTRACT_ITEM})
    private Result extractItem(Response response) {
        List<String> cates = response.eval("//li/a[@class='qvEaq _1CBrG']/@href");
        for (String cate : cates) {
            cate = cate.substring(cate.lastIndexOf("/") + 1);
            Integer indexTmp = dbManager.get(cate);
            if (indexTmp == null)
                indexTmp = 1;
            AtomicInteger aInt = new AtomicInteger(indexTmp);
            indexMap.put(cate, aInt);
            dbManager.put(cate, aInt.get());

            indexMap.put(DUP + cate, new AtomicInteger(0));

            createReq(cate, aInt.get());
        }
        return Result.makeIgnore();
    }

    private void createReq(String cate, int index) {
        String url = String.format("https://unsplash.com/napi/topics/%s/photos?page=%d&per_page=20", cate, index);
        Request request = new Request(name);
        request.httpPool();
        request.method = infoMethods;
        request.meta.put(CATE, cate);
        request.url = url;
        addTask(request, true);
    }

    @ExtractMethod(methods = {NAME + EXTRACT_INFO})
    private Result extractInfo(Response response) {
        Result result = Result.make(response.request);

        List<UnsplashModel> list = new ArrayList<>();
        List<String> imgSign = new ArrayList<>();
        JsonArray photos = JsonParser.parseString(response.body).getAsJsonArray();
        for (int i = 0; i < photos.size(); i++) {
            UnsplashModel model = new UnsplashModel();
            JsonObject photo = photos.get(i).getAsJsonObject();
            imgSign.add(photo.get("id").getAsString());
            model.imgWrapUrl = response.request.url;
            model.imgUrl = photo.get("urls").getAsJsonObject()
                    .get("raw").getAsString();
            model.imgW = photo.get("width").getAsString();
            model.imgH = photo.get("height").getAsString();
            model.fav = photo.get("likes").getAsString();
            model.tags = new ArrayList<>();
            JsonArray tagArr = photo.getAsJsonArray("tags");
            if (tagArr != null && tagArr.size() > 0)
                for (int j = 0; j < tagArr.size(); i++)
                    model.tags.add(tagArr.get(i).getAsJsonObject().get("title").getAsString());
            list.add(model);
        }
        result.put(RESULTS, list);

        String cate = response.request.getMeta(CATE);
        if (dupList(response.request.getSite(), imgSign, true, true) != 0) {
            indexMap.get(DUP + response.request.getMeta(CATE)).set(0);
            AtomicInteger integer = indexMap.get(cate);
            if (Constant.DEBUG && integer.get() > 3)
                return result;
            createReq(cate, integer.incrementAndGet());
            dbManager.put(cate, integer.get());
        } else {
            if (indexMap.get(DUP + response.request.getMeta(CATE)).incrementAndGet() < 3) {
                logger.warn("dupCount==>" + dupCount);
                createReq(response.request.getMeta(CATE), indexMap.get(CATE).incrementAndGet());
            } else {
                logger.warn("==>dup reset");
                indexMap.get(cate).set(0);
                dbManager.put(cate, indexMap.get(cate).get());
                indexMap.get(DUP + cate).set(0);
            }
        }

        return result;
    }
}