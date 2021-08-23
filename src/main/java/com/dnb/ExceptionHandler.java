package com.dnb;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.function.Consumer;

public class ExceptionHandler {

    private static final Logger LOGGER = LogManager.getLogger(ExceptionHandler.class);

    void arithmeticExceptionExample() {
        List<Integer> integers = List.of(3, 9, 0, 7, 6, 10, 20, 6);
        integers.forEach(i -> LOGGER.info(50 / i));
    }

    @SuppressWarnings("SameParameterValue")
    static <T, E extends Exception> Consumer<T> consumerWrapper(Consumer<T> consumer, Class<E> throwableClass) {
        return i -> {
            try {
                consumer.accept(i);
            } catch (Exception ex) {
                try {
                    var exCast = throwableClass.cast(ex);
                    LOGGER.error("Exception occurred: {}", exCast.getMessage());
                } catch (ClassCastException ccEx) {
                    throw ex;
                }
            }
        };
    }

    /**
     * source: https://www.baeldung.com/java-lambda-exceptions
     */
    void consumerWrapperExample() {
        List<Integer> integers = List.of(3, 9, 0, 7, 6, 10, 20, 6);
        integers.forEach(consumerWrapper(i -> LOGGER.info(50 / i), ArithmeticException.class));
    }
}



