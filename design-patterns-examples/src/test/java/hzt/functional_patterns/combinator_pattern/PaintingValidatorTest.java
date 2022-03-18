package hzt.functional_patterns.combinator_pattern;

import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Painting;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static hzt.functional_patterns.combinator_pattern.PaintingValidator.Result;
import static hzt.functional_patterns.combinator_pattern.PaintingValidator.isCreatedBeforeTheYear;
import static hzt.functional_patterns.combinator_pattern.PaintingValidator.isInMuseum;
import static java.util.function.Predicate.not;
import static org.junit.jupiter.api.Assertions.assertSame;

class PaintingValidatorTest {

    private static List<Painting> paintingList;

    @BeforeAll
    static void setup() {
        paintingList = TestSampleGenerator.createPaintingList();
    }

    @Test
    void testValidatingValidPaintingReturnsStatusIsValid() {
        final var validPainting = getTestPainting(
                Painting::isInMuseum,
                p -> p.yearOfCreation().isBefore(Year.of(1800)));

        System.out.println(validPainting);

        final var result = isCreatedBeforeTheYear(1800)
                .and(isInMuseum())
                .apply(validPainting);

        assertSame(Result.IS_VALID, result);
    }

    @Test
    void testValidatingPaintingNotInMuseumReturnsStatusNotInMuseum() {
        final var maxYear = 1900;

        final var paintingNotInMuseum = getTestPainting(
                not(Painting::isInMuseum),
                p -> p.yearOfCreation().isBefore(Year.of(maxYear)));

        System.out.println(paintingNotInMuseum);

        final var result = isCreatedBeforeTheYear(maxYear)
                .and(isInMuseum())
                .apply(paintingNotInMuseum);

        assertSame(Result.NOT_IN_MUSEUM, result);
    }

    @Test
    void testValidatingPaintingWithInvalidMaxYearReturnsStatusCreationYearInvalid() {
        final var validPainting = getTestPainting(
                Painting::isInMuseum,
                p -> p.yearOfCreation().isAfter(Year.of(1700)));

        System.out.println(validPainting);

        final var result = isCreatedBeforeTheYear(1700)
                .and(isInMuseum())
                .apply(validPainting);

        assertSame(Result.CREATION_YEAR_INVALID, result);
    }

    @Test
    void testMultipleInvalidReturnsFirstInvalidInValidationChain() {
        final var testPainting = getTestPainting(
                not(Painting::isInMuseum),
                p -> p.yearOfCreation().isAfter(Year.of(1700)));

        System.out.println(testPainting);

        final var result = isCreatedBeforeTheYear(1700)
                .and(isInMuseum())
                .apply(testPainting);

        assertSame(Result.CREATION_YEAR_INVALID, result);
    }

    @Test
    void testValidatingPaintingInvalidCustomValidationReturnsStatusInValid() {
        final var validPainting = getTestPainting(
                Painting::isInMuseum,
                p -> p.yearOfCreation().isBefore(Year.of(1800)));

        System.out.println("validPainting = " + validPainting);

        final var result = isCreatedBeforeTheYear(1800)
                .and(isInMuseum())
                .and(p -> p.name().startsWith("a") ? Result.IS_VALID : Result.INVALID)
                .apply(validPainting);

        assertSame(Result.INVALID, result);
    }

    @Test
    void testValidatingPaintingWithOrChecksBoth() {
        final var validPainting = getTestPainting(
                Painting::isInMuseum,
                p -> p.yearOfCreation().isBefore(Year.of(1800)));

        System.out.println("validPainting = " + validPainting);

        final var result = isCreatedBeforeTheYear(1800)
                .and(isInMuseum())
                .and(p -> p.name().startsWith("a") ? Result.IS_VALID : Result.INVALID)
                .or(isInMuseum().and(isCreatedBeforeTheYear(1500)))
                .apply(validPainting);

        assertSame(Result.CREATION_YEAR_INVALID, result);
    }

    @SafeVarargs
    private static Painting getTestPainting(Predicate<Painting>... predicatesToBeSatisfied) {
        Predicate<Painting> combinedPredicate = Stream.of(predicatesToBeSatisfied).reduce(p -> true, Predicate::and);
        return paintingList.stream()
                .filter(combinedPredicate)
                .findFirst()
                .orElseThrow();
    }

}
