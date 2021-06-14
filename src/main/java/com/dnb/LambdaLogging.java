package com.dnb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LambdaLogging {

    private static final Logger LOGGER = LoggerFactory.getLogger(LambdaLogging.class);

    public static void main(String[] args) {
        new LambdaLogging().run();
    }

    private void run() {
        logIfDebugEnabled(this::expensiveMessage);
    }

    private String expensiveMessage() {
        return IntStream.range(0, 1000).mapToObj(i -> i + ",").collect(Collectors.joining());
    }

    private void logIfDebugEnabled(Supplier<String> supplier) {
        if (LOGGER.isDebugEnabled()) LOGGER.debug(supplier.get());
    }
}
