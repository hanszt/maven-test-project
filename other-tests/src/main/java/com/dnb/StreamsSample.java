package com.dnb;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.List;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

final class StreamsSample {

    private StreamsSample() {
    }

    public static Stream<Integer> returnListFromIterator(Iterator<Integer> iterator) {
        Iterable<Integer> iterable = () -> iterator;
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    public static List<BigInteger> getFibonacci(int length) {
        return getFibonacciStream(length, BigInteger.ZERO).toList();
    }

    public static BigInteger getNthFibonacciNumber(int n) {
        return getFibonacciStream(n, BigInteger.ONE)
                .reduce((first, second) -> second).orElseThrow();
    }

    public static BigInteger getSumFibonacciNumbers(int n) {
        return getFibonacciStream(n, BigInteger.ONE)
                .reduce(BigInteger.ZERO, BigInteger::add);
    }

    private static Stream<BigInteger> getFibonacciStream(int length, BigInteger n1) {
        return Stream.iterate(new BigInteger[]{n1, BigInteger.ONE},
                bigIntegers -> new BigInteger[]{bigIntegers[1], bigIntegers[0].add(bigIntegers[1])})
                .limit(length)
                .map(bigIntegers -> bigIntegers[0]);
    }

    //leibniz formula for PI: PI/4 = 1 - 1/3 + 1/5 - 1/7......
    //converges very slowly
    public static BigDecimal calculatePi(long nrOfIterations) {
        return LongStream.rangeClosed(0, nrOfIterations)
                .parallel()
                .map(StreamsSample::nextInLeibnizSeq)
                .mapToObj(nr -> BigDecimal.ONE.divide(BigDecimal.valueOf(nr), 10, RoundingMode.HALF_UP))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .multiply(BigDecimal.valueOf(4));
    }

    //leibniz formula for PI: PI/4 = 1 - 1/3 + 1/5 - 1/7......
    public static double calculatePiAsDouble(long nrOfIterations, boolean parallel) {
        final var longStreamHead = LongStream.rangeClosed(0, nrOfIterations);
        return (parallel ? longStreamHead.parallel() : longStreamHead)
                .map(StreamsSample::nextInLeibnizSeq)
                .mapToDouble(nr -> 1. / nr)
                .sum() * 4;
    }

    private static long nextInLeibnizSeq(long nr) {
        final var pow = Math.pow(-1, nr);
        final var value = nr * 2 + 1;
        return (long) (pow * value);
    }
}
