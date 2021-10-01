package com.dnb.concurrent;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

public class AsyncService {

    private static final int DELAY = 1000;
    private static final int INIT_DELAY = 2000;

    private final AtomicLong value = new AtomicLong(0);
    private final Executor executor = Executors.newFixedThreadPool(4);
    private volatile boolean initialized = false;

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
        throwIfNotInitialized();
        executor.execute(() -> {
            sleep(DELAY);
            value.addAndGet(val);
        });
    }

    public long getValue() {
        throwIfNotInitialized();
        return value.longValue();
    }

    private static void sleep(int delay) {
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
        System.out.println("Sequential:");
        IntStream.range(0, times)
                .forEach(AsyncService::expensiveMethodMock);
    }

    void executeStreamInParallelCallingExpensiveMethodMock(int times) {
        System.out.println("In parallel:");
        IntStream.range(0, times)
                .parallel()
                .forEach(AsyncService::expensiveMethodMock);
    }

    static long executeAsyncServiceAndTime(int times, IntConsumer consumer) {
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
        System.out.println("Dit is call " + i);
    }
}
