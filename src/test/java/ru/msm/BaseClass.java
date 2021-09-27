package ru.msm;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class BaseClass {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected long defaultWaitingTime;

    @BeforeEach
    public void before() {

        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        defaultWaitingTime = 30;
        //driver.manage().timeouts().pageLoadTimeout(defaultWaitingTime, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(defaultWaitingTime, TimeUnit.SECONDS);

        wait = new WebDriverWait(driver, defaultWaitingTime, 1000);

        String baseUrl = "https://www.rgs.ru/";
        //1. Перейти по ссылке http://www.rgs.ru
        driver.get(baseUrl);
    }

    @AfterEach
    public void after() {
        driver.quit();
    }

}
