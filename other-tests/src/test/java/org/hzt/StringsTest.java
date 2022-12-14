package org.hzt;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StringsTest {

    @Test
    void testSplitNonRegex() {
        final var s = "This is a test";
        final var strings = Strings.splitNonRegex(s, " ");
        assertEquals(List.of("This", "is", "a", "test"), strings);
    }

    @Test
    void testSplitNonRegexByEmptyDelimiter() {
        final var s = "Test";
        final var strings = Strings.splitNonRegex(s, "");
        final var actual = StringsKt.splitNonRegex(s, "");

        assertAll(
                () ->         assertEquals(List.of("T", "e", "s", "t"), strings),
                () -> assertEquals(actual, strings)
        );
    }

    @Test
    void testSplitNonRegexJavaVsKotlinTailRecImpl() {
        final var s = "This is a test";
        final var expected = Strings.splitNonRegex(s, " ");
        final var actual = StringsKt.splitNonRegex(s, " ");

        assertEquals(actual, expected);
    }

}
