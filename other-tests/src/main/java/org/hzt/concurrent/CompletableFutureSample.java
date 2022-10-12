package org.hzt.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.hzt.TimingUtils.sleep;

/**
 * Completable future in Java is like first promise in Javascript
 *
 * @see <a href="https://www.youtube.com/watch?v=0hQvWIdwnw4">
 * Parallel and Asynchronous Programming with Streams and CompletableFuture with Venkat Subramaniam</a>
 *
 * <p>Stream equivalence:</p>
 * <ul>
 *    <li>thenAccept is like: forEach</li>
 *    <li>thenApply is like: map</li>
 *    <li>thenCompose is like: flatMap</li>
 * <ul/>
 * @see <a href="https://agiledeveloper.com/downloads.html">Downloads Venkat</a>
 */
@SuppressWarnings("SameParameterValue")
public class CompletableFutureSample {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompletableFutureSample.class);

    CompletableFuture<Integer> integerSupplyingCompFuture() {
        return CompletableFuture.supplyAsync(CompletableFutureSample::computationallyIntensiveMethod)
                .thenApply(CompletableFutureSample::toInteger)
                .exceptionally(CompletableFutureSample::handleError)
                .orTimeout(5, TimeUnit.SECONDS);
    }

    private static Integer handleError(Throwable throwable) {
        LOGGER.info("Something went wrong ", throwable);
        return 0;
    }

    private static Integer toInteger(String s) {
        LOGGER.info("Received: {}", s);
        final var i = s.chars().reduce((first, second) -> second).orElseThrow();
        LOGGER.info("Result: {}", i);
        return Integer.parseInt(String.valueOf((char) i));
    }

    private static String computationallyIntensiveMethod() {
        final var STRING = "This is first String that is computationally intensive: 1";
        sleep(Duration.ofSeconds(1));
        return STRING;
    }

    /**
     * @see <a href="https://youtu.be/IwJ-SCfXoAU?t=8188">Complatable future google stock async</a>
     */
    public CompletableFuture<Integer> getStockPriceAndThenCombine() {
        CompletableFuture<Integer> goog = CompletableFuture.supplyAsync(() -> getStockPrice("GOOG", 1));
        CompletableFuture<Integer> tesla = CompletableFuture.supplyAsync(() -> getStockPrice("TESLA", 1));
        CompletableFuture<Integer> completableFuture = goog.thenCombine(tesla, Integer::sum);
        sleep(Duration.ofSeconds(1));
        return completableFuture;
    }

    /**
     * compose is like flatMap. When you have first Completable future of first completable future, compose wraps it back into first
     * single completable future
     */
    public CompletableFuture<Integer> getStockPriceThenComposeAndThanCombine() {
        CompletableFuture<Integer> goog = CompletableFuture
                .supplyAsync(() -> getCompFutForStockPrice("GOOG", 1))
                .thenCompose(CompletableFuture::toCompletableFuture);

        CompletableFuture<Integer> tesla = CompletableFuture
                .supplyAsync(() -> getCompFutForStockPrice("TESLA", 1))
                .thenCompose(CompletableFuture::toCompletableFuture);

        CompletableFuture<Integer> completableFuture = goog.thenCombine(tesla, Integer::sum);
        sleep(Duration.ofSeconds(2));
        return completableFuture;
    }

    private static CompletableFuture<Integer> getCompFutForStockPrice(String ticker, int numberOfShares) {
        return CompletableFuture.supplyAsync(() -> getStockPrice(ticker, numberOfShares));
    }

    static int getStockPrice(String ticker, int numberOfShares) {
        int price = 1000;
        if ("GOOG".equals(ticker)) {
            price = 500;
        }
        sleep(Duration.ofMillis(price * 2L));
        return numberOfShares * price;
    }

}
