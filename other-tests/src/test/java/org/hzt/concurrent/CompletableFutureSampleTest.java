package org.hzt.concurrent;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        assertNotNull(completableFuture);
    }

    @Test
    void testComposeAndThenCombiningTwoCompletableFutures() {
        var completableFuture = completableFutureSample.getStockPriceThenComposeAndThanCombine();
        completableFuture.thenAccept(System.out::println);
        assertNotNull(completableFuture);
    }
}
