package hzt.stream.utils;

import org.hzt.TestSampleGenerator;
import org.hzt.model.Painting;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
