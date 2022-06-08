package org.hzt.stream;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomStreamTest {

    @Test
    void testCustomStreamMethod() {
        final var strings = CustomStream.of(List.of("This", "is", "a", "test"))
                .filterNot("This"::contains)
                .toList();

        assertEquals(List.of("a", "test"), strings);
    }

}
