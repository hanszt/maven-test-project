package org.hzt;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConsumerExceptionWrappersSampleTest {

    private final ConsumerExceptionWrappersSample consumerExceptionWrappersSample = new ConsumerExceptionWrappersSample();

    @Test
    void testThrowsRunTimeException() {
       assertThrows(ArithmeticException.class, consumerExceptionWrappersSample::arithmeticExceptionExample);
    }

    @Test
    void testThrowsNoRunTimeException() {
        assertDoesNotThrow(consumerExceptionWrappersSample::consumerWrapperExample);
    }
}
