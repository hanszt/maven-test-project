package org.hzt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Consumer;

public class ConsumerExceptionWrappersSample {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerExceptionWrappersSample.class);

    void arithmeticExceptionExample() {
        List<Integer> integers = List.of(3, 9, 0, 7, 6, 10, 20, 6);
        integers.forEach(i -> LOGGER.info("{}", 50 / i));
    }

    @SuppressWarnings("SameParameterValue")
    static <T, E extends Exception> Consumer<T> consumerWrapper(Consumer<T> consumer, Class<E> throwableClass) {
        return i -> wrapCheckedException(consumer, throwableClass, i);
    }

    private static <T, E extends Exception> void wrapCheckedException(Consumer<T> consumer, Class<E> throwableClass, T i) {
        try {
            consumer.accept(i);
        } catch (@SuppressWarnings("all") Exception ex) {
            try {
                var exCast = throwableClass.cast(ex);
                LOGGER.error("Exception occurred: {}", exCast.getMessage());
            } catch (ClassCastException ccEx) {
                throw new IllegalStateException(ccEx);
            }
        }
    }

    /**
     * source: https://www.baeldung.com/java-lambda-exceptions
     */
    void consumerWrapperExample() {
        List<Integer> integers = List.of(3, 9, 0, 7, 6, 10, 20, 6);
        integers.forEach(consumerWrapper(i -> LOGGER.info("{}", 50 / i), ArithmeticException.class));
    }
}



