package org.hzt;

import org.hzt.iterators.WindowedIterator;
import org.hzt.iterators.ZipWithNextIterator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.LongPredicate;
import java.util.stream.DoubleStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Spliterator.IMMUTABLE;
import static java.util.Spliterator.ORDERED;

final class Streams {

    private Streams() {
    }

    public static Stream<Integer> returnStreamFromIterator(Iterator<Integer> iterator) {
        Iterable<Integer> iterable = () -> iterator;
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    public static Stream<BigInteger> fibonacciStream() {
        final BigInteger[] seed = {BigInteger.ZERO, BigInteger.ONE};
        return Stream.iterate(seed, fibPair -> new BigInteger[]{fibPair[1], fibPair[0].add(fibPair[1])})
                .map(fibPair -> fibPair[0]);
    }

    public static Stream<BigInteger> subtractingFibonacciStream() {
        final BigInteger[] seed = {BigInteger.ZERO, BigInteger.ONE.negate()};
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
                .iterate(new Pair(BigInteger.ZERO, BigInteger.ONE), Pair::next)
                .map(Pair::first);
    }

    /**
     * A stream implementation that is inspired by the sieve of eratosthenes
     * @see <a href="https://en.wikipedia.org/wiki/Sieve_of_Pritchard">Sieve of Pritchard</a>
     * @see <a href="https://stackoverflow.com/questions/43760641/java-8-streams-and-the-sieve-of-eratosthenes">Java 8: streams and the Sieve of Eratosthenes</a>
     * @return a LongStream of primes starting from 2
     */
    @SuppressWarnings("squid:S3864")
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
    public static Stream<BigDecimal> parallelLeibnizBdStream(long nrOfIterations) {
        return LongStream.rangeClosed(0, nrOfIterations)
                .parallel()
                .map(Streams::nextInLeibnizSeq)
                .mapToObj(nr -> BigDecimal.ONE.divide(BigDecimal.valueOf(nr), 10, RoundingMode.HALF_UP));
    }

    //leibniz formula for PI: PI/4 = 1 - 1/3 + 1/5 - 1/7......
    public static DoubleStream leibnizStream(long nrOfIterations) {
        return LongStream.rangeClosed(0, nrOfIterations)
                .map(Streams::nextInLeibnizSeq)
                .mapToDouble(nr -> 1. / nr);
    }

    public static DoubleStream parallelLeibnizStream(long nrOfIterations) {
        return LongStream.rangeClosed(0, nrOfIterations)
                .parallel()
                .map(Streams::nextInLeibnizSeq)
                .mapToDouble(nr -> 1. / nr);
    }

    private static long nextInLeibnizSeq(long nr) {
        final var pow = Math.pow(-1, nr);
        final var value = nr * 2 + 1;
        return (long) (pow * value);
    }

    public static Stream<BigInteger> collatzStream(BigInteger initValue) {
        final var three = BigInteger.valueOf(3);
        return Stream.iterate(initValue,
                nr -> nr.mod(BigInteger.TWO).equals(BigInteger.ZERO) ?
                        nr.divide(BigInteger.TWO) :
                        nr.multiply(three).add(BigInteger.ONE));
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
        return StreamSupport.stream(() -> Spliterators.spliteratorUnknownSize(zipWithNextIterator, ORDERED), ORDERED,  false);
    }

    public static <T> Stream<List<T>> windowed(Iterable<T> iterable, int size, int step, boolean partialWindows) {
        final var windowedIterator = WindowedIterator.of(iterable.iterator(), size, i -> size, step, i -> step, partialWindows);
        return StreamSupport.stream(() -> Spliterators.spliteratorUnknownSize(windowedIterator, ORDERED), ORDERED,  false);
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

}
