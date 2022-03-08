package hzt;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class StreamsTests {

    @Test
    void testStreamCollectToMap() {
        Set<String> set = Set.of("Ralph", "Hans", "Lucy", "Sophie");

        final var personMap = set.stream()
                .filter(not(String::isEmpty))
                .map(Person::new)
                .filter(person -> person.name.length() > 3)
                .collect(Collectors.toMap(person -> person.name, person -> person.name.length()));

        System.out.println("Map: ");
        personMap.entrySet().forEach(System.out::println);

        assertFalse(personMap.isEmpty());
    }

    @Test
    void testFindFirst() {
        final var pietje = new Person("Pietje");
        String[] array = {"Ralph", "HAns", "Lucy", "Sophie", "Sophie"};
        final var firstPerson = Stream.of(array)
                .filter(String::isEmpty)
                .map(Person::new)
                .filter(person -> person.name.length() > 3)
                .findFirst()
                .orElse(pietje);

        System.out.println("First person:");
        System.out.println(firstPerson);

        assertEquals(new Person("Pietje"), firstPerson);
    }

    @Test
    void testSumOfIntStreamReturnsInt() {
        final int sum = IntStream.rangeClosed(0, 100).sum();
        assertEquals(5050, sum);
    }

    //q42 test 3
//    You need to understand the following points to answer this question:
//    1. The reduce method needs a BinaryOperator.
//    This interface is meant to consume two arguments and produce one output.
//    It is applied repeatedly on the elements in the stream until only one element is left.
//    The first argument is used to provide an initial value to start the process.
//    (If you don't pass this argument, a different reduce method will be invoked and that returns an Optional object.)
//
//    2. The Stream.max method requires a Comparator. All you need to implement this interface using a lambda expression
//    is a reference to any method that takes two arguments and returns an int. The name of the method doesn't matter.
//    That is why it is possible to pass the reference of Integer's max method as
//    an argument to Stream's max method. However, Integer.max works very differently from Integer.compare.
//    The max method returns the maximum of two numbers while the compare method returns a difference between two numbers.
//    Therefore, when you pass Integer::max to Stream's max, you will not get the correct maximum element from the stream.
//    That is why //2 will compile but will not work correctly.
//
// 4 is basically same as //2. It will not work correctly for the same reason.
    @Test
    void testFindingMaxValue() {
        List<Integer> ls = List.of(3, 4, 6, 9, 2, 5, 7);
        assertEquals(9, ls.stream().reduce(Integer.MIN_VALUE, (a, b) -> a > b ? a : b)); //1
        assertEquals(3, ls.stream().max(Integer::max).get()); //2
        assertEquals(9, ls.stream().max(Integer::compare).get()); //3
        assertEquals(OptionalInt.of(9), ls.stream().mapToInt(i -> i).max()); //3
        assertEquals(Optional.of(3), ls.stream().max((a, b) -> a > b ? a : b)); //4
    }

    @Test
    void testPartitioningByGroupingBy() {
        final var strings = List.of("Hans", "Sophie", "Evie", "Huib", "Ted");
        final var personList = strings.stream()
                .map(Person::new)
                .collect(Collectors.toUnmodifiableList());

        final var expected = personList.stream()
                .collect(Collectors.partitioningBy(p -> p.id > 3));

        final var partitioningWithGroupingBy = personList.stream()
                .collect(Collectors.groupingBy(p -> p.id > 3));

        partitioningWithGroupingBy.entrySet().forEach(System.out::println);

        assertEquals(expected, partitioningWithGroupingBy);
    }

    private static class Person {

        private static int next = 0;

        private final long id = ++next;
        private final String name;

        public Person(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            return this == o || (o instanceof Person && Objects.equals(name, ((Person) o).name));
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }

        @Override
        public String toString() {
            return "Person{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
