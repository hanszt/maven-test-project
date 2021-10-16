package com.dnb;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public final class LambdaLogging {

    private static final Logger LOGGER = LogManager.getLogger(LambdaLogging.class);

    private LambdaLogging() {
    }

    public static void logIfDebugEnabled(Supplier<String> supplier) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(supplier.get());
        }
    }
}
