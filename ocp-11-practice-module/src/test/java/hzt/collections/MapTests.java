package hzt.collections;

import hzt.model.Person;
import hzt.model.Student;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class MapTests {

    private static final Random RANDOM = new Random();

    @Test
    void testConcurrentMapPutIfAbsentDoesNotChangeMapWhenKeyPresent() {
        ConcurrentMap<String, Object> cache = new ConcurrentHashMap<>();
        final var originalLocalDate = LocalDate.of(2021, 10, 18);

        cache.put("111", originalLocalDate);
        cache.putIfAbsent("111", LocalDate.of(2020, Month.NOVEMBER, 3));

        assertEquals(originalLocalDate, cache.get("111"));
    }

    @Test
    void testComputeIfAbsent() {
        Map<String, List<Double>> groupedValues = new HashMap<>(Map.of("Hans", new ArrayList<>(List.of(8.0))));
        final String hans = "Hans";
        final double grade = 7;

        final var hansGrades = groupedValues.computeIfAbsent(hans, name -> new ArrayList<>());
        final var pietGrades = groupedValues.computeIfAbsent("Piet", name -> new ArrayList<>());
        hansGrades.add(grade);
        pietGrades.add(6.0);

        assertEquals(2, hansGrades.size());
        assertEquals(1, pietGrades.size());
    }

    @Test
    void testComputeIfPresent() {
        Map<String, List<Double>> groupedValues = new HashMap<>(Map.of("Hans", new ArrayList<>(List.of(8.0))));
        final String hans = "Hans";
        final double grade = 7;

        assertTrue(addToGradesIfPresent(groupedValues, grade, hans));
        assertFalse(addToGradesIfPresent(groupedValues, 6.0, "Piet"));

        assertEquals(1, groupedValues.size());
    }

    private boolean addToGradesIfPresent(Map<String, List<Double>> groupedValues, double grade, String key) {
        final var newGrades = groupedValues.computeIfPresent(key,
                (name, value) -> new ArrayList<>(List.of(grade)));
        return Optional.ofNullable(newGrades).isPresent();
    }

    private List<Double> addPersonAndGrade(Map<String, List<Double>> groupedValues, double grade, String key) {
        return groupedValues.compute(key, (name, value) -> new ArrayList<>(List.of(grade)));
    }

    @Test
    void testCompute() {
        Map<String, List<Double>> groupedValues = new HashMap<>(Map.of("Hans", new ArrayList<>(List.of(8.0))));
        final String hans = "Hans";
        final double grade = 7;

        assertFalse(addPersonAndGrade(groupedValues, grade, hans).isEmpty());
        assertFalse(addPersonAndGrade(groupedValues, 6.0, "Piet").isEmpty());

        assertEquals(2, groupedValues.size());
    }

    @Test
    void testMapMerge() {
        Map<String, List<Double>> groupedValues = new HashMap<>(Map.of("Hans", new ArrayList<>(List.of(8.0))));

        final var mergedGradesHans = groupedValues
                .merge("Hans", new ArrayList<>(), (name, grades) -> new ArrayList<>(List.of(7.0)));
        final var mergedGradesPiet = groupedValues
                .merge("Piet", new ArrayList<>(), (name, grades) -> new ArrayList<>(List.of(6.0)));

        assertEquals(1, groupedValues.get("Hans").size());
        assertEquals(0, groupedValues.get("Piet").size());
        assertEquals(1, mergedGradesHans.size());
        assertEquals(0, mergedGradesPiet.size());
    }

    //q20 test 5
    //
    // The LinkedHashMap class maintains the elements in the order of their insertion time. This property can be used to build the required cache as follows:
    //1. Insert the key-value pairs as you do normally where key will be the object identifier and value will be the object to be cached.
    //2. When a key is requested, remove it from the LinkedHashMap and then insert it again. This will make sure that this pair marked as inserted latest.
    //3. If the capacity is full, remove the first element.
    //
    //Note that you cannot simply insert the key-value again (without first removing it) because a reinsertion operation does not affect the position of the pair.
    @Test
    void testLinkedHashMapMaintainsOrderOfInsertion() {
        Map<Long, Object> map = new LinkedHashMap<>();
        for (int i = 1; i <= 100; i++) {
            map.put(System.nanoTime(), new Student(i, "Person " + i, 6.5 + (1.2 * RANDOM.nextGaussian())));
        }
        map.entrySet().forEach(System.out::println);

        final var firstEntry = map.entrySet().iterator().next();
        final var lastEntry = last(map.entrySet());

        final var minimumTimestamp = map.entrySet().stream()
                .min(Map.Entry.comparingByKey())
                .orElseThrow();

        final var maxTimestamp = map.entrySet().stream()
                .max(Map.Entry.comparingByKey())
                .orElseThrow();

        System.out.println("firstEntry = " + firstEntry);
        System.out.println("lastEntry = " + lastEntry);
        assertEquals(firstEntry, minimumTimestamp);
        assertEquals(lastEntry, maxTimestamp);
    }

    private static <T> T last(Iterable<T> iterable) {
        var iterator = iterable.iterator();
        T t = iterator.next();
        while (iterator.hasNext()) {
            t = iterator.next();
        }
        return t;
    }

    @Test
    void testCollectToMapFromSourceWithDuplicatedKeysUsingMergeFunctionTakesFirst() {
        final var personList = List.of(
                new Person(1, "Sophie"),
                new Person(2L, "Hans"),
                new Person(1L, "Ted"),
                new Person(2L, "Huib"),
                new Person(2L, "Matthijs"));

        final var nameMap = personList.stream()
                .collect(Collectors.toMap(Person::getId, Person::getName, (first, second) -> first));

        assertEquals(Map.of(1L, "Sophie", 2L, "Hans"), nameMap);
    }

    @Test
    void testCollectToMapFromSourceWithDuplicatedKeysUsingMergeFunctionTakesLast() {
        final var personList = List.of(
                new Person(1, "Sophie"),
                new Person(2L, "Hans"),
                new Person(1L, "Ted"),
                new Person(2L, "Huib"),
                new Person(2L, "Matthijs"));

        final var nameMap = personList.stream()
                .collect(Collectors.toMap(Person::getId, Person::getName, (first, second) -> second));

        assertEquals(Map.of(1L, "Ted", 2L, "Matthijs"), nameMap);
    }

    @Test
    void testCollectToMapFromSourceWithDuplicatedKeysNotUsingMergeFunctionThrowsException() {
        final var stream = Stream.of(
                new Person(1, "Sophie"),
                new Person(2L, "Hans"),
                new Person(1L, "Ted"),
                new Person(2L, "Huib"),
                new Person(2L, "Matthijs"));

        final var personMapCollector = Collectors.toMap(Person::getId, Person::getName);
        //noinspection ResultOfMethodCallIgnored
        assertThrows(IllegalStateException.class, () -> stream.collect(personMapCollector));
    }

    @Test
    void testUpdateMapValues() {
        final var map = IntStream.range(0, 10)
                .boxed()
                .collect(Collectors.toMap(i -> i, AtomicInteger::new));

        updateMapValues(map.values());

        final var count = map.values().stream()
                .filter(i -> i.get() == 0)
                .count();

        assertEquals(5, count);
    }

    private static void updateMapValues(Collection<AtomicInteger> values) {
        int counter = 0;
        for (AtomicInteger value : values) {
            if (counter % 2 == 0) {
                value.set(0);
            }
            counter++;
        }
    }


}
