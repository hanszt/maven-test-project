package org.hzt;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class ArrayTests {

    @Test
    void testGivenNullArraysWhenAssertingArraysEqualityThenEqual() {
        int[] expected = null;
        int[] actual = null;

        //noinspection ConstantConditions
        assertArrayEquals(expected, actual);
    }
}
