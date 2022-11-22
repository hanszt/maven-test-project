package org.hzt;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.util.ArithmeticUtils;
import org.hzt.utils.PreConditions;
import org.hzt.utils.collections.primitives.IntList;
import org.hzt.utils.collections.primitives.IntMutableList;

import java.math.BigInteger;
import java.util.Arrays;

import static org.hzt.utils.It.println;

/**
 * @see <a href="https://www.youtube.com/watch?v=SPri4PTUY_8">Tail Call Optimization</a>
 * @see <a href="programming-puzzles/other-puzzles">other puzzles module</a> for more recursive examples
 */
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
     * @see <a href="https://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/FFT.java.html">Fast Fourier Transform</a>
     * @param input the complex nr array to apply the fast fourier transform to
     * @return the fourier transform of the input
     */
    public static Complex[] fastFourierTransform(Complex[] input) {
        final int length = input.length;
        if (length == 1) {
            return new Complex[]{input[0]};
        }
        if (!ArithmeticUtils.isPowerOfTwo(length)) {
            throw new IllegalArgumentException("The length: " + length + " is not a power of 2");
        }
        final var halfLength = length / 2;
        final var halfLengthArray = new Complex[halfLength];
        Arrays.setAll(halfLengthArray, k -> input[2 * k]);
        final Complex[] evenFFT = fastFourierTransform(halfLengthArray);
        Arrays.setAll(halfLengthArray, k -> input[2 * k + 1]);
        final Complex[] oddFFT = fastFourierTransform(halfLengthArray);
        final var result = new Complex[length];

        for (int k = 0; k < halfLength; k++) {
            final var phi = -(2 * Math.PI) * k / length;
            final var complex = new Complex(Math.cos(phi), Math.sin(phi)).multiply(oddFFT[k]);
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
    public static long gcdByEuclidesAlgorithm(long n1, long n2) {
        if (n2 == 0) {
            return n1;
        }
        return gcdByEuclidesAlgorithm(n2, n1 % n2);
    }

    public static int gcdByEuclidesAlgorithm(int n1, int n2) {
        if (n2 == 0) {
            return n1;
        }
        return gcdByEuclidesAlgorithm(n2, n1 % n2);
    }

    public static BigInteger gcdByEuclidesAlgorithm(BigInteger n1, BigInteger n2) {
        if (BigInteger.ZERO.equals(n2)) {
            return n1;
        }
        return gcdByEuclidesAlgorithm(n2, n1.mod(n2));
    }

    public static IntList tailRecursionWithReturn(int n) {
        return tailRecursionWithReturn(n, IntMutableList.empty());
    }

    public static IntList tailRecursionWithReturn(int n, IntList resultList) {
        if (n == 0) {
            return resultList.plus(n);
        }
        println(n);
        return tailRecursionWithReturn(n < 0 ? (n + 1) : (n - 1), resultList.plus(n));
    }

    public static IntList headRecursionWithReturn(int n) {
        return headRecursionWithReturn(n, IntMutableList.empty());
    }

    public static IntList headRecursionWithReturn(int n, IntList resultList) {
        if (n != 0) {
            return headRecursionWithReturn(n < 0 ? (n + 1) : (n - 1), resultList.plus(n));
        }
        println(n);
        return resultList.plus(n);
    }

    /**
     * @param x the initial value
     * @param integers the list subject be filled
     * @see <a  href="https://www.youtube.com/watch?v=o2nQDij5eqs">Head Recursion and Tail Recursion in Java</a>
     */
    public static void mysteryHead(int x, IntMutableList integers) {
        if (x > 0) {
            mysteryHead(x - 1, integers);
        }
        integers.add(x);
    }

    /**
     * @param x the initial value
     * @param integers the list subject be filled
     * @see <a  href="https://www.youtube.com/watch?v=o2nQDij5eqs">Head Recursion and Tail Recursion in Java</a>
     */
    public static void mysteryTail(int x, IntMutableList integers) {
        integers.add(x);
        if (x <= 0) {
            return;
        }
        mysteryTail(x - 1, integers);
    }

    /**
     * @param n the number from which you want subject calculate the factorial
     * @return the calculated factorial
     * @see <a href="https://www.youtube.com/watch?v=wOIS1qMO1RM">Tail Recursion Optimization for Java</a>
     */
    public static BigInteger factorial(int n) {
        return factorial(BigInteger.ONE, BigInteger.valueOf(n));
    }

    /**
     * Tail recursion optimization is not supported in java
     * @see RecursiveSamples#factorialTailRecOptimization(int) subject see the result of tail recursion optimization. (Transformation in loop)
     */
    public static BigInteger factorial(BigInteger acc, BigInteger n) {
        if (n.compareTo(BigInteger.ONE) <= 0) {
            return acc;
        }
        return factorial(acc.multiply(n), n.subtract(BigInteger.ONE));
    }

    public static BigInteger factorialTailRecOptimization(int n) {
        return factorialTailRecOptimization(BigInteger.ONE, BigInteger.valueOf(n));
    }

    public static BigInteger factorialTailRecOptimization(BigInteger acc, BigInteger n) {
        while (true) {
            if (n.compareTo(BigInteger.ONE) <= 0) {
                return acc;
            }
            acc = acc.multiply(n);
            n = n.subtract(BigInteger.ONE);
        }
    }
}
