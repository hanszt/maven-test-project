package com.dnb.concurrent;

import org.awaitility.Awaitility;
import org.awaitility.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.awaitility.Awaitility.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Hans Zuidervaart
 * A test to get familiar with awaitility
 * and other concurency principles
 * @see <a href="https://www.baeldung.com/awaitlity-testing">Introduction to Awaitility</a>
 */
class AsyncServiceTest {

    private AsyncService asyncService;

    @BeforeEach
    public void setUp() {
        asyncService = new AsyncService();
    }

    @Test
    void testInitializeWithCustomProperties() {
        Awaitility.setDefaultPollInterval(10, TimeUnit.MILLISECONDS);
        Awaitility.setDefaultPollDelay(Duration.ZERO);
        Awaitility.setDefaultTimeout(Duration.ONE_MINUTE);
        asyncService.initialize();
        await().until(asyncService::isInitialized);
    }

    @Test
    void testInitialize() {
        asyncService.initialize();
        await().atLeast(Duration.ONE_HUNDRED_MILLISECONDS)
                .atMost(Duration.FIVE_SECONDS)
                .with()
                .pollInterval(Duration.ONE_HUNDRED_MILLISECONDS)
                .until(asyncService::isInitialized);
    }

    @Test
    void testInitializeWithArgumentMatchers() {
        asyncService.initialize();
        await().until(asyncService::isInitialized);
        long value = 5;
        asyncService.addValue(value);
        await().until(asyncService::getValue, equalTo(value));
    }

    @Test
    void testIgnoringExceptions() {
        asyncService.initialize();
        given().ignoreException(IllegalStateException.class)
                .await().atMost(Duration.FIVE_SECONDS)
                .atLeast(Duration.FIVE_HUNDRED_MILLISECONDS)
                .until(asyncService::getValue, equalTo(0L));
    }

    @Test
    void testParallelStreamExecutesFasterThanSequentialStream() {
        final int TIMES_EXECUTED = 64;
        long durationSeq = AsyncService.executeAsyncServiceAndTime(TIMES_EXECUTED, asyncService::executeStreamCallingExpensiveMethodMock);
        long durationParallel = AsyncService.executeAsyncServiceAndTime(TIMES_EXECUTED, asyncService::executeStreamInParallelCallingExpensiveMethodMock);
        System.out.println("durationSeq = " + durationSeq / 1e9 + " s");
        System.out.println("durationParallel = " + durationParallel / 1e9 + " s");
        long timesFaster = durationSeq / durationParallel;
        System.out.println("timesFaster = " + timesFaster);
        assertTrue(durationSeq > durationParallel);
    }
}
