package org.hzt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class BigDecimalSampleTest {

    /**
     * @see <a href="https://www.tutorialspoint.com/java/math/bigdecimal_equals.htm">Java bigDecimal equals</a>
     */
    @Test
    void sameValueNotSameScaleResultsInFalseUsingEquals() {
        String value = "100000";
        final var expected = new BigDecimal(value);
        final var actual = new BigDecimal(value).setScale(4, RoundingMode.HALF_UP);
        assertNotEquals(expected, actual);
    }

    /**
     * @see <a href="https://www.tutorialspoint.com/java/math/bigdecimal_equals.htm">Java bigDecimal equals</a>
     */
    @Test
    void sameValueNotSameScaleResultsInTrueUsingCompareTo() {
        String value = "100000";
        final var expected = new BigDecimal(value);
        final var actual = new BigDecimal(value).setScale(4, RoundingMode.HALF_UP);
        assertEquals(0, expected.compareTo(actual));
    }

    @Test
    void testBigDecimalOfNull() {
        //noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> new BigDecimal((String) null));
    }

    @Test
    void testBigDecimalDivisionWithoutScaleThrowsArithmeticExceptionWhenInfiniteDecimalExpansion() {
        final var THREE = BigDecimal.valueOf(3);
        //noinspection ResultOfMethodCallIgnored,BigDecimalMethodWithoutRoundingCalled
        final var exception = assertThrows(ArithmeticException.class, () -> BigDecimal.ONE.divide(THREE));
        assertEquals("Non-terminating decimal expansion; no exact representable decimal result.", exception.getMessage());
    }

    @Test
    void testBigDecimalDivisionWithoutScaleWorksWhenNoInfiniteDecimalExpansion() {
        final var TWO = BigDecimal.valueOf(2);
        //noinspection BigDecimalMethodWithoutRoundingCalled
        assertEquals(BigDecimal.valueOf(0.5), BigDecimal.ONE.divide(TWO));
    }

    @ParameterizedTest(name = "Parsing {0} by patten `{1}` should yield {2} as BigDecimal value")
    @MethodSource("decimalFormatParams")
    void testDecimalFormat(String input, String pattern, BigDecimal expected) {
        assertEquals(expected, BigDecimalSample.parseBigDecimal(input, pattern));
    }

    private static List<Arguments> decimalFormatParams() {
        return List.of(
                arguments("10,692,467,440,017.111", "#,##0.0#", BigDecimal.valueOf(10692467440017.111)),
                arguments("10692467440017.111", "#.#", BigDecimal.valueOf(10692467440017.111)));
    }

    static class Runner {
        public static void main(String... args) {
            var bigDecimal = new BigDecimal("123.1267854341");
            var bigDecimal2 = new BigDecimal("1231267854341");

            var scaledBigDecimalFloor = bigDecimal.setScale(2, RoundingMode.FLOOR);
            var scaledBigDecimalCeil = bigDecimal.setScale(2, RoundingMode.CEILING);
            var scaledBigDecimalHalfDown = bigDecimal.setScale(2, RoundingMode.HALF_DOWN);
            var scaledBigDecimalHalfEven = bigDecimal.setScale(2, RoundingMode.HALF_EVEN);
            var scaledBigDecimalHalfUp = bigDecimal2.setScale(4, RoundingMode.HALF_UP);

            out.println(bigDecimal + " after changing the scale to 2 and rounding with floor is " + scaledBigDecimalFloor);
            out.println(bigDecimal + " after changing the scale to 2 and rounding with ceil is " + scaledBigDecimalCeil);
            out.println(bigDecimal + " after changing the scale to 2 and rounding with half down is " + scaledBigDecimalHalfDown);
            out.println(bigDecimal + " after changing the scale to 2 and rounding with half even is " + scaledBigDecimalHalfEven);
            out.println(bigDecimal2 + " after changing the scale to 4 and rounding with mode half up is " + scaledBigDecimalHalfUp);

            out.println("Scale of: " + bigDecimal + " is: " + bigDecimal.scale());
            out.println("Scale of: " + scaledBigDecimalHalfUp + " is: " + scaledBigDecimalHalfUp.scale());
            out.println("Scale of: " + scaledBigDecimalHalfDown + " is: " + scaledBigDecimalHalfDown.scale());
        }
    }

}
