package samples.wallpaper;

import com.example.core.context.SpiderApp;
import com.example.core.download.DownloadWork;
import com.example.core.models.Request;
import com.example.core.models.Response;
import com.example.core.models.Result;
import com.example.core.utils.Constant;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class WPTempCate extends WPTemp {
    protected Map<String, AtomicInteger> indexMap = new HashMap<>();
    protected static final String EXTRACT_CATE = ".extractCate";

    protected static final String DUP = "dup_";
    protected static final String CATE = "cater";
    protected String[] cateMethods;
    protected String homeUrl;

    @Override
    public void init() {
        initRequest(homeUrl, cateMethods);
    }

    protected int initCateIndex(String cate) {
        return initCateIndex(cate, 1);
    }

    protected int initCateIndex(String cate, int start) {
        Integer indexTmp = dbManager.get(cate);
        if (indexTmp == null)
            indexTmp = start;
        AtomicInteger aInt = new AtomicInteger(indexTmp);
        indexMap.put(cate, aInt);
        dbManager.put(cate, aInt.get());
        indexMap.put(DUP + cate, new AtomicInteger(0));
        return aInt.get();
    }

    protected void createCateReq(String cate, String url) {
        Request request = new Request(name);
        request.httpPool();
        request.method = itemMethods;
        request.meta.put(CATE, cate);
        request.url = url;
        addTask(request, true);
    }

    protected String getCateUrl(String cate, int index) {
        return null;
    }

    protected void addRequest(Response response, String url) {
        Request request = response.request.clone();
        request.method = infoMethods;
        request.url = response.getSite() + url;
        addTask(request);
    }

    protected boolean dupUrls(Response response, List<String> urls, boolean site, boolean separator, boolean save) {
        String cate = response.request.getMeta(CATE);
        if (dupList((site ? response.getSite() : "") + (separator ? "/" : ""), urls, save) != 0) {
            indexMap.get(DUP + response.request.getMeta(CATE)).set(0);
            AtomicInteger integer = indexMap.get(cate);
            if (Constant.DEBUG && integer.get() > 3)
                return false;
            createCateReq(cate, getCateUrl(cate, integer.incrementAndGet()));
            dbManager.put(cate, integer.get());
        } else {
            if (indexMap.get(DUP + response.request.getMeta(CATE)).incrementAndGet() < 3) {
                logger.warn("dupCount==>" + dupCount);
                createCateReq(response.request.getMeta(CATE), getCateUrl(response.request.getMeta(CATE), indexMap.get(CATE).incrementAndGet()));
            } else {
                logger.warn("==>dup reset");
                indexMap.get(cate).set(0);
                dbManager.put(cate, indexMap.get(cate).get());
                indexMap.get(DUP + cate).set(0);
            }
            return false;
        }
        return true;
    }
}
