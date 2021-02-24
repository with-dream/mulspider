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

@Spider(name = Unsplash.NAME, enable = true)
public class Unsplash extends WPTemp {
    public static final String NAME = "Unsplash";

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
    protected String getUrl() {
        return String.format("https://unsplash.com/napi/landing_pages/wallpapers?page=%d&per_page=20", index.getAndIncrement());
    }

    @ExtractMethod(methods = {NAME + EXTRACT_ITEM})
    private Result extractItem(Response response) {
        JsonObject object = JsonParser.parseString(response.body).getAsJsonObject();
        JsonArray photos = object.getAsJsonArray("photos");
        for (int i = 0; i < photos.size(); i++) {
            JsonObject photo = photos.get(i).getAsJsonObject();
            int width = photo.get("width").getAsInt();
            logger.info("");
        }

        return Result.makeIgnore();
    }
}

/**

 * */