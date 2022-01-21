package hzt.collections;

import hzt.utils.Pair;
import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Museum;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class NavigableMapXTest {

    @Test
    void testGetNavigableMap() {
        final var museumListContainingNulls = SetX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final NavigableMap<String, Museum> expected = new TreeMap<>(museumListContainingNulls.stream()
                .filter(m -> m != null && m.getName() != null)
                .collect(Collectors.toMap(Museum::getName, Function.identity())));

        final var actual = museumListContainingNulls
                .toNavigableMapAssociatedBy(Museum::getName);

        assertAll(
                () -> assertIterableEquals(actual.keySet(), expected.keySet()),
                () -> assertIterableEquals(actual.values(), expected.values())
        );
    }

    @Test
    void testGetNavigableMapAssociatedWith() {
        final var museumSetContainingNulls = MutableSetX
                .of(TestSampleGenerator.getMuseumListContainingNulls());

        final NavigableMap<Museum, String> expected = new TreeMap<>(museumSetContainingNulls.stream()
                .filter(m -> m != null && m.getName() != null)
                .collect(Collectors.toMap(Function.identity(), Museum::getName)));

        final NavigableMapX<Museum, String> actual = museumSetContainingNulls
                .notNullBy(Museum::getName)
                .toNavigableMapAssociatedWith(Museum::getName);

        final Museum firstMuseum = actual.first().getKey();

        System.out.println("firstMuseum = " + firstMuseum);

        assertAll(
                () -> assertIterableEquals(actual.keySet(), expected.keySet()),
                () -> assertIterableEquals(actual.values(), expected.values())
        );
    }

}
