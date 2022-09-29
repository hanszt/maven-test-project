package org.hzt;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v102.network.Network;
import org.openqa.selenium.devtools.v102.network.model.Headers;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HelloSeleniumTest {

    @Test
    void testHelloSeleniumWithFireFox() {
        final var webDriver = WebDriverManagerConfig.setupFirefoxWebDriver(new FirefoxOptions());
        final var screenshotPath = new HelloSelenium(webDriver).helloSelenium();
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
            final var chromeOptions = new ChromeOptions();
            final var driver = WebDriverManagerConfig.setupChromeWebDriver(chromeOptions.setHeadless(true));
            final var screenshotPath = new HelloSelenium(driver).helloSelenium();
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

            driver.get("https://" + username + ":" + password + "@" + "the-internet.herokuapp.com/basic_auth");

            getTittleAndAssertProperCredentials();
        }

        /**
         * <a href="https://applitools.com/blog/selenium-4-chrome-devtools/">Introduction to Chrome DevTools</a>
         */
        @Test
        void testUsingDevtools() {
            final var username = "admin";
            final var password = "admin";

            sendCredentialsUsingDevTools(username, password);

            driver.get("https://the-internet.herokuapp.com/basic_auth");

           getTittleAndAssertProperCredentials();
        }

        @SuppressWarnings("SameParameterValue")
        private void sendCredentialsUsingDevTools(String username, String password) {
            final DevTools devTools = ((ChromeDriver) driver).getDevTools();
            devTools.createSession();

            final var maxTotalBufferSize = Optional.of(100_000);
            // Enable the Network domain of devtools
            devTools.send(Network.enable(maxTotalBufferSize, maxTotalBufferSize, maxTotalBufferSize));

            final var auth = username + ":" + password;
            final var encodeToString = Base64.getEncoder().encodeToString(auth.getBytes());
            final Map<String, Object> headers = Map.of("Authorization", "Basic " + encodeToString);

            devTools.send(Network.setExtraHTTPHeaders(new Headers(headers)));
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
