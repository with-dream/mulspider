import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;

public class Test {
    private final static Logger logger = LoggerFactory.getLogger(Test.class);

    public static void main(String[] args) {
        String urls = "yyyy-MM-dd HH:mm:ss";
        String[] url = urls.split("-|\\s+|:");
        logger.error("%s{}", "=====" + Arrays.toString(url));
    }
}