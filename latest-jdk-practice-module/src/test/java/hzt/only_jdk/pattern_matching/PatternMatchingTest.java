package hzt.only_jdk.pattern_matching;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class PatternMatchingTest {


    @ParameterizedTest
    @ValueSource(strings = {
            "param1 -> 6",
            "par -> 7",
            "This is a test! -> 15"
    })
    void testStringToInteger(String string) {
        final var split = string.split(" -> ");
        final var input = split[0];
        final var expected = Integer.parseInt(split[1]);

        final var length = PatternMatching.toInteger(input);

        assertEquals(expected, length);
    }

    @Test
    void testNullToInteger() {
        final var value = PatternMatching.toInteger(null);
        assertEquals(0, value);
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

        assertEquals(10, value);
    }

}
