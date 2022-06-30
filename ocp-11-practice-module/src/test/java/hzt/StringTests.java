package hzt;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

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

    @Test
    void testSplitStringWithoutCommaByCommaYieldsArrayOfLengthOne() {
        final var text = "This is not a comma seperated text";
        String[] splitArray = text.split(",");
        assertEquals(1, splitArray.length);
    }

    @Test
    void testSplitEmptyStringYieldsArrayOfLengthOne() {
        String[] splitArray = "".split(",");
        assertEquals(1, splitArray.length);
    }

    @Test
    void testSplitEmptyStringByPatternYieldsStreamOfOne() {
        long count = Pattern.compile(",").splitAsStream("").count();
        assertEquals(1L, count);
    }

    @Test
    void testSplitStringByPatternWithoutCommaByCommaYieldsStreamOfOne() {
        final var text = "This is not a comma seperated text";
        long count = Pattern.compile(",").splitAsStream(text).count();
        assertEquals(1L, count);
    }
}
