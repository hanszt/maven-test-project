package org.hzt;

import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CharsetTests {

    @Test
    void testAvailableCharsets() {
        final var charsets = Charset.availableCharsets();
        charsets.entrySet().forEach(System.out::println);
        assertTrue(charsets.containsKey("windows-1252"));
    }
}
