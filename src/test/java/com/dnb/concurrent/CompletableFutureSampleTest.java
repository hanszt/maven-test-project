package com.dnb.concurrent;

import org.junit.jupiter.api.Test;

class CompletableFutureSampleTest {

    private final CompletableFutureSample completableFutureSample = new CompletableFutureSample();

    @Test
    void testCompletableFuture() {
        final var integer = completableFutureSample.completableFuture().getNow(0);
        System.out.println("integer = " + integer);
    }

    @Test
    void testCombiningTwoCompletableFutures() {
        completableFutureSample.getStockPriceAndThenCombine();
    }

    @Test
    void testComposeAndThenCombiningTwoCompletableFutures() {
        completableFutureSample.getStockPriceThenComposeAndThanCombine();
    }
}
