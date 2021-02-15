package com.example.core.extract;

import com.example.core.context.Config;
import com.example.core.context.Work;
import com.example.core.models.Response;
import com.example.core.utils.Constant;
import com.example.core.utils.ThreadUtils;

public class ExtractWork extends Work {

    public ExtractWork(Config config) {
        super(config);
    }

    @Override
    protected boolean work() {
//        if (System.currentTimeMillis() - currentDelayTime > closeDelayTime)
//            return false;

        Response response = dbManager.getResponse();
        if (response == null) {
            delay(Constant.EMPTY_DELAY_TIME);
            return true;
        }
        if (config.breakpoint)
            dbManager.put(threadIndex + Constant.EXTRACT, response);

        handleTask(WorkType.extract, response, response.request);

        if (config.breakpoint)
            dbManager.del(threadIndex + Constant.EXTRACT);

        ThreadUtils.sleep(response.request.delayTime);
        currentDelayTime = System.currentTimeMillis();
        return true;
    }


}
