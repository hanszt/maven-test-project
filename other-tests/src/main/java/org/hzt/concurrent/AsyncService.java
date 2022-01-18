package org.hzt.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

public class AsyncService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncService.class);
    private static final int DELAY = 1000;
    private static final int INIT_DELAY = 2000;

    private final AtomicLong value = new AtomicLong(0);
    private final Executor executor = Executors.newFixedThreadPool(4);
    private volatile boolean initialized;

    AsyncService(boolean initialized) {
        this.initialized = initialized;
    }

    AsyncService() {
        this(false);
    }

    void initialize() {
        executor.execute(() -> {
            sleep(INIT_DELAY);
            initialized = true;
        });
    }

    boolean isInitialized() {
        return initialized;
    }

    void addValue(long val) {
        addValue(val, Duration.ofMillis(DELAY));
    }

    void addValue(long val, Duration delay) {
        throwIfNotInitialized();
        executor.execute(() -> {
            sleep(delay.toMillis());
            value.addAndGet(val);
        });
    }

    public long getValue() {
        throwIfNotInitialized();
        return value.longValue();
    }

    private static void sleep(long delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void throwIfNotInitialized() {
        if (!initialized) {
            throw new IllegalStateException("Service is not initialized");
        }
    }

    void executeStreamCallingExpensiveMethodMock(int times) {
        LOGGER.info("Sequential:");
        IntStream.range(0, times)
                .forEach(AsyncService::expensiveMethodMock);
    }

    void executeStreamInParallelCallingExpensiveMethodMock(int times) {
        LOGGER.info("In parallel:");
        IntStream.range(0, times)
                .parallel()
                .forEach(AsyncService::expensiveMethodMock);
    }

    static long executeAsyncServiceAndTime(@SuppressWarnings("SameParameterValue") int times, IntConsumer consumer) {
        long start = System.nanoTime();
        consumer.accept(times);
        return System.nanoTime() - start;
    }

    private static void expensiveMethodMock(int i) {
        try {
            final var MILLIS_SECONDS = 50;
            Thread.sleep(MILLIS_SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        LOGGER.info("Dit is call nr {}", i);
    }
}
