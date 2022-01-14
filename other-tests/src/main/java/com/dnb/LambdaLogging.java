package com.dnb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public final class LambdaLogging {

    private static final Logger LOGGER = LoggerFactory.getLogger(LambdaLogging.class);

    private LambdaLogging() {
    }

    public static void logIfDebugEnabled(Supplier<String> supplier) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(supplier.get());
        }
    }

    public static void logIfInfoEnabled(Supplier<String> supplier) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(supplier.get());
        }
    }
}
