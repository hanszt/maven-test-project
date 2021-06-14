package com.dnb.collectors_samples;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

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
        var actual = collectorSamples.collectingAndThen(items);
        items.forEach(System.out::println);
        assertEquals(expected, actual);
    }

    @Test
    void getNullWhenMoreThanOneInList() {
        var expected = new CashBalance("abn", true);
        var actual = collectorSamples.collectingAndThen(List.of(
                expected,
                new CashBalance("ing", true),
                new CashBalance("triodos", false)));
        assertNull(actual);
    }

    @Test
    void getNullWhenEmptyList() {
        var actual = collectorSamples.collectingAndThen(Collections.emptyList());
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
}