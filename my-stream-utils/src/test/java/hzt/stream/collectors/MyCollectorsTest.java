package hzt.stream.collectors;

import hzt.stream.utils.MyCollections;
import org.hzt.TestSampleGenerator;
import org.hzt.model.BankAccount;
import org.hzt.model.Customer;
import org.hzt.model.Museum;
import org.hzt.model.Painter;
import org.hzt.model.Painting;
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
import static hzt.stream.collectors.MyCollectors.*;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MyCollectorsTest {

    @Test
    void testBranchingPaintingDataToThreeValues() {
        //arrange
        final List<Painting> paintingList = TestSampleGenerator.createPaintingList();
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
        assertEquals(363L, maxAgeYears);
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
        assertEquals(363L, maxAgeYears);
        assertEquals(expectedAverage, averageAgePainting);
        assertEquals(1, titlesOfPaintingsNotInMuseum.size());
        assertEquals("Lentetuin, de pastorietuin te Nuenen in het voorjaar", titlesOfPaintingsNotInMuseum.get(0));
        assertEquals(expectedPaintingNameList, result.third());
        assertEquals(expectedGroupedByPainter, result.fourth());
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
        assertEquals(363L, maxAgeYears);
        assertEquals(expectedAverage, averageAgePainting);
        assertEquals(1, titlesOfPaintingsNotInMuseum.size());
        assertEquals("Lentetuin, de pastorietuin te Nuenen in het voorjaar", titlesOfPaintingsNotInMuseum.get(0));
        assertEquals(expectedPaintingNameList, result.third());
        assertEquals(expectedGroupedByPainter, result.fourth());
        assertEquals(8, result.fifth());
    }

    private static record PaintingSummary(
            Map<Boolean, List<String>> paintingInMuseumMap,
            LongSummaryStatistics paintingAgeSummaryStatistics,
            List<String> paintingNameList,
            Map<Painter, List<Painting>> groupedByPainter) {

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
        assertEquals(363L, maxAgeYears);
        assertEquals(expectedAverage, averageAgePainting);
    }

    @Test
    void testBranchingToBigDecimalSummaryStatistics() {
        final var sampleBankAccountListContainingNulls = TestSampleGenerator.createSampleBankAccountListContainingNulls();

        final var expected = sampleBankAccountListContainingNulls.stream()
                .collect(BigDecimalCollectors.summarizingBigDecimal(BankAccount::getBalance));

        final var actual = sampleBankAccountListContainingNulls.stream()
                .collect(branching(
                        counting(),
                        BigDecimalCollectors.summingBigDecimal(BankAccount::getBalance),
                        BigDecimalCollectors.toMinBigDecimal(BankAccount::getBalance),
                        BigDecimalCollectors.toMaxBigDecimal(BankAccount::getBalance),
                        BigDecimalSummaryStatistics::new
                ));

        assertNotEquals(expected.getAverage(), actual.getAverage());
        assertNotEquals(expected.getCount(), actual.getCount());
        assertEquals(expected.getMin(), actual.getMin());
        assertEquals(expected.getMax(), actual.getMax());
    }

    @Test
    void testStandardDeviatingAgePainters() {
        final List<Painting> paintingList = TestSampleGenerator.createPaintingList();

        final double standardDeviationAge = paintingList.stream()
                .collect(MyCollectors.standardDeviatingDouble(Painting::ageInYears));

        final DoubleStatistics summarizingAges = paintingList.stream()
                .collect(MyCollectors.toDoubleStatisticsBy(Painting::ageInYears));

        OptionalDouble optionalAverage = paintingList.stream()
                .mapToDouble(Painting::ageInYears)
                .average();

        System.out.println("summarizingAges = " + summarizingAges);

        assertEquals(standardDeviationAge, summarizingAges.getStandardDeviation());
        assertEquals(optionalAverage.orElseThrow(), summarizingAges.getAverage());
    }

    @Test
    void testToIntersection() {
        List<String> list1 = List.of("Hoi", "hoe", "het", "met", "jou", "lol");
        List<String> list2 = List.of("Dit", "is", "een", "zin", "Hoi", "Papa", "lol");
        List<String> list3 = List.of("Lalalala", "Nog meer", "zinnen", "Hoi", "Lief", "lol");
        List<String> list4 = List.of("Hoi", "rere", "lol", "serse", "aweaw");
        List<String> list5 = List.of("lol", "asdad", "wer", "werwe", "Hoi");
        List<String> list6 = List.of("sdfsf", "", "awr", "awr", "Hoi", "lol");

        List<List<String>> stringLists = List.of(list1, list2, list3, list4, list5, list6);

        final Set<String> expectedIntersection = MyCollections.intersect(stringLists);

        final Set<String> intersection = stringLists.stream()
                .collect(toIntersection());

        final var grouping = stringLists.stream()
                .collect(groupingBy(List::size));

        System.out.println("intersection = " + intersection);

        assertEquals(2, intersection.size());
        assertTrue(intersection.containsAll(Set.of("Hoi", "lol")));
        assertEquals(expectedIntersection, intersection);
    }

    @Test
    void testIntersectingBy() {
        final List<Museum> museumList = TestSampleGenerator.createMuseumList();

        final var nameLists = museumList.stream()
                .<List<String>>mapMulti((museum, consumer) ->
                        consumer.accept(museum.getPaintingList().stream()
                                .map(Painting::name)
                                .toList()))
                .toList();

        System.out.println("Name Lists");
        nameLists.forEach(System.out::println);
        System.out.println();

        var expected = nameLists.stream()
                            .collect(toIntersection());

        var paintingNamesPresentInAllMuseums = museumList.stream()
                .map(Museum::getPaintingList)
                .collect(intersectingBy(Painting::name));

        System.out.println("paintingNamesPresentInAllMuseums = " + paintingNamesPresentInAllMuseums);

        assertEquals(expected, paintingNamesPresentInAllMuseums);
    }

    @Test
    void testMultiMappingLargeStream() {
        final int NR_OF_ACCOUNTS = 1_000_000;

        final List<BankAccount> expectedBankAccounts = IntStream.range(0, NR_OF_ACCOUNTS)
                .boxed()
                .mapMulti(MyCollectorsTest::toBankAccount)
                .toList();

        System.out.println("bankAccounts.size() = " + expectedBankAccounts.size());

        List<BankAccount> actualBankaccountList = IntStream.range(0, NR_OF_ACCOUNTS)
                .boxed()
                .collect(multiMappingToList(MyCollectorsTest::toBankAccount));

        assertEquals(actualBankaccountList.size(), expectedBankAccounts.size());
        assertEquals(actualBankaccountList, expectedBankAccounts);
    }

    private static void toBankAccount(Integer value, Consumer<BankAccount> consumer) {
        final String accountNumber = String.valueOf(value);
        consumer.accept(new BankAccount(accountNumber,
                new Customer("cid" + accountNumber, "Name" + value * 2, Collections.emptyList())));
    }

}
