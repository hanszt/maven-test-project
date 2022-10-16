package hzt.only_jdk.pattern_matching;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PatternMatchingTest {


    @ParameterizedTest
    @CsvSource({
            "param1, 6",
            "par, 7",
            "This is a test!, 15"
    })
    void testStringToInteger(String input, String actual) {
        final var expected = Integer.parseInt(actual);

        final var length = PatternMatching.toInteger(input);

        assertEquals(expected, length);
    }

    @Test
    void testNullToInteger() {
        final var value = PatternMatching.toInteger(null);
        assertEquals(0, value);
    }

    @Test
    void testIntArrayToInteger() {
        final var value = PatternMatching.toInteger(new int[] {2,2,3,4});
        assertEquals(2, value);
    }

    @Test
    void testNavigableSetToInteger() {
        final var set = new TreeSet<>(Set.of(248, 247, 10023));
        final var value = PatternMatching.toInteger(set);
        assertEquals(247, value);
    }

    @Test
    void testDecompositionUsingRecords() {
        final var point = new PatternMatching.Point(1, 2);

        final var value = PatternMatching.toInteger(point);

        assertEquals(2, value);
    }

    @Test
    void testDecompositionUsingRecordsSumGreaterThanOrEqualToTen() {
        final var point = new PatternMatching.Point(4, 7);

        final var value = PatternMatching.toInteger(point);

        assertEquals(11, value);
    }

    @Test
    void testNestedDecompositionUsingRecords() {
        final var point1 = new PatternMatching.Point(1, 2);
        final var point2 = new PatternMatching.Point(3, 4);

        final var rectangle = new PatternMatching.Rectangle(point1, point2);

        final var value = PatternMatching.toInteger(rectangle);

        assertEquals(8, value);
    }

}
