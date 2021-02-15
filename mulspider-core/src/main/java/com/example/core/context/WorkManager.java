package com.example.core.context;

import com.example.core.db.DBManager;
import com.example.core.download.DownloadWork;
import com.example.core.extract.ExtractWork;
import com.example.core.result.ResultWork;

import java.util.concurrent.atomic.AtomicInteger;

public class WorkManager {
    private DBManager dbManager;
    protected AtomicInteger threadDownCount = new AtomicInteger(1);
    protected AtomicInteger threadExtraCount = new AtomicInteger(1);
    protected AtomicInteger threadResultCount = new AtomicInteger(1);
    protected AtomicInteger threadDownCur = new AtomicInteger(0);
    protected AtomicInteger threadExtraCur = new AtomicInteger(0);
    protected AtomicInteger threadResultCur = new AtomicInteger(0);
    protected Config config;

    public WorkManager(DBManager dbManager, Config config) {
        this.dbManager = dbManager;
        this.config = config;
    }

    public void init(int downThread, int extractThread, int resultThread) {
        threadDownCount.set(downThread);
        threadExtraCount.set(extractThread);
        threadResultCount.set(resultThread);
        for (int i = 0; i < downThread; i++) {
            Work work = new DownloadWork(this.config);
            work.threadCount = threadDownCount;
            work.threadCountCur = threadDownCur;
            work.threadIndex = i;
            work.setDbManager(this.dbManager);
            Context.instance().add(work);
        }

        for (int i = 0; i < extractThread; i++) {
            ExtractWork work = new ExtractWork(this.config);
            work.threadCount = threadExtraCount;
            work.threadCountCur = threadExtraCur;
            work.threadIndex = i;
            work.setDbManager(this.dbManager);
            Context.instance().add(work);
        }

        for (int i = 0; i < resultThread; i++) {
            Work work = new ResultWork(this.config);
            work.threadCount = threadResultCount;
            work.threadCountCur = threadResultCur;
            work.threadIndex = i;
            work.setDbManager(this.dbManager);
            Context.instance().add(work);
        }
    }
}
