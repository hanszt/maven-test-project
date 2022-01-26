package hzt.collections;

import hzt.function.It;
import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Museum;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class NavigableSetXTest {

    @Test
    void testGetNavigableSet() {
        final var museumListContainingNulls = SetX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final var expected = museumListContainingNulls.stream()
                .map(Museum::getName)
                .filter(Objects::nonNull)
                .sorted()
                .collect(Collectors.toCollection(TreeSet::new));

        final var names = museumListContainingNulls.toNavigableSetOf(Museum::getName);

        var list = MutableListX.<Integer>empty();

        final var average = names
                .onEachOf(String::length, It
                        .<Consumer<Integer>>self(System.out::println)
                        .andThen(list::add))
                .filterBy(String::length, length -> length > 14)
                .averageOfInts(String::length);

        System.out.println("average = " + average);

        names.forEach(System.out::println);

        assertAll(
                () -> assertIterableEquals(ListX.of(14, 15, 14), list),
                () -> assertIterableEquals(names, expected)
        );
    }

    @Test
    void testToSetSortedBy() {
        final var museumListContainingNulls = SetX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final NavigableSet<Museum> expected = museumListContainingNulls.stream()
                .filter(i -> i != null && i.getName() != null)
                .sorted(Comparator.comparing(Museum::getName))
                .collect(Collectors.toCollection(TreeSet::new));

        final var sortedMuseums = museumListContainingNulls.toSetSortedBy(Museum::getName);

        sortedMuseums.forEach(System.out::println);

        assertIterableEquals(sortedMuseums, expected);
    }
}
