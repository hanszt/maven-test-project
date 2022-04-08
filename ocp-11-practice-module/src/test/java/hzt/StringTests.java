package hzt;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StringTests {

    @Test
    void testReverseWithNonAsciiCodeCharacters() {
        final var input = "pinigų";
        final var reverse = new StringBuilder(input).reverse().toString();

        assertAll(
                () -> assertEquals("ųginip", reverse),
                () -> assertEquals(6, input.length()),
                () -> assertEquals(6, input.codePoints().count())
        );
    }

    @Test
    void testReverseSurrogatePairString() {
        String smileyFace = "😀";
        final var reverse = new StringBuilder(smileyFace).reverse().toString();

        assertAll(
                () -> assertEquals(smileyFace, reverse),
                () -> assertEquals(1, smileyFace.codePoints().count()),
                () -> assertEquals(2, smileyFace.length())
        );
    }
}
