package com.dnb.collectors_samples;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class CollectorSamplesTest {

    private final CollectorSamples collectorSamples = new CollectorSamples();

    @Test
    void getExpectedWhenOnlyOneInList() {
        var expected = new CashBalance("abn", true);
        var items = List.of(
                expected,
                new CashBalance("ing", false),
                new CashBalance("triodos", false));
        var actual = collectorSamples.collectingAndThenToFirstElementIfSizeOne(items).orElse(null);
        items.forEach(System.out::println);
        assertEquals(expected, actual);
    }

    @Test
    void getNullWhenMoreThanOneInList() {
        var expected = new CashBalance("abn", true);
        var actual = collectorSamples.collectingAndThenToFirstElementIfSizeOne(List.of(
                expected,
                new CashBalance("ing", true),
                new CashBalance("triodos", false))).orElse(null);
        assertNull(actual);
    }

    @Test
    void getNullWhenEmptyList() {
        var actual = collectorSamples.collectingAndThenToFirstElementIfSizeOne(Collections.emptyList()).orElse(null);
        assertNull(actual);
    }

    @Test
    void reduceGetExpectedWhenOnlyOneInList() {
        var expected = new CashBalance("abn", true);
        var actual = collectorSamples.reduce(List.of(
                expected,
                new CashBalance("ing", false),
                new CashBalance("triodos", false)));
        assertEquals(expected, actual);
    }

    @Test
    void reduceThrowsIllegalStateExceptionWhenMoreThanOneInList() {
        var list = List.of(
                new CashBalance("abn", true),
                new CashBalance("ing", true),
                new CashBalance("triodos", false));
        assertThrows(CollectorSamples.MoreThanOneElementException.class, () -> collectorSamples.reduce(list));
    }

    @Test
    void reduceGetNullWhenEmptyList() {
        assertNull(collectorSamples.reduce(Collections.emptyList()));
    }

    @Test
    void reduceGetNullWhenIsOpeningAllFalse() {
        var list = List.of(
                new CashBalance("abn", false),
                new CashBalance("ing", false),
                new CashBalance("triodos", false));
        assertNull(collectorSamples.reduce(list));
    }

    @Test
    void givenAListOfBigDecimals_calculateTheCorrectAverage() {
        var average = new BigDecimal("2000.00");
        var list = List.of(new BigDecimal("1000"), new BigDecimal("2000"), new BigDecimal("3000"));
        //act
        var actual = CollectorSamples.getBigDecimalAverage(list);
        assertEquals(average, actual);
    }

    @Test
    void givenAListOfBigDecimals_calculateBigDecimalSummaryStatistics() {
        var average = new BigDecimal("2000.00");
        final var min = new BigDecimal("1000");
        final var max = new BigDecimal("3000");
        var list = List.of(new BigDecimal("2000"), min, new BigDecimal("1500"), max, new BigDecimal("2500"));
        //act
        var summaryStatistics = CollectorSamples.getBigDecimalSummaryStatistics(list);
        assertEquals(average, summaryStatistics.getAverage());
        assertEquals(min, summaryStatistics.getMin());
        assertEquals(max, summaryStatistics.getMax());
        assertEquals(list.size(), summaryStatistics.getCount().intValue());
        assertEquals(new BigDecimal("10000"), summaryStatistics.getSum());
    }
}