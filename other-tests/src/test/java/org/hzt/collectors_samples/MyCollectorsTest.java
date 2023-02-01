package org.hzt.collectors_samples;

import org.hzt.model.Employee;
import org.hzt.model.Person;
import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Book;
import org.hzt.test.model.Museum;
import org.hzt.test.model.Painting;
import org.hzt.utils.It;
import org.hzt.utils.Lazy;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.ranges.IntRange;
import org.hzt.utils.sequences.Sequence;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingInt;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.*;
import static org.hzt.collectors_samples.MyCollectors.*;
import static org.hzt.utils.It.println;
import static org.hzt.utils.Patterns.blankStringPattern;
import static org.hzt.utils.collectors.BigDecimalCollectors.averagingBigDecimal;
import static org.hzt.utils.collectors.BigDecimalCollectors.summarizingBigDecimal;
import static org.hzt.utils.function.predicates.StringPredicates.contains;
import static org.junit.jupiter.api.Assertions.*;

class MyCollectorsTest {

    private static final Lazy<List<String>> lazyReadWordsOfShakespeare = Lazy.of(MyCollectorsTest::readWordsInWorksOfShakespeare);

    @Test
    void testGetExpectedWhenOnlyOneInList() {
        var expected = new CashBalance("abn", true);
        var items = List.of(
                expected,
                new CashBalance("ing", false),
                new CashBalance("triodos", false));

        var actual = items.stream()
                .filter(CashBalance::isOpening)
                .collect(MyCollectorsTest.toFirstElementIfSizeOne()).orElse(null);

        items.forEach(It::println);

        assertEquals(expected, actual);
    }

    private static <T> Collector<T, ?, Optional<T>> toFirstElementIfSizeOne() {
        return collectingAndThen(toUnmodifiableList(), MyCollectorsTest::returnElementIfSizeOne);
    }

    private static <T> Optional<T> returnElementIfSizeOne(List<T> list) {
        if (list.size() == 1) {
            return Optional.of(list.get(0));
        }
        return Optional.empty();
    }

    @Test
    void testGetNullWhenMoreThanOneInList() {
        var expected = new CashBalance("abn", true);
        var actual = Stream.of(
                        expected,
                        new CashBalance("ing", true),
                        new CashBalance("triodos", false))
                .filter(CashBalance::isOpening)
                .collect(MyCollectorsTest.toFirstElementIfSizeOne())
                .orElse(null);

        assertNull(actual);
    }

    @Test
    void testGetNullWhenEmptyList() {
        var actual = Stream.<CashBalance>empty()
                .filter(CashBalance::isOpening)
                .collect(MyCollectorsTest.toFirstElementIfSizeOne())
                .orElse(null);

        assertNull(actual);
    }

    @Test
    void testReduceGetExpectedWhenOnlyOneInList() {
        var expected = new CashBalance("abn", true);
        var actual = reduceThrowing(Stream.of(
                expected,
                new CashBalance("ing", false),
                new CashBalance("triodos", false))).orElse(null);

        assertEquals(expected, actual);
    }

    @Test
    void testReduceThrowsIllegalStateExceptionWhenMoreThanOneInList() {
        var list = List.of(
                new CashBalance("abn", true),
                new CashBalance("ing", true),
                new CashBalance("triodos", false));

        final var stream = list.stream();
        assertThrows(MoreThanOneElementException.class, () -> reduceThrowing(stream));
    }

    @NotNull
    private static Optional<CashBalance> reduceThrowing(Stream<CashBalance> stream) {
        return stream
                .filter(CashBalance::isOpening)
                .reduce((a, b) -> {
                    throw new MoreThanOneElementException();
                });
    }

    @Test
    void testReduceGetNullWhenEmptyList() {
        final var reduce = reduceThrowing(Stream.of(new CashBalance("a bank", false)));
        assertTrue(reduce::isEmpty);
    }

    @Test
    void testReduceGetNullWhenIsOpeningAllFalse() {
        var list = List.of(
                new CashBalance("abn", false),
                new CashBalance("ing", false),
                new CashBalance("triodos", false));

        assertNull(reduceThrowing(list.stream()).orElse(null));
    }

    private static class MoreThanOneElementException extends RuntimeException {
    }

    @Test
    void testGivenAListOfBigDecimalsCalculateTheCorrectAverage() {
        var average = new BigDecimal("2000.00");
        var list = List.of(
                new BigDecimal("1000"),
                new BigDecimal("2000"),
                new BigDecimal("3000"));
        //act
        var actual = list.stream().collect(averagingBigDecimal());
        assertEquals(average, actual);
    }

    @Test
    void testGivenAListOfBigDecimalsCalculateBigDecimalSummaryStatistics() {
        var average = new BigDecimal("2000.00");
        final var min = new BigDecimal("1000");
        final var max = new BigDecimal("3000");

        var list = List.of(
                new BigDecimal("2000"), min,
                new BigDecimal("1500"), max,
                new BigDecimal("2500"));
        //act
        var summaryStatistics = list.stream()
                .collect(summarizingBigDecimal());

        assertAll(
                () -> assertEquals(average, summaryStatistics.getAverage()),
                () -> assertEquals(min, summaryStatistics.getMin()),
                () -> assertEquals(max, summaryStatistics.getMax()),
                () -> assertEquals(list.size(), (int) summaryStatistics.getCount()),
                () -> assertEquals(new BigDecimal("10000"), summaryStatistics.getSum())
        );
    }

    @Test
    void testCollectToNavigableSet() {
        final var strings =
                Stream.of("hallo", "hoe", "is", "het", "?", "dit", "is", "een", "test", "hoe", "vind", "je", "dat");

        final var navigableSet = strings.collect(toNavigableSet(comparingInt(String::length)));

        assertAll(
                () -> assertEquals(5, navigableSet.size()),
                () -> assertTrue(Sequence.of(navigableSet).isSortedBy(String::length))
        );
    }

    @Test
    void testGroupingBy() {
        List<String> strings =
                List.of("hallo", "hoe", "is", "het", "?", "dit", "is", "een", "test", "hoe", "vind", "je", "dat");

        var stringGroupedByLength = strings.stream()
                .collect(groupingBy(String::length));

        final var STRING_OF_LENGTH_THREE = 3;
        assertEquals(List.of("hoe", "het", "dit", "een", "hoe", "dat"), stringGroupedByLength.get(STRING_OF_LENGTH_THREE));
    }

    @Test
    void testPartitioningBy() {
        List<String> strings =
                List.of("hallo", "hoe", "is", "het", "?", "dit", "is", "een", "test", "hoe", "vind", "je", "dat");

        var partitionedByMap = strings.stream()
                .collect(partitioningBy(contains("e")));

        assertAll(
                () -> assertEquals(List.of("hoe", "het", "een", "test", "hoe", "je"), partitionedByMap.get(true)),
                () -> assertEquals(List.of("hallo", "is", "?", "dit", "is", "vind", "dat"), partitionedByMap.get(false))
        );
    }

    @Test
    void testGroupingByThenCountingThenConvertingCountToInteger() {
        final var countByBookCategory = TestSampleGenerator.createBookList().stream()
                .collect(groupingBy(Book::getCategory,
                        collectingAndThen(counting(),
                                Long::intValue)));
        //assert
        assertEquals(Map.of("Programming", 2, "Educational", 1, "Fiction", 4), countByBookCategory);
    }

    @Test
    void testGroupingByThenMappingToTitleCollectingToList() {
        //act
        final var bookTitleByCategory = TestSampleGenerator.createBookList().stream()
                .collect(groupingBy(Book::getCategory,
                        mapping(Book::getTitle, toUnmodifiableList())));
        //assert
        assertAll(
                () -> assertTrue(bookTitleByCategory.get("Fiction")
                        .containsAll(List.of("Harry Potter", "Lord of the Rings", "The da Vinci Code"))),
                () -> assertTrue(bookTitleByCategory.get("Programming")
                        .containsAll(List.of("Pragmatic Programmer", "OCP 11 Volume 1"))),
                () -> assertTrue(bookTitleByCategory.get("Educational")
                        .contains("Homo Deus"))
        );
    }

    @Test
    void testCollectingAndThenMaxByAndTransformResult() {
        //act
        final var personList = Person.createTestPersonList();
        var personWithMaxAgeByCollectMaxBy = personList.stream()
                .collect(collectingAndThen(maxBy(comparing(Person::getAge)),
                        personOpt -> personOpt.map(Person::getFirstName)));

        var personWithMaxAge = personList.stream()
                .max(comparing(Person::getAge))
                .map(Person::getFirstName);
        //assert
        assertEquals(personWithMaxAge, personWithMaxAgeByCollectMaxBy);
    }

    @Test
    void testGroupingByLastNameThenFilteringByIsPlayingPianoThenMappingToFirstName() {
        //act
        final var groupedByLastNamePlayingPianoToFirstName = Person.createTestPersonList().stream()
                .collect(groupingBy(Person::getLastName,
                        filtering(Person::isPlayingPiano,
                                mapping(Person::getFirstName,
                                        toUnmodifiableList()))));

        final var peoplePlayingPiano = groupedByLastNamePlayingPianoToFirstName.values().stream()
                .flatMap(Collection::stream)
                .collect(toUnmodifiableSet());
        //assert
        final var expectedKeySet = Set.of("Burgmeijer", "Ruigrok", "Jacobs", "Vullings", "Bello", "Zuidervaart");

        assertAll(
                () -> assertEquals(expectedKeySet, groupedByLastNamePlayingPianoToFirstName.keySet()),
                () -> assertEquals(Set.of("Sophie", "Hans", "Henk", "Nikolai"), peoplePlayingPiano)
        );


    }

    @Test
    void testFlatMapping() {
        //arrange
        final var otherPersonList = List.of(
                new Person("Matthijs", "Bayer", LocalDate.of(1993, 4, 4), true),
                new Employee("Joop", "Schat", LocalDate.of(1994, 1, 2)));
        final var listOfPersonLists = List.of(Person.createTestPersonList(), otherPersonList);

        var groupedByLastNamePlayingPianoToFirstName2 = listOfPersonLists.stream()
                .flatMap(Collection::stream)
                .filter(Person::isPlayingPiano)
                .map(Person::getFirstName)
                .collect(toUnmodifiableSet());
        //act
        var groupedByLastNamePlayingPianoToFirstName = listOfPersonLists.stream()
                .collect(flatMapping(Collection::stream,
                        filtering(Person::isPlayingPiano,
                                mapping(Person::getFirstName,
                                        toUnmodifiableSet()))));
        //assert
        final var expectedKeySet = Set.of("Sophie", "Henk", "Matthijs", "Nikolai", "Hans");

        assertAll(
                () -> assertEquals(expectedKeySet, groupedByLastNamePlayingPianoToFirstName),
                () -> assertEquals(groupedByLastNamePlayingPianoToFirstName, groupedByLastNamePlayingPianoToFirstName2)
        );
    }

    @Test
    void testParallelStreamWithNonCurrentCollectorRequiredCombiningPhase() {
        final List<String> words = lazyReadWordsOfShakespeare.get();

        final var parallelStream = words.parallelStream()
                .filter(not(String::isBlank));

        //noinspection ResultOfMethodCallIgnored
        Consumer<Stream<String>> toListThrowInCombingPhase = s -> s.collect(toListThrowWhenCombining());
        assertThrows(UnsupportedOperationException.class, () -> toListThrowInCombingPhase.accept(parallelStream));
    }

    @Test
    void testSequentialStreamWithNonCurrentCollectorDoesNotUseCombingPhase() {
        final List<String> words = lazyReadWordsOfShakespeare.get();

        final var expected = words.stream()
                .filter(not(String::isBlank))
                .collect(toList());

        final var actual = words.stream()
                .filter(not(String::isBlank))
                .collect(toListThrowWhenCombining());

        System.out.println("actual.size() = " + actual.size());

        assertEquals(expected, actual);
    }

    public static <T> Collector<T, List<T>, List<T>> toListThrowWhenCombining() {
        final BinaryOperator<List<T>> combiner = (l, r) -> {
            throw new UnsupportedOperationException("Combining phase not supported");
        };
        return Collector.of(ArrayList::new, List::add, combiner, Collector.Characteristics.IDENTITY_FINISH);
    }

    /**
     * These intermediate collect methods are evaluated eagerly in each step. So not very efficient
     */
    @Nested
    class StreamReturningCollectorTests {

        @Test
        void testFattenFilterMapToListByIntermediateCollectors() {
            final var museumList = TestSampleGenerator.createMuseumList();

            final var expected = museumList.stream()
                    .map(Museum::getPaintings)
                    .flatMap(Collection::stream)
                    .filter(Painting::isInMuseum)
                    .map(Painting::painter)
                    .toList();

            final var painters = museumList.stream()
                    .collect(flattenToStream(Museum::getPaintings))
                    .collect(filterToStream(Painting::isInMuseum))
                    .collect(mapToStream(Painting::painter))
                    .toList();

            expected.forEach(System.out::println);

            assertEquals(expected, painters);
        }

        @Test
        void testCollectWindowing() {
            final var nrs = IntRange.of(0, 1_000).boxed();

            final var windowSize = 135;
            final var step = 9;
            final var expected = Sequence.of(nrs)
                    .windowed(windowSize, step, true, ListX::toList)
                    .toList();

            final var painters = nrs.stream()
                    // after this collect, the stream is evaluated even-though it returns a stream again
                    .collect(windowing(windowSize, step, true))
                    .toList();

            expected.forEach(System.out::println);

            assertEquals(expected, painters);
        }
    }

    @Test
    void testNestedIntermediateCollectorsEvaluateLazy() {
        final var actual = new ArrayList<>();

        final var localDate = IntStream.of(1, 2, 3, 4)
                .mapToObj(Long::valueOf)
                .collect(
                        peeking(actual::add,
                                mapping(LocalDate::ofEpochDay,
                                        filtering(not(LocalDate::isLeapYear),
                                                forEach(actual::add)))));

        assertAll(
                () -> assertNull(localDate),
                () -> assertEquals(List.of(
                        1L, LocalDate.ofEpochDay(1L),
                        2L, LocalDate.ofEpochDay(2L),
                        3L, LocalDate.ofEpochDay(3L),
                        4L, LocalDate.ofEpochDay(4L)), actual)
        );
    }

    @Nested
    class ConcurrentCollectorTests {

        @Test
        void testGroupingByToConcurrentMap() {
            final var words = lazyReadWordsOfShakespeare.get();

            final var expected = words.parallelStream()
                    .filter(not(String::isBlank))
                    .collect(groupingByConcurrent(String::length, toSet()));

            final var actual = words.parallelStream()
                    .filter(not(String::isBlank))
                    .collect(ConcurrentCollectors.groupingByConcurrentToSet(String::length));

            System.out.println("expected.size() = " + expected.size());

            assertEquals(expected, actual);
        }

        @Test
        void testToUnorderedListConcurrent() {
            final var words = lazyReadWordsOfShakespeare.get();

            final var maxSize = 1_000;

            final var sequentialUsingConcurrentCollector = words.stream()
                    .filter(not(String::isBlank))
                    .limit(maxSize)
                    .collect(ConcurrentCollectors.toUnorderedConcurrentList());

            final var sequentialList = words.stream()
                    .filter(not(String::isBlank))
                    .limit(maxSize)
                    .collect(toList());

            final var actual = words.parallelStream()
                    .filter(not(String::isBlank))
                    .limit(maxSize)
                    .collect(ConcurrentCollectors.toUnorderedConcurrentList());

            System.out.println("list size = " + actual.size());

            assertAll(
                    () -> assertTrue(sequentialList.containsAll(actual)),
                    () -> assertTrue(actual.containsAll(sequentialList)),
                    () -> assertEquals(sequentialList, sequentialUsingConcurrentCollector)
            );
        }

        @Test
        void testCollectToConcurrentSkipListMap() {
            final var words = lazyReadWordsOfShakespeare.get();

            final var expected = words.stream()
                    .filter(not(String::isBlank))
                    .distinct()
                    .collect(toNavigableMap(It::self, String::length));

            final var actual = words.parallelStream()
                    .filter(not(String::isBlank))
                    .distinct()
                    .collect(ConcurrentCollectors.toConcurrentMap(It::self, String::length, ConcurrentSkipListMap::new));

            System.out.println("actual size = " + actual.size());

            assertEquals(expected, actual);
        }

        @Test
        void testToOrderedListConcurrent() {
            final var words = lazyReadWordsOfShakespeare.get();

            final var expected = words.stream()
                    .filter(not(String::isBlank))
                    .collect(toList());

            final var list = words.parallelStream()
                    .filter(not(String::isBlank))
                    .collect(ConcurrentCollectors.toOrderedConcurrentList());

            System.out.println("list size = " + list.size());

            assertEquals(expected, list);
        }

        @Test
        void testToConcurrentSkipListSet() {
            final var words = lazyReadWordsOfShakespeare.get();

            final var maxSize = 1_000;

            final NavigableSet<String> expected = words.stream()
                    .filter(not(String::isBlank))
                    .limit(maxSize)
                    .collect(collectingAndThen(toSet(), TreeSet::new));

            final NavigableSet<String> actual = words.parallelStream()
                    .filter(not(String::isBlank))
                    .limit(maxSize)
                    .collect(ConcurrentCollectors.toConcurrentSkipListSet());

            System.out.println("set size = " + actual.size());

            assertAll(
                    () -> assertEquals(expected, actual),
                    () -> assertTrue(Sequence.of(actual).isSortedBy(It::self))
            );
        }

        @Test
        void testToConcurrentLinkedQueue() {
            final var words = lazyReadWordsOfShakespeare.get();

            final var maxSize = 1_000;

            final var expected = words.parallelStream()
                    .filter(not(String::isBlank))
                    .limit(maxSize)
                    .toList();

            final var actual = words.parallelStream()
                    .filter(not(String::isBlank))
                    .limit(maxSize)
                    .sorted()
                    .collect(ConcurrentCollectors.toQueueConcurrent());

            System.out.println("list size = " + actual.size());

            assertAll(
                    () -> assertFalse(Sequence.of(actual).isSortedBy(It::self)),
                    () -> assertTrue(expected.containsAll(actual)),
                    () -> assertTrue(actual.containsAll(expected))
            );
        }

        @Test
        void testToConcurrentSet() {
            final var words = lazyReadWordsOfShakespeare.get();

            System.out.println("words count = " + words.size());

            final var concurrentSet = words.parallelStream()
                    .filter(not(String::isBlank))
                    .collect(ConcurrentCollectors.toSetConcurrent());

            final var set = words.stream()
                    .filter(not(String::isBlank))
                    .collect(ConcurrentCollectors.toSetConcurrent());

            final var sequentialSet = words.stream()
                    .filter(not(String::isBlank))
                    .collect(toSet());

            System.out.println("set size = " + set.size());

            assertAll(
                    () -> assertEquals(concurrentSet, sequentialSet),
                    () -> assertEquals(sequentialSet, set)
            );
        }

        @Test
        void testToConcurrentMap() {
            final var words = lazyReadWordsOfShakespeare.get();

            final var actualSequential = words.stream()
                    .filter(not(String::isBlank))
                    .distinct()
                    .collect(ConcurrentCollectors.toConcurrentMap(s -> s, String::length));

            final var expected = words.parallelStream()
                    .filter(not(String::isBlank))
                    .distinct()
                    .collect(toConcurrentMap(s -> s, String::length));

            final var actual = words.parallelStream()
                    .filter(not(String::isBlank))
                    .distinct()
                    .collect(ConcurrentCollectors.toConcurrentMap(s -> s, String::length));

            System.out.println("expected.size() = " + expected.size());

            assertAll(
                    () -> assertEquals(expected, actual),
                    () -> assertEquals(expected, actualSequential)
            );
        }

        @Test
        void testJoinConcurrent() {
            final var words = lazyReadWordsOfShakespeare.get();

            final var actualSequential = words.stream()
                    .filter(not(String::isBlank))
                    .collect(ConcurrentCollectors.concurrentJoining());

            final var expected = words.parallelStream()
                    .filter(not(String::isBlank))
                    .collect(joining());

            final var actual = words.parallelStream()
                    .filter(not(String::isBlank))
                    .collect(ConcurrentCollectors.concurrentJoining());

            System.out.println("expected.length() = " + expected.length());

            assertAll(
                    () -> assertEquals(expected.length(), actual.length()),
                    () -> assertEquals(expected, actualSequential)
            );
        }
    }

    @NotNull
    private static List<String> readWordsInWorksOfShakespeare() {
        final var userDir = Path.of(System.getProperty("user.dir")).getParent();
        final var path = userDir.resolve("_input").resolve("shakespeareworks.txt");
        println("Reading words of " + path + "...");

        try (final var lines = Files.lines(path)) {
            return lines
                    .flatMap(blankStringPattern::splitAsStream)
                    .filter(not(String::isBlank))
                    .toList();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
