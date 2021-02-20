package samples.wallpaper;

import com.example.core.context.Config;
import com.example.core.context.SpiderApp;
import com.example.core.download.DownloadWork;
import com.example.core.models.Request;
import com.example.core.models.Response;
import com.example.core.models.Result;
import org.slf4j.Logger;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class WPTemp extends SpiderApp {
    protected Logger logger;

    protected static final String EXTRACT_ITEM = ".extractItem";
    protected static final String EXTRACT_INFO = ".extractInfo";

    protected AtomicInteger index = new AtomicInteger(1);
    protected AtomicInteger count = new AtomicInteger(0);
    protected int emptyCount, dupCount;
    protected String baseUrl;
    protected String[] infoMethods;
    protected String[] listMethods;
    protected DownloadWork.DownType downType = DownloadWork.DownType.CLIENT_POOL;

    @Override
    public void init() {
        Integer pindex = dbManager.get("pageIndex");
        if (pindex != null && pindex > 0)
            index.set(pindex);
        logger.info("init pindex==>" + pindex);
        Request request = new Request(name);
        if (downType == DownloadWork.DownType.CLIENT_POOL)
            request.httpPool();
        else if (downType == DownloadWork.DownType.CLIENT_WEBDRIVER)
            request.headless();
        request.method = listMethods;
        request.url = baseUrl + index.get();
        addTask(request, true);
    }

    protected Result duplicate(Response response, List<String> urls, boolean site) {
        return duplicate(response, urls, site, false);
    }

    protected Result duplicate(Response response, List<String> urls, boolean site, boolean separator) {
        if (urls == null || urls.isEmpty()) {
            logger.warn("==>extractItem urls is empty  emptyCount:" + emptyCount + " url:" + response.request.url);
            if (emptyCount++ == 5) {
                index.set(0);
                dbManager.put("pageIndex", index.get());
            }
            return Result.makeIgnore();
        }
        emptyCount = 0;

        if (dupList(site ? response.request.getSite() : "", urls, separator) != 0) {
            dupCount = 0;
            if (index.get() != 10)
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
        int indexTmp = index.incrementAndGet();
        request.url = baseUrl + indexTmp;
        request.method = listMethods;
        dbManager.put("pageIndex", index.get());
        addTask(request);
    }

}
