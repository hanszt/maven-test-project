package com.dnb;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;

class ExceptionThrowingTest {

    @Test
    void exceptionGetMessageTest() {
        try {
            throwRuntimeException();
        }catch (RuntimeException e) {
            StackTraceElement[] stackTraceElements = e.getStackTrace();
            String stacktrace = Arrays.stream(stackTraceElements)
                    .peek(System.out::println)
                    .map(StackTraceElement::toString)
                    .collect(Collectors.joining());
            assertFalse(stacktrace.isEmpty());
        }

    }

    private void throwRuntimeException() {
        throw new RuntimeException("");
    }
}
