package org.navara;

public final class TestUtils {

    private TestUtils() {
    }

    public static void printMatrix(final int[][] grid) {
        for (final int[] row : grid) {
            for (final int cell : row) {
                System.out.print(cell + ", ");
            }
            System.out.println();
        }
    }
}
