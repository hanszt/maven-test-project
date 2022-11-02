package org.navara.utils;

public final class MatrixGenerator {

    private MatrixGenerator() {
    }

    public static int[][] buildMatrix(final int width, final int height, final int colFrequency, final int rowFrequency) {
        final int[][] grid = new int[width][height];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                final boolean inRowRange = row > height / 4. && row < height * 3 / 4.;
                final boolean inColRange = col > width / 4. && col < width * 3 / 4.;
                final boolean freqCondition = row % rowFrequency == 0 || col % colFrequency == 0;
                grid[col][row] = inRowRange && inColRange && freqCondition ? 1 : 0;
            }
        }
        return grid;
    }
}
