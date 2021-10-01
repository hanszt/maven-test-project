package com.dnb.concurrent;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CompletableFutureSampleTest {

    private final CompletableFutureSample completableFutureSample = new CompletableFutureSample();

    @Test
    void testCompletableFuture() {
        final var integer = completableFutureSample.completableFuture().getNow(0);
        System.out.println("integer = " + integer);
        Assertions.assertNotNull(integer);
    }

    @Test
    void testCombiningTwoCompletableFutures() {
        var completableFuture = completableFutureSample.getStockPriceAndThenCombine();
        Assertions.assertNotNull(completableFuture);
    }

    @Test
    void testComposeAndThenCombiningTwoCompletableFutures() {
        var completableFuture = completableFutureSample.getStockPriceThenComposeAndThanCombine();
        Assertions.assertNotNull(completableFuture);
    }
}
