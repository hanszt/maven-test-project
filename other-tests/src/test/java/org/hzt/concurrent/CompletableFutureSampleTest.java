package org.hzt.concurrent;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CompletableFutureSampleTest {

    private final CompletableFutureSample completableFutureSample = new CompletableFutureSample();

    @Test
    void testCompletableFutureGetNow() {
        final var integer = completableFutureSample.integerSupplyingCompFuture().getNow(0);
        System.out.println("integer = " + integer);
        assertEquals(0 , integer);
    }

    @Test
    void testCombiningTwoCompletableFutures() {
        var completableFuture = completableFutureSample.getStockPriceAndThenCombine();
        completableFuture.thenAccept(System.out::println);
        Assertions.assertNotNull(completableFuture);
    }

    @Test
    void testComposeAndThenCombiningTwoCompletableFutures() {
        var completableFuture = completableFutureSample.getStockPriceThenComposeAndThanCombine();
        completableFuture.thenAccept(System.out::println);
        Assertions.assertNotNull(completableFuture);
    }

    @Test
    void testCompletableFuturesListResultUsingMapMulti() {
        final List<CompletableFuture<Integer>> completableFutures = List.of(
                completableFutureSample.getStockPriceAndThenCombine(),
                completableFutureSample.getStockPriceThenComposeAndThanCombine());

        final List<Integer> stockPrices = completableFutures.stream()
                .<Integer>mapMulti(CompletableFuture::thenAccept)
                .toList();

        assertEquals(List.of(1500, 1500), stockPrices);
    }
}
