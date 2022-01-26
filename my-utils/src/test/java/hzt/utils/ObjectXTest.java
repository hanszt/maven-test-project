package hzt.utils;

import org.hzt.test.model.Painting;
import org.junit.jupiter.api.Test;
import test.Generator;
import test.model.PaintingAuction;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ObjectXTest {

    @Test
    void testCreateAnObjectXOfSomethingAndUseTheDefaultFunctions() {
        final LocalDate localDate = ObjectX.of(Generator.createVanGoghAuction())
                .apply(a -> a.setMostPopularPainting(Painting.of("Nijntje")))
                .let(PaintingAuction::getDateOfOpening);

        assertEquals(2, localDate.getDayOfMonth());
    }
}
