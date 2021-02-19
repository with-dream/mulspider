package samples.wallpaper;

import com.example.core.annotation.ResultMethod;
import com.example.core.annotation.SpiderWork;
import com.example.core.annotation.WorkInit;
import com.example.core.models.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

@SpiderWork
public class WallPaperResult {
    private final static Logger logger = LoggerFactory.getLogger(WallPaperResult.class);
    public static final String WallPaperResult = "wallpaper.result";
    AtomicInteger count = new AtomicInteger(0);

    @WorkInit
    public void init() {

    }

    @ResultMethod(methods = {WallPaperResult}, lock = true)
    public void result(Result result) {
        logger.info(count.incrementAndGet() + "==" + result.result);
    }
}
