package hzt.stream;

import org.hzt.TestSampleGenerator;
import org.hzt.model.Book;
import org.hzt.model.Painter;
import org.hzt.model.Painting;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static hzt.stream.EntryStreamUtils.*;
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
        final var groupedByCategoryMap = TestSampleGenerator.createBookList().stream()
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
        final var groupedByCategoryMap = TestSampleGenerator.createBookList().stream()
                .collect(Collectors.groupingBy(Book::getCategory));

        System.out.println("groupedByCategoryMap:");
        groupedByCategoryMap.entrySet().forEach(System.out::println);

        final var expectedMap = groupedByCategoryMap.entrySet().stream()
                .map(e -> Map.entry(e.getKey(), Set.copyOf(e.getValue())))
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
        final var groupedByPainterMap = TestSampleGenerator.createPaintingList().stream()
                .collect(Collectors.groupingBy(Painting::painter));

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
        final var groupedByPainterMap = TestSampleGenerator.createPaintingList().stream()
                .collect(Collectors.groupingBy(Painting::painter));

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
        final var groupedByCategoryMap = TestSampleGenerator.createBookList().stream()
                .collect(Collectors.groupingBy(Book::getCategory));

        System.out.println("groupedByCategoryMap:");
        groupedByCategoryMap.entrySet().forEach(System.out::println);

        groupedByCategoryMap.entrySet().stream()
                .map(value(Set::copyOf))
                .filter(byKey(startsWithAnyOf("E", "F")))
                .filter(byValue(not(Set::isEmpty)))
                .forEach(entry(EntryStreamUtilsTest::assertResult)
                        .andThen(entry((bookCategory, bookSet) ->
                                System.out.println("key: " +  bookCategory + ", value: " + bookSet))));
    }

    private static void assertResult(String category, Set<Book> books) {
        assertTrue(category.startsWith("E") || category.startsWith("F"));
        assertFalse(books.isEmpty());
    }

}
