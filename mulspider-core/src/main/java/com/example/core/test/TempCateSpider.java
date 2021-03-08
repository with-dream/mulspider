package com.example.core.test;

import com.example.core.download.DownloadWork;
import com.example.core.models.Request;
import com.example.core.models.Response;
import com.example.core.utils.Constant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class TempCateSpider extends TempSpider {
    protected Map<String, AtomicInteger> indexMap = new HashMap<>();

    protected static final String DUP_COUNT = "dup_count_";
    protected static final String CATE = "cater";
    protected String homeUrl;
    protected String cateUrl;
    private AtomicInteger resetCount = new AtomicInteger(0);

    @Override
    protected void initUrl() {
        initRequest(homeUrl, cateMethods);
    }

    protected int initCateIndex(String cate) {
        Integer indexTmp = dbManager.get(cate);
        if (indexTmp == null)
            indexTmp = initIndex();
        AtomicInteger aInt = new AtomicInteger(indexTmp);
        indexMap.put(cate, aInt);
        indexMap.put(DUP_COUNT + cate, new AtomicInteger(0));
        resetCount.incrementAndGet();
        return aInt.get();
    }

    protected void createCateReq(String cate, String url) {
        Request request = new Request(name);
        if (downType == DownloadWork.DownType.CLIENT_POOL)
            request.httpPool();
        else if (downType == DownloadWork.DownType.CLIENT_WEBDRIVER)
            request.headless();
        request.method = itemMethods;
        request.meta.put(CATE, cate);
        request.url = url;
        addTask(request, true);
    }

    protected String getCateUrl(String cate, int index) {
        return String.format(cateUrl, cate, index);
    }

    protected boolean dupUrls(Response response, List<String> urls, boolean site) {
        return dupUrls(response, urls, site, false);
    }

    protected boolean dupUrls(Response response, List<String> urls, boolean site, boolean separator) {
        return dupUrls(response, urls, (site ? response.getSite() : "") + (separator ? "/" : ""), false);
    }

    /**
     * @return true 有为请求的url false url全部重复
     */
    protected boolean dupUrls(Response response, List<String> urls, String baseUrl, boolean save) {
        String cate = response.request.getMeta(CATE);
        AtomicInteger cateDupCount = indexMap.get(DUP_COUNT + response.request.getMeta(CATE));
        AtomicInteger integer = indexMap.get(cate);
        if (dupList(baseUrl, urls, save) != 0) {
            cateDupCount.set(0);
            if (Constant.DEBUG && integer.get() > 3)
                return false;
            createCateReq(cate, getCateUrl(cate, integer.incrementAndGet()));
            dbManager.put(cate, integer.get());
        } else {
            if (cateDupCount.incrementAndGet() < 3) {
                logger.warn("dupCount==>" + dupCount);
                createCateReq(cate, getCateUrl(cate, integer.incrementAndGet()));
                dbManager.put(cate, integer.get());
            } else {
                logger.warn("==>dup reset");
                integer.set(initIndex());
                dbManager.put(cate, integer.get());
                cateDupCount.set(0);
                logger.info("resetCount==>" + resetCount.get());
                if (resetCount.decrementAndGet() == 0) {
                    restartTime(Constant.RESTART_TIME);
                    resetCount.set(0);
                }
            }
            return false;
        }
        return true;
    }
}
