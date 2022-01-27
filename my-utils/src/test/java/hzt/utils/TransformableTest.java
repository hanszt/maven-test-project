package hzt.utils;

import hzt.collections.MutableListX;
import org.hzt.test.model.Painter;
import org.hzt.test.model.Painting;
import org.junit.jupiter.api.Test;
import test.Generator;
import test.model.PaintingAuction;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransformableTest {

    @Test
    void testCreateAnObjectXOfSomethingAndUseTheDefaultFunctions() {
        final var vanGoghAuction = Generator.createVanGoghAuction();

        final var localDate = Transformable.of(vanGoghAuction)
                .apply(a -> a.setMostPopularPainting(Painting.of("Nijntje")))
                .let(PaintingAuction::getDateOfOpening);

        assertEquals(2, localDate.getDayOfMonth());
    }

    @Test
    void testAlsoWhen() {
        final var list = MutableListX.<Painting>empty();

        final var expected = new Painter("Test", "Hallo", LocalDate.of(2000, 1, 1));

        final var vanGoghAuction = Generator.createVanGoghAuction();

        final var painter = Transformable.of(vanGoghAuction)
                .apply(auction -> auction.setMostPopularPainting(Painting.of("Nijntje")))
                .run(PaintingAuction::getMostPopularPainting)
                .apply(System.out::println)
                .alsoUnless(Painting::isInMuseum, list::add)
                .run(Painting::painter)
                .let(p -> expected);

        assertTrue(list::isNotEmpty);
        assertEquals(expected, painter);
    }
}
