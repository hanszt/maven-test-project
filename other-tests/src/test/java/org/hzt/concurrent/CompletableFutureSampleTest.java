package org.hzt.concurrent;

import org.hzt.collectors_samples.MyCollectors;
import org.hzt.utils.It;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static org.hzt.concurrent.CompletableFutureSample.supplyPrice;
import static org.hzt.utils.It.println;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CompletableFutureSampleTest {

    private final CompletableFutureSample completableFutureSample = new CompletableFutureSample();

    @Test
    void testCompletableFutureGetNow() {
        final var integer = completableFutureSample.integerSupplyingCompFuture().getNow(0);
        println("integer = " + integer);
        assertEquals(0 , integer);
    }

    @Test
    void testCombiningTwoCompletableFutures() {
        var completableFuture = completableFutureSample.getStockPriceAndThenCombine();

        completableFuture.thenAccept(It::println);

        assertEquals(1_500, completableFuture.join());
    }

    @Test
    void testComposeAndThenCombiningTwoCompletableFutures() {
        var completableFuture = completableFutureSample.getStockPriceThenComposeAndThanCombine();

        completableFuture.thenAccept(It::println);

        assertEquals(1_500, completableFuture.join());
    }

    @Test
    void testCollectToFutureJoinAndSum() {
        final var googleStockPrice = CompletableFuture.supplyAsync(() -> supplyPrice(Duration.ofSeconds(1), 500));
        final var teslaStockprice = CompletableFuture.supplyAsync(() -> supplyPrice(Duration.ofMillis(500), 1_000));
        final var combinedPrice = completableFutureSample.getStockPriceThenComposeAndThanCombine();

        final var future = Stream.of(googleStockPrice, teslaStockprice, combinedPrice)
                .collect(MyCollectors.toFuture());

        final var sum = future.join()
                .mapToInt(Integer::intValue)
                .reduce(0, Integer::sum);

        assertEquals(3_000, sum);
    }
}
