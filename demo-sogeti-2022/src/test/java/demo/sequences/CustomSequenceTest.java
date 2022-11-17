package demo.sequences;

import demo.IndexedValue;
import demo.It;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import static demo.It.println;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

class CustomSequenceTest {

    static Sequence<BigInteger> fibonacciSequence() {
        record Pair(BigInteger first, BigInteger second) {
            Pair next() {
                return new Pair(second, first.add(second));
            }
        }
        return Sequence
                .iterate(new Pair(BigInteger.ZERO, BigInteger.ONE), Pair::next)
                .map(Pair::first);
    }

    @Nested
    class FibonacciAndGoldenRatioTests {

        private static final int SCALE = 15;

        @TestFactory
        Sequence<DynamicTest> testConsecutiveFibNrRatiosConvergeToGoldenRatio() {
            return fibonacciSequence()
                    .skipWhile(n -> n.equals(BigInteger.ZERO))
                    .onEach(It::println)
                    .map(BigDecimal::new)
                    .zipWithNext((cur, next) -> next.divide(cur, SCALE, RoundingMode.HALF_UP))
                    .mapIndexed(this::fibRatioApproximatesGoldenRatio)
                    .take(200)
                    .skip(12)
                    ;
        }

        private DynamicTest fibRatioApproximatesGoldenRatio(int index, BigDecimal ratio) {
            final double GOLDEN_RATIO = (1 + Math.sqrt(5)) / 2.0;
            final int ratioNr = index + 1;
            final var delta = 1e-5;
            final var displayName = "Ratio " + ratioNr + ": " + ratio + " approximates golden ratio with error " + delta;

            return dynamicTest(displayName, () -> assertEquals(GOLDEN_RATIO, ratio.doubleValue(), delta));
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

        static void main(String[] args) {
            final FizzBuzzer fizzBuzzer = FizzBuzzer
                    .start()
                    .fizz()
                    .bizz()
                    .even()
                    .buzz()
                    .odd()
            ;

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
