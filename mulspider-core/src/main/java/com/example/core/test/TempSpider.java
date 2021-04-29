package com.example.core.test;

import com.example.core.annotation.ExtractMethod;
import com.example.core.context.SpiderApp;
import com.example.core.download.DownloadWork;
import com.example.core.models.Request;
import com.example.core.models.Response;
import com.example.core.models.Result;
import com.example.core.utils.Constant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import samples.wallpaper.WallPaperResult;
import samples.wallpaper.WallPaperResultModel;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class TempSpider extends SpiderApp {
    protected Logger logger;

    protected static final String PRE_ITEM = "preItem";
    protected static final String EXTRACT_CATE = ".extractCate";
    protected static final String EXTRACT_ITEM = ".extractItem";
    protected static final String EXTRACT_INFO = ".extractInfo";
    protected static final String EXTRACT_INFO_1 = ".extractInfo_1";
    protected static final String DOWN_FILE = ".down_file";

    protected static final String RESULT = "result";
    protected static final String RESULT_LIST = "result_list";

    protected AtomicInteger index = new AtomicInteger(1);
    protected AtomicInteger count = new AtomicInteger(0);
    protected AtomicInteger emptyCount = new AtomicInteger(0);
    protected AtomicInteger dupCount = new AtomicInteger(0);
    protected boolean preReq;
    protected String baseUrl, preUrl;
    private static String[] preMethods = new String[]{PRE_ITEM};
    protected String[] cateMethods;
    protected String[] itemMethods;
    protected String[] infoMethods;
    protected String[] infoMethods_1;
    protected String[] downMethods;

    protected DownloadWork.DownType downType = DownloadWork.DownType.CLIENT_POOL;
    protected boolean responseCookie;
    protected Map<String, String> headers;

    @Override
    public void init() {
        cateMethods = new String[]{name + EXTRACT_CATE};
        itemMethods = new String[]{name + EXTRACT_ITEM};
        infoMethods = new String[]{name + EXTRACT_INFO};
        infoMethods_1 = new String[]{name + EXTRACT_INFO_1};
        downMethods = new String[]{name + DOWN_FILE};

        if (preReq) {
            initRequest(preUrl, preMethods);
        } else {
            initUrl();
        }
    }

    protected void initUrl() {
        Integer pindex = dbManager.get("pageIndex");
        if (pindex == null)
            pindex = initIndex();
        index.set(pindex);
        logger.info("init pindex==>" + pindex);
        downMethods = new String[]{WallPaperResult.WallPaperFile};
        initRequest(getUrl());
    }

    @ExtractMethod(methods = {PRE_ITEM})
    private Result preMethod(Response response) {
        preReq(response);
        initUrl();
        return Result.makeIgnore();
    }

    protected void preReq(Response response) {

    }

    protected int initIndex() {
        return 1;
    }

    protected void initRequest(String url, String[] mothods) {
        Request request = new Request(name);
        if (downType == DownloadWork.DownType.CLIENT_WEBDRIVER)
            request.headless();
        else
            request.httpPool();
        request.responseCookie = responseCookie;
        request.method = mothods;
        request.url = url;
        if (headers != null && !headers.isEmpty())
            request.headers = headers;
        addTask(request, true);
    }

    protected void initRequest(String url) {
        initRequest(url, itemMethods);
    }

    protected boolean duplicate(Response response, List<String> urls, boolean site, boolean separator) {
        return duplicate(response, urls, (site ? response.getSite() : "") + (separator ? "/" : ""));
    }

    protected boolean duplicate(Response response, List<String> urls, boolean site) {
        return duplicate(response, urls, site ? response.getSite() : "");
    }

    protected boolean duplicate(Response response, List<String> urls, String baseUrl) {
        return duplicate(response, urls, baseUrl, false);
    }

    protected boolean duplicate(Response response, List<String> urls, String baseUrl, boolean save) {
        if (urls == null || urls.isEmpty()) {
            logger.warn("==>extractItem urls is empty  emptyCount:" + emptyCount + " url:" + response.request.url);
            if (emptyCount.incrementAndGet() == 5) {
                index.set(0);
                dbManager.put("pageIndex", index.get());
            }
            return false;
        }
        emptyCount.set(0);

        if (dupList(baseUrl, urls, save) != 0) {
            dupCount.set(0);
            if (Constant.DEBUG) {
                if (index.get() < 10)
                    addRequest(response, false);
            } else
                addRequest(response, false);
        } else if (dupCount.incrementAndGet() < 3) {
            logger.warn("dupCount==>" + dupCount.get());
            addRequest(response, true);
        } else {
            logger.warn("==>dup reset");
            index.set(0);
            dbManager.put("pageIndex", index.get());
            emptyCount.set(0);
            dupCount.set(0);
            restartTime(Constant.RESTART_TIME);
            return false;
        }
        return true;
    }

    protected void addRequest(Response response, boolean force) {
        Request request = response.request.clone();
        request.url = getUrl();
        request.method = itemMethods;
        dbManager.put("pageIndex", index.get());
        addTask(request, force);
    }

    protected String getUrl() {
        return String.format(baseUrl, index.getAndIncrement());
    }

    protected void downFile(String url, String fileName, String suffix) {
        Request request = new Request(this.name);
        request.url = url;
        if (downType == DownloadWork.DownType.CLIENT_POOL)
            request.httpPool();
        else if (downType == DownloadWork.DownType.CLIENT_WEBDRIVER)
            request.headless();
        request.method = downMethods;

        String imgSuffix = suffix;
        if (StringUtils.isEmpty(imgSuffix))
            imgSuffix = url.substring(url.lastIndexOf("."));
        request.meta.put(Constant.DOWN_FILE_PATH, WallPaperResult.DOWN_PATH + this.name);
        request.meta.put(Constant.DOWN_FILE_NAME, (StringUtils.isEmpty(fileName) ? UUID.randomUUID().toString() : fileName)
                + imgSuffix);
        addTask(request);
    }
}


