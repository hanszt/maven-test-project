package hzt.collections;

import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Museum;
import org.hzt.test.model.Painting;
import org.junit.jupiter.api.Test;
import test.Generator;
import test.model.PaintingAuction;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ListXTest {

    @Test
    void testMutableListX() {
        final MutableListX<PaintingAuction> museums = Generator.createAuctions().toMutableList();

        final List<LocalDate> expected = museums.stream()
                .map(PaintingAuction::getDateOfOpening)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        final ListX<LocalDate> dates = museums.map(PaintingAuction::getDateOfOpening).toListX();

        assertEquals(expected, dates);
    }

    @Test
    void testTakeWhile() {
        final List<Museum> museumList = TestSampleGenerator.getMuseumListContainingNulls();

//        final List<Museum> expected = museumList.stream()
//                .takeWhile(museum -> museum.getPaintings().size() < 3).collect(Collectors.toList());

        final MutableListX<Museum> actual = ListX.of(museumList)
                .takeToListXWhile(museum -> museum.getPaintings().size() < 3).toMutableList();

        System.out.println("actual = " + actual);

        assertTrue(actual.isNotEmpty());
    }

    @Test
    void testBinarySearch() {
        final ListX<Integer> sortedList = ListX.of(-1, 0, 1, 2, 3, 4, 5);

        int valueToSearchFor = 2;

        final int indexInSortedList = sortedList.binarySearch(value -> value.compareTo(valueToSearchFor));

        assertEquals(3, indexInSortedList);
    }

    @Test
    void testBinaryOfStringSearch() {
        final MutableListX<String> sortedList = MutableListX.of("adi", "hans", "huib", "sophie", "ted");

        final int indexInSortedList = sortedList.binarySearch(string -> string.compareTo("sophie"));
        final int invertedInsertionPoint = sortedList.binarySearch(string -> string.compareTo("matthijs"));
        // the inverted insertion point (-insertion point - 1)
        final int insertionIndex = -invertedInsertionPoint - 1;

        assertAll(
                () -> assertEquals(3, indexInSortedList),
                () -> assertEquals(3, insertionIndex)
        );
    }

    @Test
    void testToListYieldsUnModifiableList() {
        PaintingAuction auction = Generator.createVanGoghAuction();
        final Year yearToAdd = Year.of(2000);

        final List<Year> years = auction.toListOf(Painting::getYearOfCreation);

        assertThrows(UnsupportedOperationException.class, () -> years.add(yearToAdd));
    }

    @Test
    void testRandomWithinBound() {
        final ListX<Integer> integers = ListX.of(1, 2, 3, 4, 5);
        final MutableMapX<Integer, MutableListX<Integer>> group = IterableX.iterate(integers::random, list -> list.size() < 10_000).group();

        assertAll(
                () -> assertEquals(integers.size(), group.size()),
                () -> group.values().forEach(list -> assertTrue(list.isNotEmpty()))
        );
    }

    @Test
    void buildList() {
        final ListX<String> strings = ListX.build(this::getStringList);

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
