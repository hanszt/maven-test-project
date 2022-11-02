package org.navara;

import org.junit.jupiter.api.Test;
import org.navara.utils.MatrixGenerator;

import static org.junit.jupiter.api.Assertions.*;

class Assignment3FastTest {

    private final Assignment3Fast assignment3Fast = new Assignment3Fast();
    private final int[][] largeGrid = MatrixGenerator.buildMatrix(400, 400, 8, 10);

    @Test
    void testWhenNoHousesInGridThrowIllegalArgumentException() {
        final int[][] grid = {
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0}
        };

        assertThrows(IllegalArgumentException.class, () -> assignment3Fast.solution(5, grid));
    }

    @Test
    void testFindPointsDistancePAwayFromCenter() {
        final int[][] expected = {
                { 0,  0,  0,  0,  0},
                { 0,  0,  0, -1,  0},
                { 0,  0, -1, -1, -1},
                { 0, -1, -1,  1, -1},
                { 0,  0, -1, -1, -1}
        };

        final int[][] grid = {
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 1, 0},
                {0, 0, 0, 0, 0}
        };

        Assignment3Fast.decrementAllNonHouseSpotsWithinMaxDistance(3, 3, 2, grid);

        assertArrayEquals(expected, grid);
    }

    @Test
    void testSolutionSmallMatrix() {
        final int[][] inputGrid = {{0, 1}, {0, 0}};

        final var solution = assignment3Fast.solution(1, inputGrid);

        assertEquals(2, solution);
    }

    @Test
    void testSolutionMatrix2() {
        final int[][] inputGrid = {{0, 0, 0, 0}, {0, 0, 1, 0}, {1, 0, 0, 1}};

        final var solution = assignment3Fast.solution(2, inputGrid);

        assertEquals(2, solution);
    }

    @Test
    void testSolutionMatrix3() {
        final int[][] inputGrid = {{0, 0, 0, 1}, {0, 1, 0, 0}, {0, 0, 1, 0}, {1, 0, 0, 0}, {0, 0, 0, 0}};

        final var solution = assignment3Fast.solution(4, inputGrid);

        assertEquals(8, solution);
    }

    @Test
    void testSolutionLargeMatrix() {
        final int[][] inputGrid = MatrixGenerator.buildMatrix(100, 100, 8, 10);

        TestUtils.printMatrix(inputGrid);

        final var solution = assignment3Fast.solution(50, inputGrid);

        assertEquals(46, solution);
    }

    @Test
    void testSolutionVeryLargeMatrix() {
        final var solution = assignment3Fast.solution(800, largeGrid);

//        TestUtils.printMatrix(inputGrid);

        assertEquals(151719, solution);
    }

}
