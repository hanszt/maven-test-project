package org.navara;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class Assignment3Bfs {

    public int solution(final int maxDistance, final int[][] grid) {
        final Set<Location> houseLocations = new HashSet<>();

        final List<Location> locations = convertToLocations(grid);
        for (final Location location : locations) {
            if (location.isHouse()) {
                final Set<Location> suitableLocations = bfs(location, maxDistance);
                location.addSuitableLocations(suitableLocations);
                houseLocations.add(location);
            }
        }

        if (houseLocations.isEmpty()) {
            throw new IllegalStateException("There should be at least one house in the matrix...");
        }

        final Set<Location> intersect = intersect(houseLocations.stream()
                .map(Location::getSuitableSet)
                .toList());

        intersect.removeAll(houseLocations);
        return intersect.size();
    }

    public static <T> Set<T> intersect(final Collection<? extends Collection<T>> collections) {
        final Set<T> common = new HashSet<>();
        if (!collections.isEmpty()) {
            final Iterator<? extends Collection<T>> iterator = collections.iterator();
            common.addAll(iterator.next());
            while (iterator.hasNext()) {
                common.retainAll(iterator.next());
            }
        }
        return common;
    }

    public static Set<Location> bfs(final Location start, final int maxDistance) {
        final Set<Location> alreadyVisited = new HashSet<>();
        final Queue<Location> queue = new ArrayDeque<>();
        queue.add(start);

        Location currentNode;

        while (!queue.isEmpty()) {
            currentNode = queue.remove();
            if (currentNode.calculateDistanceFrom(start) > maxDistance) {
                return alreadyVisited;
            } else {
                alreadyVisited.add(currentNode);
                queue.addAll(currentNode.getNeighBors());
                queue.removeAll(alreadyVisited);
            }
        }
        return alreadyVisited;
    }

    static List<Location> convertToLocations(final int[][] locationGrid) {
        return convertToLocations(convertToLocationGrid(locationGrid));
    }

    static List<Location> convertToLocations(final Location[][] locationGrid) {
        final List<Location> locationList = new ArrayList<>();
        for (int y = 0; y < locationGrid.length; y++) {
            final Location[] row = locationGrid[y];
            for (int x = 0; x < row.length; x++) {
                final Location location = row[x];
                final int yNorth = y - 1;
                if (yNorth >= 0) {
                    location.addNeighbour(locationGrid[yNorth][x]);
                }
                final int ySouth = y + 1;
                if (ySouth < locationGrid.length) {
                    location.addNeighbour(locationGrid[ySouth][x]);
                }
                final int xWest = x - 1;
                if (xWest >= 0) {
                    location.addNeighbour(locationGrid[y][xWest]);
                }
                final int xEast = x + 1;
                if (xEast < row.length) {
                    location.addNeighbour(locationGrid[y][xEast]);
                }
                locationList.add(location);
            }
        }
        return locationList;
    }

    private static Location[][] convertToLocationGrid(final int[][] grid) {
        if (grid.length == 0) {
            throw new IllegalStateException("Grid length was 0");
        }
        final Location[][] locationGrid = new Location[grid.length][grid[0].length];
        for (int y = 0; y < grid.length; y++) {
            final int[] row = grid[y];
            for (int x = 0; x < row.length; x++) {
                final int type = row[x];
                locationGrid[y][x] = new Location(x, y, type == 1);
            }
        }
        return locationGrid;
    }

}
