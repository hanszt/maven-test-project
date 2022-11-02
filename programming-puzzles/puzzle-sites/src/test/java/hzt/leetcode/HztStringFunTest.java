package hzt.leetcode;

import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.strings.StringX;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.*;

@SuppressWarnings("SpellCheckingInspection")
class HztStringFunTest {

    @ParameterizedTest
    @ValueSource(strings = {
            " -> a",
            "z -> aa",
            "hallo -> hallp",
            "zzz -> aaaa",
            "aaa -> aab",
            "azz -> baa",
            "jdgyz -> jdgza",
            "zzzzzzz -> aaaaaaaa"})
    void testIncrementStringLikeNr(final String string) {
        final var split = StringX.of(string).split(" -> ");
        final var input = split.first();
        final var expected = split.last();

        final var actual = HztStringFun.incrementLowerCaseAlphabeticString(input);

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "a -> ",
            "aa -> z",
            "hallp -> hallo",
            "aaaa -> zzz",
            "aab -> aaa",
            "baa -> azz",
            "bcaa -> bbzz",
            "jdgza -> jdgyz",
            "aaaaaaaa -> zzzzzzz",
            "magnolia -> magnolhz"})
    void testDecrementStringLikeNr(final String string) {
        final var split = StringX.of(string).split(" -> ");
        final var input = split.first();
        final var expected = split.last();

        final var actual = HztStringFun.decrementLowerCaseAlphabeticString(input);

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", "aweq we", "asda1", "!asdwq", "aAsdw"})
    void testStringsNotCompatibleForIncrementAndDecrementStringMethods(final String string) {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> HztStringFun.incrementLowerCaseAlphabeticString(string)),
                () -> assertThrows(IllegalArgumentException.class, () -> HztStringFun.decrementLowerCaseAlphabeticString(string))
        );
    }

    @Test
    @DisplayName("Test generate decremental String array")
    void testThrows() {
       assertThrows(IllegalArgumentException.class, () -> HztStringFun.decrementLowerCaseAlphabeticString(""));
    }

    @Test
    @DisplayName("Test generate incremental String array")
    void testGenerateIncrementalStringArray() {
        final var strings = Stream.iterate("raar", HztStringFun::incrementLowerCaseAlphabeticString)
                .limit(3_500_000)
                .sorted()
                .toArray(String[]::new);

        final var containsHallo = Arrays.binarySearch(strings, "hallo", String::compareTo) >= 0;

        assertTrue(containsHallo);
    }

    @Test
    @DisplayName("Test generate decremental String array")
    void testGenerateDecrementalStringArray() {
        final var strings = Stream
                .iterate("test", Predicate.not(""::equals), HztStringFun::decrementLowerCaseAlphabeticString)
                .sorted()
                .toArray(String[]::new);

        assertTrue(Arrays.binarySearch(strings, "huis", String::compareTo) >= 0);
    }

    @Test
    void testStringIncrementArrayIsSorted() {
        final var stream = Stream
                .iterate("raar", HztStringFun::incrementLowerCaseAlphabeticString)
                .limit(3_500_000);

        assertFalse(Sequence.ofStream(stream).isSorted(String::compareTo));
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForZigZagConversionTests")
    void testZigZagConversion(final String expected, final int nrOfRows, final String inputString) {

        final var actual1 = HztStringFun.zigzagConversionMySolution(inputString, nrOfRows);
        final var actual2 = HztStringFun.zigZagConversionMostVotedSolution(inputString, nrOfRows);

        assertAll(
                () -> assertEquals(expected, actual1),
                () -> assertEquals(expected, actual2)
        );
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForZigZagConversionTests")
    void testReverseZigZagConversion(final String inputString, final int nrOfRows, final String expected) {

        final var actual = HztStringFun.reverseZigzagConversion(inputString, nrOfRows);

        assertEquals(expected, actual);
    }

    private static Stream<Arguments> provideArgumentsForZigZagConversionTests() {
        return Stream.of(
                arguments("AB", 1, "AB"),
                arguments("PAHNAPLSIIGYIR", 3, "PAYPALISHIRING"),
                arguments("PINALSIGYAHRPI", 4, "PAYPALISHIRING"),
                arguments("PHASIYIRPLIGAN", 5, "PAYPALISHIRING")
        );
    }
}
