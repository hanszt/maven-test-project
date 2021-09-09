package com.dnb;

import com.dnb.model.Data;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StreamsSampleTest {

    private static final List<Data> list = Arrays.asList(
            new Data("1", BigDecimal.valueOf(1_000_000)),
            new Data("2", BigDecimal.valueOf(4_000_000)),
            new Data("3", BigDecimal.valueOf(2_000_000)),
            new Data("4", BigDecimal.valueOf(2_000_000)));

    @Test
    void testGettingSumUsingStramReduction() {
        BigDecimal sum = list.stream().map(Data::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        assertEquals(BigDecimal.valueOf(9_000_000), sum);
    }

    @Test
    void testGroupingByAndMapping() {
        var map = list.stream()
                .collect(groupingBy(Data::getAmount, mapping(Data::getId, toUnmodifiableSet())));
        map.forEach((key, value) -> System.out.println("key = " + key + ", value = " + value));
        assertEquals(3, map.size());
    }

    @Test
    void testCounting() {
        var summaryStatistics = list.stream().map(Data::getId).mapToInt(Integer::parseInt).summaryStatistics();
        assertEquals(2.5, summaryStatistics.getAverage());
    }

    @Test
    void testStreamLimit() {
        List<Integer> list  = getIntegerStream().collect(toUnmodifiableList());
        final var limitedList = list.stream().limit(10).collect(toUnmodifiableList());
        assertEquals(10, limitedList.size());
    }

    @Test
    void testReturnStreamFromIterator() {
        final var iterator = getIntegerStream().iterator();
        final var result = StreamsSample.returnListFromIterator(iterator)
                .map(String::valueOf)
                .collect(toUnmodifiableList());
        assertEquals(getIntegerStream().map(String::valueOf).collect(toUnmodifiableList()), result);
    }

    private Stream<Integer> getIntegerStream() {
        return IntStream.range(0, 100).boxed();
    }

    @Test
    void testFibonacciUsingStreams() {
        final var expected = Stream.of(0, 1, 1, 2, 3, 5, 8, 13, 21, 34)
                .map(BigInteger::valueOf)
                .collect(toUnmodifiableList());
        final var fibonacciList = StreamsSample.getFibonacci(10);
        assertEquals(expected, fibonacciList);
    }

    @Test
    void testNthFibonacciNrUsingStreams() {
        final var fibonacciList = StreamsSample.getNthFibonacciNumber(500);
        assertEquals(new BigDecimal(
                "139423224561697880139724382870407283950070256587697307264108962948325571622863290691557658876222521294125"
        ).toBigInteger(), fibonacciList);
    }

    @Test
    void testSumFibonacciNrUsingStreams() {
        final var fibonacciList = StreamsSample.getSumFibonacciNumbers(500);
        assertEquals(new BigDecimal(
                "365014740723634211012237077906479355996081581501455497852747829366800199361550174096573645929019489792750"
        ).toBigInteger(), fibonacciList);
    }

    @Test
    void testCalculatePiParallelUsingStreams() {
        Timer<BigDecimal> timer = Timer.timeAFunction(10_000_000, StreamsSample::calculatePi);
        final var result = timer.getResult();
        System.out.println("Milliseconds = " + timer.getTimeInMillis());
        assertEquals(new BigDecimal("3.1415927972"), result);
    }

    @Test
    void testCalculatePiParallelUsingDoubleStream() {
        final var iterations = 100_000_000;
        //act
        Timer<Double> sequentialTimer = Timer.timeAFunction(iterations,
                (long nrOfIterations) -> StreamsSample.calculatePiAsDouble(nrOfIterations, false));
        Timer<Double> parallelTimer = Timer.timeAFunction(iterations,
                (long nrOfIterations) -> StreamsSample.calculatePiAsDouble(nrOfIterations, true));

        final var seqTimeInMillis = sequentialTimer.getTimeInMillis();
        final var parallelTimeInMillis = parallelTimer.getTimeInMillis();
        System.out.println("Sequential in milliseconds = " + seqTimeInMillis);
        System.out.println("Parallel in milliseconds = " + parallelTimeInMillis);
        System.out.println("parallelTimer.getResult() = " + parallelTimer.getResult());
        //assert
        assertTrue(parallelTimeInMillis < seqTimeInMillis);
        assertEquals(parallelTimer.getResult(), sequentialTimer.getResult());
    }

}
