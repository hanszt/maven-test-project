package org.hzt;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

public final class NetConnectionTestUtils {

    private NetConnectionTestUtils() {
    }

    private static boolean canConnectToHttpUrl(String urlAsString) {
        try {
            final var url = new URL(urlAsString);
            final var urlConnection = (HttpURLConnection) url.openConnection();
            return HttpURLConnection.HTTP_NOT_FOUND != urlConnection.getResponseCode();
        } catch (MalformedURLException e) {
            throw new IllegalStateException(e);
        } catch (IOException e) {
            return false;
        }
    }

    public static void assumeCanConnectToHttpUrl(String urlAsString) {
        assumeTrue(canConnectToHttpUrl(urlAsString), "Site at url " + urlAsString + " is not available. Check your internet connection.");
    }
}
