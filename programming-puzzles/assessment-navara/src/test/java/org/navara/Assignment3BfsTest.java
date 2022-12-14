package org.navara;

import org.junit.jupiter.api.Test;
import org.navara.utils.MatrixGenerator;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class Assignment3BfsTest {

    private final Assignment3Bfs assignment3Bfs = new Assignment3Bfs();

    @Test
    void testGetNeighbors() {
        final int[][] grid = {
                {0, 0, 0, 1},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {1, 0, 0, 0},
                {0, 0, 0, 0}
        };

        System.out.println("width = " + grid[0].length);
        System.out.println("height = " + grid.length);

        final var locations = Assignment3Bfs.convertToLocations(grid);

        for (final Location location : locations) {
            final Set<Location> neighBors = location.getNeighBors();
            System.out.println(location);
            System.out.println(location.getNeighBors().size());
            assertTrue(neighBors.size() >= 2);
            assertTrue(neighBors.size() <= 4);
        }
    }

    @Test
    void testFindSuitable() {
        final int[][] inputGrid = {
                {0, 0, 0, 1},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {1, 0, 0, 0},
                {0, 0, 0, 0}};

        final List<Location> locations = Assignment3Bfs.convertToLocations(inputGrid);
        final Location testLocation = locations.stream()
                .filter(Location::isHouse)
                .findFirst()
                .orElseThrow();

        System.out.println("testLocation = " + testLocation);

        final var suitableLocations = Assignment3Bfs.bfs(testLocation, 3);

        System.out.println("Suitable locations: ");
        suitableLocations.stream()
                .filter(Location::isAvailable)
                .sorted(Comparator.comparing(Location::getX).thenComparing(Location::getY))
                .forEach(location -> System.out.println(location + ", distance: " + location.calculateDistanceFrom(testLocation)));

        assertEquals(10, suitableLocations.size());
    }

    @Test
    void testSolutionSmallMatrix() {
        final int[][] inputGrid = {{0, 1}, {0, 0}};

        final var solution = assignment3Bfs.solution(1, inputGrid);

        assertEquals(2, solution);
    }

    @Test
    void testSolutionMatrix2() {
        final int[][] inputGrid = {{0, 0, 0, 0}, {0, 0, 1, 0}, {1, 0, 0, 1}};

        final var solution = assignment3Bfs.solution(2, inputGrid);

        assertEquals(2, solution);
    }

    @Test
    void testSolutionMatrix3() {
        final int[][] inputGrid = {
                {0, 0, 0, 1},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {1, 0, 0, 0},
                {0, 0, 0, 0}
        };

        final var solution = assignment3Bfs.solution(4, inputGrid);

        assertEquals(8, solution);
    }

    @Test
    void testSolutionLargeMatrix() {
        final int[][] inputGrid = MatrixGenerator.buildMatrix(50, 50, 8, 10);

        TestUtils.printMatrix(inputGrid);

        final var solution = assignment3Bfs.solution(30, inputGrid);

        assertEquals(170, solution);
    }


}
