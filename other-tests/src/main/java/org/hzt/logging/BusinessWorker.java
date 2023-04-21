package org.hzt.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BusinessWorker {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessWorker.class);

    private BusinessWorker() {
    }

    public static void generateLogs(String msg) {
        LOGGER.trace(msg);
        LOGGER.debug(msg);
        LOGGER.info(msg);
        LOGGER.warn(msg);
        LOGGER.error(msg);
    }
}
