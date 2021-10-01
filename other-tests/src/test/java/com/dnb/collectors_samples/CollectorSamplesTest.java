package com.dnb.collectors_samples;

import com.dnb.TestSampleProvider;
import com.dnb.model.Book;
import com.dnb.model.Employee;
import com.dnb.model.Painting;
import com.dnb.model.Person;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static hzt.stream.StreamUtils.by;
import static hzt.stream.collectors.MyCollectors.toBigDecimalAverage;
import static hzt.stream.collectors.MyCollectors.toBigDecimalSummaryStatistics;
import static hzt.stream.predicates.ComparingPredicates.greaterThan;
import static hzt.stream.predicates.ComparingPredicates.greaterThanInt;
import static hzt.stream.predicates.StringPredicates.contains;
import static java.util.AbstractMap.SimpleEntry;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
        var actual = list.stream().collect(toBigDecimalAverage());
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
                .collect(toBigDecimalSummaryStatistics());
        assertEquals(average, summaryStatistics.getAverage());
        assertEquals(min, summaryStatistics.min());
        assertEquals(max, summaryStatistics.max());
        assertEquals(list.size(), (int) summaryStatistics.count());
        assertEquals(new BigDecimal("10000"), summaryStatistics.sum());
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
        final var countByBookCategory = TestSampleProvider.createBookList().stream()
                .collect(groupingBy(Book::getCategory,
                        collectingAndThen(counting(),
                                Long::intValue)));
        //assert
        assertEquals(Map.of("Programming", 2, "Educational", 1, "Fiction", 4), countByBookCategory);
    }

    @Test
    void testGroupingByThenMappingToTitleCollectingToList() {
        //act
        final var bookTitleByCategory = TestSampleProvider.createBookList().stream()
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
        final var personList = TestSampleProvider.createTestPersonList();
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
        final var groupedByLastNamePlayingPianoToFirstName = TestSampleProvider.createTestPersonList().stream()
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
        final var listOfPersonLists = List.of(TestSampleProvider.createTestPersonList(), otherPersonList);
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

    @Test
    void testTeeing() {
        final var setListSimpleEntry = TestSampleProvider.createTestPersonList().stream()
                .collect(teeing(filtering(Person::isPlayingPiano,
                                toUnmodifiableSet()),
                        mapping(Person::getAge, toUnmodifiableList()),
                        SimpleEntry::new));

        System.out.println(setListSimpleEntry);
        assertFalse(setListSimpleEntry.getKey().isEmpty());
        assertFalse(setListSimpleEntry.getValue().isEmpty());
    }

    @Test
    void testInceptionCollecting() {
        final var testPersonList = TestSampleProvider.createTestPersonList();

        final var personSummaryStatistics = testPersonList.stream()
                .collect(teeing(
                        filtering(by(Person::getAge, greaterThan(30)),
                                teeing(counting(), summingLong(Person::getAge),
                                        SimpleEntry::new)),
                        filtering(Person::isPlayingPiano,
                                teeing(maxBy(comparing(Person::getAge)),
                                        minBy(comparing(Person::getAge)),
                                        SimpleEntry::new)),
                        PersonStatistics::new));

        final var summaryStatistics = testPersonList.stream()
                .mapToInt(Person::getAge)
                .filter(greaterThanInt(30))
                .summaryStatistics();
        System.out.println(personSummaryStatistics);
        assertEquals(personSummaryStatistics.getAverageAge(), summaryStatistics.getAverage());
        assertEquals(personSummaryStatistics.getAgesSumPersonsOlderThan30(), summaryStatistics.getSum());
    }

    private static class PersonStatistics {

        private final long nrOfPersonsOlderThan30;
        private final long agesSumPersonsOlderThan30;
        private final Person minAgePersonPlayingPiano;
        private final Person maxAgePersonPlayingPiano;

        public PersonStatistics(SimpleEntry<Long, Long> simpleEntry1,
                                SimpleEntry<Optional<Person>, Optional<Person>> simpleEntry2) {
            this.nrOfPersonsOlderThan30 = simpleEntry1.getKey();
            this.agesSumPersonsOlderThan30 = simpleEntry1.getValue();
            this.minAgePersonPlayingPiano = simpleEntry2.getKey().orElse(null);
            this.maxAgePersonPlayingPiano = simpleEntry2.getValue().orElse(null);
        }

        public long getAgesSumPersonsOlderThan30() {
            return agesSumPersonsOlderThan30;
        }

        public double getAverageAge() {
            return nrOfPersonsOlderThan30 > 0 ? (double) agesSumPersonsOlderThan30 / nrOfPersonsOlderThan30 : 0.0d;
        }

        @Override
        public String toString() {
            return "PersonStatistics{" +
                    "nrOfPersonsOlderThan30=" + nrOfPersonsOlderThan30 +
                    ", agesSumPersonsOlderThan30=" + agesSumPersonsOlderThan30 +
                    ", minAgePersonPlayingPiano=" + minAgePersonPlayingPiano +
                    ", maxAgePersonPlayingPiano=" + maxAgePersonPlayingPiano +
                    '}';
        }
    }

    @Test
    void testTeeingByPaintingData() {
        //arrange
        final List<Painting> paintingList = TestSampleProvider.getPaintingList();
        final double expectedAverage = paintingList.stream()
                .mapToInt(Painting::ageInYears)
                .average().orElse(0);
        //act
        final var result = paintingList.stream()
                .collect(teeing(
                        partitioningBy(Painting::isInMuseum, mapping(Painting::getName, toList())),
                        summarizingLong(Painting::ageInYears),
                        AbstractMap.SimpleEntry::new));

        final Map<Boolean, List<String>> paintingNameInMuseumMap = result.getKey();
        final LongSummaryStatistics paintingAgeSummaryStatistics = result.getValue();

        final double averageAgePainting = paintingAgeSummaryStatistics.getAverage();
        final long maxAgeYears = paintingAgeSummaryStatistics.getMax();

        System.out.println("paintingAgeSummaryStatistics = " + paintingAgeSummaryStatistics);
        paintingNameInMuseumMap.entrySet().forEach(System.out::println);
        //assert
        final List<String> titlesOfPaintingsNotInMuseum = paintingNameInMuseumMap.get(false);
        assertEquals(363L, maxAgeYears);
        assertEquals(expectedAverage, averageAgePainting);
        assertEquals(1, titlesOfPaintingsNotInMuseum.size());
        assertEquals("Lentetuin, de pastorietuin te Nuenen in het voorjaar", titlesOfPaintingsNotInMuseum.get(0));
    }


}
