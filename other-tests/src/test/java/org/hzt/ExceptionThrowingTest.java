package org.hzt;

import org.hzt.utils.It;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static java.util.stream.Collectors.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

class ExceptionThrowingTest {

    @Test
    void testExceptionGetMessageTest() {
        try {
            throwRuntimeException();
            fail();
        }catch (RuntimeException e) {
            StackTraceElement[] stackTraceElements = e.getStackTrace();

            String stacktrace = Arrays.stream(stackTraceElements)
                    .peek(It::println)
                    .map(StackTraceElement::toString)
                    .collect(joining());

            assertFalse(stacktrace.isEmpty());
        }

    }

    private void throwRuntimeException() {
        throw new RuntimeException("");
    }
}
