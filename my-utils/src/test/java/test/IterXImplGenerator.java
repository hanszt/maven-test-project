package test;

import hzt.collections.ListX;
import hzt.collections.MutableListX;
import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Painter;
import org.hzt.test.model.Painting;
import test.model.PaintingAuction;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;

public final class IterXImplGenerator {

    private static MutableListX<PaintingAuction> paintingAuctions;

    private IterXImplGenerator() {
    }

    public static MutableListX<PaintingAuction> getAuctionsContainingNulls() {
        if (paintingAuctions == null) {
            paintingAuctions = createAuctions();
        }
        return paintingAuctions;
    }

    public static MutableListX<PaintingAuction> createAuctions() {

        final var groupedByLastName = TestSampleGenerator.createPaintingList().stream()
                .collect(Collectors.groupingBy(painting -> painting.painter().getLastname()));

        final List<Painting> vanGoghPaintings = groupedByLastName.get("van Gogh");
        final List<Painting> vermeerPaintings = groupedByLastName.get("Vermeer");
        final List<Painting> picassoPaintings = groupedByLastName.get("Picasso");

        final var painter = new Painter("Hans", "Zuidervaart", LocalDate.of(1989, 10 ,18));
        return MutableListX.of(
                new PaintingAuction(null, null, List.of(new Painting("Test", painter, Year.of(2000), false))),
                new PaintingAuction("Van Gogh Museum", LocalDate.of(1992, Month.APRIL, 2), vanGoghPaintings),
                new PaintingAuction("Vermeer Museum", LocalDate.of(1940, Month.JANUARY, 23), vermeerPaintings),
                new PaintingAuction("Picasso Museum", LocalDate.of(1965, Month.AUGUST, 4), picassoPaintings));
    }

}
