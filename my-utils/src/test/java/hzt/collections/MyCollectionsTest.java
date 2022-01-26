package hzt.collections;

import hzt.utils.MyCollections;
import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Museum;
import org.hzt.test.model.Painter;
import org.hzt.test.model.Painting;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;

class MyCollectionsTest {

    @Test
    void testIntersect() {
        final List<Collection<Integer>> collections = Arrays.asList(Arrays.asList(1, 2, 3, 4, 5, 7), Arrays.asList(2, 4, 5), Arrays.asList(4, 5, 6));
        final Set<Integer> intersect = MyCollections.intersect(collections);

        assertEquals(new HashSet<>(Arrays.asList(4, 5)), intersect);
    }

    @Test
    void testCollect() {
        List<Painting> list = TestSampleGenerator.createPaintingList();
        final Map<Painter, List<Painting>> expected = list.stream().collect(Collectors.groupingBy(Painting::painter));
        final Map<Painter, List<Painting>> actual = MyCollections.collect(list, Collectors.groupingBy(Painting::painter));

        assertEquals(expected, actual);
    }

    @Test
    void testPipelineIterables() {
        List<Painting> paintings = TestSampleGenerator.createPaintingList();
        final List<Painter> painterList = MyCollections.filter(
                MyCollections.map(paintings, Painting::painter), painter -> painter.getFirstName().startsWith("V"));

        final List<Painter> painters = paintings.stream()
                .map(Painting::painter)
                .filter(painter -> painter.getFirstName().startsWith("V"))
                .collect(Collectors.toList());

        painters.forEach(System.out::println);

        assertIterableEquals(painters, painterList);
    }

    @Test
    void testIterableFilterMapWithLinesMatch() {
        List<Museum> museumList = TestSampleGenerator.getMuseumListContainingNulls();
        final List<String> painterNamesActual = MyCollections.distinct(MyCollections.filter(MyCollections.map(MyCollections.flatMap(MyCollections.map(museumList,
                                Museum::getPaintings),
                                Painting::painter),
                        Painter::getFirstName),
                name -> name.length() > 3));

        final List<String> painterNamesExpected = museumList.stream()
                .flatMap(museum -> museum.getPaintings().stream())
                .map(Painting::painter)
                .map(Painter::getFirstName)
                .filter(name -> name.length() > 3)
                .distinct()
                .collect(Collectors.toList());

        painterNamesActual.forEach(System.out::println);

        assertLinesMatch(painterNamesActual, painterNamesExpected);
    }
}
