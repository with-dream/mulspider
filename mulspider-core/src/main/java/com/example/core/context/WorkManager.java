package com.example.core.context;

import com.example.core.db.DBManager;
import com.example.core.download.DownloadWork;
import com.example.core.extract.ExtractWork;
import com.example.core.result.ResultWork;
import com.example.core.utils.Constant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import samples.wallpaper.Cons;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class WorkManager {
    private final static Logger logger = LoggerFactory.getLogger(WorkManager.class);
    private DBManager dbManager;
    private Config config;
    private SpiderApp spiderApp;
    private boolean restartLimit = true;

    private AtomicInteger threadDownCount = new AtomicInteger(1);
    private AtomicInteger threadExtraCount = new AtomicInteger(1);
    private AtomicInteger threadResultCount = new AtomicInteger(1);
    private AtomicInteger threadDownCur = new AtomicInteger(0);
    private AtomicInteger threadExtraCur = new AtomicInteger(0);
    private AtomicInteger threadResultCur = new AtomicInteger(0);

    public WorkManager(SpiderApp spiderApp, Config config) {
        this.spiderApp = spiderApp;
        this.dbManager = spiderApp.dbManager;
        this.config = config;
    }

    public void checkThread(boolean force) {
        boolean restart = true;
        boolean continueWork = true;
        int reqSize = (int) dbManager.getRequestSize();

        if (!force) {
            Long restartTime = dbManager.get(Constant.RESTART_DELAY_TAG);
            continueWork = restartTime == null || restartTime == 0 || (restartTime > 0 && System.currentTimeMillis() > restartTime);
            restart = restartTime != null && restartTime > 0 && System.currentTimeMillis() > restartTime;
            if (restartTime != null && restartTime != 0)
                logger.info("restartTime diff==>{}", (restartTime - System.currentTimeMillis()) / 1000);

            String restartLimitStart = dbManager.get(Constant.RESTART_LIMIT_START);
            if (StringUtils.isNotEmpty(restartLimitStart)) {
                Long restartLimitDuration = dbManager.get(Constant.RESTART_LIMIT_DURATION);

                String[] format = {"ss", "mm:", "HH:", "dd ", "MM-"};
                String[] limitStart = restartLimitStart.split("-|\\s+|:");
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < limitStart.length; i++)
                    sb.append(format[i]);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(sb.toString());
                try {
                    Date dateStart = simpleDateFormat.parse(restartLimitStart);
                    Date curDate = new Date();
                    if (restartLimitDuration == null || restartLimitDuration == 0)
                        restartLimit = curDate.after(dateStart);
                    else {
                        Date dateEnd = new Date(dateStart.getTime() + restartLimitDuration);
                        restartLimit = curDate.after(dateStart) && curDate.before(dateEnd);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            if (restartLimit && restart && threadDownCur.get() == 0) {
                spiderApp.restart();
                dbManager.put(Constant.RESTART_DELAY_TAG, 0L);
            }
        }

        if (reqSize > 0 && restartLimit && continueWork) {
            int downTh = Math.min(reqSize / 3, threadDownCount.get());
            downTh = Math.max(1, downTh);
            if (downTh > threadDownCur.get())
                for (int i = threadDownCur.get(); i < downTh; i++) {
                    DownloadWork work = new DownloadWork(this.config);
                    work.threadCount = threadDownCount;
                    work.threadCountCur = threadDownCur;
                    work.stopForce = !restartLimit;
                    work.threadIndex = i;
                    work.setDbManager(this.dbManager);
                    Context.instance().add(work);
                }
        }

        int resSize = (int) dbManager.getResponseSize();
        if (resSize > 0) {
            int extractTh = Math.min(reqSize / 3, threadExtraCount.get());
            extractTh = Math.max(1, extractTh);
            if (extractTh > threadExtraCur.get())
                for (int i = threadExtraCur.get(); i < extractTh; i++) {
                    ExtractWork work = new ExtractWork(this.config);
                    work.threadCount = threadExtraCount;
                    work.threadCountCur = threadExtraCur;
                    work.threadIndex = i;
                    work.setDbManager(this.dbManager);
                    Context.instance().add(work);
                }
        }

        int resultSize = (int) dbManager.getResultSize();
        if (resultSize > 0) {
            int resultTh = Math.min(reqSize / 3, threadResultCount.get());
            resultTh = Math.max(1, resultTh);
            if (resultTh > threadResultCur.get())
                for (int i = threadResultCur.get(); i < resultTh; i++) {
                    Work work = new ResultWork(this.config);
                    work.threadCount = threadResultCount;
                    work.threadCountCur = threadResultCur;
                    work.threadIndex = i;
                    work.setDbManager(this.dbManager);
                    Context.instance().add(work);
                }
        }
    }

    public void init(int downThread, int extractThread, int resultThread) {
        threadDownCount.set(downThread);
        threadExtraCount.set(extractThread);
        threadResultCount.set(resultThread);

        checkThread(true);
    }
}
