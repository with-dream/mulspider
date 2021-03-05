package com.example.core.result;

import com.example.core.context.Config;
import com.example.core.context.Work;
import com.example.core.models.Result;
import com.example.core.utils.Constant;

public class ResultWork extends Work {

    public ResultWork(Config config) {
        super(config);
    }

    @Override
    protected boolean work() {
        if (System.currentTimeMillis() - currentDelayTime > closeDelayTime) {
            logger.info("==>result work close");
            return false;
        }

        Result result = dbManager.getResult();
        if (result == null) {
            delay(Constant.EMPTY_DELAY_TIME);
            return true;
        }

        if (config.breakpoint)
            dbManager.put(threadIndex + Constant.RESULT_BK, result);

        handleTask(WorkType.result, result, result.request);

        if (config.breakpoint)
            dbManager.del(threadIndex + Constant.RESULT_BK);

        delay(result.request.delayTime);
        currentDelayTime = System.currentTimeMillis();
        return true;
    }
}
