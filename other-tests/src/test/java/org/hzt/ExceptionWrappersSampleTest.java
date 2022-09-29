package org.hzt;

import org.hzt.utils.It;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExceptionWrappersSampleTest {

    private final ExceptionWrappersSample exceptionWrappersSample = new ExceptionWrappersSample();

    @Test
    void testThrowsRunTimeException() {
       assertThrows(ArithmeticException.class, exceptionWrappersSample::arithmeticExceptionExample);
    }

    @Test
    void testThrowsNoRunTimeException() {
        assertDoesNotThrow(exceptionWrappersSample::consumerWrapperExample);
    }

    @Test
    void testThrowingLogsStacktrace() {
        assertDoesNotThrow(exceptionWrappersSample::consumeThrowingExample);
    }

    @Test
    void testStreamHandlingExceptionsUsingEither() {
        final var stream1 = IntStream.range(0, 100).boxed();
        final var stream2 = Stream.of(null, null, 3);

        final var results = Stream.concat(stream1, stream2)
                .map(Result.catching(ExceptionWrappersSampleTest::throwingWhenLargerThan20Times2))
                .peek(It::println)
                .filter(not(Result::hasError))
                .toList();

        assertEquals(22, results.size());
    }

    private static int throwingWhenLargerThan20Times2(int v1) throws MyCheckedException {
        final var THROWING_BOUND = 20;
        if (v1 > THROWING_BOUND) {
            throw new MyCheckedException();
        }
        return v1 * 2;
    }

    private static class MyCheckedException extends Exception {

    }

    @FunctionalInterface
    interface ThrowingFunction<T, R> {
        R apply(T value) throws Throwable;

    }

    private static class Result<T, R> {

        private final Throwable throwable;
        private final T originalValue;
        private final R resultVal;

        public Result(T value, R resultValue) {
            this.originalValue = value;
            this.resultVal = resultValue;
            this.throwable = null;
        }

        public Result(T value, Throwable throwable) {
            this.originalValue = value;
            this.resultVal = null;
            this.throwable = throwable;
        }

        public static <T, R> Function<T, Result<T, R>> catching(ThrowingFunction<? super T, ? extends R> throwingFunction) {
            return t -> {
                try {
                    final R result = throwingFunction.apply(t);
                    return new Result<>(t, result);
                } catch (Throwable e) {
                    return new Result<>(t, e);
                }
            };
        }

        public Optional<T> originalValue() {
            return Optional.ofNullable(originalValue);
        }

        public Optional<R> succes() {
            return Optional.ofNullable(resultVal);
        }

        public Optional<Throwable> error() {
            return Optional.ofNullable(throwable);
        }

        public boolean hasError() {
            return throwable != null;
        }

        @Override
        public String toString() {
            return "Result{" +
                    "throwable=" + throwable +
                    ", originalValue=" + originalValue +
                    ", resultVal=" + resultVal +
                    '}';
        }
    }
}
