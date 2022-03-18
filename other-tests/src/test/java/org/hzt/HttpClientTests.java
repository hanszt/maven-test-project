package org.hzt;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpConnectTimeoutException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Paths;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * <a href="https://openjdk.java.net/groups/net/httpclient/intro.html">
 *     Introduction to the Java HTTP Client</a>
 */
class HttpClientTests {

    @Test
    void testHttpClientGetRequest() {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://openjdk.java.net/"))
                .build();

        final var result = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .join();

        assertTrue(result.contains("body"));
    }

    @Test
    void testPostSynchronous() {
        HttpClient client = buildHttpClient();

        assertThrows(HttpConnectTimeoutException.class, () -> {
            HttpRequest request = buildHttpPostRequest();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println(response.statusCode());
            System.out.println(response.body());
        });
    }

    private HttpRequest buildHttpPostRequest() throws FileNotFoundException {
        return HttpRequest.newBuilder()
                .uri(URI.create("http://openjdk.java.net/"))
                .timeout(Duration.ofSeconds(2))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofFile(Paths.get("input/file.json")))
                .build();
    }

    private HttpClient buildHttpClient() {
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .proxy(ProxySelector.of(new InetSocketAddress("www-proxy.com", 8080)))
//                .authenticator(Authenticator.getDefault())
                .build();
    }
}