package org.hzt;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;

import static org.junit.jupiter.api.Assertions.assertTrue;

class HelloSeleniumTest {

    @Test
    void testHelloSeleniumWithFireFox() {
        final var webDriver = WebDriverManagerConfig.setupFirefoxWebDriver(new FirefoxOptions());
        final var screenshotPath = new HelloSelenium(webDriver).helloSelenium();
        assertTrue(screenshotPath.toFile().exists());
    }

    @Test
    void testHelloSeleniumWithChromeHeadless() {
        final var chromeOptions = new ChromeOptions().setHeadless(true);
        final var driver = WebDriverManagerConfig.setupChromeWebDriver(chromeOptions);
        final var screenshotPath = new HelloSelenium(driver).helloSelenium();
        assertTrue(screenshotPath.toFile().exists());
    }

}
