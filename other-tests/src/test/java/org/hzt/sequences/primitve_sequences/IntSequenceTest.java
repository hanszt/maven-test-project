package org.hzt.sequences.primitve_sequences;

import org.hzt.utils.numbers.IntX;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IntSequenceTest {

    @Test
    void tesIntSequenceMapToArray() {
        final var ints = IntSequence.of(1, 2, 3, 4, 5, 6)
                .map(i -> i * 2)
                .filter(i -> i % 4 == 0)
                .toArray();

        for (int i : ints) {
            System.out.println("i = " + i);
        }
        assertArrayEquals(new int[] {4, 8, 12}, ints);
    }

    @Test
    void testIntSequenceInForLoop() {
        for (int i : IntSequence.of(1, 2, 3, 4, 5).map(i -> i * 2)) {
            assertTrue(IntX.isEven(i));
        }
    }

    @Test
    void testIntSequenceFromListOfInteger() {
        List<Integer> list = List.of(1,2, 3, 4, 5);
        final var count = IntSequence.of(list).count();

        assertEquals(5, count);
    }

    @Test
    void tesIntSequenceFromIntStream() {
        final var count = IntSequence.of(1, 2, 3, 4 ,5, 6)
                .map(i -> i * 2)
                .filter(i -> i % 4 == 0)
                .count();

        assertEquals(3, count);
    }
}
