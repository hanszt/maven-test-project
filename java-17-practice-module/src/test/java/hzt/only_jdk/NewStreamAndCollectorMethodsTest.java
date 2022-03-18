package hzt.only_jdk;

import org.hzt.concurrent.CompletableFutureSample;
import org.hzt.model.Person;
import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Painting;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static hzt.function.predicates.ComparingPredicates.greaterThan;
import static hzt.function.predicates.ComparingPredicates.greaterThanInt;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.*;
import static org.hzt.stream.StreamUtils.by;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class NewStreamAndCollectorMethodsTest {

    @Test
    void testTeeing() {
        final var entry = Person.createTestPersonList().stream()
                .collect(teeing(filtering(Person::isPlayingPiano,
                                toUnmodifiableSet()),
                        mapping(Person::getAge, toUnmodifiableList()),
                        Map::entry));

        System.out.println(entry);
        assertFalse(entry.getKey().isEmpty());
        assertFalse(entry.getValue().isEmpty());
    }

    @Test
    void testInceptionCollecting() {
        final var testPersonList = Person.createTestPersonList();

        final var personSummaryStatistics = testPersonList.stream()
                .collect(teeing(
                        filtering(by(Person::getAge, greaterThan(30)),
                                teeing(counting(), summingLong(Person::getAge),
                                        Map::entry)),
                        filtering(Person::isPlayingPiano,
                                teeing(maxBy(comparing(Person::getAge)),
                                        minBy(comparing(Person::getAge)),
                                        Map::entry)),
                        PersonStatistics::new));

        final var summaryStatistics = testPersonList.stream()
                .mapToInt(Person::getAge)
                .filter(greaterThanInt(30))
                .summaryStatistics();
        System.out.println(personSummaryStatistics);
        assertEquals(personSummaryStatistics.getAverageAge(), summaryStatistics.getAverage());
        assertEquals(personSummaryStatistics.getAgesSumPersonsOlderThan30(), summaryStatistics.getSum());
    }

    @Test
    void testTeeingByPaintingData() {
        //arrange
        final List<Painting> paintingList = TestSampleGenerator.createPaintingList();
        final double expectedAverage = paintingList.stream()
                .mapToInt(Painting::ageInYears)
                .average().orElse(0);
        //act
        final var result = paintingList.stream()
                .collect(teeing(
                        partitioningBy(Painting::isInMuseum, mapping(Painting::name, toList())),
                        summarizingLong(Painting::ageInYears),
                        Map::entry));

        final Map<Boolean, List<String>> paintingNameInMuseumMap = result.getKey();
        final LongSummaryStatistics paintingAgeSummaryStatistics = result.getValue();

        final double averageAgePainting = paintingAgeSummaryStatistics.getAverage();
        final long maxAgeYears = paintingAgeSummaryStatistics.getMax();

        System.out.println("paintingAgeSummaryStatistics = " + paintingAgeSummaryStatistics);
        paintingNameInMuseumMap.entrySet().forEach(System.out::println);
        //assert
        final List<String> titlesOfPaintingsNotInMuseum = paintingNameInMuseumMap.get(false);

        assertEquals(paintingList.stream().mapToLong(Painting::ageInYears).max().orElseThrow(), maxAgeYears);
        assertEquals(expectedAverage, averageAgePainting);
        assertEquals(1, titlesOfPaintingsNotInMuseum.size());
        assertEquals("Lentetuin, de pastorietuin te Nuenen in het voorjaar", titlesOfPaintingsNotInMuseum.get(0));
    }

    @Test
    void testCompletableFuturesListResultUsingMapMulti() {
        CompletableFutureSample completableFutureSample = new CompletableFutureSample();

        final List<CompletableFuture<Integer>> completableFutures = List.of(
                completableFutureSample.getStockPriceAndThenCombine(),
                completableFutureSample.getStockPriceThenComposeAndThanCombine());

        final List<Integer> stockPrices = completableFutures.stream()
                .<Integer>mapMulti(CompletableFuture::thenAccept)
                .toList();

        assertEquals(List.of(1500, 1500), stockPrices);
    }

    private static class PersonStatistics {

        private final long nrOfPersonsOlderThan30;
        private final long agesSumPersonsOlderThan30;
        private final Person minAgePersonPlayingPiano;
        private final Person maxAgePersonPlayingPiano;

        public PersonStatistics(Map.Entry<Long, Long> simpleEntry1,
                                Map.Entry<Optional<Person>, Optional<Person>> simpleEntry2) {
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
}
