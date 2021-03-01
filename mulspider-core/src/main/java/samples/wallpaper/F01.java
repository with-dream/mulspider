package samples.wallpaper;

import com.example.core.annotation.ExtractMethod;
import com.example.core.annotation.Spider;
import com.example.core.context.Config;
import com.example.core.models.Request;
import com.example.core.models.Response;
import com.example.core.models.Result;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Spider(name = F01.NAME, enable = true)
public class F01 extends WPTemp {
    public static final String NAME = "F01";
    private static final int PAGE_COUNT = 30;

    public F01() {
        logger = LoggerFactory.getLogger(this.getClass());
        baseUrl = "https://f01.co/wallpaper/api.php?cid=360new&start=%d&count=%d";
        headers = new HashMap<>();
        headers.put("accept-encoding", "gzip");
        itemMethods = new String[]{NAME + EXTRACT_ITEM, WallPaperResult.WallPaperResult};
    }

    @Override
    protected Config config(Config config) {
        config.downThreadCount = 1;
        config.breakpoint = true;
        return config;
    }

    @Override
    protected String getUrl() {
        return String.format(baseUrl, index.getAndIncrement() * PAGE_COUNT, PAGE_COUNT);
    }

    @ExtractMethod(methods = {NAME + EXTRACT_ITEM})
    private Result extractItem(Response response) {
        Result result = Result.make(response.request);

        List<WallPaperResultModel> results = new ArrayList<>();
        List<String> urls = new ArrayList<>();
        JsonArray datas = response.gson().getAsJsonObject().getAsJsonArray("data");
        for (int i = 0; i < datas.size(); i++) {
            JsonObject info = datas.get(i).getAsJsonObject();
            F01Model model = new F01Model();
            model.imgWrapUrl = response.request.url;
            model.imgUrl = info.get("url").getAsString();
            urls.add(model.imgUrl);
            model.fav = info.get("download_times").getAsString();
            String tag = info.get("tag").getAsString();
            if (StringUtils.isNotEmpty(tag)) {
                String[] t = tag.split("_category_");
                if (ArrayUtils.isNotEmpty(t) && t.length > 1) {
                    model.tags = new ArrayList<>();
                    for (int j = 1; j < t.length; j++)
                        model.tags.add(t[j].replace("_", "").trim());
                }
            }

            WallPaperResultModel resModel = model.cover();
            results.add(resModel);
            downFile(resModel);
        }

        Result resTmp;
        if ((resTmp = duplicate(response, urls, false)) != null)
            return resTmp;

        result.result.put(RESULT_LIST, results);
        return result;
    }

}
