package hzt.stream.collectors;

import hzt.stream.TestSampleGenerator;
import hzt.stream.model.Painter;
import hzt.stream.model.Painting;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.stream.Collectors;

import static hzt.stream.collectors.MyCollectors.branching;
import static java.util.stream.Collectors.*;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toUnmodifiableList;
import static org.junit.jupiter.api.Assertions.*;

class MyCollectorsTest {

    @Test
    void testBranchingPaintingDataToThreeValues() {
        //arrange
        final List<Painting> paintingList = TestSampleGenerator.getPaintingList();
        final double expectedAverage = paintingList.stream()
                .mapToInt(Painting::ageInYears)
                .average().orElse(0);
        final var expectedPaintingNameList = paintingList.stream()
                .map(Painting::getName)
                .toList();
        //act
        final var paintingSummary = paintingList.stream()
                .collect(branching(
                        partitioningBy(Painting::isInMuseum, mapping(Painting::getName, toList())),
                        summarizingLong(Painting::ageInYears),
                        mapping(Painting::getName, toUnmodifiableList()),
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
        final List<Painting> paintingList = TestSampleGenerator.getPaintingList();
        final double expectedAverage = paintingList.stream()
                .mapToInt(Painting::ageInYears)
                .average().orElse(0);
        final var expectedPaintingNameList = paintingList.stream()
                .map(Painting::getName)
                .filter(s -> s.length() > 10)
                .toList();
        final var expectedGroupedByPainter = paintingList.stream()
                .collect(Collectors.groupingBy(Painting::getPainter));

        //act
        final var result = paintingList.stream()
                .collect(branching(
                        partitioningBy(Painting::isInMuseum, mapping(Painting::getName, toList())),
                        summarizingLong(Painting::ageInYears),
                        mapping(Painting::getName, filtering(s -> s.length() > 10, toUnmodifiableList())),
                        groupingBy(Painting::getPainter),
                        MyCollectors::toQuadTuple));

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
}
