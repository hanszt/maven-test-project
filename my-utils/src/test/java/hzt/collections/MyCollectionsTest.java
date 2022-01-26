package hzt.collections;

import hzt.utils.MyCollections;
import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Museum;
import org.hzt.test.model.Painter;
import org.hzt.test.model.Painting;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;

class MyCollectionsTest {

    @Test
    void testIntersect() {
        final var collections = List.of(List.of(1, 2, 3, 4, 5, 7), Set.of(2, 4, 5), Set.of(4, 5, 6));
        final var intersect = MyCollections.intersect(collections);

        assertEquals(Set.of(4, 5), intersect);
    }

    @Test
    void testCollect() {
        var list = TestSampleGenerator.createPaintingList();
        final var expected = list.stream().collect(Collectors.groupingBy(Painting::painter));
        final var actual = MyCollections.collect(list, Collectors.groupingBy(Painting::painter));

        assertEquals(expected, actual);
    }

    @Test
    void testPipelineIterables() {
        var paintings = TestSampleGenerator.createPaintingList();
        final var painterList = MyCollections.filter(
                MyCollections.map(paintings, Painting::painter), painter -> painter.getFirstName().startsWith("V"));

        final var painters = paintings.stream()
                .map(Painting::painter)
                .filter(painter -> painter.getFirstName().startsWith("V"))
                .collect(Collectors.toUnmodifiableList());

        painters.forEach(System.out::println);

        assertIterableEquals(painters, painterList);
    }

    @Test
    void testIterableFilterMapWithLinesMatch() {
        var museumList = TestSampleGenerator.getMuseumListContainingNulls();
        final var painterNamesActual = MyCollections.distinct(MyCollections.filter(MyCollections.map(MyCollections.flatMap(MyCollections.map(museumList,
                                Museum::getPaintings),
                                Painting::painter),
                        Painter::getFirstName),
                name -> name.length() > 3));

        final var painterNamesExpected = museumList.stream()
                .flatMap(museum -> museum.getPaintings().stream())
                .map(Painting::painter)
                .map(Painter::getFirstName)
                .filter(name -> name.length() > 3)
                .distinct()
                .collect(Collectors.toUnmodifiableList());

        painterNamesActual.forEach(System.out::println);

        assertLinesMatch(painterNamesActual, painterNamesExpected);
    }
}
