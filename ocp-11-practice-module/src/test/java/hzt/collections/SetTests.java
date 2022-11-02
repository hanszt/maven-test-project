package hzt.collections;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingInt;
import static org.junit.jupiter.api.Assertions.*;

class SetTests {

    @Test
    @Disabled("Takes to long")
    void testUnboundedSetAddThrowsOutOfMemoryError() {
        assertThrows(OutOfMemoryError.class, this::fillSetUntilOutOfMemory);
    }

    private void fillSetUntilOutOfMemory() {
        Set<String> set = new HashSet<>();
        try {
            int counter = 0;
            //noinspection InfiniteLoopStatement
            while (true) {
                set.add("hoi" + counter);
                counter++;
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            System.out.println(set.size());
        }
    }

    @Test
    void testSetOfArraysCanStoreMultipleEqualContentArrays() {
        Set<int[]> arraySet = new HashSet<>();
        arraySet.add(new int[]{1, 2, 3, 4});

        assertAll(
                () -> assertTrue(arraySet.add(new int[]{1, 2, 3, 4})),
                () -> assertEquals(2, arraySet.size())
        );
    }

    @Test
    void testTreeSetComparingByStringLengthMaintainsUniquenessByResultOfComparison() {
        final var inputSet = Set.of("Mies", "Aap", "Noot", "Gijsbrecht");

        final var treeSet = inputSet.stream()
                .collect(Collectors.toCollection(() -> new TreeSet<>(comparingInt(String::length))));

        System.out.println("inputSet = " + inputSet);
        System.out.println("treeSet = " + treeSet);

        final var hashSetFromTreeSet = new HashSet<>(treeSet);

        assertAll(
                () -> assertEquals(3, treeSet.size()),
                () -> assertTrue(treeSet.containsAll(inputSet)),
                () -> assertFalse(hashSetFromTreeSet.containsAll(inputSet))
        );
    }
}
