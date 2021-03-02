package samples.wallpaper;

import com.example.core.context.SpiderApp;
import com.example.core.download.DownloadWork;
import com.example.core.models.Request;
import com.example.core.models.Response;
import com.example.core.models.Result;
import com.example.core.utils.Constant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class WPTemp extends SpiderApp {
    protected Logger logger;

    protected static final String EXTRACT_ITEM = ".extractItem";
    protected static final String EXTRACT_INFO = ".extractInfo";
    protected static final String EXTRACT_INFO_1 = ".extractInfo_1";
    protected static final String TAGS = "tags";
    protected static final String THUM = "thumbnail";
    protected static final String THUMW = "thumW";
    protected static final String THUMH = "thumH";
    protected static final String RESULT = "result";
    protected static final String RESULT_LIST = "result_list";
    protected static final String JPG = ".jpg";

    protected AtomicInteger index = new AtomicInteger(1);
    protected AtomicInteger count = new AtomicInteger(0);
    protected int emptyCount, dupCount;
    protected String baseUrl;
    protected String[] infoMethods;
    protected String[] infoMethods_1;
    protected String[] itemMethods;
    protected String[] downMethods;
    protected DownloadWork.DownType downType = DownloadWork.DownType.CLIENT_POOL;
    protected Map<String, String> headers;

    @Override
    public void init() {
        Integer pindex = dbManager.get("pageIndex");
        if (pindex != null && pindex > 0)
            index.set(pindex);
        logger.info("init pindex==>" + pindex);
        downMethods = new String[]{WallPaperResult.WallPaperFile};
        initRequest(getUrl());
    }

    protected void initRequest(String url, String[] mothods) {
        Request request = new Request(name);
        if (downType == DownloadWork.DownType.CLIENT_POOL)
            request.httpPool();
        else if (downType == DownloadWork.DownType.CLIENT_WEBDRIVER)
            request.headless();
        request.method = mothods;
        request.url = url;
        if (headers != null && !headers.isEmpty())
            request.headers = headers;
        addTask(request, true);
    }

    protected void initRequest(String url) {
        initRequest(url, itemMethods);
    }

    protected Result duplicate(Response response, List<String> urls, boolean site, boolean separator) {
        return duplicate(response, urls, (site ? response.getSite() : "") + (separator ? "/" : ""));
    }

    protected Result duplicate(Response response, List<String> urls, boolean site) {
        return duplicate(response, urls, site ? response.getSite() : "");
    }

    protected Result duplicate(Response response, List<String> urls, String baseUrl) {
        if (urls == null || urls.isEmpty()) {
            logger.warn("==>extractItem urls is empty  emptyCount:" + emptyCount + " url:" + response.request.url);
            if (emptyCount++ == 5) {
                index.set(0);
                dbManager.put("pageIndex", index.get());
            }
            return Result.makeIgnore();
        }
        emptyCount = 0;

        if (dupList(baseUrl, urls, false) != 0) {
            dupCount = 0;
            if (Constant.DEBUG && index.get() < 10)
                addRequest(response);
        } else if (dupCount++ < 3) {
            logger.warn("dupCount==>" + dupCount);
            addRequest(response);
        } else {
            logger.warn("==>dup reset");
            index.set(0);
            dbManager.put("pageIndex", index.get());
            emptyCount = 0;
            dupCount = 0;
        }
        return null;
    }

    protected void addRequest(Response response) {
        Request request = response.request.clone();
        request.url = getUrl();
        request.method = itemMethods;
        dbManager.put("pageIndex", index.get());
        addTask(request);
    }

    protected String getUrl() {
        return String.format(baseUrl, index.getAndIncrement());
    }

    protected void downFile(WallPaperResultModel model, String suffix) {
        Request request = new Request(name);
        request.url = model.imgUrl;
        request.method = downMethods;

        String imgSuffix = suffix;
        if (StringUtils.isEmpty(imgSuffix))
            imgSuffix = model.imgUrl.substring(model.imgUrl.lastIndexOf("."));
        request.meta.put(Constant.DOWN_FILE, WallPaperResult.DOWN_PATH + name + "/" + UUID.randomUUID().toString()
                + imgSuffix);
        addTask(request);
    }

    protected void downFile(WallPaperResultModel model) {
        downFile(model, null);
    }
}
