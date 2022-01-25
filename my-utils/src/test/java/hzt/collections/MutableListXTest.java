package hzt.collections;

import org.junit.jupiter.api.Test;
import test.Generator;
import test.model.PaintingAuction;

import java.time.LocalDate;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MutableListXTest {

    @Test
    void testMutableListX() {
        final var museums = Generator.createAuctions().toMutableList();

        final var expected = museums.stream()
                .map(PaintingAuction::getDateOfOpening)
                .toList();

        final var dates = museums.map(PaintingAuction::getDateOfOpening);

        assertEquals(expected, dates);
    }

    @Test
    void testListWithAll() {
        final var museums = Generator.createAuctions().toMutableList();

        final var expected = museums.stream()
                .map(PaintingAuction::getDateOfOpening)
                .collect(Collectors.toList());
        expected.add(LocalDate.EPOCH);
        expected.add(LocalDate.MAX);

        final var dates = museums
                .map(PaintingAuction::getDateOfOpening)
                .plus(ListX.of(LocalDate.EPOCH, LocalDate.MAX));

        System.out.println("dates = " + dates);

        assertEquals(expected, dates);
    }

    @Test
    void testAlso() {
        final var museums = Generator.createAuctions().toMutableList();

        final var expected = museums.stream()
                .map(PaintingAuction::getDateOfOpening)
                .collect(Collectors.toList());
        expected.add(LocalDate.MIN);

        final var dates = museums
                .map(PaintingAuction::getDateOfOpening)
                .also(localDates -> localDates.add(LocalDate.MIN));

        System.out.println("dates = " + dates);

        assertEquals(expected, dates);
    }

    @Test
    void testWhen() {
        final var museums = Generator.createAuctions().toMutableList();

        final var expected = museums.stream()
                .map(PaintingAuction::getDateOfOpening)
                .collect(Collectors.toList());

        final var dates = museums
                .map(PaintingAuction::getDateOfOpening)
                .when(ListX::isEmpty, list -> list.add(LocalDate.MIN));

        System.out.println("dates = " + dates);

        assertEquals(expected, dates);
    }

}
