package org.hzt;

import org.hzt.utils.strings.StringX;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.lang.System.out;
import static org.hzt.BigDecimalSample.comparingBigDecimalsUsingCompareTo;
import static org.hzt.BigDecimalSample.comparingBigDecimalsUsingEquals;
import static org.junit.jupiter.api.Assertions.*;

class BigDecimalSampleTest {

    /**
     * @see <a href="https://www.tutorialspoint.com/java/math/bigdecimal_equals.htm">Java bigDecimal equals</a>
     */
    @Test
    void sameValueNotSameScaleResultsInFalseUsingEquals() {
        String value = "100000";
        final var expected = new BigDecimal(value);
        var actual = new BigDecimal(value);
        actual = actual.setScale(4, RoundingMode.HALF_UP);
        assertFalse(comparingBigDecimalsUsingEquals(expected, actual));
    }

    /**
     * @see <a href="https://www.tutorialspoint.com/java/math/bigdecimal_equals.htm">Java bigDecimal equals</a>
     */
    @Test
    void sameValueNotSameScaleResultsInTrueUsingCompareTo() {
        String value = "100000";
        final var expected = new BigDecimal(value);
        var actual = new BigDecimal(value);
        actual = actual.setScale(4, RoundingMode.HALF_UP);
        assertTrue(comparingBigDecimalsUsingCompareTo(expected, actual));
    }

    public static void main(String[] args) {
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

    @Test
    void testBigDecimalOfNull() {
        //noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> new BigDecimal((String) null));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "10,692,467,440,017.111 | #,##0.0# -> 10692467440017.111",
            "10692467440017.111 | #.# -> 10692467440017.111"
    })
    void testDecimalFormat(String string) {
        final var split = StringX.of(string).split(" | ", " -> ");
        final var input = split.get(0);
        final var pattern = split.get(1);
        final var expected = new BigDecimal(split.get(2));

        final var bigDecimal = BigDecimalSample.parseBigDecimal(input, pattern);

        assertEquals(expected, bigDecimal);
    }

}
