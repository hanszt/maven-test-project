package org.hzt;

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
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.IntStream;

import static java.util.function.Predicate.not;
import static org.hzt.RecursiveSamples.Aoc2020Sampe.MAX_STEP_APART;
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

        //1, 1, 2, 3, 5, 8, 13, 21, 34, 55...
        assertAll(
                () -> assertEquals(55, fib),
                () -> assertEquals(expected, fib),
                () -> assertEquals(55, fibonacciBinet(n))
        );
    }

    public static double fibonacciBinet(int n) {
        double goldenRatio = (1 + Math.sqrt(5)) / 2;
        return Math.round(Math.pow(goldenRatio, n) / Math.sqrt(5));
    }

    @Test
    void testBigIntFib() {
        final var n = 30;

        final var expected = Streams.fibonacciStream()
                .skip(n)
                .findFirst()
                .orElseThrow();

        final var actual = RecursiveSamples.bigIntFib(n);

        assertEquals(expected, actual);
    }

    @Test
    void testFibWithCache() {
        final var n = 5_000;

        final var expected = Streams.fibonacciStream()
                .skip(n)
                .findFirst()
                .orElseThrow();

        final var actual = RecursiveSamples.fibWithCash(n);

        assertEquals(expected, actual);
    }

    @Test
    void testMemoizedFib() {
        final var n = 1500;

        final var expected = Streams.fibonacciStream()
                .skip(n)
                .findFirst()
                .orElseThrow();

        final var actual = RecursiveSamples.memoizedFib(n);

        System.out.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Nested
    class Aoc2020SampeTests {

        @Test
        void testDay10Part2Aoc2020() {
            final var ints = IntStream.of(16, 10, 15, 5, 1, 11, 7, 19, 6, 12, 4).sorted().toArray();
            final var input = new int[ints.length + 2];
            final var firstValue = 0;
            final var lastValue = ints[ints.length - 1] + MAX_STEP_APART;
            System.arraycopy(ints, 0, input, 1, ints.length);
            input[0] = firstValue; // add socket jolt value
            input[input.length - 1] = lastValue; // add built in phone adaptor jolt value

            final var result = RecursiveSamples.Aoc2020Sampe.numberOfWaysToCompleteAdaptorChain(input);
            final var expected = RecursiveSamples.Aoc2020Sampe.numberOfWaysToCompleteAdaptorChainWithCache(input);

            assertAll(
                    () -> assertEquals(8L, result),
                    () -> assertEquals(result, expected)
            );
        }

        @Test
        void testDay10Part2Aoc2020WithCache() throws IOException {
            final var ints = sortedInput();
            final var input = new int[ints.length + 2];
            final var firstValue = 0;
            final var lastValue = ints[ints.length - 1] + MAX_STEP_APART;
            System.arraycopy(ints, 0, input, 1, ints.length);
            input[0] = firstValue; // add socket jolt value
            input[input.length - 1] = lastValue; // add built in phone adaptor jolt value
            final var result = RecursiveSamples.Aoc2020Sampe.numberOfWaysToCompleteAdaptorChainWithCache(input);

            assertEquals(99_214_346_656_768L, result);
        }

        private static int[] sortedInput() throws IOException {
            try (final var lines = Files.lines(Path.of("input/20201210-input-day10.txt"))) {
                return lines
                        .filter(not(String::isEmpty))
                        .mapToInt(Integer::parseInt)
                        .sorted()
                        .toArray();
            }
        }
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

    @ParameterizedTest(name = "the least common multiple of {0} and {1} should be {2}")
    @CsvSource({
            "12, 36, 36",
            "13, 17, 221",
            "5, 7, 35",
            "6, 4, 12",
            "24, 36, 72"}
    )
    void testLeastCommonMultiple(long nr1, long nr2, long expected) {
        final var lcm = RecursiveSamples.lcm(nr1, nr2);
        assertEquals(expected, lcm);
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

            assertEquals(factorial1, factorial2);
        }

        @Test
        void testTailRecursionStackOverflowInJava() {
            assertThrows(StackOverflowError.class, () -> RecursiveSamples.factorial(50_000));
        }

        @Test
        void testNoStackOverflowWithKotlinTailCallOptimizationOrLoopVersionInJava() {
            final var n = 10_000;

            final var factorial = RecursiveSamplesKt.factorial(n);
            final var factorialFromJavaLoop = RecursiveSamples.factorialTailRecOptimization(n);

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
