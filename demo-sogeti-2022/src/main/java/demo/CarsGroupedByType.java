package demo;

import org.hzt.utils.collections.MapX;
import org.hzt.utils.collections.MutableListX;
import org.hzt.utils.sequences.Sequence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static org.hzt.utils.It.println;

public class CarsGroupedByType {

    static final List<Car> cars = List.of(
            new Car("Renault", Car.Type.HATCH_BACK, 5_000),
            new Car("Mercedes", Car.Type.COUPE, 30_000),
            new Car("Bmw", Car.Type.STATION_WAGON, 40_000),
            new Car("Volvo", Car.Type.SEDAN, 9_000),
            new Car("Tesla", Car.Type.SUV, 30_000),
            new Car("Toyota", Car.Type.HATCH_BACK, 10_000),
            new Car("Volvo", Car.Type.STATION_WAGON, 20_000),
            new Car("Peugeot", Car.Type.STATION_WAGON, 20_000)
    );

    public static void main(String[] args) {
//        final var carNamesGroupedByType = groupByUsingStream(cars);
//        final var carNamesGroupedByType = groupByImperative(cars);
        final var carNamesGroupedByType = groupByUsingSequence(cars);
        println("carsNamesGroupedByType = " + carNamesGroupedByType);
    }

    static Map<Car.Type, List<String>> groupByImperative(List<Car> cars) {
        Map<Car.Type, List<String>> map = new HashMap<>();
        for (Car car : cars) {
            if (car.price() <= 20_000) {
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

    static Map<Car.Type, List<String>> groupByUsingStream(List<Car> cars) {
        return cars.stream()
                .filter(car -> car.price() <= 20_000)
                .collect(groupingBy(Car::type, mapping(Car::brand, toList())));
    }

    static MapX<Car.Type, MutableListX<String>> groupByUsingSequence(List<Car> cars) {
        return Sequence.of(cars)
                .filter(car -> car.price() <= 20_000)
                .groupMapping(Car::type, Car::brand);
    }
}
