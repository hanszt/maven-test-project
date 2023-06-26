package org.hzt;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

import static org.hzt.NetConnectionTestUtils.assumeCanConnectToHttpUrl;
import static org.hzt.NetConnectionTestUtils.createFireFoxWebDriver;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HelloSeleniumTest {

    @Test
    void testHelloSeleniumWithFireFox() {
        final var url = "https://selenium.dev";

        assumeCanConnectToHttpUrl(url);
        final var webDriver = createFireFoxWebDriver(new FirefoxOptions());
        final var screenshotPath = new HelloSelenium(webDriver).takeScreenshot(url);

        assertTrue(screenshotPath.toFile().exists());
    }

    @Nested
    class ChromeDriverTests {

        private static WebDriver driver;

        @BeforeAll
        static void setup() {
            final var chromeOptions = new ChromeOptions();
            driver = WebDriverManagerConfig.setupChromeWebDriver(chromeOptions);
        }

        @Test
        void testHelloSeleniumWithChromeHeadless() {
            final var url = "https://selenium.dev";
            assumeCanConnectToHttpUrl(url);

            final var chromeOptions = new ChromeOptions();
            final var driver = WebDriverManagerConfig.setupChromeWebDriver(chromeOptions.setHeadless(true));
            final var screenshotPath = new HelloSelenium(driver).takeScreenshot(url);

            assertTrue(screenshotPath.toFile().exists());
        }

        /**
         * <a href="https://www.lambdatest.com/blog/handling-login-popup-in-selenium-webdriver-using-java/">
         * How To Handle Login Pop-up In Selenium WebDriver Using Java</a>
         */
        @Test
        void testPasswordPopup() {
            final var username = "admin";
            final var password = "admin";

            final var url = "https://" + username + ":" + password + "@" + "the-internet.herokuapp.com/basic_auth";

            assumeCanConnectToHttpUrl(url);

            driver.get(url);

            getTittleAndAssertProperCredentials();
        }

        /**
         * <a href="https://applitools.com/blog/selenium-4-chrome-devtools/">Introduction to Chrome DevTools</a>
         */
        @Test
        void testUsingDevtools() {
            final var username = "admin";
            final var password = "admin";

            new AuthorisationUtils(driver).sendCredentialsUsingDevTools(username, password);

            final var url = "https://the-internet.herokuapp.com/basic_auth";
            assumeCanConnectToHttpUrl(url);

            driver.get(url);

            getTittleAndAssertProperCredentials();
        }

        private void getTittleAndAssertProperCredentials() {
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
            final var title = driver.getTitle();

            System.out.println("The page title is " + title);

            final var text = driver.findElement(By.tagName("p")).getText();

            assertEquals("Congratulations! You must have the proper credentials.", text);
        }
    }

}
