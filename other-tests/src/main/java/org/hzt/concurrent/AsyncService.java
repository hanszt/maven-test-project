package org.hzt.concurrent;

import org.hzt.utils.TimingUtils;

import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public class AsyncService {
    private static final int DELAY = 100;
    private static final int INIT_DELAY = 200; // ms

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
            TimingUtils.sleep(Duration.ofMillis(INIT_DELAY));
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
            TimingUtils.sleep(delay);
            value.addAndGet(val);
        });
    }

    public long getValue() {
        throwIfNotInitialized();
        return value.longValue();
    }

    private void throwIfNotInitialized() {
        if (!initialized) {
            throw new IllegalStateException("Service is not initialized");
        }
    }
}
