package org.hzt;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;

public final class WebDriverManagerConfig {

    private WebDriverManagerConfig() {
    }

    public static WebDriver setupChromeWebDriver(ChromeOptions chromeOptions) {
        return WebDriverManager.chromedriver()
                .capabilities(chromeOptions)
                .create();
    }

    public static WebDriver setupFirefoxWebDriver(FirefoxOptions firefoxOptions) {
        return WebDriverManager.firefoxdriver()
                .capabilities(firefoxOptions)
                .create();
    }
}
