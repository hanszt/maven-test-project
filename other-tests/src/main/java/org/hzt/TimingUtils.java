package org.hzt;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public final class TimingUtils {

    private TimingUtils() {
    }

    public static void sleep(Duration duration) {
        try {
            TimeUnit.NANOSECONDS.sleep(duration.toNanos());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
