package org.hzt;

import io.github.bonigarcia.wdm.config.WebDriverManagerException;
import org.junit.jupiter.api.Assumptions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

public final class NetConnectionTestUtils {

    private NetConnectionTestUtils() {
    }

    public static void assumeCanConnectToHttpUrl(String urlAsString) {
        assumeTrue(canConnectToHttpUrl(urlAsString), "Site at url " + urlAsString + " is not available. Check your internet connection.");
    }

    public static WebDriver createFireFoxWebDriver(FirefoxOptions options) {
        try {
            return WebDriverManagerConfig.setupFirefoxWebDriver(options);
        } catch (WebDriverManagerException e) {
            Assumptions.abort(() -> ("Could not create firefox webdriver: " + e.getMessage()));
            throw e;
        }
    }

    private static boolean canConnectToHttpUrl(String urlAsString) {
        try {
            final var url = URI.create(urlAsString).toURL();
            final var urlConnection = (HttpURLConnection) url.openConnection();
            return HttpURLConnection.HTTP_NOT_FOUND != urlConnection.getResponseCode();
        } catch (MalformedURLException e) {
            throw new IllegalStateException(e);
        } catch (IOException e) {
            return false;
        }
    }
}
