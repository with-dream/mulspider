package com.example.core.download.selenium;

import com.example.core.context.Work;
import com.example.core.models.Request;
import com.example.core.models.Response;
import com.example.core.download.DownloadHandle;
import com.example.core.utils.CharsetUtils;
import com.example.core.utils.ThreadUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SeleniumDownloader extends DownloadHandle {
    public enum DriverType {
        CHROME, FIREFOX, SAFARI, CHROME_HEADLESS, PHANTOMJS
    }

    protected final static Logger logger = LoggerFactory.getLogger(SeleniumDownloader.class);

    private WebDriver createDriver(Request request) {
        WebDriver webDriver = null;
        switch (request.driver) {
            case CHROME:
            case CHROME_HEADLESS:
                ChromeOptions options = new ChromeOptions();
                if (request.driver == DriverType.CHROME_HEADLESS) {
                    //设置 chrome 的无头模式
                    options.addArguments("--headless");
                    options.addArguments("--disable-gpu");
                    options.addArguments("--no-sandbox");
                    options.addArguments("--disable-dev-shm-usage");
                    options.addArguments("--start-maximized");
                    //因为报表页面必须滚动才能全部展示，这里直接给个很大的高度
//                    options.addArguments("--window-size=1280,8600");
                }

                String driver = null;
                if(SystemUtils.IS_OS_WINDOWS)
                    driver = "./plugin/chromedriver.exe";
                else if(SystemUtils.IS_OS_MAC)
                    driver = "./plugin/chromedriver";

                initProperty("webdriver.chrome.driver", driver);
                webDriver = new ChromeDriver(options);
                break;
            case PHANTOMJS:
                initProperty("phantomjs.binary.path", "./plugin/phantomjs-2.1.1-macosx/bin/phantomjs");
                DesiredCapabilities desiredCapabilities = DesiredCapabilities.phantomjs();
                webDriver = new PhantomJSDriver(desiredCapabilities);
                break;
        }

        return webDriver;
    }

    private void initProperty(String property, String driverPath) {
        if (StringUtils.isEmpty(driverPath))
            throw new RuntimeException(String.format("initProperty err property:%s  driverPath:%s", property, driverPath));

        String driver = System.getProperty(property);
        if (StringUtils.isNotEmpty(driver))
            return;

        URL driverUrl = this.getClass().getClassLoader().getResource(driverPath);
        String absoluteUrl = driverUrl.getPath();

        File f = new File(absoluteUrl);
        if (!f.exists())
            throw new RuntimeException(String.format("initProperty err driverPath not exist property:%s  driverPath:%s", property, driverPath));

        System.setProperty(property, absoluteUrl);
    }

    @Override
    public Response down(Request request) {
        Random random = new Random();
        Response response = null;
        WebDriver webDriver = createDriver(request);
        try {
            webDriver.manage().timeouts().implicitlyWait(request.timeOut * 3, TimeUnit.SECONDS);
            WebDriver.Options manage = webDriver.manage();
            manage.getCookies().clear();
            if (request.cookie != null) {
                for (Map.Entry<String, String> cookieEntry : request.cookie
                        .entrySet()) {
                    Cookie cookie = new Cookie(cookieEntry.getKey(),
                            cookieEntry.getValue());
                    manage.addCookie(cookie);
                }
            }

            webDriver.get(request.url);
        } catch (Exception e) {
            logger.error("Driver dowork==>" + request.url + "  err:" + e.getMessage());
        } finally {
            ThreadUtils.sleep(300);
            WebElement webElement = webDriver.findElement(By.xpath("/html"));
            String body = webElement.getAttribute("outerHTML");
            response = Response.make(request, body, 200);
            if (response.resCharset == null)
                response.resCharset = CharsetUtils.guessEncoding(body.getBytes());

            int delay = request.delayTime;
            if (delay == 0)
                delay = (int) (500 + 500 * random.nextFloat());
            ThreadUtils.sleep(delay);
            webDriver.close();
        }

        return response;
    }
}
