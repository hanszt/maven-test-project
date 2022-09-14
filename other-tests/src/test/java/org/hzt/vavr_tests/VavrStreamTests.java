package org.hzt.vavr_tests;

import io.vavr.collection.List;
import io.vavr.collection.Stream;
import org.junit.jupiter.api.Test;

import static org.hzt.utils.It.println;
import static org.junit.jupiter.api.Assertions.assertEquals;

class VavrStreamTests {

    @Test
    void testStreamMap() {
        var result = Stream.iterate(0, i -> ++i)
                .take(12)
                .map(String::valueOf)
                .reject(s -> s.contains("0"))
                .map(Integer::parseInt)
                .toList();

        println("result = " + result);

        assertEquals(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 11), result);
    }

    @Test
    void testStreamScan() {
        var result = Stream.iterate(0, i -> i + 2)
                .take(10)
                .scan(10, (acc, next) -> acc - next)
                .toList();

        println("result = " + result);

        assertEquals(List.of(10, 10, 8, 4, -2, -10, -20, -32, -46, -62, -80), result);
    }
}
