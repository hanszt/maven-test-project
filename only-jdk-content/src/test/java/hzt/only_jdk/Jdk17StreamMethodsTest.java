package hzt.only_jdk;

import org.hzt.TestSampleGenerator;
import org.hzt.model.Museum;
import org.hzt.model.Painter;
import org.hzt.model.Painting;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class Jdk17StreamMethodsTest {

    /**
     * https://nipafx.dev/java-16-stream-mapmulti/
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

        System.out.println("dates = " + dates);
        //assert
        assertEquals(expectedDates, dates);
    }

    @Test
    void testMapMultiForFlatMappingAStreamOfIterables() {
        //arrange
        final var museumList = TestSampleGenerator.createMuseumList();

        final var expectedPaintings = museumList.stream()
                .map(Museum::getPaintingList)
                .flatMap(Collection::stream)
                .toList();
        //act
        final var actualPaintings = museumList.stream()
                .map(Museum::getPaintingList)
                .<Painting>mapMulti(Iterable::forEach)
                .toList();

        System.out.println("paintings = " + actualPaintings);
        //assert
        assertEquals(expectedPaintings, actualPaintings);
    }

    @Test
    void testMapMultiForMappingToOtherTypeUsingOtherStream() {
        //arrange
        final var museumList = TestSampleGenerator.createMuseumList();

        final var expectedDates = museumList.stream()
                .map(Museum::getPaintingList)
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

    //     (Functional?) programming question. What's that collection/stream/array operation called that processes a variable number of input elements to a single result?
//    https://twitter.com/nipafx/status/1319656592925708289
}
