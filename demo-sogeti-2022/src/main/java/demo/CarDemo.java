package demo;

import demo.sequences.MySequence;
import org.hzt.utils.collections.MapX;
import org.hzt.utils.collections.MutableListX;
import org.hzt.utils.sequences.Sequence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static org.hzt.utils.It.println;

public final class CarDemo {

    public static final List<Car> cars = List.of(
            new Car("Renault", Car.Type.HATCH_BACK, 5_000),
            new Car("Mercedes", Car.Type.COUPE, 30_000),
            new Car("Bmw", Car.Type.STATION_CAR, 40_000),
            new Car("Volvo", Car.Type.SEDAN, 9_000),
            new Car("Suzuki", Car.Type.STATION_CAR, 15_000),
            new Car("Tesla", Car.Type.SUV, 30_000),
            new Car("Toyota", Car.Type.HATCH_BACK, 10_000),
            new Car("Volvo", Car.Type.STATION_CAR, 20_000),
            new Car("Peugeot", Car.Type.STATION_CAR, 20_000),
            new Car("Renault", Car.Type.HATCH_BACK, 10_000)
    );

    private CarDemo() {
    }

    public static class FindFirstCar {

        public static void main(String[] args) {
            println("imperative:");
            final var result1 = firstStationCarLessThan20_000Imperative(cars);
            println(result1);
            println("By stream:");
            final var result2 = firstStationCarLessThan20_000ByStream(cars);
            println(result2);
        }

        public static String firstStationCarLessThan20_000Imperative(List<Car> cars) {
            for (var car : cars) {
                final var leap = car.isPriceLessThanEqual20_000();
                if (leap) {
                    final var yearAfter2000 = car.isOfTypeStationCar();
                    if (yearAfter2000) {
                        return car.brand();
                    }
                }
            }
            throw new NoSuchElementException();
        }

        static String firstStationCarLessThan20_000ByStream(List<Car> cars) {
            return cars.stream()
                    .filter(Car::isPriceLessThanEqual20_000)
                    .filter(Car::isOfTypeStationCar)
                    .map(Car::printAndGetBrand)
                    .findFirst()
                    .orElseThrow();
        }

        public static String firstStationCarLessThan20_000BySequence(List<Car> cars) {
            return MySequence.of(cars)
                    .filter(Car::isPriceLessThanEqual20_000)
                    .filter(Car::isOfTypeStationCar)
                    .map(Car::printAndGetBrand)
                    .first();
        }
    }

    public static class FindAllCars {

        public static void main(String[] args) {
            println("imperative:");
            final var result1 = allCarBrandsStationCarsLessThan20_000Imperative(cars);
            println(result1);
            println("By stream:");
            final var result2 = allCarBrandsStationCarsLessThan20_000Imperative(cars);
            println(result2);
        }

        public static List<String> allCarBrandsStationCarsLessThan20_000Imperative(List<Car> cars) {
            List<String> brands = new ArrayList<>();
            for (var car : cars) {
                final var leap = car.isPriceLessThanEqual20_000();
                if (leap) {
                    final var yearAfter2000 = car.isOfTypeStationCar();
                    if (yearAfter2000) {
                        brands.add(car.brand());
                    }
                }
            }
            return List.copyOf(brands);
        }

        public static List<String> allCarBrandsStationCarsLessThan20_000ByStream(List<Car> cars) {
            return cars.stream()
                    .filter(Car::isPriceLessThanEqual20_000)
                    .filter(Car::isOfTypeStationCar)
                    .map(Car::printAndGetBrand)
                    .toList();
        }

        public static List<String> allCarBrandsStationCarsLessThan20_000BySequence(List<Car> cars) {
            return MySequence.of(cars)
                    .filter(Car::isPriceLessThanEqual20_000)
                    .filter(Car::isOfTypeStationCar)
                    .map(Car::printAndGetBrand)
                    .toList();
        }
    }

    public static class CarsGroupedByType {

        public static void main(String[] args) {
            println("Imperative");
            final var carNamesGroupedByTypeImperative = groupByImperative(cars);
            println("Stream");
            final var carNamesGroupedByTypeStream = groupByUsingStream(cars);
            println("Sequence");
            final var carNamesGroupedByTypeSequence = groupByUsingSequence(cars);
            println("carsNamesGroupedByTypeImperative = " + carNamesGroupedByTypeImperative);
            println("carsNamesGroupedByTypeStream = " + carNamesGroupedByTypeStream);
            println("carsNamesGroupedByTypeSequence = " + carNamesGroupedByTypeSequence);
        }

        public static Map<Car.Type, List<String>> groupByImperative(List<Car> cars) {
            Map<Car.Type, List<String>> map = new HashMap<>();
            for (Car car : cars) {
                if (car.isPriceLessThanEqual20_000()) {
                    final var type = car.type();
                    final var strings = map.get(type);
                    if (strings == null) {
                        final var list = new ArrayList<String>();
                        list.add(car.brand());
                        map.put(type, list);
                    } else {
                        strings.add(car.brand());
                    }
                }
            }
            return map;
        }

        public static Map<Car.Type, List<String>> groupByUsingStream(List<Car> cars) {
            return cars.stream()
                    .filter(Car::isPriceLessThanEqual20_000)
                    .collect(groupingBy(Car::type, mapping(Car::brand, toList())));
        }

        public static MapX<Car.Type, MutableListX<String>> groupByUsingSequence(List<Car> cars) {
            return Sequence.of(cars)
                    .filter(Car::isPriceLessThanEqual20_000)
                    .groupMapping(Car::type, Car::brand);
        }
    }
}
