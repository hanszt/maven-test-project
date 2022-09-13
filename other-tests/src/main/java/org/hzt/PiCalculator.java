package org.hzt;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class PiCalculator {

    private static final BigDecimal TWO = new BigDecimal("2");
    private static final BigDecimal FOUR = new BigDecimal("4");
    private static final BigDecimal FIVE = new BigDecimal("5");
    private static final BigDecimal TWO_THIRTY_NINE = new BigDecimal("239");

    private PiCalculator() {
    }

    /**
     * @param numDigits The nr of digits to calculate pi to
     * @return Pi as BigDecimal by the requested nr of digits
     * @see <a href="https://stackoverflow.com/questions/8370290/generating-pi-to-nth-digit-java">generating pi to nth digit java</a>
     * @see <a href="https://mathworld.wolfram.com/MachinsFormula.html">Machin's Formula</a>
     * @see <a href="https://medium.com/@cosinekitty/how-to-calculate-a-million-digits-of-pi-d62ce3db8f58">How to Calculate a Million Digits of Pi</a>
     * <p><img src="../../../../../documentation/machins-formula.png" alt="Machins formula"></p>
     */
    public static BigDecimal piByMachinsFormula(final int numDigits) {
        // To account for inaccuracies in calculating the arcCot
        final int calcDigits = numDigits + 10;

        final var subtract = FOUR.multiply(arcCot(FIVE, calcDigits))
                .subtract(arcCot(TWO_THIRTY_NINE, calcDigits));

        return FOUR.multiply(subtract).setScale(numDigits, RoundingMode.DOWN);
    }

    /**
     * Calculate the inverse cotangent. Probably by a maclaurin series expansion
     *
     * @param x         the value to perform the calculation on
     * @param numDigits the precision
     * @return the inverse cotangent of x
     * @see <a href="https://en.wikipedia.org/wiki/Inverse_trigonometric_functions">Inverse trigonometric functions</a>
     * @see <a href="https://archive.lib.msu.edu/crcmath/math/math/i/i189.htm">Inverse Cotangent series</a>
     */
    private static BigDecimal arcCot(final BigDecimal x, final int numDigits) {
        final var one = BigDecimal.ONE.setScale(numDigits, RoundingMode.DOWN);

        BigDecimal sum = one.divide(x, RoundingMode.DOWN);
        BigDecimal xPower = sum;
        boolean add = false;

        for (BigDecimal n = new BigDecimal("3"); ; n = n.add(TWO), add = !add) {
            xPower = xPower.divide(x.pow(2), RoundingMode.DOWN);
            final BigDecimal term = xPower.divide(n, RoundingMode.DOWN);
            sum = add ? sum.add(term) : sum.subtract(term);
            if (term.compareTo(BigDecimal.ZERO) == 0) {
                break;
            }
        }
        return sum;
    }
}
