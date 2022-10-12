package org.hzt;

import java.time.Duration;

public final class TimingUtils {

    private TimingUtils() {
    }

    public static void sleep(Duration duration) {
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
