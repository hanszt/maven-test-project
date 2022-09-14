package org.hzt;

import org.apache.commons.math3.complex.Complex;
import org.hzt.utils.It;
import org.hzt.utils.strings.StringX;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import static org.hzt.utils.It.println;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    @ValueSource(strings = {
            "2, 4 -> 2",
            "12, 36 -> 12",
            "35, 12 -> 1",
            "-5, 5 -> 5"})
    void testGreatestCommonDivisor(String input) {
        final var strings = StringX.of(input).split(", ", " -> ");
        final var first = Long.parseLong(strings.get(0));
        final var second = Long.parseLong(strings.get(1));
        final var gcd = RecursiveSamples.gcdByEuclidsAlgorithm(first, second);

        final var expected = Long.parseLong(strings.get(2));

        assertEquals(expected, gcd);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "2, 4 -> 2",
            "12, 36 -> 12",
            "35, 12 -> 1",
            "-5, 5 -> 5"})
    void testGreatestIntCommonDivisor(String input) {
        final var strings = StringX.of(input).split(", ", " -> ");
        final var first = Integer.parseInt(strings.get(0));
        final var second = Integer.parseInt(strings.get(1));
        final var gcd = RecursiveSamples.gcdByEuclidsAlgorithm(first, second);

        final var expected = Integer.parseInt(strings.get(2));

        assertEquals(expected, gcd);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "2, 4 -> 2",
            "12, 36 -> 12",
            "35, 12 -> 1",
            "-5, 5 -> 5",
            "31940434634990099905, 51680708854858323072 -> 1"})
    void testGreatestCommonDivisorBigInt(String input) {
        final var strings = StringX.of(input).split(", ", " -> ");
        final var first = new BigInteger(strings.get(0));
        final var second = new BigInteger(strings.get(1));
        final var gcd = RecursiveSamples.gcdByEuclidsAlgorithm(first, second);

        final var expected = new BigInteger(strings.get(2));

        assertEquals(expected, gcd);
    }
}
