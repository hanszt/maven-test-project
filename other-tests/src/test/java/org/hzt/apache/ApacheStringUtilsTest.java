package org.hzt.apache;

import org.apache.commons.lang3.StringUtils;
import org.hzt.utils.strings.StringX;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ApacheStringUtilsTest {

    @Test
    void testRightPad() {
        final var rightPad = StringUtils.rightPad("Hi", 10);

        assertAll(
                () -> assertEquals("Hi        ", rightPad),
                () -> assertEquals(10, rightPad.length())
        );
    }

    @Test
    void testAbbreviate() {
        final var input = "Abbreviation";
        final var maxLength = 7;
        final var abbreviation = StringUtils.abbreviate(input, maxLength);
        final var abbreviated = abbreviate(input, maxLength);

        assertAll(
                () -> assertEquals("Abbr...", abbreviation),
                () -> assertEquals(abbreviated.length(), abbreviation.length())
        );
    }

    @Test
    void testGetIfEmpty() {
        final StringX hello = StringUtils.getIfEmpty(StringX.of(""), () -> StringX.of("Hello"));

        assertEquals(2, hello.group().get('l').size());
    }

    @Test
    void testCapitalize() {
        final var capitalized = StringUtils.capitalize("tHis sentence NEEDS to be Capitalized!".toLowerCase());
        assertEquals("This sentence needs to be capitalized!", capitalized);
    }

    public static String abbreviate(String s, int maxLength) {
        Objects.requireNonNull(s, "The input string must not be null");
        return s.length() <= maxLength ? s : s.substring(0, maxLength);
    }
}
