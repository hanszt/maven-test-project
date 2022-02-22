package org.hzt;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SpliteratorTest {

    @Test
    void testEstimateSize() {
        List<Integer> list = List.of(1, 2, 3, 4, 5);

        final var estimateSize = list.spliterator().estimateSize();

        assertEquals(5, estimateSize);
    }
}
