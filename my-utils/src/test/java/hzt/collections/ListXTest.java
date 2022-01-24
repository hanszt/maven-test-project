package hzt.collections;

import org.hzt.test.TestSampleGenerator;
import org.junit.jupiter.api.Test;
import test.IterXImplGenerator;
import test.model.PaintingAuction;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ListXTest {

    @Test
    void testMutableListX() {
        final var museums = IterXImplGenerator.createAuctions().toMutableList();

        final var expected = museums.stream()
                .map(PaintingAuction::getDateOfOpening)
                .toList();

        final var dates = museums.map(PaintingAuction::getDateOfOpening);

        assertEquals(expected, dates);
    }

    @Test
    void testTakeWhile() {
        final var museumList = TestSampleGenerator.getMuseumListContainingNulls();

        final var expected = museumList.stream()
                .takeWhile(museum -> museum.getPaintings().size() < 3).toList();

        final var actual = ListX.of(museumList)
                .takeToListXWhile(museum -> museum.getPaintings().size() < 3).toMutableList();

        System.out.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testBinarySearch() {
        final var sortedList = ListX.of(-1, 0, 1, 2, 3, 4, 5);

        int valueToSearchFor = 2;

        final var indexInSortedList = sortedList.binarySearch(i -> i - valueToSearchFor);

        assertEquals(3, indexInSortedList);
    }

    @Test
    void testBinaryOfStringSearch() {
        final var sortedList = MutableListX.of("adi", "hans", "huib", "sophie", "ted");

        final var indexInSortedList = sortedList.binarySearch(string -> string.compareTo("sophie"));
        final var invertedInsertionPoint = sortedList.binarySearch(string -> string.compareTo("matthijs"));
        // the inverted insertion point (-insertion point - 1)
        final var insertionIndex = -invertedInsertionPoint - 1;

        assertEquals(3, indexInSortedList);
        assertEquals(3, insertionIndex);
    }
}
