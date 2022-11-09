package org.hzt;

import org.apache.commons.math3.complex.Complex;
import org.hzt.utils.It;
import org.hzt.utils.collections.primitives.IntList;
import org.hzt.utils.collections.primitives.IntMutableList;
import org.hzt.utils.sequences.Sequence;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ArgumentConverter;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import static org.hzt.utils.It.println;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class RecursiveSamplesTest {

    @Test
    void testFib() {
        final var n = 10;

        final var expected = Streams.fibonacciStream()
                .skip(n)
                .mapToLong(BigInteger::longValue)
                .findFirst()
                .orElseThrow();

        final var fib = RecursiveSamples.fib(n);

        assertAll(
                () -> assertEquals(55, fib),
                () -> assertEquals(expected, fib)
        );
    }

    @Test
    void testSumAllNrsBetween() {
        final var start = 0;
        final var end = 4;

        final var expected = IntStream.rangeClosed(start, end).sum();

        println(expected);

        final var sum = RecursiveSamples.sum(start, end);

        assertAll(
                () -> assertEquals(10, sum),
                () -> assertEquals(expected, sum)
        );
    }

    @Test
    void testStackOverflowThrowsStackOverflowError() {
        assertThrows(StackOverflowError.class, RecursiveSamples::stackOverflow);
    }

    @Test
    void testFastFourierTransform() {
        final var complexes = DoubleStream
                .iterate(0, d -> d + (Math.PI / 32))
                .map(Math::sin)
                .mapToObj(real -> new Complex(real, 0))
                .limit(8192)
                .toArray(Complex[]::new);

        final var fastFourierTransform = RecursiveSamples.fastFourierTransform(complexes);

        final var largerValueCount = Arrays.stream(fastFourierTransform)
                .mapToDouble(Complex::abs)
                .filter(absValue -> absValue > 1)
                .peek(It::println)
                .count();

        assertAll(
                () -> assertEquals(complexes.length, fastFourierTransform.length),
                () -> assertEquals(2, largerValueCount)
        );
    }

    @ParameterizedTest
    @MethodSource("defaultCommonDivisorParams")
    void testGreatestCommonDivisor(long first, long second, long expected) {
        final var gcd = RecursiveSamples.gcdByEuclidesAlgorithm(first, second);
        assertEquals(expected, gcd);
    }

    @ParameterizedTest
    @MethodSource("defaultCommonDivisorParams")
    void testGreatestIntCommonDivisor(int first, int second, int expected) {
        final var gcd = RecursiveSamples.gcdByEuclidesAlgorithm(first, second);
        assertEquals(expected, gcd);
    }

    static Sequence<Arguments> defaultCommonDivisorParams() {
        return Sequence.of(
                arguments(2, 4, 2),
                arguments(12, 36, 12),
                arguments(35, 12, 1),
                arguments(-5, 5, 5)
        );
    }

    @ParameterizedTest
    @MethodSource("bigIntCommonDivisorParams")
    void testGreatestCommonDivisorBigInt(
            @ConvertWith(ToBigIntegerConverter.class) BigInteger first,
            @ConvertWith(ToBigIntegerConverter.class) BigInteger second,
            @ConvertWith(ToBigIntegerConverter.class) BigInteger expected) {
        final var gcd = RecursiveSamples.gcdByEuclidesAlgorithm(first, second);
        assertEquals(expected, gcd);
    }

    static class ToBigIntegerConverter implements ArgumentConverter {

        @Override
        public Object convert(Object source, ParameterContext context) {
            return source instanceof Integer integer ? BigInteger.valueOf(integer) : source;
        }
    }

    static Sequence<Arguments> bigIntCommonDivisorParams() {
        return defaultCommonDivisorParams()
                .plus(arguments(new BigInteger("31940434634990099905"), new BigInteger("51680708854858323072"), 1));
    }

    @Nested
    class TailAndHeadRecursionTests {
        @Test
        void testHeadRecursion() {
            final var integers = RecursiveSamples.headRecursionWithReturn(10);
            assertEquals(IntList.of(10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0), integers);
        }

        @Test
        void testTailRecursion() {
            final var integers = RecursiveSamples.tailRecursionWithReturn(10);
            assertEquals(IntList.of(10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0), integers);
        }

        @Test
        void testMysteryHead() {
            IntMutableList list = IntMutableList.empty();
            RecursiveSamples.mysteryHead(4, list);
            assertEquals(IntList.of(0, 1, 2, 3, 4), list);
        }

        @Test
        void testMysteryTail() {
            IntMutableList list = IntMutableList.empty();
            RecursiveSamples.mysteryTail(4, list);
            assertEquals(IntList.of(4, 3, 2, 1, 0), list);
        }

        @Test
        void testFactorialRecursive() {
            final var factorial1 = RecursiveSamples.factorial(5);
            final var factorial2 = RecursiveSamples.factorialTailRecOptimization(5);

            System.out.println("factorial = " + factorial1);

            assertEquals(factorial1 ,factorial2);
        }

        @Test
        void testTailRecursionStackOverflowInJava() {
            assertThrows(StackOverflowError.class, () -> RecursiveSamples.factorial(50_000));
        }

        @Test
        void testNoStackOverflowWithKotlinTailCallOptimizationOrLoopVersionInJava() {
            final var factorial = RecursiveSamplesKt.factorial(50_000);
            final var factorialFromJavaLoop = RecursiveSamples.factorialTailRecOptimization(50_000);
            assertEquals(factorial, factorialFromJavaLoop);
        }

        /**
         * In mathematics, the Dottie number is a constant that is the unique real root of the equation: cos(x) = x
         */
        @Test
        void testCalculateFixedPointUsingTailRecursion() {
            final var fixPoint = RecursiveSamplesKt.findFixPoint();
            final var dottieNumberApproximation = 0.7390851332151611;
            assertEquals(dottieNumberApproximation, fixPoint);
        }
    }
}
