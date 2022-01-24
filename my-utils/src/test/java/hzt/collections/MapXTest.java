package hzt.collections;

import hzt.utils.Pair;
import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Museum;
import org.hzt.test.model.Painting;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MapXTest {

    @Test
    void testInvertMap() {
        final var museumMap = TestSampleGenerator.createMuseumMap();

        final var expected = museumMap.entrySet().stream()
                .map(e -> Map.entry(e.getValue(), e.getKey()))
                .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));

        final var actual = MapX.of(museumMap).toInvertedMap();

        System.out.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testToIterXThanSum() {
        final var museumMap = TestSampleGenerator.createMuseumMap();

        final var expected = museumMap.values().stream()
                .map(Museum::getDateOfOpening)
                .mapToInt(LocalDate::getDayOfMonth)
                .sum();

        final var actual = MapX.of(museumMap)
                .valuesToIterX(Museum::getDateOfOpening)
                .sumOf(LocalDate::getDayOfMonth);

        System.out.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testForEachIndexed() {
        List<IndexedValue<Map.Entry<String, Museum>>> list = new ArrayList<>();
        BiConsumer<Integer, Map.Entry<String, Museum>> biConsumer = (index, value) -> list.add(new IndexedValue<>(index, value));

        final var museumListContainingNulls = TestSampleGenerator.getMuseumListContainingNulls();

        ListX.of(museumListContainingNulls).associateBy(Museum::getName).forEachIndexed(biConsumer);

        list.forEach(System.out::println);

        assertEquals(3, list.size());
    }

    @Test
    void flatMapToListOf() {
        final var museumMap = TestSampleGenerator.createMuseumMap();

        final var expected = museumMap.entrySet().stream()
                .flatMap(e -> e.getValue().getPaintings().stream())
                .collect(Collectors.toUnmodifiableList());

        final var actual = MapX.of(museumMap).flatMapValuesToListOf(Museum::getPaintings);

        System.out.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testMapOfMapXSameAsInputMap() {
        final var museumMap = TestSampleGenerator.createMuseumMap();

        final var mapX = MapX.of(museumMap);
        MapX.of(mapX).entrySet().forEach(System.out::println);

        System.out.println("mapX = " + mapX);

        assertEquals(museumMap, mapX);
    }

    @Test
    void testComputeIfAbsent() {
        final var museumMap = ListX.of(TestSampleGenerator.createMuseumList())
                .associateBy(Museum::getName);

        var expected = museumMap.get("Van Gogh Museum");

        final var mapX = MutableMapX.of(museumMap);

        final var van_gogh = mapX.computeIfAbsent("Van Gogh Museum", key -> {
            throw new IllegalStateException();
        });

        System.out.println("van_gogh = " + van_gogh);

        assertEquals(expected, van_gogh);
    }

    @Test
    void testMapXToList() {
        final var museumMapX = MapX.of(TestSampleGenerator.createMuseumMap());

        final var pairs = museumMapX.toListXOf(Pair::ofEntry);

        assertEquals(Set.of("Picasso Museum", "Van Gogh Museum", "Vermeer Museum"), pairs.toSetXOf(Pair::first));
    }

    @Test
    void testFlatMapToList() {
        final var museumMapX = MapX.of(TestSampleGenerator.createMuseumMap());

        final var pairs = museumMapX.flatMapToListXOf(e -> e.getValue().getPaintings());

        final var expected = "Meisje met de rode hoed";

        final var actual = ListX.of(pairs).maxOf(Painting::name);

        assertEquals(expected, actual);
    }
}
