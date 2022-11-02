package org.navara;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class Location {

    private final int x;
    private final int y;
    private final boolean isHouse;
    private final Set<Location> neighBors = new HashSet<>();
    private final Set<Location> suitableLocations = new HashSet<>();

    Location(final int x, final int y, final boolean isHouse) {
        this.x = x;
        this.y = y;
        this.isHouse = isHouse;
    }

    int calculateDistanceFrom(final Location other) {
        final int distanceX = Math.abs(this.x - other.x);
        final int distanceY = Math.abs(this.y - other.y);
        return distanceX + distanceY;
    }

    boolean distanceToAllHousesLessThanOrEqualToMaxDistance(final int maxDistance, final List<Location> locations) {
        return locations.stream()
                .filter(Location::isHouse)
                .allMatch(other -> calculateDistanceFrom(other) <= maxDistance);
    }

    boolean isAvailable() {
        return !isHouse;
    }

    boolean isHouse() {
        return isHouse;
    }

    void addNeighbour(final Location neighbor) {
        neighBors.add(neighbor);
    }

    void addSuitableLocations(final Collection<Location> suitable) {
        this.suitableLocations.addAll(suitable);
    }

    public Set<Location> getNeighBors() {
        return Collections.unmodifiableSet(neighBors);
    }

    public Set<Location> getSuitableSet() {
        return Set.copyOf(suitableLocations);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Location{" +
                "x=" + x +
                ", y=" + y +
                ", isHouse=" + isHouse +
                ", neighbours=" + neighBoursAsString() +
                '}';
    }

    private String neighBoursAsString() {
        return neighBors.stream()
                .map(neighBor -> "Location{x=" + neighBor.x + ", y=" + neighBor.y + "}")
                .collect(Collectors.joining(", "));
    }

}
