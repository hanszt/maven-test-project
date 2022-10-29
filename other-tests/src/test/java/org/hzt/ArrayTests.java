package org.hzt;

import org.hzt.utils.collections.BinarySearchable;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ArrayTests {

    @Test
    void testGivenNullArraysWhenAssertingArraysEqualityThenEqual() {
        int[] expected = null;
        int[] actual = null;

        //noinspection ConstantConditions
        assertArrayEquals(expected, actual);
    }

    @Test
    void testBinarySearch() {
        final var ints = IntStream.iterate(1, i -> i < Short.MAX_VALUE, i -> i * 2).toArray();
        System.out.println(Arrays.toString(ints));

        final var valueToSearch = 16;
        final var index = BinarySearchable.binarySearch(ints.length, 0, ints.length, mid -> ints[mid] - valueToSearch);
        final var expected = Arrays.binarySearch(ints, valueToSearch);

        assertAll(
                () -> assertEquals(4, index),
                () -> assertEquals(expected, index)
        );
    }
}
