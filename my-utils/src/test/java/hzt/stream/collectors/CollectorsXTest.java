package hzt.stream.collectors;

import hzt.collections.MyCollections;
import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.BankAccount;
import org.hzt.test.model.Customer;
import org.hzt.test.model.Museum;
import org.hzt.test.model.Painter;
import org.hzt.test.model.Painting;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static hzt.stream.StreamUtils.nullSafe;
import static hzt.stream.collectors.BigDecimalCollectors.*;
import static hzt.stream.collectors.CollectorsX.*;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CollectorsXTest {

    @Test
    void testBranchingPaintingDataToThreeValues() {
        //arrange
        final var paintingList = TestSampleGenerator.createPaintingList();
        final double expectedAverage = paintingList.stream()
                .mapToInt(Painting::ageInYears)
                .average().orElse(0);
        final var expectedPaintingNameList = paintingList.stream()
                .map(Painting::name)
                .toList();
        //act
        final var paintingSummary = paintingList.stream()
                .collect(branching(
                        partitioningBy(Painting::isInMuseum, mappingToList(Painting::name)),
                        summarizingLong(Painting::ageInYears),
                        mappingToList(Painting::name),
                        PaintingSummary::new));

        final LongSummaryStatistics paintingAgeSummaryStatistics = paintingSummary.paintingAgeSummaryStatistics;
        final double averageAgePainting = paintingAgeSummaryStatistics.getAverage();
        final long maxAgeYears = paintingAgeSummaryStatistics.getMax();

        System.out.println("paintingAgeSummaryStatistics = " + paintingAgeSummaryStatistics);
        paintingSummary.paintingInMuseumMap.entrySet().forEach(System.out::println);
        System.out.println("PaintingSummary:");
        System.out.println(paintingSummary);
        //assert
        final List<String> titlesOfPaintingsNotInMuseum = paintingSummary.paintingInMuseumMap.get(false);
        final var expectedMaxAge = paintingList.stream().mapToLong(Painting::ageInYears).max().orElseThrow();
        assertEquals(expectedMaxAge, maxAgeYears);
        assertEquals(expectedAverage, averageAgePainting);
        assertEquals(1, titlesOfPaintingsNotInMuseum.size());
        assertEquals("Lentetuin, de pastorietuin te Nuenen in het voorjaar", titlesOfPaintingsNotInMuseum.get(0));
        assertEquals(expectedPaintingNameList, paintingSummary.paintingNameList);
    }

    @Test
    void testBranchingPaintingDataToFour() {
        //arrange
        final List<Painting> paintingList = TestSampleGenerator.createPaintingList();
        final double expectedAverage = paintingList.stream()
                .mapToInt(Painting::ageInYears)
                .average().orElse(0);
        final var expectedPaintingNameList = paintingList.stream()
                .map(Painting::name)
                .filter(s -> s.length() > 10)
                .toList();
        final var expectedGroupedByPainter = paintingList.stream()
                .collect(Collectors.groupingBy(Painting::painter));

        //act
        final var result = paintingList.stream()
                .collect(branching(
                        partitioningBy(Painting::isInMuseum, mappingToList(Painting::name)),
                        summarizingLong(Painting::ageInYears),
                        mapping(Painting::name, filteringToList(name -> name.length() > 10)),
                        groupingBy(Painting::painter)));

        final LongSummaryStatistics paintingAgeSummaryStatistics = result.second();
        final double averageAgePainting = paintingAgeSummaryStatistics.getAverage();
        final long maxAgeYears = paintingAgeSummaryStatistics.getMax();

        System.out.println(result);

        //assert
        final List<String> titlesOfPaintingsNotInMuseum = result.first().get(false);
        final var expectedMaxAge = paintingList.stream().mapToLong(Painting::ageInYears).max().orElseThrow();

        assertAll(
                () -> assertEquals(expectedMaxAge, maxAgeYears),
                () -> assertEquals(expectedAverage, averageAgePainting),
                () -> assertEquals(1, titlesOfPaintingsNotInMuseum.size()),
                () -> assertEquals("Lentetuin, de pastorietuin te Nuenen in het voorjaar", titlesOfPaintingsNotInMuseum.get(0)),
                () -> assertIterableEquals(expectedPaintingNameList, result.third()),
                () -> assertEquals(expectedGroupedByPainter, result.fourth())
        );
    }

    @Test
    void testBranchingPaintingDataToFive() {
        //arrange
        final List<Painting> paintingList = TestSampleGenerator.createPaintingList();
        final double expectedAverage = paintingList.stream()
                .mapToInt(Painting::ageInYears)
                .average().orElse(0);
        final var expectedPaintingNameList = paintingList.stream()
                .map(Painting::name)
                .filter(s -> s.length() > 10)
                .toList();
        final var expectedGroupedByPainter = paintingList.stream()
                .collect(Collectors.groupingBy(Painting::painter));

        //act
        final var result = paintingList.stream()
                .collect(branching(
                        partitioningBy(Painting::isInMuseum, mappingToList(Painting::name)),
                        summarizingLong(Painting::ageInYears),
                        mapping(Painting::name, filteringToList(name -> name.length() > 10)),
                        groupingBy(Painting::painter),
                        counting()));

        final LongSummaryStatistics paintingAgeSummaryStatistics = result.second();
        final double averageAgePainting = paintingAgeSummaryStatistics.getAverage();
        final long maxAgeYears = paintingAgeSummaryStatistics.getMax();

        System.out.println(result);

        //assert
        final List<String> titlesOfPaintingsNotInMuseum = result.first().get(false);
        final var expectedMaxAge = paintingList.stream().mapToLong(Painting::ageInYears).max().orElseThrow();
        assertAll(
                () -> assertEquals(expectedMaxAge, maxAgeYears),
                () -> assertEquals(expectedAverage, averageAgePainting),
                () -> assertEquals(1, titlesOfPaintingsNotInMuseum.size()),
                () -> assertEquals("Lentetuin, de pastorietuin te Nuenen in het voorjaar", titlesOfPaintingsNotInMuseum.get(0)),
                () -> assertEquals(expectedPaintingNameList, result.third()),
                () -> assertEquals(expectedGroupedByPainter, result.fourth()),
                () -> assertEquals(8, result.fifth())
        );
    }

    private record PaintingSummary(
            Map<Boolean, List<String>> paintingInMuseumMap,
            LongSummaryStatistics paintingAgeSummaryStatistics,
            List<String> paintingNameList,
            Map<Painter, List<Painting>> groupedByPainter) {

        @SuppressWarnings("unused")
        public PaintingSummary(Map<Boolean, List<String>> paintingInMuseumMap,
                               LongSummaryStatistics paintingAgeSummaryStatistics) {
            this(paintingInMuseumMap, paintingAgeSummaryStatistics, Collections.emptyList());
        }

        public PaintingSummary(Map<Boolean, List<String>> paintingInMuseumMap,
                               LongSummaryStatistics paintingAgeSummaryStatistics,
                               List<String> paintingNameList) {
            this(paintingInMuseumMap, paintingAgeSummaryStatistics, paintingNameList, Collections.emptyMap());
        }
    }

    @Test
    void testTeeingToEntry() {
        //arrange
        final List<Painting> paintingList = TestSampleGenerator.createPaintingList();
        final double expectedAverage = paintingList.stream()
                .mapToLong(Painting::ageInYears)
                .average().orElse(0);

        var keyCollector = flatMapping(
                nullSafe(Painting::painter, Painter::getDateOfBirth),
                summarizingLong(LocalDate::getYear));

        //act
        final Map.Entry<LongSummaryStatistics, LongSummaryStatistics> result = paintingList.stream()
                .collect(teeingToEntry(keyCollector, summarizingLong(Painting::ageInYears)));

        final LongSummaryStatistics paintingAgeSummaryStatistics = result.getValue();
        final double averageAgePainting = paintingAgeSummaryStatistics.getAverage();
        final long maxAgeYears = paintingAgeSummaryStatistics.getMax();

        System.out.println(result);

        //assert
        final var expectedMaxAge = paintingList.stream().mapToLong(Painting::ageInYears).max().orElseThrow();
        assertAll(
                () -> assertEquals(expectedMaxAge, maxAgeYears),
                () -> assertEquals(expectedAverage, averageAgePainting)
        );
    }

    @Test
    void testBranchingToBigDecimalSummaryStatistics() {
        final var sampleBankAccountListContainingNulls = TestSampleGenerator.createSampleBankAccountListContainingNulls();

        final var expected = sampleBankAccountListContainingNulls.stream()
                .collect(summarizingBigDecimal(BankAccount::getBalance));

        final var actual = sampleBankAccountListContainingNulls.stream()
                .collect(branching(
                        counting(),
                        summingBigDecimal(BankAccount::getBalance),
                        toMinBigDecimal(BankAccount::getBalance),
                        toMaxBigDecimal(BankAccount::getBalance),
                        BigDecimalSummaryStatistics::new
                ));
        assertAll(
                () -> assertNotEquals(expected.getAverage(), actual.getAverage()),
                () -> assertNotEquals(expected.getCount(), actual.getCount()),
                () -> assertEquals(expected.getMin(), actual.getMin()),
                () -> assertEquals(expected.getMax(), actual.getMax())
        );
    }

    @Test
    void testStandardDeviatingAgePainters() {
        final List<Painting> paintingList = TestSampleGenerator.createPaintingList();

        final double standardDeviationAge = paintingList.stream()
                .collect(CollectorsX.standardDeviatingDouble(Painting::ageInYears));

        final DoubleStatistics summarizingAges = paintingList.stream()
                .collect(CollectorsX.toDoubleStatisticsBy(Painting::ageInYears));

        OptionalDouble optionalAverage = paintingList.stream()
                .mapToDouble(Painting::ageInYears)
                .average();

        System.out.println("summarizingAges = " + summarizingAges);

        assertAll(
                () -> assertEquals(standardDeviationAge, summarizingAges.getStandardDeviation()),
                () -> assertEquals(optionalAverage.orElseThrow(), summarizingAges.getAverage())
        );
    }

    @Test
    void testToIntersection() {
        final var list1 = List.of("Hoi", "hoe", "het", "met", "jou", "lol");
        final var list2 = List.of("Dit", "is", "een", "zin", "Hoi", "Papa", "lol");
        final var list3 = List.of("Lalalala", "Nog meer", "zinnen", "Hoi", "Lief", "lol");
        final var list4 = List.of("Hoi", "rere", "lol", "serse", "aweaw");
        final var list5 = List.of("lol", "asdad", "wer", "werwe", "Hoi");
        final var list6 = List.of("sdfsf", "", "awr", "awr", "Hoi", "lol");

        List<List<String>> stringLists = List.of(list1, list2, list3, list4, list5, list6);

        final Set<String> expectedIntersection = MyCollections.intersect(stringLists);

        final Set<String> intersection = stringLists.stream()
                .collect(toIntersection());

        System.out.println("intersection = " + intersection);

        assertAll(
                () -> assertEquals(2, intersection.size()),
                () -> assertTrue(intersection.containsAll(Set.of("Hoi", "lol"))),
                () -> assertEquals(expectedIntersection, intersection)
        );
    }

    @Test
    void testIntersectingBy() {
        final List<Museum> museumList = TestSampleGenerator.getMuseumListContainingNulls();

        final var nameLists = museumList.stream()
                .<List<String>>mapMulti((museum, consumer) ->
                        consumer.accept(museum.getPaintings().stream()
                                .map(Painting::name)
                                .toList()))
                .toList();

        System.out.println("Name Lists");
        nameLists.forEach(System.out::println);
        System.out.println();

        var expected = nameLists.stream()
                .collect(toIntersection());

        var paintingNamesPresentInAllMuseums = museumList.stream()
                .map(Museum::getPaintings)
                .collect(intersectingBy(Painting::name));

        System.out.println("paintingNamesPresentInAllMuseums = " + paintingNamesPresentInAllMuseums);

        assertEquals(expected, paintingNamesPresentInAllMuseums);
    }

    @Test
    void testMultiMappingLargeStream() {
        final int NR_OF_ACCOUNTS = 1_000_000;

        final List<BankAccount> expectedBankAccounts = IntStream.range(0, NR_OF_ACCOUNTS)
                .boxed()
                .mapMulti(CollectorsXTest::toBankAccount)
                .toList();

        System.out.println("bankAccounts.size() = " + expectedBankAccounts.size());

        List<BankAccount> actualBankaccountList = IntStream.range(0, NR_OF_ACCOUNTS)
                .boxed()
                .collect(multiMappingToList(CollectorsXTest::toBankAccount));

        assertEquals(actualBankaccountList, expectedBankAccounts);
    }

    private static void toBankAccount(Integer value, Consumer<BankAccount> consumer) {
        final String accountNumber = String.valueOf(value);
        consumer.accept(new BankAccount(accountNumber,
                new Customer("cid" + accountNumber, "Name" + value * 2, Collections.emptyList())));
    }

}