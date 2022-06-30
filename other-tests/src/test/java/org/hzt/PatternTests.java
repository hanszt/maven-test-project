package org.hzt;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PatternTests {

    @Test
    void testPatternSplitAsStream() {
        final String string = "dit, is, een, test";

        final var strings = Pattern.compile(",")
                .splitAsStream(string)
                .map(String::trim)
                .toList();

        assertEquals(List.of("dit", "is", "een", "test"), strings);
    }

    @Test
    void testPattern() {
        final var string = "This, is, a,, test";
        final var pattern = Pattern.compile(",");

        final var commaCount = string.chars()
                .mapToObj(i -> (char) i)
                .peek(System.out::println)
                .map(String::valueOf)
                .filter(pattern.asPredicate())
                .count();

        assertEquals(4, commaCount);
    }

}
