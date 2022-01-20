package hzt.collections;

import hzt.stream.function.ConsumerX;
import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Museum;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class MutableNavigableSetXTest {

    @Test
    void testGetNavigableSet() {
        final var museumListContainingNulls = SetX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final var expected = museumListContainingNulls.stream()
                .map(Museum::getName)
                .filter(Objects::nonNull)
                .sorted()
                .collect(Collectors.toCollection(TreeSet::new));

        final var names = museumListContainingNulls.toSortedSetOf(Museum::getName);

        var list = MutableListX.empty();

        final var average = names
                .onEachOf(String::length, ConsumerX.of(System.out::println)
                        .andThen(list::add))
                .filterNotNullToMutableListBy(String::length, l -> l > 14)
                .averageOf(String::length);

        System.out.println("average = " + average);

        names.forEach(System.out::println);

        assertAll(
                () -> assertIterableEquals(ListX.of(14, 15, 14), list),
                () -> assertIterableEquals(names, expected)
        );
    }
}
