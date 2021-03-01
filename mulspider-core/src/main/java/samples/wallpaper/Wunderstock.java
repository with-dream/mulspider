package samples.wallpaper;

import com.example.core.annotation.ExtractMethod;
import com.example.core.annotation.Spider;
import com.example.core.context.Config;
import com.example.core.models.Response;
import com.example.core.models.Result;
import com.example.core.utils.Constant;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Spider(name = Wunderstock.NAME, enable = false)
public class Wunderstock extends WPTempCate {
    public static final String NAME = "Wunderstock";
    public static final int OFFSET = 30;

    public Wunderstock() {
        logger = LoggerFactory.getLogger(this.getClass());
        cateMethods = new String[]{NAME + EXTRACT_CATE};
        infoMethods = new String[]{NAME + EXTRACT_INFO};
        itemMethods = new String[]{NAME + EXTRACT_ITEM};
        homeUrl = "https://wunderstock.com";
    }

    @Override
    protected Config config(Config config) {
        config.downThreadCount = 1;
        config.breakpoint = true;
        return config;
    }

    @Override
    protected String getCateUrl(String cate, int index) {
        return String.format("https://api.wunderstock.com/search?category=%s&lang=en&status=1&source=wunderstock&limit=%d&offset=%d", cate, OFFSET, index * OFFSET);
    }

    @ExtractMethod(methods = {NAME + EXTRACT_CATE})
    private Result extractCate(Response response) {
        Elements eles = response.jsoup().select("a.CardCategory_cardCategory__3Jdeu");
        List<String> cates = new ArrayList<>();
        for (Element e : eles)
            cates.add(e.attr("href"));
        for (String cate : cates) {
            cate = cate.substring(1);
            int cateIndex = initCateIndex(cate, 0);
            createCateReq(cate, getCateUrl(cate, cateIndex));
            if (Constant.DEBUG)
                break;
        }
        return Result.makeIgnore();
    }

    @ExtractMethod(methods = {NAME + EXTRACT_ITEM})
    private Result extractItem(Response response) {
        Result result = Result.make(response.request);
        result.request.method = new String[]{WallPaperResult.WallPaperResult};

        List<WallPaperResultModel> list = new ArrayList<>();
        List<String> imgSign = new ArrayList<>();
        JsonArray photos = JsonParser.parseString(response.body).getAsJsonObject().getAsJsonArray("data");
        for (int i = 0; i < photos.size(); i++) {
            WunderstockModel model = new WunderstockModel();
            JsonObject photo = photos.get(i).getAsJsonObject();
            String id = photo.get("id").getAsString();
            if (duplicate(response.getSite() + "/" + id, false))
                continue;

            imgSign.add(id);
            model.imgWrapUrl = response.request.url;
            model.imgUrl = photo.get("orig").getAsJsonObject()
                    .get("url").getAsString();
            model.fav = photo.get("likes").getAsString();
            model.views = photo.get("views").getAsString();
            model.tags = new ArrayList<>();
            JsonArray tagArr = photo.getAsJsonArray("subcategories");
            if (tagArr != null && tagArr.size() > 0)
                for (int j = 0; j < tagArr.size(); j++)
                    model.tags.add(tagArr.get(j).getAsString());

            WallPaperResultModel resModel = model.cover();
//            downFile(resModel);

            list.add(resModel);
        }
        result.put(RESULT_LIST, list);

        dupUrls(response, imgSign, true, true, true);

        return result;
    }
}