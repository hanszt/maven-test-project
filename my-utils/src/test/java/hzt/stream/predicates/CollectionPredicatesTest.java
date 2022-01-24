package hzt.stream.predicates;

import hzt.collections.MyCollections;
import hzt.stream.StreamUtils;
import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Painter;
import org.hzt.test.model.Painting;
import org.junit.jupiter.api.Test;

import java.util.List;

import static hzt.stream.StreamUtils.by;
import static hzt.stream.predicates.CollectionPredicates.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CollectionPredicatesTest {

    @Test
    void testContainsAll() {
        assertNotNull(containsAll());
    }

    @Test
    void testContainsAllOfIterable() {
        final List<Painting> paintingList = TestSampleGenerator.createPaintingList();

        final Painter firstPainter = paintingList.stream()
                .map(Painting::painter)
                .findFirst()
                .orElseThrow();

        firstPainter.forEach(System.out::println);
        System.out.println();

        final var paintings = paintingList.stream()
                .filter(by(Painting::painter, MyCollections::listOfIterable, containsAll(firstPainter)))
                .toList();

        paintings.forEach(System.out::println);

        assertEquals(StreamUtils.streamOf(firstPainter).count(), paintings.size());
    }

    @Test
    void testContainsAny() {
        assertNotNull(containsAny());
    }

    @Test
    void testContainsNone() {
        assertNotNull(containsNone());
    }
}
