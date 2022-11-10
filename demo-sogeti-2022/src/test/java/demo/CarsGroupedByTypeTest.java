package demo;

import org.hzt.utils.iterables.Collectable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CarsGroupedByTypeTest {

    @Test
    void testGroupBy() {
        final var imperative = CarsGroupedByType.groupByImperative(CarsGroupedByType.cars);
        final var streamsResult = CarsGroupedByType.groupByUsingStream(CarsGroupedByType.cars);
        final var tempSequencesResult = CarsGroupedByType.groupByUsingSequence(CarsGroupedByType.cars);

        final var sequencesResult = tempSequencesResult.mapByValues(Collectable::toList).toMap();

        assertAll(
                () -> assertEquals(streamsResult, imperative),
                () -> assertEquals(sequencesResult, streamsResult)
        );
    }

}
