import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class Test {
    private final static Logger logger = LoggerFactory.getLogger(Test.class);

    public static void main(String[] args) {
        String urls = "/showinfo-1-28310-1.html";
        String url = urls.replaceAll("-\\d+\\.html", "-" + 5 + ".html");
        logger.error("%s{}", "=====" + url);
    }
}