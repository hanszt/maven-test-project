package hzt.leetcode;

import org.hzt.utils.sequences.Sequence;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@SuppressWarnings("SpellCheckingInspection")
class HztStringFunTest {

    @ParameterizedTest(name = "The increment of `{0}` should be: `{1}`")
    @CsvSource(value = {
            "'' -> a",
            "z -> aa",
            "hallo -> hallp",
            "zzz -> aaaa",
            "aaa -> aab",
            "azz -> baa",
            "jdgyz -> jdgza",
            "zzzzzzz -> aaaaaaaa"}, delimiterString = " -> ")
    void testIncrementStringLikeNr(final String input, String expected) {
        final var actual = HztStringFun.incrementLowerCaseAlphabeticString(input);
        assertEquals(expected, actual);
    }

    @ParameterizedTest(name = "The decrement of `{0}` should be: `{1}`")
    @CsvSource(value = {
            "a -> ''",
            "aa -> z",
            "hallp -> hallo",
            "aaaa -> zzz",
            "aab -> aaa",
            "baa -> azz",
            "bcaa -> bbzz",
            "jdgza -> jdgyz",
            "aaaaaaaa -> zzzzzzz",
            "magnolia -> magnolhz"}, delimiterString = " -> ")
    void testDecrementStringLikeNr(final String input, String expected) {
        final var actual = HztStringFun.decrementLowerCaseAlphabeticString(input);
        assertEquals(expected, actual);
    }

    @ParameterizedTest(name = "`{0}` is not compatible for string increment or decrement")
    @ValueSource(strings = {" ", "aweq we", "asda1", "!asdwq", "aAsdw"})
    void testStringsNotCompatibleForIncrementAndDecrementStringMethods(final String string) {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> HztStringFun.incrementLowerCaseAlphabeticString(string)),
                () -> assertThrows(IllegalArgumentException.class, () -> HztStringFun.decrementLowerCaseAlphabeticString(string))
        );
    }

    @Test
    @DisplayName("Test generate decremental String ints")
    void testThrows() {
        assertThrows(IllegalArgumentException.class, () -> HztStringFun.decrementLowerCaseAlphabeticString(""));
    }

    @Test
    @DisplayName("Test generate incremental String ints")
    void testGenerateIncrementalStringArray() {
        final var strings = Stream.iterate("raar", HztStringFun::incrementLowerCaseAlphabeticString)
                .limit(3_500_000)
                .sorted()
                .toArray(String[]::new);

        final var containsHallo = Arrays.binarySearch(strings, "hallo", String::compareTo) >= 0;

        assertTrue(containsHallo);
    }

    @Test
    @DisplayName("Test generate decremental String ints")
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

        assertFalse(Sequence.of(stream::iterator).isSorted(String::compareTo));
    }

    @ParameterizedTest(name = "`{2}` using `{1}` rows, should be converted to `{0}`")
    @MethodSource("provideArgumentsForZigZagConversionTests")
    void testZigZagConversion(final String expected, final int nrOfRows, final String inputString) {

        final var actual1 = HztStringFun.zigzagConversionMySolution(inputString, nrOfRows);
        final var actual2 = HztStringFun.zigZagConversionMostVotedSolution(inputString, nrOfRows);

        assertAll(
                () -> assertEquals(expected, actual1),
                () -> assertEquals(expected, actual2)
        );
    }

    @ParameterizedTest(name = "The reverse zigzag of {0} with {1} rows is: {2}")
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

    @TestFactory
    Stream<DynamicTest> testNrOfDistinctSequences() {
        return Stream.of(nrOfDistinctSequencesTest("rabbbit", "rabbit", 3),
                        nrOfDistinctSequencesTest("babgbag", "bag", 5),
                        nrOfDistinctSequencesTest("loop", "ga", 0),
                        nrOfDistinctSequencesTest("pennenlikker", "pen", 4));
    }

    DynamicTest nrOfDistinctSequencesTest(String s, String t, int expected) {
        final var displayName = "The nr of distinct subsequences of '%s' which equal '%s', should be %d".formatted(s, t, expected);
        final var actual = HztStringFun.numDistinct(s, t);
        return dynamicTest(displayName, () -> assertEquals(expected, actual));
    }
}
