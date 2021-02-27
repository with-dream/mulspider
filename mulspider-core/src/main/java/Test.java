import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class Test {
    private final static Logger logger = LoggerFactory.getLogger(Test.class);
    public static void main(String[] args) {

//        logger.error("error level==>"+SystemUntils.IS);
        File f = new File("E:/project/java/mulspider/mulspider-core/./file/download/Wunderstock");

        logger.error("%s{}", "====="+f.mkdirs());
    }
}