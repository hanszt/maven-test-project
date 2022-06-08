package org.hzt;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PatternTests {

    @Test
    void testPatternSplitAsStream() {
        final String string = "dit, is, een, test";

        final var strings = Pattern.compile(",")
                .splitAsStream(string)
                .map(String::trim)
                .collect(Collectors.toUnmodifiableList());

        assertEquals(List.of("dit", "is", "een", "test"), strings);
    }

}
