package org.hzt;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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
}
