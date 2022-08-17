package org.hzt.apache;

import org.apache.commons.lang3.StringUtils;
import org.hzt.utils.strings.StringX;
import org.junit.jupiter.api.Test;

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
        final var abbreviation = StringUtils.abbreviate("Abbreviation", 7);
        assertEquals("Abbr...", abbreviation);
    }

    @Test
    void testGetIfEmpty() {
        final StringX hello = StringUtils.getIfEmpty(StringX.of(""), () -> StringX.of("Hello"));

        assertEquals(2, hello.group().get('l').size());
    }
}
