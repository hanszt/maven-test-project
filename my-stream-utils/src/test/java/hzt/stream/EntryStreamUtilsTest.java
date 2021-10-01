package hzt.stream;

import hzt.stream.model.Book;
import hzt.stream.model.Painter;
import hzt.stream.model.Painting;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static hzt.stream.EntryStreamUtils.*;
import static hzt.stream.TestSampleGenerator.*;
import static hzt.stream.collectors.MyCollectors.toUnmodifiableMap;
import static hzt.stream.predicates.StringPredicates.startsWithAnyOf;
import static java.util.function.Predicate.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EntryStreamUtilsTest {

    @Test
    void testMappingEntriesWithEntryFunction() {
        final var groupedByCategoryMap = createBookList().stream()
                .collect(Collectors.groupingBy(Book::getCategory));

        final var groupedByBookNameListAsString = groupedByCategoryMap.entrySet().stream()
                .map(value(Set::copyOf))
                .map(toSingle(EntryStreamUtilsTest::asString))
                .toList();

        groupedByBookNameListAsString.forEach(System.out::println);

        assertTrue(groupedByBookNameListAsString.stream().noneMatch(String::isEmpty));
    }

    @Test
    void testMapEntryThenFilterByValueThenFilterByKeyThenCollectToMap() {
        final var groupedByCategoryMap = createBookList().stream()
                .collect(Collectors.groupingBy(Book::getCategory));

        System.out.println("groupedByCategoryMap:");
        groupedByCategoryMap.entrySet().forEach(System.out::println);

        final var expectedMap = groupedByCategoryMap.entrySet().stream()
                .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), Set.copyOf(e.getValue())))
                .filter(e -> e.getKey() != null && (e.getKey().startsWith("E") || e.getKey().startsWith("F")))
                .filter(e -> e.getValue() != null && !e.getValue().isEmpty())
                .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));


        final var actualMap = groupedByCategoryMap.entrySet().stream()
                .map(value(Set::copyOf))
                .filter(byKey(startsWithAnyOf("E", "F")))
                .filter(byValue(not(Set::isEmpty)))
                .collect(toUnmodifiableMap());

        System.out.println("Result:");
        actualMap.entrySet().forEach(System.out::println);

        assertEquals(expectedMap, actualMap);
    }

    private static String asString(String s, Set<Book> books) {
        return s + " -> " + books.stream()
                .map(Book::toString)
                .collect(Collectors.joining(", "));
    }

    @Test
    void testStreamingOverMapEntriesThenFilterByEntryThenMappingToEntryThenCollectingToMap() {
        final var groupedByPainterMap = getPaintingList().stream()
                .collect(Collectors.groupingBy(Painting::getPainter));

        groupedByPainterMap.entrySet().stream()
                .map(value(List::size))
                .forEach(System.out::println);

        final var result = groupedByPainterMap.entrySet().stream()
                .filter(byEntry(EntryStreamUtilsTest::painterBornIn19thCenturyAndListGreaterThan1))
                .map(toEntry(Painter::getLastname, List::size))
                .collect(toUnmodifiableMap());

        System.out.println("result = " + result);
        assertTrue(result.keySet().containsAll(List.of("van Gogh", "Picasso")));
    }

    private static boolean painterBornIn19thCenturyAndListGreaterThan1(Painter painter, List<Painting> paintingList) {
        final var year = painter.getDateOfBirth().getYear();
        return 1800 <= year && year < 1900 && paintingList.size() >= 2;
    }

    @Test
    void testStreamingOverMapEntriesThenFilterByKeyThenMappingToInvertedEntryThrowsDuplicatedKeyException() {
        final var groupedByPainterMap = getPaintingList().stream()
                .collect(Collectors.groupingBy(Painting::getPainter));

        groupedByPainterMap.entrySet().stream()
                .map(value(List::size))
                .forEach(System.out::println);

        Throwable throwable = assertThrows(IllegalStateException.class, () -> invertKeysAndValues(groupedByPainterMap));
        throwable.printStackTrace();
    }

    private void invertKeysAndValues(Map<Painter, List<Painting>> groupedByPainterMap) {
        groupedByPainterMap.entrySet().stream()
                .filter(byValue(list -> list.size() > 1))
                .map(toInvertedEntry(Painter::getLastname, List::size))
                .collect(toUnmodifiableMap())
                .entrySet().forEach(System.out::println);
    }

    @Test
    void testConsumingEntryStreamWithBiFunction() {
        final var groupedByCategoryMap = createBookList().stream()
                .collect(Collectors.groupingBy(Book::getCategory));

        System.out.println("groupedByCategoryMap:");
        groupedByCategoryMap.entrySet().forEach(System.out::println);

        groupedByCategoryMap.entrySet().stream()
                .map(value(Set::copyOf))
                .filter(byKey(startsWithAnyOf("E", "F")))
                .filter(byValue(not(Set::isEmpty)))
                .forEach(unwrapEntry(EntryStreamUtilsTest::assertResult)
                        .andThen(unwrapEntry((k, v) -> System.out.println("key: " +  k + ", value: " + v))));
    }

    private static void assertResult(String category, Set<Book> books) {
        assertTrue(category.startsWith("E") || category.startsWith("F"));
        assertFalse(books.isEmpty());
    }

}