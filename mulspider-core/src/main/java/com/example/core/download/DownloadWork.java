package com.example.core.download;

import com.example.core.context.Config;
import com.example.core.context.Context;
import com.example.core.download.proxypool.LocalProxyPool;
import com.example.core.models.Request;
import com.example.core.models.Response;
import com.example.core.context.Work;
import com.example.core.download.httppool.HttpClientPoolDownloader;
import com.example.core.download.selenium.SeleniumDownloader;
import com.example.core.utils.Constant;
import com.example.core.utils.ThreadUtils;
import org.apache.commons.lang3.StringUtils;

public class DownloadWork extends Work {
    public enum DownType {
        CLIENT_POOL, CLIENT_WEBDRIVER
    }

    private DownloadHandle downloader;
    public volatile boolean stopForce;

    public DownloadWork(Config config) {
        super(config);
    }

    @Override
    protected boolean work() {
        if (stopForce)
            return false;

        if (System.currentTimeMillis() - currentDelayTime > closeDelayTime) {
            logger.info("==>down work close");
            return false;
        }

        Request request = dbManager.getRequest();
        if (request == null) {
            delay(Constant.EMPTY_DELAY_TIME);
            return true;
        }

        if (config.breakpoint)
            dbManager.put(threadIndex + Constant.REQUEST_BK, request);

        switch (request.downType) {
            case CLIENT_POOL:
                if (downloader == null || !(downloader instanceof HttpClientPoolDownloader))
                    downloader = new HttpClientPoolDownloader();
                break;
            case CLIENT_WEBDRIVER:
                if (downloader == null || !(downloader instanceof SeleniumDownloader))
                    downloader = new SeleniumDownloader();
                break;
        }

        downloader.downloadTimeout = config.downloadTimeout;

        if (Context.instance().globalConfig.proxy_pool) {
            LocalProxyPool.proxy(request);
        }

        Response response = downloader.down(request);
        if (response == null || (StringUtils.isEmpty(response.body) && StringUtils.isEmpty(request.getMeta(Constant.DOWN_FILE_PATH)))) {
            logger.error("DownloadWork response is empty==>" + request.url);
            ThreadUtils.sleep(Constant.EMPTY_DELAY_TIME);
            return true;
        }
        if (StringUtils.isEmpty(request.getMeta(Constant.DOWN_FILE_PATH)))
            logger.debug("DownloadWork==>" + response.body.length());

        dbManager.addTask(response);

        if (config.breakpoint)
            dbManager.del(threadIndex + Constant.REQUEST_BK);

        ThreadUtils.sleep(Math.max(Constant.MIN_DOWN_DELAY_TIME, response.request.delayTime));
        currentDelayTime = System.currentTimeMillis();
        return true;
    }
}
