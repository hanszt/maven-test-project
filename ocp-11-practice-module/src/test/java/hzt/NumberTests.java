package hzt;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("divzero")
class NumberTests {

    @Test
    void testPositiveDoubleDividedByZero() {
        double numerator = 1;
        double d = 0;
        float f = 0;
        long l = 0;
        int i = 0;
        short s = 0;
        byte b = 0;

        assertAll(
                () -> assertEquals(Double.POSITIVE_INFINITY, numerator / d),
                () -> assertEquals(Double.POSITIVE_INFINITY, numerator / f),
                () -> assertEquals(Double.POSITIVE_INFINITY, numerator / l),
                () -> assertEquals(Double.POSITIVE_INFINITY, numerator / i),
                () -> assertEquals(Double.POSITIVE_INFINITY, numerator / s),
                () -> assertEquals(Double.POSITIVE_INFINITY, numerator / b)
        );
    }

    @Test
    void testNegativeDoubleDividedByZero() {
        double numerator = -1;
        double d = 0;
        float f = 0;
        long l = 0;
        int i = 0;
        short s = 0;
        byte b = 0;

        assertAll(
                () -> assertEquals(Double.NEGATIVE_INFINITY, numerator / d),
                () -> assertEquals(Double.NEGATIVE_INFINITY, numerator / f),
                () -> assertEquals(Double.NEGATIVE_INFINITY, numerator / l),
                () -> assertEquals(Double.NEGATIVE_INFINITY, numerator / i),
                () -> assertEquals(Double.NEGATIVE_INFINITY, numerator / s),
                () -> assertEquals(Double.NEGATIVE_INFINITY, numerator / b)
        );
    }

    @Test
    void testPositiveFloatDividedByZero() {
        float numerator = 1;
        double d = 0;
        float f = 0;
        long l = 0;
        int i = 0;
        short s = 0;
        byte b = 0;

        assertAll(
                () -> assertEquals(Float.POSITIVE_INFINITY, numerator / d),
                () -> assertEquals(Float.POSITIVE_INFINITY, numerator / f),
                () -> assertEquals(Float.POSITIVE_INFINITY, numerator / l),
                () -> assertEquals(Float.POSITIVE_INFINITY, numerator / i),
                () -> assertEquals(Float.POSITIVE_INFINITY, numerator / s),
                () -> assertEquals(Float.POSITIVE_INFINITY, numerator / b)
        );
    }

    @Test
    void testNegativeFloatDividedByZero() {
        float numerator = -1;
        double d = 0;
        float f = 0;
        long l = 0;
        int i = 0;
        short s = 0;
        byte b = 0;

        assertAll(
                () -> assertEquals(Float.NEGATIVE_INFINITY, numerator / d),
                () -> assertEquals(Float.NEGATIVE_INFINITY, numerator / f),
                () -> assertEquals(Float.NEGATIVE_INFINITY, numerator / l),
                () -> assertEquals(Float.NEGATIVE_INFINITY, numerator / i),
                () -> assertEquals(Float.NEGATIVE_INFINITY, numerator / s),
                () -> assertEquals(Float.NEGATIVE_INFINITY, numerator / b)
        );
    }

    @Test
    void testBigDecimalDivideByZero() {
        //noinspection ResultOfMethodCallIgnored
        assertThrows(ArithmeticException.class, () -> BigDecimal.TEN.divide(BigDecimal.ZERO, 2, RoundingMode.HALF_UP));
    }

    @Test
    void testBigDecimalFromEmptyString() {
        assertThrows(NumberFormatException.class, () -> new BigDecimal(""));
    }

    @Test
    void testBigDecimalMovePointLeft() {
        assertEquals(0, BigDecimal.valueOf(0.10).compareTo(BigDecimal.TEN.movePointLeft(2)));
    }

    @Test
    void testToHexString() {
        assertEquals("14", Integer.toHexString(20));
    }

    @Test
    void testBintIntegerEquals() {
        final var equals = BigInteger.valueOf(0).equals(BigInteger.ZERO);
        assertTrue(equals);
    }

}
