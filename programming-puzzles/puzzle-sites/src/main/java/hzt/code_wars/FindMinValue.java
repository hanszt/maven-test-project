package hzt.code_wars;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FindMinValue {

    private static final Logger LOGGER = LoggerFactory.getLogger(FindMinValue.class);

    public static void main(final String[] args) {
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
        final int minSumOfMask = findMinSumOfMask(input, mask);
        LOGGER.info("minSumOfMask = {}", minSumOfMask);
    }

    static int findMinSumOfMask(final int[][] input, final boolean[][] mask) {
        int minSum = Integer.MAX_VALUE;
        for (int x = 0; x <= input.length - mask.length; x++) {
            for (int y = 0; y <= input[0].length - mask[0].length; y++) {
                final int sumMask = getSumMask(input, mask, x, y);
                LOGGER.debug("sumMask = {}", sumMask);
                if (sumMask < minSum) {
                    minSum = sumMask;
                }
            }
        }
        return minSum;
    }

    private static int getSumMask(final int[][] input, final boolean[][] mask, final int x, final int y) {
        int sumMask = 0;
        for (int xMask = 0; xMask < mask.length; xMask++) {
            for (int yMask = 0; yMask < mask[0].length; yMask++) {
                if (mask[xMask][yMask]) {
                    sumMask += input[x + xMask][y + yMask];
                }
            }
        }
        return sumMask;
    }
}
