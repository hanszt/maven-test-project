package hzt.collections;

import org.hzt.test.TestSampleGenerator;
import org.junit.jupiter.api.Test;
import test.IterXImplGenerator;
import test.model.PaintingAuction;

import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ListXTest {

    @Test
    void testMutableListX() {
        final var museums = IterXImplGenerator.createAuctions();

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
                .takeToListWhile(museum -> museum.getPaintings().size() < 3);

        System.out.println("actual = " + actual);

        assertEquals(expected, actual);
    }
}
