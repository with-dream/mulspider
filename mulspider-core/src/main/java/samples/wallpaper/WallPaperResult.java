package samples.wallpaper;

import com.example.core.annotation.ExtractMethod;
import com.example.core.annotation.ResultMethod;
import com.example.core.annotation.SpiderWork;
import com.example.core.annotation.WorkInit;
import com.example.core.models.Request;
import com.example.core.models.Response;
import com.example.core.models.Result;
import com.example.core.utils.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

@SpiderWork
public class WallPaperResult {
    private final static Logger logger = LoggerFactory.getLogger(WallPaperResult.class);
    public static final String WallPaperResult = "wallpaper.result";
    public static final String WallPaperFile = "wallpaper.file";
    public static final String DOWN_PATH = "./file/download/";
    AtomicInteger count = new AtomicInteger(0);

    @WorkInit
    public void init() {

    }

    @ResultMethod(methods = {WallPaperResult})
    public void result(Result result) {

        logger.info(count.incrementAndGet() + "==" + result.result);
    }

    @ExtractMethod(methods = {WallPaperFile})
    public Result downFile(Response response) {
        logger.info("downFile==>" + response);
        //TODO 下载失败的图片 需要另外处理
        return Result.makeIgnore();
    }
}
