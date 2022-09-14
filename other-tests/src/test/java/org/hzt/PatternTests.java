package org.hzt;

import org.hzt.utils.It;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import static org.hzt.utils.It.println;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void testPatternAsPredicate() {
        final var string = "This, is, a,, test";
        final var pattern = Pattern.compile(",");

        final var commaCount = string.chars()
                .mapToObj(i -> (char) i)
                .peek(It::println)
                .map(String::valueOf)
                .filter(pattern.asPredicate())
                .count();

        assertEquals(4, commaCount);
    }

    @Test
    void testPatternMatcherReplaceAll() {
        final var nonDigits = "\\D";
        final var nonDigitPattern = Pattern.compile(nonDigits);
        final var testString = "PhoneNr: 0645344553, Mail: hello@test.com";

        final var result = nonDigitPattern.matcher(testString).replaceAll("d");

        final var expected = testString.replaceAll(nonDigits, "d");

        println(result);

        assertEquals(expected, result);
    }

    @Nested
    class MatcherTests {

        @Test
        void testPatternMatcherReplaceAllByResult() {
            final var nonDigits = "\\d";
            final var nonDigitPattern = Pattern.compile(nonDigits);
            final var testString = "PhoneNr: 0645344553, Mail: hello@test.com";

            final var result = nonDigitPattern.matcher(testString).replaceAll("d");

            final var expected = testString.replaceAll(nonDigits, "d");

            println(result);

            assertEquals(expected, result);
        }

        @Test
        void testMatcherGroup() {
            final var input = "6 example input 4";
            final var pattern = Pattern.compile("(\\d)(.*)(\\d)");
            final var matcher = pattern.matcher(input);

            final var matches = matcher.matches();
            final var groupCount = matcher.groupCount();

            assertAll(
                    () -> assertTrue(matches),
                    () -> assertEquals(3, groupCount),
                    () -> assertEquals("6 example input 4", matcher.group()),
                    () -> assertEquals("6 example input 4", matcher.group(0)),
                    () -> assertEquals("6", matcher.group(1)),
                    () -> assertEquals(" example input ", matcher.group(2)),
                    () -> assertEquals("4", matcher.group(3))
            );
        }

        @Test
        void testMatcherResults() {
            final var input = "6 example input 4 45345";
            final var matcher = Pattern.compile("\\d+").matcher(input);

            final var matchResults = matcher.results()
                    .map(MatchResult::group)
                    .toList();

            assertEquals(List.of("6", "4", "45345"), matchResults);
        }

        @Test
        void testMatcherResultsOfAComplicatedRegex() {
            final var input = """
                6 example input 4, here is some extra text
                hello some text without nr start
                3 second input 6
                1 third input 1032
                """;

            final var textBeginningAndEndingWithNr = Pattern.compile("(\\d)(.*)(\\d)");
            final var results = textBeginningAndEndingWithNr.matcher(input)
                    .results()
                    .map(MatchResult::group)
                    .toList();

            final var expected = List.of(
                    "6 example input 4",
                    "3 second input 6",
                    "1 third input 1032");

            assertEquals(expected, results);
        }

    }

    @Test
    void testReplaceFirstWithPlaceHolders() {
        final var input = "6 example input 4";
        final var matcher = Pattern.compile("(\\d)(.*)(\\d)").matcher(input);

        assertAll(
                () -> assertEquals("number 46", matcher.replaceFirst("number $3$1")),
                () -> assertEquals("number 46", matcher.replaceFirst(m -> "number " + m.group(3) + m.group(1)))
        );
    }

    @Test
    void testPatternQuote() {
        final var regex = "[aeiou]";
        final var input = "Hello how are you welcome to Tutorialspoint";

        final var inputContainsVowels = Pattern.compile(regex).matcher(input).find();

        final var quotedRegex = Pattern.quote(regex);
        final var inputContainsVowelsWithQuotedRegex = Pattern.compile(quotedRegex).matcher(input).find();

        assertAll(
                () -> assertEquals("\\Q[aeiou]\\E", quotedRegex),
                () -> assertTrue(inputContainsVowels),
                () -> assertFalse(inputContainsVowelsWithQuotedRegex)
        );
    }

    @Test
    void testFilteringOutAllNrsInAText() {
        final var input = """
                6 example input 4, here is some extra text
                hello some text without nr start
                3 second input 6
                1 third input 1032
                """;

        final var integerPattern = Pattern.compile("\\d+");

        final var results = integerPattern.matcher(input)
                .results()
                .map(MatchResult::group)
                .mapToInt(Integer::parseInt)
                .toArray();

        final int[] expected = {6, 4, 3, 6, 1, 1032};

        assertArrayEquals(expected, results);
    }

    @Tag("Splitting")
    @RepeatedTest(3)
    void testSplitByEmptyString() {
        final var test = "Test";
        final var emptyStringPattern = Pattern.compile("");

        final var split1 = emptyStringPattern.split(test);
        final var split2 = emptyStringPattern.splitAsStream(test).toArray(String[]::new);
        final var split3 = test.split("");

        final String[] expected = {"T", "e", "s", "t"};

        assertAll(
                () -> assertArrayEquals(expected, split1),
                () -> assertArrayEquals(expected, split2),
                () -> assertArrayEquals(expected, split3)
        );
    }

}
