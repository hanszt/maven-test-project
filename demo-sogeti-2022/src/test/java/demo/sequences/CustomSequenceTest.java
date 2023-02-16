package demo.sequences;

import demo.IndexedValue;
import demo.It;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.stream.LongStream;

import static demo.It.println;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

class CustomSequenceTest {

    private static final DecimalFormat RATIO_FORMAT = new DecimalFormat("0.00000000");
    /**
     * @see <a href= "https://docs.oracle.com/javase/7/docs/api/java/text/DecimalFormat.html">DecimalFormat</a>
     */
    private static final DecimalFormat ERROR_FORMAT = new DecimalFormat("0.000E0");

    @Nested
    class FibonacciAndGoldenRatioTests {

        private static final Sequence<BigInteger> fibonacciSequence = Sequence
                .iterate(new Pair(BigInteger.ZERO, BigInteger.ONE), Pair::next)
                .map(Pair::first);

        private record Pair(BigInteger first, BigInteger second) {
            Pair next() {
                return new Pair(second, first.add(second));
            }
        }

        private static final int PRECISION = 1_000;
        private static final BigDecimal GOLDEN_RATIO =
                (BigDecimal.ONE.add(BigDecimal.valueOf(5)
                                .sqrt(new MathContext(PRECISION, RoundingMode.HALF_UP)))
                        .multiply(BigDecimal.valueOf(0.5)));


        @TestFactory
        Sequence<DynamicTest> testConsecutiveFibNrRatiosConvergeToGoldenRatio() {
            return fibonacciSequence
                    .skip(1) // skip first element to avoid division by zero
                    .onEach(It::println)
                    .map(BigDecimal::new)
                    .zipWithNext((cur, next) -> next.divide(cur, PRECISION, RoundingMode.HALF_UP))
                    .mapIndexed(this::fibRatioApproximatesGoldenRatio)
                    .take(200)
                    .skip(12)
                    ;
        }

        private DynamicTest fibRatioApproximatesGoldenRatio(int index, BigDecimal ratio) {
            final var error = GOLDEN_RATIO.subtract(ratio).abs();

            final var displayName = "Ratio " + (index + 1) + ": " + RATIO_FORMAT.format(ratio) +
                    " approximates golden ratio with error " + ERROR_FORMAT.format(error);

            final var ratioAsDouble = ratio.doubleValue();
            final var golderRatio = GOLDEN_RATIO.doubleValue();
            final var margin = 1e-5;
            return dynamicTest(displayName, () -> assertEquals(golderRatio, ratioAsDouble, margin));
        }
    }

    @Nested
    class TribonacciAndRauzyRatioTests {

        private static final Sequence<BigInteger> tribonacciSequence = Sequence
                .iterate(new Triple(BigInteger.ZERO, BigInteger.ONE, BigInteger.ONE), Triple::next)
                .map(Triple::first);

        private record Triple(BigInteger first, BigInteger second, BigInteger third) {
            Triple next() {
                return new Triple(second, third, first.add(second).add(third));
            }
        }

        private static final int PRECISION = 1_000;
        /**
         * with k solution of k^3+k^2+k-1=0
         *
         * @see <a href="https://en.wikipedia.org/wiki/Rauzy_fractal">Rauzy fractal</a>
         */
        private static final double RAUZY_FRACTAL_RATIO;

        static {
            final var a = 17 + 3 * Math.sqrt(33);
            RAUZY_FRACTAL_RATIO = 3 / (-1 - (2 / Math.cbrt(a)) + Math.cbrt(a));
        }

        /**
         * @see <a href= "https://docs.oracle.com/javase/7/docs/api/java/text/DecimalFormat.html">DecimalFormat</a>
         */
        private static final DecimalFormat ERROR_FORMAT = new DecimalFormat("0.000E0");


        @TestFactory
        Sequence<DynamicTest> testConsecutiveNrRatiosConvergeToRauzyRatio() {
            return tribonacciSequence
                    .skip(1) // skip first element to avoid division by zero
                    .onEach(It::println)
                    .map(BigDecimal::new)
                    .zipWithNext((cur, next) -> next.divide(cur, PRECISION, RoundingMode.HALF_UP))
                    .mapIndexed(this::ratioApproximatesRauzyRatio)
                    .take(200)
                    .skip(13)
                    ;
        }

        private DynamicTest ratioApproximatesRauzyRatio(int index, BigDecimal ratio) {
            final var rauzyRatio = BigDecimal.valueOf(RAUZY_FRACTAL_RATIO);
            final var error = rauzyRatio.subtract(ratio).abs();

            final var displayName = "Ratio " + (index + 1) + ": " + RATIO_FORMAT.format(ratio) +
                    " approximates golden ratio with error " + ERROR_FORMAT.format(error);

            final var ratioAsDouble = ratio.doubleValue();
            final var golderRatio = rauzyRatio.doubleValue();
            final var margin = 1e-5;
            return dynamicTest(displayName, () -> assertEquals(golderRatio, ratioAsDouble, margin));
        }
    }

    @Nested
    class MaclaurinExpansionForETest {

        private static final int PRECISION = 1_000;
        /**
         * @see <a href="https://blogs.ubc.ca/infiniteseriesmodule/units/unit-3-power-series/taylor-series/maclaurin-expansion-of-ex/">
         * Maclaurin Expansion of e^x</a>
         */
        private static final Sequence<BigDecimal> macLaurinSequenceXIs1 = Sequence
                .iterate(0, n -> n + 1)
                .map(MaclaurinExpansionForETest::factorial)
                .map(BigDecimal::new)
                .map(factorial -> BigDecimal.ONE.divide(factorial, PRECISION, RoundingMode.HALF_UP));

        private static BigInteger factorial(long n) {
            return LongStream.rangeClosed(2, n)
                    .mapToObj(BigInteger::valueOf)
                    .reduce(BigInteger.ONE, BigInteger::multiply);
        }

        @Test
        void testConvertToE() {
            final var e = macLaurinSequenceXIs1
                    .take(15)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            System.out.println("e = " + RATIO_FORMAT.format(e));

            assertEquals(Math.E, e.doubleValue(), 1e-10);
        }
    }

    @Nested
    class FizzBuzzerTests {

        @Test
        void testFizzBuzzer() {
            final FizzBuzzer fizzBuzzer = FizzBuzzer
                    .start()
                    .fizz()
                    .buzz();

            final long count = fizzBuzzer
                    .take(100)
                    .filter(s -> s.contains("buzz"))
                    .count();

            final String actual = fizzBuzzer
                    .take(16)
                    .skip(3)
                    .joinToString(", ");

            for (String s : fizzBuzzer.take(3)) {
                println("s = " + s);
            }
            assertAll(
                    () -> assertEquals("4, buzz, fizz, 7, 8, fizz, buzz, 11, fizz, 13, 14, fizzbuzz, 16", actual),
                    () -> assertEquals(20L, count)
            );
        }

        @TestFactory
        Sequence<DynamicTest> testFizzBuzzerContainsBuzzAtMultipleOf5() {
            return FizzBuzzer.start()
                    .fizz()
                    .buzz()
                    .withIndex()
                    .onEach(It::println)
                    .filter(value -> (value.index() + 1) % 5 == 0)
                    .map(this::everyFifthContainsBuzz)
                    .take(100);
        }

        private DynamicTest everyFifthContainsBuzz(IndexedValue<String> indexedValue) {
            final int n = indexedValue.index() + 1;
            String name = "Value at n=" + n + " contains buzz";
            return dynamicTest(name, () -> assertTrue(indexedValue.value().contains("buzz")));
        }

        @TestFactory
        Sequence<DynamicTest> testFizzBuzzerContainsFizzAtMultipleOf3() {
            return FizzBuzzer.start()
                    .fizz()
                    .buzz()
                    .withIndex()
                    .onEach(It::println)
                    .filter(value -> (value.index() + 1) % 3 == 0)
                    .map(this::everyThirdContainsFizz)
                    .take(100);
        }

        private DynamicTest everyThirdContainsFizz(IndexedValue<String> indexedValue) {
            final int n = indexedValue.index() + 1;
            String name = "Value at n=" + n + " contains fizz";
            return dynamicTest(name, () -> assertTrue(indexedValue.value().contains("fizz")));
        }
    }

    @FunctionalInterface
    private interface FizzBuzzer extends Sequence<String> {

        private static boolean isNaturalNr(String current) {
            return current.chars().allMatch(Character::isDigit);
        }

        private static String next(int index, String current, int modulo, String string) {
            return next(index, current, modulo, 0, string);
        }

        private static String next(int index, String current, int modulo, int offSet, String string) {
            int value = index + 1;
            final boolean isNaturalNr = isNaturalNr(current);
            final boolean match = value % modulo == offSet;
            if (isNaturalNr) {
                return match ? string : String.valueOf(value);
            }
            return match ? current + string : current;
        }

        static FizzBuzzer start() {
            return Sequence.iterate(1, n -> n + 1).map(String::valueOf)::iterator;
        }

        default FizzBuzzer fizz() {
            return mapIndexed((index, value) -> next(index, value, 3, "fizz"))::iterator;
        }

        default FizzBuzzer buzz() {
            return mapIndexed((index, value) -> next(index, value, 5, "buzz"))::iterator;
        }

        default FizzBuzzer bizz() {
            return mapIndexed((index, value) -> next(index, value, 7, "bizz"))::iterator;
        }

        default FizzBuzzer even() {
            return mapIndexed((index, value) -> next(index, value, 2, "even"))::iterator;
        }

        default FizzBuzzer odd() {
            return mapIndexed((index, value) -> next(index, value, 2, 1, "odd"))::iterator;
        }

        static void main(String... args) {
            final FizzBuzzer fizzBuzzer = FizzBuzzer
                    .start()
                    .fizz()
                    .bizz()
                    .even()
                    .buzz()
                    .odd();

            final long count = fizzBuzzer
                    .take(100_000)
                    .filter(s -> s.equals("fizzbizzevenbuzz"))
                    .count();

            final String actual = fizzBuzzer
                    .take(16)
                    .skip(3)
                    .joinToString(", ");

            println(fizzBuzzer.take(2_000).joinToString());
            println("count = " + count);
            println("actual = " + actual);
        }
    }
}
