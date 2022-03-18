package org.hzt.collectors_samples;

import org.hzt.model.Employee;
import org.hzt.model.Person;
import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Book;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.*;
import static org.hzt.utils.collectors.BigDecimalCollectors.averagingBigDecimal;
import static org.hzt.utils.collectors.BigDecimalCollectors.summarizingBigDecimal;
import static org.hzt.utils.function.predicates.StringPredicates.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CollectorSamplesTest {

    private final CollectorSamples collectorSamples = new CollectorSamples();

    @Test
    void testGetExpectedWhenOnlyOneInList() {
        var expected = new CashBalance("abn", true);
        var items = List.of(
                expected,
                new CashBalance("ing", false),
                new CashBalance("triodos", false));
        var actual = collectorSamples.collectingAndThenToFirstElementIfSizeOne(items).orElse(null);
        items.forEach(System.out::println);
        assertEquals(expected, actual);
    }

    @Test
    void testGetNullWhenMoreThanOneInList() {
        var expected = new CashBalance("abn", true);
        var actual = collectorSamples.collectingAndThenToFirstElementIfSizeOne(List.of(
                expected,
                new CashBalance("ing", true),
                new CashBalance("triodos", false)))
                .orElse(null);
        assertNull(actual);
    }

    @Test
    void testGetNullWhenEmptyList() {
        var actual = collectorSamples
                .collectingAndThenToFirstElementIfSizeOne(Collections.emptyList())
                .orElse(null);
        assertNull(actual);
    }

    @Test
    void testReduceGetExpectedWhenOnlyOneInList() {
        var expected = new CashBalance("abn", true);
        var actual = collectorSamples.reduce(List.of(
                expected,
                new CashBalance("ing", false),
                new CashBalance("triodos", false)), CashBalance::isOpening);
        assertEquals(expected, actual);
    }

    @Test
    void testReduceThrowsIllegalStateExceptionWhenMoreThanOneInList() {
        var list = List.of(
                new CashBalance("abn", true),
                new CashBalance("ing", true),
                new CashBalance("triodos", false));
        assertThrows(CollectorSamples.MoreThanOneElementException.class,
                () -> collectorSamples.reduce(list, CashBalance::isOpening));
    }

    @Test
    void testReduceGetNullWhenEmptyList() {
        assertNull(collectorSamples.reduce(Collections.emptyList(), CashBalance::isOpening));
    }

    @Test
    void testReduceGetNullWhenIsOpeningAllFalse() {
        var list = List.of(
                new CashBalance("abn", false),
                new CashBalance("ing", false),
                new CashBalance("triodos", false));
        assertNull(collectorSamples.reduce(list, CashBalance::isOpening));
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
        assertEquals(average, summaryStatistics.getAverage());
        assertEquals(min, summaryStatistics.getMin());
        assertEquals(max, summaryStatistics.getMax());
        assertEquals(list.size(), (int) summaryStatistics.getCount());
        assertEquals(new BigDecimal("10000"), summaryStatistics.getSum());
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

        assertEquals(List.of("hoe", "het", "een", "test", "hoe", "je"), partitionedByMap.get(true));
        assertEquals(List.of("hallo", "is", "?", "dit", "is", "vind", "dat"), partitionedByMap.get(false));
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
        assertTrue(bookTitleByCategory.get("Fiction")
                .containsAll(List.of("Harry Potter", "Lord of the Rings", "The da Vinci Code")));
        assertTrue(bookTitleByCategory.get("Programming")
                .containsAll(List.of("Pragmatic Programmer", "OCP 11 Volume 1")));
        assertTrue(bookTitleByCategory.get("Educational")
                .contains("Homo Deus"));
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
        assertEquals(expectedKeySet, groupedByLastNamePlayingPianoToFirstName.keySet());
        assertEquals(Set.of("Sophie", "Hans", "Henk", "Nikolai"), peoplePlayingPiano);
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
        assertEquals(expectedKeySet, groupedByLastNamePlayingPianoToFirstName);
        assertEquals(groupedByLastNamePlayingPianoToFirstName, groupedByLastNamePlayingPianoToFirstName2);
    }

}
