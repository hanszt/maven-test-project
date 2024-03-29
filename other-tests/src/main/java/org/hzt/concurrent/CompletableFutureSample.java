package org.hzt.concurrent;

import org.hzt.utils.TimingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

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
                .orTimeout(2, TimeUnit.SECONDS);
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
        final var STRING = "This is a String that is computationally intensive: 1";
        TimingUtils.sleep(Duration.ofMillis(500));
        return STRING;
    }

    /**
     * @see <a href="https://youtu.be/IwJ-SCfXoAU?t=8188">Complatable future google stock async</a>
     */
    public CompletableFuture<Integer> getStockPriceAndThenCombine() {
        CompletableFuture<Integer> googleStock = CompletableFuture.supplyAsync(() -> supplyPrice(Duration.ofMillis(200), 500));
        CompletableFuture<Integer> teslaStock = CompletableFuture.supplyAsync(() -> supplyPrice(Duration.ofMillis(100), 1_000));
        CompletableFuture<Integer> completableFuture = googleStock.thenCombine(teslaStock, Integer::sum);
        TimingUtils.sleep(Duration.ofMillis(200));
        return completableFuture;
    }

    static int supplyPrice(Duration duration, int price) {
        TimingUtils.sleep(duration);
        return price;
    }

    /**
     * compose is like flatMap. When you have first Completable future of first completable future, compose wraps it back into first
     * single completable future
     */
    public CompletableFuture<Integer> getStockPriceThenComposeAndThanCombine() {
        var goog = CompletableFuture.supplyAsync(() -> supplyPrice(Duration.ofMillis(100), 500));
        var tesla = CompletableFuture.supplyAsync(() -> supplyPrice(Duration.ofMillis(200), 1_000));

        final var future1 = CompletableFuture.supplyAsync(() -> goog)
                .thenCompose(CompletableFuture::toCompletableFuture);

        final var future2 = CompletableFuture.supplyAsync(() -> tesla)
                .thenCompose(CompletableFuture::toCompletableFuture);

        CompletableFuture<Integer> completableFuture = future1.thenCombine(future2, Integer::sum);
        TimingUtils.sleep(Duration.ofMillis(300));
        return completableFuture;
    }

}
