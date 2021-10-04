package hzt.stream.collectors;

import org.hzt.TestSampleGenerator;
import org.hzt.model.Painter;
import org.hzt.model.Painting;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.stream.Collectors;

import static hzt.stream.StreamUtils.nullSafe;
import static hzt.stream.collectors.MyCollectors.branching;
import static hzt.stream.collectors.MyCollectors.filteringToList;
import static hzt.stream.collectors.MyCollectors.mappingToList;
import static hzt.stream.collectors.MyCollectors.teeingToEntry;
import static java.util.stream.Collectors.flatMapping;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.summarizingLong;
import static org.junit.jupiter.api.Assertions.assertEquals;

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


}
