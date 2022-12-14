package hzt.http;

import com.google.gson.Gson;
import hzt.http.model.Transcript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * @see <a href="https://www.youtube.com/watch?v=9oq7Y8n1t00">Speech api tutorial</a>
 */
public class SpeechApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpeechApiController.class);
    public static final String REFACTOR_CODE_TO_GET_URL_FROM_CUSTOMIZABLE_PARAM = "squid:S1075";

    public static void main(String[] args) {
        try {
            Transcript transcript = new Transcript();
            transcript.setAudioUrl("My url");

            Gson gson = new Gson();
            final var json = gson.toJson(transcript);

            LOGGER.info("json = {}", json);

            @SuppressWarnings(REFACTOR_CODE_TO_GET_URL_FROM_CUSTOMIZABLE_PARAM)
            HttpRequest postRequest = HttpRequest.newBuilder()
                    .uri(new URI("https://api.assemblyai.com/v2/transcript"))
                    .headers("Authorization", "API_KEY_TO_BE_SPECIFIED")
                    .POST(HttpRequest.BodyPublishers.ofString(""))
                    .build();

            HttpClient httpClient = HttpClient.newHttpClient();

            HttpResponse<String> response = httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString());

            final var body = response.body();
            LOGGER.info("response.body() = {}", body);
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(e);
        }

    }
}
