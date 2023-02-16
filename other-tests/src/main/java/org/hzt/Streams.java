package org.hzt;

import org.apache.commons.math3.fraction.BigFraction;
import org.apache.commons.math3.fraction.Fraction;
import org.hzt.iterators.WindowedIterator;
import org.hzt.iterators.ZipWithNextIterator;
import org.hzt.utils.It;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.LongPredicate;
import java.util.regex.Pattern;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.math.BigInteger.*;
import static java.util.Spliterator.*;
import static java.util.function.Predicate.not;
import static org.hzt.utils.It.*;

public final class Streams {

    private Streams() {
    }

    public static Stream<Integer> streamFromIterator(Iterator<Integer> iterator) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, 0), false);
    }

    public static Stream<BigInteger> fibonacciStream() {
        final BigInteger[] seed = {ZERO, ONE};
        return Stream.iterate(seed, fibPair -> new BigInteger[]{fibPair[1], fibPair[0].add(fibPair[1])})
                .map(fibPair -> fibPair[0]);
    }

    public static Stream<BigInteger> subtractingFibonacciStream() {
        final BigInteger[] seed = {ZERO, ONE.negate()};
        return Stream.iterate(seed, fibPair -> new BigInteger[]{fibPair[1], fibPair[0].subtract(fibPair[1])})
                .map(fibPair -> fibPair[0]);
    }

    public static Stream<BigInteger> fibonacciStreamV2() {
        record Pair(BigInteger first, BigInteger second) {
            Pair next() {
                return new Pair(second, first.add(second));
            }

        }
        return Stream
                .iterate(new Pair(ZERO, ONE), Pair::next)
                .map(Pair::first);
    }

    public static LongStream fibonacciLongStream() {
        final long[] seed = {0L, 1L};
        return Stream.iterate(seed, pair -> new long[]{pair[1], pair[0] + pair[1]})
                .mapToLong(p -> p[0]);
    }

    /**
     * A stream implementation that is inspired by the sieve of eratosthenes
     *
     * @return a LongStream of primes starting from 2
     * @see <a href="https://en.wikipedia.org/wiki/Sieve_of_Pritchard">Sieve of Pritchard</a>
     * @see <a href="https://stackoverflow.com/questions/43760641/java-8-streams-and-the-sieve-of-eratosthenes">Java 8: streams and the Sieve of Eratosthenes</a>
     */
    public static LongStream primes() {
        return LongStream
                .iterate(2, l -> ++l)
                .filter(isPrime(new AtomicReference<>(x -> true)));
    }

    private static LongPredicate isPrime(final AtomicReference<LongPredicate> isPrimeRef) {
        return l -> {
            final var longPredicate = isPrimeRef.get();
            isPrimeRef.set(longPredicate.and(v -> v % l != 0));
            return longPredicate.test(l);
        };
    }

    //leibniz formula for PI: PI/4 = 1 - 1/3 + 1/5 - 1/7......
    //converges very slowly
    public static Stream<BigDecimal> leibnizBdStream(long nrOfIterations) {
        final var four = BigDecimal.valueOf(4);
        return LongStream.rangeClosed(0, nrOfIterations)
                .map(Streams::nextInLeibnizSeq)
                .mapToObj(nr -> four.divide(BigDecimal.valueOf(nr), 10, RoundingMode.HALF_UP));
    }

    //leibniz formula for PI: PI/4 = 1 - 1/3 + 1/5 - 1/7......
    public static Stream<Fraction> leibnizFractionStream(int nrOfIterations) {
        return IntStream.rangeClosed(0, nrOfIterations)
                .map(Streams::nextInLeibnizSeq)
                .mapToObj(nr -> new Fraction(4, nr));
    }

    //leibniz formula for PI: PI/4 = 1 - 1/3 + 1/5 - 1/7......
    public static Stream<BigFraction> leibnizBigFractionStream(int nrOfIterations) {
        return IntStream.rangeClosed(0, nrOfIterations)
                .map(Streams::nextInLeibnizSeq)
                .mapToObj(nr -> new BigFraction(4, nr));
    }

    //leibniz formula for PI: PI/4 = 1 - 1/3 + 1/5 - 1/7......
    public static DoubleStream leibnizStream(long nrOfIterations) {
        return LongStream.rangeClosed(0, nrOfIterations)
                .map(Streams::nextInLeibnizSeq)
                .mapToDouble(nr -> 4. / nr);
    }

    private static long nextInLeibnizSeq(long nr) {
        final var pow = Math.pow(-1, nr);
        final var value = nr * 2 + 1;
        return (long) (pow * value);
    }

    private static int nextInLeibnizSeq(int nr) {
        final var pow = Math.pow(-1, nr);
        final var value = nr * 2 + 1;
        return (int) (pow * value);
    }

    public static Stream<BigInteger> collatzStream(BigInteger initValue) {
        final var three = valueOf(3);
        return Stream
                .iterate(initValue, nr -> nr.mod(TWO).equals(ZERO) ? nr.divide(TWO) : nr.multiply(three).add(ONE));
    }

    /**
     * @param initValue the starting value of a stream following the rules of the Collatz conjecture
     * @return A stream following the collatz sequence
     * @see <a href="https://www.youtube.com/watch?v=094y1Z2wpJg">The Simplest Math Problem No One Can Solve - Collatz Conjecture</a>
     */
    public static LongStream collatzStream(long initValue) {
        return LongStream.iterate(initValue, nr -> nr % 2 == 0 ? (nr / 2) : (nr * 3 + 1L));
    }

    public static <T> Stream<T> empty() {
        return StreamSupport.stream(new Spliterators.AbstractSpliterator<>(0L, IMMUTABLE) {
            @Override
            public boolean tryAdvance(Consumer<? super T> action) {
                return false;
            }
        }, false);
    }

    public static <T, R> Stream<R> zipWithNext(Iterable<T> iterable, BiFunction<T, T, R> merger) {
        final var zipWithNextIterator = new ZipWithNextIterator<>(iterable.iterator(), merger);
        return StreamSupport.stream(() -> Spliterators.spliteratorUnknownSize(zipWithNextIterator, ORDERED), ORDERED, false);
    }

    public static <T> Stream<List<T>> windowed(Iterable<T> iterable, int size, int step, boolean partialWindows) {
        final var windowedIterator = WindowedIterator.of(iterable.iterator(), size, i -> size, step, i -> step, partialWindows);
        return StreamSupport.stream(() -> Spliterators.spliteratorUnknownSize(windowedIterator, ORDERED), ORDERED, false);
    }

    public static <T> Stream<List<T>> windowed(Iterable<T> iterable, int size, int step) {
        return windowed(iterable, size, step, true);
    }

    public static <T> Stream<List<T>> windowed(Iterable<T> iterable, int size, boolean partialWindows) {
        return windowed(iterable, size, 1, partialWindows);
    }

    public static <T> Stream<List<T>> windowed(Iterable<T> iterable, int size) {
        return windowed(iterable, size, 1);
    }

    public static Stream<String> consoleLines(String stopKey) {
        final var scanner = new Scanner(System.in);
        final Spliterator<String> spliterator = new Spliterators.AbstractSpliterator<>(Integer.MAX_VALUE, Spliterator.ORDERED) {

            @Override
            public boolean tryAdvance(Consumer<? super String> action) {
                final var nextLine = scanner.nextLine();
                final var shouldAcceptNextLine = !stopKey.equalsIgnoreCase(nextLine);
                if (shouldAcceptNextLine) {
                    action.accept(nextLine);
                }
                return shouldAcceptNextLine;
            }
        };
        return StreamSupport.stream(() -> spliterator, ORDERED, false);
    }

    public static Stream<String> consoleLines(Pattern stopKey) {
        final var scanner = new Scanner(System.in);
        final Iterator<String> iterator = new Iterator<>() {

            private boolean shouldReturnNextLine = true;
            private String nextLine = null;

            @Override
            public boolean hasNext() {
                nextLine = scanner.nextLine();
                shouldReturnNextLine = !stopKey.matcher(nextLine).matches();
                return shouldReturnNextLine;
            }

            @Override
            public String next() {
                if (shouldReturnNextLine) {
                    return nextLine;
                }
                throw new NoSuchElementException("No next line found");
            }
        };
        return StreamSupport.stream(() -> Spliterators.spliteratorUnknownSize(iterator, ORDERED), ORDERED, false);
    }

    public static void main(String... args) {
        final var scanner = new Scanner(System.in);
        final var maxNrOfMultiLines = 8;

        println("Enter the lines from which the length will be echoed back: (Enter 'stop echo' to finish)");
        consoleLines("stop echo")
                .limit(maxNrOfMultiLines)
                .mapToInt(String::length)
                .forEach(It::println);

        println("Enter the text you want to be processed as batch: (Enter 'stop batch' to finish)");
        final var stop_batch = "stop batch";
        consoleLines(stop_batch)
                .limit(maxNrOfMultiLines)
                .onClose(() -> printf("Max length (%s) reached or '%s' called", maxNrOfMultiLines, stop_batch))
                .toList()
                .forEach(It::println);

        println("Enter a single line text");
        println(scanner.next());

        It.println("Enter ten inputs:");
        final var firstTenTokens = scanner.tokens()
                .takeWhile(not("stop"::equals))
                .limit(10)
                .toList();

        println("firstTenTokens = " + firstTenTokens);
    }
}
