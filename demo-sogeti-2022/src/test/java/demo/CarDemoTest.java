package demo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static demo.CarDemo.CarsGroupedByType;
import static demo.CarDemo.FindFirstCar;
import static demo.CarDemo.cars;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayNameGeneration(DisplayNameGenerator.IndicativeSentences.class)
class CarDemoTest {

    @Test
    @DisplayName("find first station car brand less than 20_000")
    void testFindFirstStationCarLessThan20_000() {
        final var imperative = FindFirstCar.firstStationCarLessThan20_000Imperative(cars);
        final var streamsResult = FindFirstCar.firstStationCarLessThan20_000ByStream(cars);
        final var sequencesResult = FindFirstCar.firstStationCarLessThan20_000BySequence(cars);

        assertAll(
                () -> assertEquals(streamsResult, imperative),
                () -> assertEquals(sequencesResult, streamsResult)
        );
    }

    @Test
    @DisplayName("find all car brands of station cars less than 20_000")
    void testFindAllCarBrandsStationCarsLessThan20_000() {
        final var imperative = CarDemo.FindAllCars.allCarBrandsStationCarsLessThan20_000Imperative(cars);
        final var streamsResult = CarDemo.FindAllCars.allCarBrandsStationCarsLessThan20_000ByStream(cars);
        final var sequencesResult = CarDemo.FindAllCars.allCarBrandsStationCarsLessThan20_000BySequence(cars);

        assertAll(
                () -> assertEquals(streamsResult, imperative),
                () -> assertEquals(sequencesResult, streamsResult)
        );
    }

    @Test
    void testGroupBy() {
        final var imperative = CarsGroupedByType.groupByCarTypeImperative(CarDemo.cars);
        final var streamsResult = CarsGroupedByType.groupByUsingStream(CarDemo.cars);
        final var sequencesResult = CarsGroupedByType.groupByUsingSequence(CarDemo.cars);

        assertAll(
                () -> assertEquals(streamsResult, imperative),
                () -> assertEquals(sequencesResult, streamsResult)
        );
    }

}
