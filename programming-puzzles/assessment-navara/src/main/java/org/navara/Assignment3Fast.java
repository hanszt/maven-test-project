package org.navara;

/**
 * This is the fast version. It is less readable probably but it is fast
 */
public class Assignment3Fast {

    public int solution(final int maxDistance, final int[][] grid) {
        int nrOfHouses = 0;
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                final var isAHouse = grid[row][col] == 1;
                if (isAHouse) {
                    nrOfHouses++;
                    decrementAllNonHouseSpotsWithinMaxDistance(row, col, maxDistance, grid);
                }
            }
        }
        if (nrOfHouses == 0) {
            throw new IllegalArgumentException("There should be at least one house in the matrix...");
        }
        return countSuitableSpots(grid, nrOfHouses);
    }

    private static int countSuitableSpots(final int[][] grid, final int nrOfHouses) {
        int sum = 0;
        for(int[] row : grid) {
            for (int value : row) {
                if (value == -nrOfHouses) {
                    sum++;
                }
            }
        }
        return sum;
    }

    static void decrementAllNonHouseSpotsWithinMaxDistance(final int row, final int col, final int maxDistance, final int[][] grid) {
        final var startingRow = row - maxDistance;

        for (int rowi = Math.max(startingRow, 0); rowi <= row + maxDistance && rowi < grid.length; rowi++) {

            final var rowDistance = Math.abs(rowi - row);
            final var colOffSet = Math.abs(rowDistance - maxDistance);
            final var startingCol = col - colOffSet;

            for (int coli = Math.max(startingCol, 0); coli <= col + colOffSet && coli < grid[0].length; coli++) {

                final var isNotAHouse = grid[rowi][coli] != 1;
                if (isNotAHouse) {
                    grid[rowi][coli]--;
                }
            }
        }
    }
}
