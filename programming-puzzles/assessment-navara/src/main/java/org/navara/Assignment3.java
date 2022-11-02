package org.navara;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the version I made during the test. It is elegant but slow so not very good actually
 */
public class Assignment3 {

    public long solution(final int maxDistance, final int[][] grid) {
        final List<Location> locations = convertToLocations(grid);

        return locations.stream()
                .filter(Location::isAvailable)
                .filter(available -> available.distanceToAllHousesLessThanOrEqualToMaxDistance(maxDistance, locations))
                .count();
    }

    private static List<Location> convertToLocations(final int[][] grid) {
        final List<Location> locations = new ArrayList<>();

        for (int y = 0; y < grid.length; y++) {
            final int[] row = grid[y];
            for (int x = 0; x < row.length; x++) {
                final int type = row[x];
                final Location location = new Location(x, y, type == 1);
                locations.add(location);
            }
        }
        return locations;
    }
}
