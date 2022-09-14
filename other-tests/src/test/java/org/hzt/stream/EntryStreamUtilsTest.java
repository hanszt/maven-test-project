package org.hzt.stream;

import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Book;
import org.hzt.test.model.Painter;
import org.hzt.test.model.Painting;
import org.hzt.utils.It;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.collections.MutableMapX;
import org.hzt.utils.collectors.CollectorsX;
import org.hzt.utils.streams.StreamX;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;
import static org.hzt.StringPredicates.startsWithAnyOf;
import static org.hzt.stream.EntryStreamUtils.*;
import static org.hzt.utils.It.*;
import static org.hzt.utils.collectors.CollectorsX.toMap;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EntryStreamUtilsTest {

    @Test
    void testMappingEntriesWithEntryFunction() {
        final Map<String, List<Book>> groupedByCategoryMap = TestSampleGenerator.createBookList().stream()
                .collect(Collectors.groupingBy(Book::getCategory));

        final ListX<String> groupedByBookNameListAsString = groupedByCategoryMap.entrySet().stream()
                .map(value(HashSet::new))
                .map(toSingle(EntryStreamUtilsTest::asString))
                .collect(CollectorsX.toListX());

        groupedByBookNameListAsString.forEach(It::println);

        assertTrue(groupedByBookNameListAsString.none(String::isEmpty));
    }

    @Test
    void testMapEntryThenFilterByValueThenFilterByKeyThenCollectToMap() {
        final var groupedByCategoryMap = MutableMapX.ofMap(TestSampleGenerator.createBookList().stream()
                .collect(Collectors.groupingBy(Book::getCategory)));

        println("groupedByCategoryMap:");
        groupedByCategoryMap.entrySet().forEach(It::println);

        final Map<String, Set<Book>> expectedMap = groupedByCategoryMap.entrySet().stream()
                .map(e -> Map.entry(e.getKey(), new HashSet<>(e.getValue())))
                .filter(e -> e.getKey() != null && (e.getKey().startsWith("E") || e.getKey().startsWith("F")))
                .filter(e -> e.getValue() != null && !e.getValue().isEmpty())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));


        final Map<String, HashSet<Book>> actualMap = groupedByCategoryMap.entrySet().stream()
                .map(value(HashSet::new))
                .filter(byKey(startsWithAnyOf("E", "F")))
                .filter(byValue(books -> !books.isEmpty()))
                .collect(toMap());

        final var actualMap1 = StreamX.ofMap(groupedByCategoryMap)
                .mapByValues(HashSet::new)
                .filterKeys(startsWithAnyOf("E", "F"))
                .filterValues(not(Set::isEmpty))
                .toMap();

        println("Result:");
        actualMap.entrySet().forEach(It::println);

        assertAll(
                () -> assertEquals(expectedMap, actualMap),
                () -> assertEquals(expectedMap, actualMap1)
        );
    }

    private static String asString(String s, Set<Book> books) {
        return s + " -> " + books.stream()
                .map(Book::toString)
                .collect(Collectors.joining(", "));
    }

    @Test
    void testStreamingOverMapEntriesThenFilterByEntryThenMappingToEntryThenCollectingToMap() {
        final Map<Painter, List<Painting>> groupedByPainterMap = TestSampleGenerator.createPaintingList().stream()
                .collect(Collectors.groupingBy(Painting::painter));

        groupedByPainterMap.entrySet().stream()
                .map(value(List::size))
                .forEach(It::println);

        final Map<String, Integer> result = groupedByPainterMap.entrySet().stream()
                .filter(byEntry(EntryStreamUtilsTest::painterBornIn19thCenturyAndListGreaterThan1))
                .map(toEntry(Painter::getLastname, List::size))
                .collect(toMap());

        println("result = " + result);
        assertTrue(result.keySet().containsAll(Arrays.asList("van Gogh", "Picasso")));
    }

    private static boolean painterBornIn19thCenturyAndListGreaterThan1(Painter painter, List<Painting> paintingList) {
        final int year = painter.getDateOfBirth().getYear();
        return 1800 <= year && year < 1900 && paintingList.size() >= 2;
    }

    @Test
    void testStreamingOverMapEntriesThenFilterByKeyThenMappingToInvertedEntryThrowsDuplicatedKeyException() {
        final Map<Painter, List<Painting>> groupedByPainterMap = TestSampleGenerator.createPaintingList().stream()
                .collect(Collectors.groupingBy(Painting::painter));

        groupedByPainterMap.entrySet().stream()
                .map(value(List::size))
                .forEach(It::println);

        Throwable throwable = assertThrows(IllegalStateException.class, () -> invertKeysAndValues(groupedByPainterMap));
        throwable.printStackTrace();
    }

    private void invertKeysAndValues(Map<Painter, List<Painting>> groupedByPainterMap) {
        groupedByPainterMap.entrySet().stream()
                .filter(byValue(list -> list.size() > 1))
                .map(toInvertedEntry(Painter::getLastname, List::size))
                .collect(toMap())
                .entrySet().forEach(It::println);
    }

    @Test
    void testConsumingEntryStreamWithBiFunction() {
        final Map<String, List<Book>> groupedByCategoryMap = TestSampleGenerator.createBookList().stream()
                .collect(Collectors.groupingBy(Book::getCategory));

        println("groupedByCategoryMap:");
        groupedByCategoryMap.entrySet().forEach(It::println);

        groupedByCategoryMap.entrySet().stream()
                .map(value(HashSet::new))
                .filter(byKey(startsWithAnyOf("E", "F")))
                .filter(byValue(books -> !books.isEmpty()))
                .forEach(entry(EntryStreamUtilsTest::assertResult)
                        .andThen(entry((bookCategory, bookSet) ->
                                println("key: " + bookCategory + ", value: " + bookSet))));
    }

    private static void assertResult(String category, HashSet<Book> books) {
        assertTrue(category.startsWith("E") || category.startsWith("F"));
        assertFalse(books.isEmpty());
    }

}
