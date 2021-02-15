package com.example.core.test;

import com.example.core.utils.D;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.util.concurrent.TimeUnit;

public class Platform {
    public static void main(String[] args) {
        SeleniumTest test = new SeleniumTest();
        URL driverUrl = test.getClass().getClassLoader().getResource("./plugin/phantomjs-2.1.1-macosx/bin/phantomjs");
        for (int i = 0; i < 3; i++) {
            System.setProperty("phantomjs.binary.path", driverUrl.getPath());

            DesiredCapabilities desiredCapabilities = DesiredCapabilities.phantomjs();
            WebDriver webDriver = new PhantomJSDriver(desiredCapabilities);
            webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

            webDriver.get("https://home.cnblogs.com/blog");
//        D.d("cookie==>" + webDriver.manage().getCookies());

            WebElement webElement = webDriver.findElement(By.xpath("/html"));
            String content = webElement.getAttribute("outerHTML");
            D.d("res==>" + content);
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            webDriver.close();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
