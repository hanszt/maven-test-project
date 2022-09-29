package hzt.only_jdk;

import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Museum;
import org.hzt.test.model.Painter;
import org.hzt.test.model.Painting;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class Jdk17StreamMethodsTest {

    /**
     * @see <a href="https://nipafx.dev/java-16-stream-mapmulti/">Faster flatMaps with Stream::mapMulti in Java 16</a>
     */
    @Test
    void testMapMultiForOptional() {
        //arrange
        final List<Painting> paintingList = TestSampleGenerator.createPaintingList();

        final List<LocalDate> expectedDates = paintingList.stream()
                .map(Painting::painter)
                .map(Painter::getDateOfDeath)
                .flatMap(Optional::stream)
                .toList();
        //act
        final List<LocalDate> dates = paintingList.stream()
                .map(Painting::painter)
                .map(Painter::getDateOfDeath)
                .<LocalDate>mapMulti(Optional::ifPresent)
                .toList();

        final List<LocalDate> dates2 = paintingList.stream()
                .map(Painting::painter)
                .mapMulti(Painter::getDateOfDeathIfPresent)
                .toList();

        System.out.println("dates = " + dates);
        //assert
        assertEquals(expectedDates, dates);
        assertEquals(dates, dates2);
    }

    @Test
    void testMapMultiForFlatMappingAStreamOfIterables() {
        //arrange
        final var museumList = TestSampleGenerator.getMuseumListContainingNulls();

        final var expectedPaintings = museumList.stream()
                .map(Museum::getPaintings)
                .flatMap(Collection::stream)
                .toList();
        //act
        final var actualPaintings = museumList.stream()
                .map(Museum::getPaintings)
                .<Painting>mapMulti(Iterable::forEach)
                .toList();

        System.out.println("paintings = " + actualPaintings);
        //assert
        assertEquals(expectedPaintings, actualPaintings);
    }

    @Test
    void testMapMultiForMappingToOtherTypeUsingOtherStream() {
        //arrange
        final var museumList = TestSampleGenerator.getMuseumListContainingNulls();

        final var expectedDates = museumList.stream()
                .map(Museum::getPaintings)
                .flatMap(Collection::stream)
                .map(Painting::painter)
                .map(Painter::getDateOfBirth)
                .filter(Objects::nonNull)
                .toList();
        //act
        final var actualDates = museumList.stream()
                .mapMulti(Museum::toDatesOfBirthPainters)
                .toList();

        System.out.println("dates = " + actualDates);
        //assert
        assertEquals(expectedDates, actualDates);
    }

    @Test
    void testVoidMethodeTakingAConsumer() {

        final var paintingList = TestSampleGenerator.createPaintingList();
        Museum museum = new Museum("", LocalDate.of(2021, 10, 21), paintingList);

        List<LocalDate> datesOfBirth = new ArrayList<>();
        museum.toDatesOfBirthPainters(datesOfBirth::add);

        System.out.println("datesOfBirth = " + datesOfBirth);

        assertFalse(datesOfBirth.isEmpty());
    }

    @Test
    void testIntStreamMapMulti() {
        final var max = IntStream.range(0, 1_000)
                .mapMulti(this::timesTwo)
                .max();

        assertEquals(OptionalInt.of(1998), max);
    }

    private void timesTwo(int value, IntConsumer consumer) {
        consumer.accept(2 * value);
    }

}
