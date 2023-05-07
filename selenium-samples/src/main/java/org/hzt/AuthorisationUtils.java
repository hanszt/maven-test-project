package org.hzt;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v107.network.Network;
import org.openqa.selenium.devtools.v107.network.model.Headers;

import java.util.Base64;
import java.util.Map;
import java.util.Optional;

public final class AuthorisationUtils {

    private final WebDriver driver;

    public AuthorisationUtils(WebDriver driver) {
        this.driver = driver;
    }

    @SuppressWarnings("SameParameterValue")
    public void sendCredentialsUsingDevTools(String username, String password) {
        if (driver instanceof HasDevTools hasDevTools) {
            final var devTools = hasDevTools.getDevTools();
            devTools.createSession();

            final var maxTotalBufferSize = Optional.of(100_000);
            // Enable the Network domain of devtools
            devTools.send(Network.enable(maxTotalBufferSize, maxTotalBufferSize, maxTotalBufferSize));

            final var auth = username + ":" + password;
            final var encodeToString = Base64.getEncoder().encodeToString(auth.getBytes());
            final Map<String, Object> headers = Map.of("Authorization", "Basic " + encodeToString);

            devTools.send(Network.setExtraHTTPHeaders(new Headers(headers)));
            return;
        }
        throw new IllegalStateException("Driver " + driver.toString() + " does not support devtools");
    }
}
