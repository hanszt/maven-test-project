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

        final var lengthConsumer = ConsumerX.<Integer>accept(System.out::println)
                .andThen(list::add);

        final var average = names
                .onEachOf(String::length, lengthConsumer)
                .filterBy(String::length, length -> length > 14)
                .averageOfInts(String::length);

        System.out.println("average = " + average);

        names.forEach(System.out::println);

        assertAll(
                () -> assertIterableEquals(ListX.of(14, 15, 14), list),
                () -> assertIterableEquals(names, expected)
        );
    }
}
