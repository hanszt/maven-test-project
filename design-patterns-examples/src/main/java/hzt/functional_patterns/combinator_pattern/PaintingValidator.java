package hzt.functional_patterns.combinator_pattern;

import org.hzt.test.model.Painting;

import java.time.Year;
import java.util.function.Function;

public interface PaintingValidator extends Function<Painting, PaintingValidator.Result> {

    static PaintingValidator isInMuseum() {
        return painting -> painting.isInMuseum() ? Result.IS_VALID : Result.NOT_IN_MUSEUM;
    }

    static PaintingValidator isCreatedBeforeTheYear(int year) {
        return painting -> painting.yearOfCreation().isBefore(Year.of(year)) ?
                Result.IS_VALID : Result.CREATION_YEAR_INVALID;
    }

    default PaintingValidator and(PaintingValidator other) {
        return painting -> {
            final var result = apply(painting);
            return result == Result.IS_VALID ? other.apply(painting) : result;
        };
    }

    default PaintingValidator or(PaintingValidator other) {
        return painting -> {
            final var result = apply(painting);
            return result == Result.IS_VALID ? result : other.apply(painting);
        };
    }

    enum Result {
        IS_VALID, NOT_IN_MUSEUM, CREATION_YEAR_INVALID, INVALID
    }
}
