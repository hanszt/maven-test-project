package org.hzt;

import one.util.streamex.StreamEx;
import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Museum;
import org.hzt.test.model.Painting;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class StreamExTests {

    @Test
    void testStreamExToFlatList() {
        final var museumList = TestSampleGenerator.getMuseumListContainingNulls();

        final var expected = museumList.stream()
                .flatMap(m -> m.getPaintings().stream())
                .toList();

        final var actual = StreamEx.of(museumList)
                .toFlatList(Museum::getPaintings);

        assertIterableEquals(expected, actual);
    }

    @Test
    void testGroupBy() {
        final List<Museum> museumList = TestSampleGenerator.getMuseumListContainingNulls();

        final var expected = museumList.stream()
                .flatMap(m -> m.getPaintings().stream())
                .collect(Collectors.groupingBy(Painting::painter));

        final var actual = StreamEx.of(museumList)
                .flatMap(museum -> museum.getPaintings().stream())
                .groupingBy(Painting::painter);

        assertEquals(expected, actual);
    }
}
