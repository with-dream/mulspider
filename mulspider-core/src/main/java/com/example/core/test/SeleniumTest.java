package com.example.core.test;

import com.example.core.utils.D;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.net.URL;
import java.util.concurrent.TimeUnit;

public class SeleniumTest {
    public static void main(String[] args) {
        SeleniumTest test = new SeleniumTest();
        URL driverUrl = test.getClass().getClassLoader().getResource("./plugin/chromedriver");

        System.setProperty("webdriver.chrome.driver", driverUrl.getPath());
        WebDriver webDriver = new ChromeDriver();
        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        webDriver.get("https://home.cnblogs.com/blog");
        String bodyStr = webDriver.getPageSource();
        D.d("cookie==>" + webDriver.manage().getCookies());

        WebElement webElement = webDriver.findElement(By.xpath("/html"));
        String content = webElement.getAttribute("outerHTML");
        D.d("res==>" + content);
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        webDriver.quit();
    }
}
