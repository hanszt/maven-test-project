package com.dnb.concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static java.lang.System.out;

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
public class CompletableFutureSample {

    CompletableFuture<Integer> completableFuture() {
        return CompletableFuture.supplyAsync(CompletableFutureSample::newString)
                .thenApply(CompletableFutureSample::toInteger)
                .exceptionally(CompletableFutureSample::handleError)
                .orTimeout(10, TimeUnit.SECONDS);
    }

    private static Integer handleError(Throwable throwable) {
        out.println("Something went wrong " + throwable);
        return 0;
    }

    private static Integer toInteger(String s) {
        out.println("Received: " + s);
        final var i = s.chars().reduce((first, second) -> second).orElseThrow();
        out.println("Result: " + i);
        return Integer.parseInt(String.valueOf((char) i));
    }

    private static String newString() {
        return "This is a String that is computationally intensive: 1";
    }

    /**
     * @see <a href="https://youtu.be/IwJ-SCfXoAU?t=8188">Complatable future google stock async</a>
     */
    void getStockPriceAndThenCombine() {
        CompletableFuture<Integer> goog = CompletableFuture.supplyAsync(() -> getStockPrice("GOOG", 1));
        CompletableFuture<Integer> tesla = CompletableFuture.supplyAsync(() -> getStockPrice("TESLA", 1));
        goog.thenCombine(tesla, Integer::sum).thenAccept(out::println);
        sleep(10_000);
    }

    /**
     * compose is like flatMap. When you have a Completable future of a completable future, compose wraps it back into a
     * single completable future
     */
    void getStockPriceThenComposeAndThanCombine() {
        CompletableFuture<Integer> goog = CompletableFuture
                .supplyAsync(() -> getStockPriceReturningCompFut("GOOG", 1))
                .thenCompose(CompletableFuture::toCompletableFuture);
        CompletableFuture<Integer> tesla = CompletableFuture
                .supplyAsync(() -> getStockPriceReturningCompFut("TESLA", 1))
                .thenCompose(CompletableFuture::toCompletableFuture);
        goog.thenCombine(tesla, Integer::sum).thenAccept(out::println);
        sleep(10_000);
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static CompletableFuture<Integer> getStockPriceReturningCompFut(String ticker, int numberOfShares) {
        return CompletableFuture.supplyAsync(() -> {
            int price = 1000;
            if ("GOOG".equals(ticker)) {
                price = 500;
            }
            sleep(price * 2);
            return numberOfShares * price;
        });
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
