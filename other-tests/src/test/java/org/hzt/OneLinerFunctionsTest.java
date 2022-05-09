package org.hzt;

import org.junit.jupiter.api.Test;

import java.util.OptionalInt;
import java.util.function.IntBinaryOperator;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OneLinerFunctionsTest {

    private static final IntPredicate isEven = i -> i % 2 == 0;
    private static final IntBinaryOperator multiply = (a, b) -> a * b;

    @Test
    void testIsEven() {
        final var evenNrCount = IntStream.range(0, 1_000)
                .filter(isEven)
                .count();

        assertEquals(500, evenNrCount);
    }

    @Test
    void testMultiplyInMapOperation() {
        final var result = IntStream.range(0, 10)
                .map(i -> multiply.applyAsInt(i, 3))
                .map(i -> i * 3)
                .toArray();

        int[] expected = {0, 9, 18, 27, 36, 45, 54, 63, 72, 81};

        assertArrayEquals(expected, result);
    }

    @Test
    void testMultiplyInReduceOperation() {
        final var result = IntStream.range(1, 10).reduce(multiply);

        assertEquals(OptionalInt.of(362880), result);
    }

}
