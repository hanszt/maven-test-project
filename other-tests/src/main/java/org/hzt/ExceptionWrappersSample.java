package org.hzt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Consumer;

public class ExceptionWrappersSample {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionWrappersSample.class);

    @SuppressWarnings("SameParameterValue")
    static <T, E extends Exception> Consumer<T> consumerWrapper(Consumer<T> consumer, Class<E> throwableClass) {
        return value -> wrapCheckedException(consumer, throwableClass, value);
    }

    private static <T, E extends Exception> void wrapCheckedException(Consumer<T> consumer, Class<E> throwableClass, T value) {
        try {
            consumer.accept(value);
        } catch (@SuppressWarnings("all") Exception ex) {
            try {
                var exCast = throwableClass.cast(ex);
                LOGGER.error("Exception occurred: {}", exCast.getMessage());
            } catch (ClassCastException ccEx) {
                throw new IllegalStateException(ccEx);
            }
        }
    }

    private static <T, E extends Throwable> Consumer<T> catching(Consumer<T> consumer, Consumer<E> exceptionConsumer) {
        return e -> consumeValueOrException(consumer, exceptionConsumer, e);
    }

    private static <T, E extends Throwable> void consumeValueOrException(Consumer<T> consumer, Consumer<E> throwableClass, T value) {
        try {
            consumer.accept(value);
        } catch (@SuppressWarnings("all") Throwable throwable) {
            //noinspection unchecked
            throwableClass.accept((E) throwable);
        }
    }

    /**
     * source: <a href="https://www.baeldung.com/java-lambda-exceptions">Exceptions in Java 8 Lambda Expressions</a>
     */
    void consumeThrowingExample() {
        List<Integer> integers = List.of(3, 9, 0, 7, 6, 10, 20, 6);
        integers.forEach(catching(i -> LOGGER.info("{}", 50 / i), e -> LOGGER.error("Something went wrong", e)));
    }

    /**
     * source: <a href="https://www.baeldung.com/java-lambda-exceptions">Exceptions in Java 8 Lambda Expressions</a>
     */
    void consumerWrapperExample() {
        List<Integer> integers = List.of(3, 9, 0, 7, 6, 10, 20, 6);
        integers.forEach(consumerWrapper(i -> LOGGER.info("{}", 50 / i), ArithmeticException.class));
    }

    void arithmeticExceptionExample() {
        List<Integer> integers = List.of(3, 9, 0, 7, 6, 10, 20, 6);
        integers.forEach(i -> LOGGER.info("{}", 50 / i));
    }
}



