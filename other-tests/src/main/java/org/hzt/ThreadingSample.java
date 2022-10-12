package org.hzt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class ThreadingSample {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadingSample.class);

    public static void main(String... args) {
        int counter = 0;
        while (counter < 10) {
            LOGGER.info("counter = {}", counter);
            TimingUtils.sleep(Duration.ofSeconds(1));
            counter++;
        }
    }
}
