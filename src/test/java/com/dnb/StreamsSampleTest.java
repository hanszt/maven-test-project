package com.dnb;

import com.dnb.model.Data;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
                .collect(groupingBy(Data::getAmount, mapping(Data::getId, toSet())));
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
        List<Integer> list  = IntStream.range(0, 100).boxed().collect(Collectors.toList());
        final var limitedList = list.stream().limit(10).collect(Collectors.toList());
        assertEquals(10, limitedList.size());
    }

}
