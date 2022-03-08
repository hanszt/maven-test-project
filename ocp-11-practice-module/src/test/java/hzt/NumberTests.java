package hzt;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        assertEquals(Double.POSITIVE_INFINITY, numerator / d);
        assertEquals(Double.POSITIVE_INFINITY, numerator / f);
        assertEquals(Double.POSITIVE_INFINITY, numerator / l);
        assertEquals(Double.POSITIVE_INFINITY, numerator / i);
        assertEquals(Double.POSITIVE_INFINITY, numerator / s);
        assertEquals(Double.POSITIVE_INFINITY, numerator / b);
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
        assertEquals(Double.NEGATIVE_INFINITY, numerator / d);
        assertEquals(Double.NEGATIVE_INFINITY, numerator / f);
        assertEquals(Double.NEGATIVE_INFINITY, numerator / l);
        assertEquals(Double.NEGATIVE_INFINITY, numerator / i);
        assertEquals(Double.NEGATIVE_INFINITY, numerator / s);
        assertEquals(Double.NEGATIVE_INFINITY, numerator / b);
    }

    @Test
    void testToHexString() {
        assertEquals("14", Integer.toHexString(20));
    }

}
