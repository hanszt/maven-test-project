package org.navara;

import org.junit.jupiter.api.Test;
import org.navara.utils.MatrixGenerator;

import java.util.function.ToLongBiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Assignment3Test {

    private final ToLongBiFunction<Integer, int[][]> assignment3 = new Assignment3()::solution;

    @Test
    void testSolutionSmallMatrix() {
        final int[][] inputGrid = {{0, 1}, {0, 0}};

        final var solution = assignment3.applyAsLong(1, inputGrid);

        assertEquals(2, solution);
    }

    @Test
    void testSolutionMatrix2() {
        final int[][] inputGrid = {{0, 0, 0, 0}, {0, 0, 1, 0}, {1, 0, 0, 1}};

        final var solution = assignment3.applyAsLong(2, inputGrid);

        assertEquals(2, solution);
    }

    @Test
    void testSolutionMatrix3() {
        final int[][] inputGrid = {
                {0, 0, 0, 1},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {1, 0, 0, 0},
                {0, 0, 0, 0}};

        final var solution = assignment3.applyAsLong(4, inputGrid);

        assertEquals(8, solution);
    }

    @Test
    void testSolutionLargeMatrix() {
        final int[][] inputGrid = MatrixGenerator.buildMatrix(100, 100, 8, 10);

        TestUtils.printMatrix(inputGrid);

        final var solution = assignment3.applyAsLong(50, inputGrid);

        assertEquals(46, solution);
    }

}
