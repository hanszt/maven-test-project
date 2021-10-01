package com.dnb.concurrent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Completable future in Java is like a promise in Javascript
 *
 * @see <a href="https://www.youtube.com/watch?v=0hQvWIdwnw4">
 *         Parallel and Asynchronous Programming with Streams and CompletableFuture with Venkat Subramaniam</a>
 *
 * <p>Stream equivalence:</p>
 * <ul>
 *    <li>thenAccept is like: forEach</li>
 *    <li>thenApply is like: map</li>
 *    <li>thenCompose is like: flatMap</li>
 * <ul/>
 * <a href="https://agiledeveloper.com/downloads.html">Downloads Venkat</a>
 */
@SuppressWarnings("SameParameterValue")
public class CompletableFutureSample {

    private static final Logger LOGGER = LogManager.getLogger(CompletableFutureSample.class);

    CompletableFuture<Integer> completableFuture() {
        return CompletableFuture.supplyAsync(CompletableFutureSample::newString)
                .thenApply(CompletableFutureSample::toInteger)
                .exceptionally(CompletableFutureSample::handleError)
                .orTimeout(10, TimeUnit.SECONDS);
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

    private static String newString() {
        final var STRING = "This is a String that is computationally intensive: 1";
        sleep(1000);
        return STRING;
    }

    /**
     * @see <a href="https://youtu.be/IwJ-SCfXoAU?t=8188">Complatable future google stock async</a>
     */
    CompletableFuture<Void> getStockPriceAndThenCombine() {
        CompletableFuture<Integer> goog = CompletableFuture.supplyAsync(() -> getStockPrice("GOOG", 1));
        CompletableFuture<Integer> tesla = CompletableFuture.supplyAsync(() -> getStockPrice("TESLA", 1));
        CompletableFuture<Void> completableFuture = goog.thenCombine(tesla, Integer::sum).thenAccept(LOGGER::info);
        sleep(10_000);
        return completableFuture;
    }

    /**
     * compose is like flatMap. When you have a Completable future of a completable future, compose wraps it back into a
     * single completable future
     */
    CompletableFuture<Void> getStockPriceThenComposeAndThanCombine() {
        CompletableFuture<Integer> goog = CompletableFuture
                .supplyAsync(() -> getStockPriceReturningCompFut("GOOG", 1))
                .thenCompose(CompletableFuture::toCompletableFuture);
        CompletableFuture<Integer> tesla = CompletableFuture
                .supplyAsync(() -> getStockPriceReturningCompFut("TESLA", 1))
                .thenCompose(CompletableFuture::toCompletableFuture);
        CompletableFuture<Void> completableFuture = goog.thenCombine(tesla, Integer::sum).thenAccept(LOGGER::info);
        sleep(10_000);
        return completableFuture;
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static CompletableFuture<Integer> getStockPriceReturningCompFut(String ticker, int numberOfShares) {
        return CompletableFuture.supplyAsync(() -> getStockPrice(ticker, numberOfShares));
    }

    private static int getStockPrice(String ticker, int numberOfShares) {
        int price = 1000;
        if ("GOOG".equals(ticker)) {
            price = 500;
        }
        sleep(price * 2);
        return numberOfShares * price;
    }
}
