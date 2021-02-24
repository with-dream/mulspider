import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Test {
    private final static Logger logger = LoggerFactory.getLogger(Test.class);
    public static void main(String[] args) {
        List<String> ss = new ArrayList<>();
        ss.add("11");
        ss.add(null);
        ss.add(null);
        ss.add("123");
        logger.error("error level==>"+ss);
    }
}