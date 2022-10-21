package org.hzt;

import org.apache.commons.math3.complex.Complex;
import org.hzt.utils.It;
import org.hzt.utils.sequences.Sequence;
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
    @MethodSource("commonDivisorParams")
    void testGreatestCommonDivisor(long first, long second, long expected) {
        final var gcd = RecursiveSamples.gcdByEuclidesAlgorithm(first, second);
        assertEquals(expected, gcd);
    }

    @ParameterizedTest
    @MethodSource("commonDivisorParams")
    void testGreatestIntCommonDivisor(int first, int second, int expected) {
        final var gcd = RecursiveSamples.gcdByEuclidesAlgorithm(first, second);
        assertEquals(expected, gcd);
    }

    static Sequence<Arguments> commonDivisorParams() {
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
            @ConvertWith(BigIntegerConverter.class) BigInteger first,
            @ConvertWith(BigIntegerConverter.class) BigInteger second,
            @ConvertWith(BigIntegerConverter.class) BigInteger expected) {
        final var gcd = RecursiveSamples.gcdByEuclidesAlgorithm(first, second);
        assertEquals(expected, gcd);
    }

    static class BigIntegerConverter implements ArgumentConverter {

        @Override
        public Object convert(Object source, ParameterContext context) {
            return source instanceof Integer integer ? BigInteger.valueOf(integer) : source;
        }
    }

    static Sequence<Arguments> bigIntCommonDivisorParams() {
        return commonDivisorParams()
                .plus(arguments(
                        new BigInteger("31940434634990099905"),
                        new BigInteger("51680708854858323072"),
                        1));
    }
}
