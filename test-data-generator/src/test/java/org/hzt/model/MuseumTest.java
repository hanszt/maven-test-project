package org.hzt.model;

import org.hzt.TestSampleGenerator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

class MuseumTest {

    @Test
    void testIteratingOverPaintingsInMuseum() {
        final List<Museum> museumList = TestSampleGenerator.createMuseumList();

        final Museum firstMuseum = museumList.stream()
                .findFirst()
                .orElseThrow();

        firstMuseum.forEach(System.out::println);

        final List<Painting> paintings = museumList.stream()
                .filter(museum -> museum.getDateOfOpening().isBefore(LocalDate.now()))
                .mapMulti(Museum::forEach)
                .filter(painting -> painting.ageInYears() > 200)
                .toList();

        System.out.println("paintings = " + paintings);

        assertFalse(paintings.isEmpty());
    }

    @Test
    void testCreatingPaintingStreamFromMuseumSpliterator() {
        final List<Museum> museumList = TestSampleGenerator.createMuseumList();

        final Museum firstMuseum = museumList.stream()
                .findFirst()
                .orElseThrow();

        final List<Painting> paintings = StreamSupport.stream(firstMuseum.spliterator(), false)
                .toList();

        assertFalse(paintings.isEmpty());
    }

}
