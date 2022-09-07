package org.hzt;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.util.ArithmeticUtils;
import org.hzt.utils.PreConditions;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

public final class RecursiveSamples {

    private RecursiveSamples() {
    }

    /**
     * @param n The index in the sequence you want the fibonacci nr of
     * @return the fibonacci nr at n
     *
     * This method has O(2^n) time complexity which is exponential and very bad
     */
    public static long fib(int n) {
        PreConditions.require(n >= 0, () -> "the position n in the fib sequence must be greater than 0 but was " + n);
        if (n == 0) {
            return 0;
        }
        if (n <= 2) {
            return 1;
        }
        return fib(n - 1) + fib(n - 2);
    }

    public static int sum(int start, int end) {
        return end > start ? (end + sum(start, end - 1)) : end;
    }

    @SuppressWarnings("InfiniteRecursion")
    public static void stackOverflow() {
        stackOverflow();
    }

    /**
     * It is assumed the length of the input array is a power of two
     *
     * @param input the complex nr array to apply the fast fourier transform to
     * @return the fourier transform of the input
     */
    public static Complex[] fastFourierTransform(Complex[] input) {
        int length = input.length;
        // base case
        if (length == 1) {
            return new Complex[]{input[0]};
        }

        if (!ArithmeticUtils.isPowerOfTwo(length)) {
            throw new IllegalArgumentException("The length: " + length + " is not a power of 2");
        }

        final var halfLength = length / 2;

        Complex[] halfLengthArray = new Complex[halfLength];

        // compute FFT of even terms
        for (int k = 0; k < halfLength; k++) {
            halfLengthArray[k] = input[2 * k];
        }
        Complex[] evenFFT = fastFourierTransform(halfLengthArray);

        // compute FFT of odd terms
        // reuse the array (to avoid n log n space)
        for (int k = 0; k < halfLength; k++) {
            halfLengthArray[k] = input[2 * k + 1];
        }
        Complex[] oddFFT = fastFourierTransform(halfLengthArray);

        return combine(evenFFT, oddFFT, length, halfLength);
    }

    @NotNull
    private static Complex[] combine(Complex[] evenFFT, Complex[] oddFFT, int length, int halfLength) {
        Complex[] result = new Complex[length];
        for (int k = 0; k < halfLength; k++) {
            double phi = -(2 * Math.PI) * k / length;
            Complex angle = new Complex(Math.cos(phi), Math.sin(phi));

            final var complex = angle.multiply(oddFFT[k]);

            result[k] = evenFFT[k].add(complex);
            result[k + halfLength] = evenFFT[k].subtract(complex);
        }
        return result;
    }

    /**
     * @param n1 value 1
     * @param n2 value 2
     * @return the greatest common divisor
     * @see <a href="https://www.baeldung.com/java-greatest-common-divisor">Finding Greatest Common Divisor in Java</a>
     */
    public static long gcdByEuclidsAlgorithm(long n1, long n2) {
        if (n2 == 0) {
            return n1;
        }
        return gcdByEuclidsAlgorithm(n2, n1 % n2);
    }

    public static BigInteger gcdByEuclidsAlgorithm(BigInteger n1, BigInteger n2) {
        if (BigInteger.ZERO.equals(n2)) {
            return n1;
        }
        return gcdByEuclidsAlgorithm(n2, n1.mod(n2));
    }
}
