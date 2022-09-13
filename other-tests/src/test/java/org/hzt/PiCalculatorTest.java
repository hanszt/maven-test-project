package org.hzt;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PiCalculatorTest {

    @Test
    void testCalculatePi() {
        final var pi = PiCalculator.piByMachinsFormula(15);
        final var expected = BigDecimal.valueOf(Math.PI);
        assertEquals(expected, pi);
    }

    @Test
    void testCalculatePiTenThousandDigits() {
        final var numDigits = 10_000;
        final var PI_FIRST_100_DIGITS =
                "3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679";

        final var expected = new BigDecimal(PI_FIRST_100_DIGITS);
        final var pi = PiCalculator.piByMachinsFormula(numDigits);

        System.out.println("pi = " + pi);

        assertAll(
                () -> assertEquals(numDigits, pi.scale()),
                () -> assertEquals(expected, pi.setScale(100, RoundingMode.DOWN))
        );
    }
}
