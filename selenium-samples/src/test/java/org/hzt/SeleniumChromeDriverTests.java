package org.hzt;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

import static org.hzt.WebDriverManagerConfig.setupChromeWebDriver;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SeleniumChromeDriverTests {

    private final ChromeDriver driver = new ChromeDriver();

    @BeforeAll
    static void setupDriverManager() {
        setupChromeWebDriver(new ChromeOptions());
    }

    @BeforeEach
    void setup() {
        driver.manage()
                .timeouts()
                .implicitlyWait(Duration.ofMillis(500));
    }

    @Test
    void testGetTitle() {
        final var url = "https://www.selenium.dev/selenium/web/web-form.html";
        NetConnectionTestUtils.assumeSiteAvailable(url);
        driver.get(url);
        final var submitFormScreenShotPath = ScreenshotMaker.takeScreenshotAndWriteToTargetFolder(driver);

        String title = driver.getTitle();

        assertAll(
                () -> assertEquals("Web form", title),
                () -> assertTrue(submitFormScreenShotPath.toFile().exists())
        );
    }

    @Test
    void testWebdriverManager() {
        final var url = "https://www.selenium.dev/selenium/web/web-form.html";
        NetConnectionTestUtils.assumeSiteAvailable(url);
        driver.get(url);
        driver.manage().window().maximize();

        WebElement textBox = driver.findElement(By.name("my-text"));
        WebElement submitButton = driver.findElement(By.cssSelector("button"));

        textBox.sendKeys("Selenium");
        submitButton.click();

        WebElement message = driver.findElement(By.id("message"));
        String value = message.getText();

        final var path = ScreenshotMaker.takeScreenshotAndWriteToTargetFolder(driver);

        assertAll(
                () -> assertEquals("Received!", value),
                () -> assertTrue(path.toFile().exists())
        );
    }

    @AfterEach
    void shutDown() {
        driver.quit();
    }

}
