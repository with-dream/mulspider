package samples.wallpaper;

import com.example.core.annotation.ResultMethod;
import com.example.core.annotation.SpiderWork;
import com.example.core.annotation.WorkInit;
import com.example.core.models.Result;
import com.example.core.utils.D;
import java.util.concurrent.atomic.AtomicInteger;

@SpiderWork
public class WallPaperResult {
    public static final String WallPaperResult = "wallpaper.result";
    AtomicInteger count = new AtomicInteger(0);

    @WorkInit
    public void init() {

    }

    @ResultMethod(methods = {WallPaperResult}, lock = true)
    public void result(Result result) {
        D.d(count.incrementAndGet() + "==" + result.result);
    }
}
