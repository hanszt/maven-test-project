package org.hzt;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OptionalTests {

    @Test
    void testTwoEmptyOptionalsAreEqual() {
        assertEquals(Optional.empty(), Optional.empty());
    }

    @Test
    void testTwoNullsAreEqual() {
        final Object a = null;
        final Object b = null;
        final var nullsEqual = Objects.equals(a, b);
        assertTrue(nullsEqual);
    }

    @Test
    void testOptionalStringsEqual() {
        final var hallo = Optional.of("Hallo");
        final var hallo1 = Optional.of("Hallo");
        assertEquals(hallo, hallo1);
    }

    @Test
    void testOptionalStringsNotEqual() {
        final var hallo = Optional.of("Hallo");
        final var hallo1 = Optional.of("Hallo1");
        assertNotEquals(hallo, hallo1);
    }

    @Test
    void testOptionalOfEntryUsedMultipleTimes() {
        final var entry = Optional.of(Map.entry("Key", "Value"));
        final var key = entry.map(Map.Entry::getKey).orElse("error key");
        final var value = entry.map(Map.Entry::getValue).orElse("error value");

        assertAll(
                () -> assertTrue(entry.isPresent()),
                () -> assertEquals("Key", key),
                () -> assertEquals("Value", value)
        );
    }
}
