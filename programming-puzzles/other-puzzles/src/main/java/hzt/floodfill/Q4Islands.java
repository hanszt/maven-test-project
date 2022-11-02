package hzt.floodfill;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public final class Q4Islands {

    private static final List<Point> dirs = List.of(new Point(1, 0), new Point(-1, 0), new Point(0, 1), new Point(0, -1));

    private Q4Islands() {
    }

//    https://en.wikipedia.org/wiki/Flood_fill
    public static void floodFill(final int[][] grid, final int row, final int col) {
        final Queue<Point> queue = new ArrayDeque<>();
        queue.add(new Point(col, row));
        while (!queue.isEmpty()) {
            final Point current = queue.poll();
            grid[current.y][current.x] = -1;
            checkNeighbors(grid, queue, current);
        }
    }

    private static void checkNeighbors(final int[][] grid, final Queue<Point> queue, final Point current) {
        for (final var dir : dirs) {
            final int xNeighbor = current.x + dir.x;
            final int yNeighbor = current.y + dir.y;
            final var width = grid[0].length;
            final var height = grid.length;
            if (xNeighbor >= 0 && xNeighbor < width && yNeighbor >= 0 && yNeighbor < height) {
                final int neighborVal = grid[yNeighbor][xNeighbor];
                if (neighborVal == 1) {
                    queue.add(new Point(xNeighbor, yNeighbor));
                }
            }
        }
    }

    public static void floodFillRecursive(final int[][] grid, final int row, final int col) {
        final var withinGrid = row >= 0 && row < grid.length && col >= 0 && col < grid[0].length;
        if (withinGrid) {
            final var valueEqualToTarget = grid[row][col] == 1;
            if (valueEqualToTarget) {
                grid[row][col] = 'X';
                floodFillRecursive(grid, row - 1, col);
                floodFillRecursive(grid, row + 1, col);
                floodFillRecursive(grid, row, col - 1);
                floodFillRecursive(grid, row, col + 1);
            }
        }
    }

    public static int findNrOfIslands(final int[][] grid, final FloodFiller filler) {
        int nrOfIslands = 0;
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                final int value = grid[row][col];
                if (value == 1) {
                    filler.floodFill(grid, row, col);
                    nrOfIslands++;
                }

            }
        }
        return nrOfIslands;
    }

    @FunctionalInterface
    public interface FloodFiller {
        void floodFill(int[][] grid, int row, int col);
    }

    private record Point(int x, int y) {
    }
}
