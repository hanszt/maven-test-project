package hzt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class AnagramCheckerTest {

    @ParameterizedTest(name = "The method areAnagrams() should return true when `{0}` and `{1}` are checked")
    @CsvSource({
            "a gentleman, elegant man",
            "laptop machines, apple macintosh",
            "avida dollars, salvador dali"
    })
    void test1(CharSequence s1, CharSequence s2) {
        assertAll(
                () -> assertTrue(AnagramChecker.areAnagramsByGrouping(s1, s2)),
                () -> assertTrue(AnagramChecker.areAnagramsByPrimes(s1, s2)),
                () -> assertTrue(AnagramChecker.areAnagramsBySorting(s1, s2))
        );
    }

    @ParameterizedTest(name = "The method areAnagrams() should return false when `{0}` and `{1}` are checked")
    @CsvSource({
            "a gentleman, spider man",
            "laptop computers, apple macintosh",
            "anagram, pentagram"})
    void test2(CharSequence s1, CharSequence s2) {
        assertAll(
                () -> assertFalse(AnagramChecker.areAnagramsByGrouping(s1, s2)),
                () -> assertFalse(AnagramChecker.areAnagramsByGrouping(s1, s2)),
                () -> assertFalse(AnagramChecker.areAnagramsByGrouping(s1, s2))
        );
    }

    @Test
    void testThrowIfOverflow() {
        final String s1 = "oh lame saint o draconian devil";
        final String s2 = "leonardo da vinci the mona lisa";
        assertTrue(AnagramChecker.areAnagramsBySorting(s1.replace(" ", ""), s2.replace(" ", "")));
        final var e = assertThrows(IllegalStateException.class, () -> AnagramChecker.areAnagramsByPrimes(s1, s2));
        assertEquals("5627880822688854268 * 2 resulted in an overflow state", e.getMessage());
    }

}
