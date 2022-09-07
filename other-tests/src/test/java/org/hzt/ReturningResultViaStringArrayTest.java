package org.hzt;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReturningResultViaStringArrayTest {

    @Test
    void testReturningInfoFromMainMethodThroughArray() {
        var arguments = new String[10];
        ReturningResultViaStringArray.main(arguments);

        final var strings = Arrays.stream(arguments)
                .collect(Collectors.toSet());

        final var expected = new HashSet<>(Arrays.asList("This", "is", "a", "test", "Hello", "World", null));

        assertEquals(expected, strings);
    }

}
