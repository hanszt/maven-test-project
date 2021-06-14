package com.dnb;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionHandlerTest {

    private final ExceptionHandler exceptionHandler = new ExceptionHandler();

    @Test
    void throwsRunTimeException() {
       assertThrows(ArithmeticException.class, exceptionHandler::arithmeticExceptionExample);
    }

    @Test
    void throwsNoRunTimeException() {
        assertAll(exceptionHandler::consumerWrapperExample);
    }
}