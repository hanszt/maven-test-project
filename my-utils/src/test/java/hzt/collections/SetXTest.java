package hzt.collections;

import org.hzt.test.model.Painting;
import org.junit.jupiter.api.Test;
import test.Generator;
import test.model.PaintingAuction;

import java.time.Year;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

class SetXTest {

    @Test
    void testToSetYieldsUnModifiableSet() {
        PaintingAuction auction = Generator.createVanGoghAuction();
        final Year yearToAdd = Year.of(2000);

        final Set<Year> years = auction.toSetOf(Painting::getYearOfCreation);

        assertThrows(UnsupportedOperationException.class, () -> years.add(yearToAdd));
    }
}
