package hzt.primitives_and_wrappers;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.text.DecimalFormat;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class PrimitiveTests {

    // Q
    @SuppressWarnings({"UnnecessaryLocalVariable", "RedundantCast"})
    @Test
    void testAddingCharsYieldsNumberTheCharIsRepresentedBy() {
        char a = 'a', b = 98; //1
        int a1 = a; //2
        int b1 = (int) b; //3
        assertEquals(195, (char) a1 + (char) b1); //4
    }

    // Q 11 test 6
    @SuppressWarnings("UnusedAssignment")
    @Test
    void testPrimitivesAsExpressions() {
        int a = 10;
        int b = 20;
        a += (a = 4);
        b = b + (b = 5);

        int finalA = a;
        int finalB = b;

        assertAll(
                () -> assertEquals(14, finalA),
                () -> assertEquals(25, finalB)
        );
    }

    /**
     * @see <a href="https://www.baeldung.com/java-decimalformat">A Practical Guide to DecimalFormat</a>
     */
    @Test
    void testDecimalFormatOnDouble() {
        final var defaultLocale = Locale.getDefault();
        try {
            Locale.setDefault(Locale.US);
            double d = 1234567.89;

            assertAll(
                    () -> assertEquals("1234567.89", new DecimalFormat("#.##").format(d)),
                    () -> assertEquals("1234567.89", new DecimalFormat("0.00").format(d)),
                    () -> assertEquals("1234567.9", new DecimalFormat("#.#").format(d)),
                    () -> assertEquals("1,234,567.9", new DecimalFormat("#,###.#").format(d))
            );
        } finally {
            Locale.setDefault(defaultLocale);
        }
    }

    /**
     * @see <a href="https://www.baeldung.com/java-operators">Java Operators</a>
     */
    @Nested
    class BooleanOperatorTets {

        @ParameterizedTest(name = "{0} && {1} should return {2}")
        @CsvSource({
                "true, true, true",
                "true, false, false",
                "false, true, false",
                "false, false, false"
        })
        void testLogicalAndOperator(boolean condition1, boolean condition2, boolean expected) {
            assertEquals(expected, condition1 && condition2);
        }

        @ParameterizedTest(name = "{0} || {1} should return {2}")
        @CsvSource({
                "true, true, true",
                "true, false, true",
                "false, true, true",
                "false, false, false"
        })
        void testLogicalOrOperator(boolean condition1, boolean condition2, boolean expected) {
            assertEquals(expected, condition1 || condition2);
        }

        @ParameterizedTest(name = "{0} ^ {1} should return {2}")
        @CsvSource({
                "true, true, false",
                "true, false, true",
                "false, true, true",
                "false, false, false"
        })
        void testLogicalExclusiveOrOperator(boolean condition1, boolean condition2, boolean expected) {
            assertEquals(expected, condition1 ^ condition2);
        }
    }
}
