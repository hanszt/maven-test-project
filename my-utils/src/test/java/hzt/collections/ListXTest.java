package hzt.collections;

import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Painting;
import org.junit.jupiter.api.Test;
import test.Generator;
import test.model.PaintingAuction;

import java.time.LocalDate;
import java.time.Year;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ListXTest {

    @Test
    void testMutableListX() {
        final var museums = Generator.createAuctions().toMutableList();

        final var expected = museums.stream()
                .map(PaintingAuction::getDateOfOpening)
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableList());

        final var dates = museums.map(PaintingAuction::getDateOfOpening).toListX();

        assertEquals(expected, dates);
    }

    @Test
    void testTakeWhile() {
        final var museumList = TestSampleGenerator.getMuseumListContainingNulls();

        final var expected = museumList.stream()
                .takeWhile(museum -> museum.getPaintings().size() < 3).collect(Collectors.toUnmodifiableList());

        final var actual = ListX.of(museumList)
                .takeToListXWhile(museum -> museum.getPaintings().size() < 3).toMutableList();

        System.out.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testBinarySearch() {
        final var sortedList = ListX.of(-1, 0, 1, 2, 3, 4, 5);

        int valueToSearchFor = 2;

        final var indexInSortedList = sortedList.binarySearch(value -> value.compareTo(valueToSearchFor));

        assertEquals(3, indexInSortedList);
    }

    @Test
    void testBinaryOfStringSearch() {
        final var sortedList = MutableListX.of("adi", "hans", "huib", "sophie", "ted");

        final var indexInSortedList = sortedList.binarySearch(string -> string.compareTo("sophie"));
        final var invertedInsertionPoint = sortedList.binarySearch(string -> string.compareTo("matthijs"));
        // the inverted insertion point (-insertion point - 1)
        final var insertionIndex = -invertedInsertionPoint - 1;

        assertAll(
                () -> assertEquals(3, indexInSortedList),
                () -> assertEquals(3, insertionIndex)
        );
    }

    @Test
    void testToListYieldsUnModifiableList() {
        var auction = Generator.createVanGoghAuction();
        final var yearToAdd = Year.of(2000);

        final var years = auction.toListOf(Painting::getYearOfCreation);

        assertThrows(UnsupportedOperationException.class, () -> years.add(yearToAdd));
    }

    @Test
    void testRandomWithinBound() {
        final var integers = ListX.of(1, 2, 3, 4, 5);
        final var group = IterableX.iterate(integers::random, list -> list.size() < 10_000).group();

        assertAll(
                () -> assertEquals(integers.size(), group.size()),
                () -> group.values().forEach(list -> assertTrue(list.isNotEmpty()))
        );
    }

    @Test
    void buildList() {
        final var strings = ListX.build(this::getStringList);

        assertAll(
                () -> assertEquals(101, strings.size()),
                () -> assertTrue(strings.contains("Hallo"))
        );
    }

    private void getStringList(MutableListX<String> list) {
        for (int i = 0; i < 100; i++) {
            list.add(String.valueOf(i));
        }
        list.add(1, "Hallo");
    }
}
