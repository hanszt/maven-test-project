package org.hzt;

import org.openqa.selenium.By;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.time.Duration;

public class HelloSelenium {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloSelenium.class);

    private final WebDriver driver;

    public HelloSelenium(WebDriver driver) {
        this.driver = driver;
    }

    public static void main(String... args) {
        searchGoogle("java", "software testing", "selenium testing");
    }

    private static void searchGoogle(String... searchTerms) {
        LOGGER.info("Google searches starting... Nr of searches: {}", searchTerms.length);
        final var chromeOptions = new ChromeOptions();
        final var webDriver = WebDriverManagerConfig.setupChromeWebDriver(chromeOptions);
        final var helloSelenium = new HelloSelenium(webDriver);
        for (String searchTerm : searchTerms) {
            final var searchBoxName = helloSelenium.searchInGoogleAndGetSearchStatistics(searchTerm);
            LOGGER.info("Search statistics: {}", searchBoxName);
        }
    }

    public Path takeScreenshot(String url) {
        try {
            driver.get(url);
            if (driver instanceof TakesScreenshot takesScreenshot) {
                return ScreenshotMaker.takeScreenshotAndWriteToTargetFolder(takesScreenshot);
            }
            throw new IllegalStateException("Can not take screenshot");
        } finally {
            driver.quit();
        }
    }


    /**
     * @param searchString the string to search for in google
     * @return The nr of suggestions from Google
     * @see <a href="https://www.youtube.com/watch?v=jMP3xOF8OWQ">How to automate Google search in Selenium with Java</a>
     */
    public String searchInGoogleAndGetSearchStatistics(String searchString) {
        driver.get("https://www.google.com");

        final var acceptButton = driver.findElement(By.id("L2AGLb"));
        sleep(Duration.ofSeconds(1));
        acceptButton.click();
        sleep(Duration.ofSeconds(1));
        final var searchBox = driver.findElement(By.name("q"));

        searchBox.sendKeys(searchString);
        sleep(Duration.ofSeconds(1));
        final var searchButton = driver.findElement(By.className("gNO89b"));
        searchButton.click();

        final var options = driver.manage();
        options.timeouts().implicitlyWait(Duration.ofSeconds(1));
        final var element = driver.findElement(By.id("result-stats"));
        options.deleteAllCookies();
        return element.getText();
    }

    private static void sleep(Duration duration) {
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
