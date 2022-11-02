package hzt.code_wars;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FindMinValueTest {

    @Test
    void testFindMinValue() {
        final int[][] input = {
                {1, 5, 3, 8, 5, -1, 5},
                {1, 5, 3, 8, 5, -1, 7},
                {1, 5, 3, 8, 5, -1, 9},
                {1, 5, 3, 8, 5, -1, 3},
                {1, 5, 3, 8, 5, -1, 6},
                {1, 5, 3, 8, 5, -1, 7},
        };
        final boolean[][] mask = {
                {true, true, true},
                {false, true, false},
                {true, true, true},
        };
        final int minSumOfMask = FindMinValue.findMinSumOfMask(input, mask);

        assertEquals(17, minSumOfMask);
    }

}
