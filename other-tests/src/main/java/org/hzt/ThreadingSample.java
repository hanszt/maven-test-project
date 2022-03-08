package org.hzt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadingSample {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadingSample.class);

    public static void main(String... args) {
        int counter = 0;
        while (counter < 10) {
            try {
                LOGGER.info("counter = {}", counter);
                Thread.sleep(1000);
            } catch (InterruptedException ee) {
                Thread.currentThread().interrupt();
            }
            counter++;
        }
    }
}
