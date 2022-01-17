package com.dnb;

import com.dnb.collectors_samples.CollectorSamples;
import com.dnb.model.Bic;
import com.dnb.model.Person;
import org.hzt.TestSampleGenerator;
import org.hzt.model.Book;
import org.hzt.model.Painting;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Comparator.comparing;
import static org.junit.jupiter.api.Assertions.*;

class OtherTests {

    @Test
    void testChars() {
        IntStream.range(33, 900).mapToObj(OtherTests::toCharacter).forEach(System.out::println);
        System.out.println();
        "Hello".chars()
                .mapToObj(OtherTests::toCharacter)
                .forEach(System.out::println);
        String.format("Hello,%nI'm Hans").lines().forEach(System.out::printf);
        char c = 'ͽ';
        assertEquals(893, c);
    }

    private static Character toCharacter(int i) {
        return (char) i;
    }

    @Test
    void testStreamFindFirstNotNullSafe() {
        List<Bic> bics = List.of(new Bic(null), new Bic(null));
        assertThrows(NullPointerException.class, () -> getAnyNameThrowingNull(bics));
    }

    @Test
    void testStreamFindFirstDoesNotThrowNull() {
        List<Bic> bics = List.of(new Bic(null), new Bic(null));
        assertTrue(getAnyName(bics).isEmpty());
    }

    @Test
    void testStreamFindFirstNotNullSafeButNotThrowing() {
        String expected = "Sophie";
        Optional<String> anyName = getAnyNameThrowingNull(List.of(new Bic(expected), new Bic(null)));
        anyName.ifPresentOrElse(assertIsEqualTo(expected), () -> fail("Not present"));
    }

    private Optional<String> getAnyNameThrowingNull(List<Bic> bics) {
        return bics.stream()
                .filter(Objects::nonNull)
                .map(Bic::getName)
                .findAny();
    }

    private Optional<String> getAnyName(List<Bic> bics) {
        return bics.stream()
                .map(Bic::getName)
                .filter(Objects::nonNull)
                .findAny();
    }

    @SuppressWarnings("OctalInteger")
    @Test
    void testOctalValues() {
        // Integers starting with first zero are octal instead of decimal
        final int OCTAL = 010;

        final int[] expected = IntStream.rangeClosed(0, 64).toArray();
        final int[] actual = IntStream.rangeClosed(00, 0100).toArray();

        Arrays.stream(actual).forEach(System.out::println);

        assertEquals(8, OCTAL);
        assertArrayEquals(expected, actual);
    }

    @Test
    void testHexValues() {
        final int[] expected = IntStream.rangeClosed(0, 256).toArray();
        final int[] actual = IntStream.rangeClosed(0x0, 0x100).toArray();

        Arrays.stream(actual).forEach(System.out::println);

        assertArrayEquals(expected, actual);
    }

    @Test
    void testBinaryValues() {
        final int[] expected = IntStream.rangeClosed(0, 8).toArray();
        final int[] actual = IntStream.rangeClosed(0b0, 0b1000).toArray();

        Arrays.stream(actual).forEach(System.out::println);

        assertArrayEquals(expected, actual);
    }

    @Test
    void testSetBehaviourContainingNull() {
        Set<String> stringSet = new HashSet<>();
        stringSet.add(null);
        assertTrue(stringSet.remove(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"bm", "Bm", "BM", "bM"})
    void testEqualsIgnoreCase(String stringToCompare) {
        assertTrue("BM".equalsIgnoreCase(stringToCompare));
    }

    @Test
    void testGenericsMethodToString() {
        var opt = Optional.of("Hallo");
        opt.ifPresentOrElse(assertIsEqualTo("Hallo"), () -> fail("Not present"));
    }

    @Test
    void testGenericsMethodToBic() {
        final var bic = new Bic("Hallo");
        var opt = Optional.of(bic);
        opt.ifPresentOrElse(assertIsEqualTo(bic), () -> fail("Not present"));
    }

    private static <T> Consumer<T> assertIsEqualTo(T other) {
        return t -> assertEquals(t, other);
    }

    @Test
    void testCreatingBathes() {
        List<String> data = IntStream.range(0, 950)
                .mapToObj(i -> "item " + i).toList();

        final int BATCH_SIZE = 100;

        List<List<String>> batches = IntStream.range(0, (data.size() + BATCH_SIZE - 1) / BATCH_SIZE)
                .mapToObj(i -> data.subList(i * BATCH_SIZE, Math.min(data.size(), (i + 1) * BATCH_SIZE))).toList();

        assertEquals(10, batches.size());
        printBatches(batches);
    }

    private void printBatches(List<List<String>> batches) {
        int counter = 0;
        for (List<String> batch : batches) {
            System.out.println("Batch " + counter);
            System.out.println("batch = " + batch);
            System.out.println();
            counter++;
        }
    }

    @Test
    void testThrowable() {
        final var throwable = new Throwable();
        System.out.println("throwable.getLocalizedMessage() = " + throwable.getLocalizedMessage());
        assertNull(throwable.getMessage());
    }

    @Test
    void testTreeSetWithComparator() {
        SortedSet<Book> set = new TreeSet<>(comparing(Book::getTitle));
        final var book = new Book("");
        final var book2 = new Book("");
        book.setTitle("Hallo");
        book2.setTitle("Test");
        set.addAll(Set.of(book, book2));
        assertEquals(book2, set.last());
    }

    @Test
    void testObjectRequireNonNullElseAndElseGet() {
        Integer integer = Objects.requireNonNullElse(null, 4);
        assertEquals(4, integer);
        Integer integer2 = Objects.requireNonNullElse(null, 4);
        assertEquals(4, integer2);
    }

    @Test
    void testEnumerationToList() {
        //unexpected
        Enumeration<String> enumeration = new Enumeration<>() {
            private int counter = 0;
            @Override
            public boolean hasMoreElements() {
                return counter++ < 100;
            }

            @Override
            public String nextElement() {
                return String.valueOf(counter);
            }
        };
        final var list = Collections.list(enumeration);
        final var stringIterator = enumeration.asIterator();
        final Iterable<String> iterable = () -> stringIterator;
        final var strings = StreamSupport.stream(iterable.spliterator(), false).toList();
        System.out.println(strings);
        assertNotEquals(strings, list);
    }

    @Test
    void testCalcPiRandom() {
        final var result = BigDecimal.valueOf(calcPiRandom(1_000_000));
        final var actual = result.setScale(2, RoundingMode.HALF_UP);
        final var expected = BigDecimal.valueOf(3.14);
        assertEquals(expected, actual);
    }

    @SuppressWarnings("SameParameterValue")
    private static double calcPiRandom(final int iterations) {
        long successCount = IntStream.rangeClosed(0 , iterations)
                .filter(OtherTests::sqrtTwoRandomNmbrsSmOne)
                .count();
        return (double) (4 * successCount) / iterations;
    }

    private static boolean sqrtTwoRandomNmbrsSmOne(int i) {
        double x = Math.random();
        double y = Math.random();
        return x * x + y * y <= 1;
    }

    @Test
    void testMapAndCollectionIllegalInitialisation() {
        // Can lead to memory leaks:
        // see: https://stackoverflow.com/questions/6802483/how-to-directly-initialize-a-hashmap-in-a-literal-way
        Map<String, String> map = new HashMap<>() {{ put("wat", "gek");put("raar", "dit moet niet kunnen"); }};
        List<String> list = new ArrayList<>() {
            {
                add("Hoi");
                add("dit");
                add("mag");
                add("ook");
                add("niet");
            }
        };
        assertEquals(List.of("Hoi", "dit", "mag", "ook", "niet"), list);
        assertEquals(Map.of("wat", "gek", "raar", "dit moet niet kunnen"), map);
    }

    @Test
    void testStreamUsedWrongDontDoThis() {
        //This can lead to first race condition when parallelized, does not maintain order.
        //Example  of shared mutability. This is devils work
        List<String> bookTitles = new ArrayList<>();
        TestSampleGenerator.createBookList().stream()
                .filter(Book::isAboutProgramming)
                .map(Book::getTitle)
                .forEach(bookTitles::add);
        assertTrue(bookTitles.containsAll(List.of("Pragmatic Programmer", "OCP 11 Volume 1")));
    }

    @Test
    void testCreatingListUsingReduce() {
        //does not maintain order
        // right way in the sense that it provides onlu local mutability.
        // But can be delegated to Collect(toUnmodifiableList()); //This does maintain order
        final var reducedList = TestSampleGenerator.createBookList().stream()
                .filter(Book::isAboutProgramming)
                .map(Book::getTitle)
                .reduce(new ArrayList<>(), CollectorSamples::accumulate, CollectorSamples::combine);
        assertTrue(reducedList.containsAll(List.of("Pragmatic Programmer", "OCP 11 Volume 1")));
    }

    @Test
    void testCreatingSetUsingReduce() {
        //does not maintain order
        // right way in the sense that it provides onlu local mutability.
        // But can be delegated to Collect(toUnmodifiableList()); //This does maintain order
        final var reducedSet = TestSampleGenerator.createBookList().stream()
                .filter(Book::isAboutProgramming)
                .map(Book::getTitle)
                .reduce(new HashSet<>(), CollectorSamples::accumulate, CollectorSamples::combine);
        assertTrue(reducedSet.containsAll(List.of("Pragmatic Programmer", "OCP 11 Volume 1")));
    }

    @Test
    void testGenerateArrayContainingOneBillionElements() {
        final var ONE_BILLION = 1_000_000_000;
        var array = IntStream.range(0, ONE_BILLION).toArray();
        assertEquals(ONE_BILLION, array.length);
    }

    @Test
    void testStartingWithMaxRangeGetNumbersByDivisor() {
        final var DIVISOR = 20;
        var array = IntStream.range(0, Integer.MAX_VALUE)
        .filter(i -> i % DIVISOR == 0)
        .toArray();
        final var expected = (Integer.MAX_VALUE / DIVISOR) + 1;
        assertEquals(expected, array.length);
    }

    @Test
    void testGenerateArrayContainingMaxIntValueElementsThrowsOutOfMemoryError() {
        final var intStream = IntStream.rangeClosed(0, Integer.MAX_VALUE);
        //noinspection ResultOfMethodCallIgnored
        assertThrows(OutOfMemoryError.class, intStream::toArray);
    }

    @Test
    void testIsPrimitive() {
        Stream.of(boolean.class, char.class, byte.class, short.class, int.class, long.class, float.class, double.class, void.class)
                .map(Class::isPrimitive)
                .forEach(Assertions::assertTrue);
    }

    @Test
    void testIsNotPrimitive() {
        Stream.of(Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Void.class)
                .map(Class::isPrimitive)
                .sorted()
                .forEach(Assertions::assertFalse);
    }

    @Test
    void testObjectToString() {
        final var painting = TestSampleGenerator.createPaintingList().stream().findAny().orElseThrow();
        final var person = Person.createTestPersonList().stream().findAny().orElseThrow();
        List<Object> objects = List.of(painting, person, LocalDate.of(1989, 10, 18), new BigInteger("2000"));

        final var string = objects.stream()
                .map(o -> o.getClass().getSimpleName())
                .collect(Collectors.joining("\n"));

        System.out.println(string);
        assertTrue(string.contains("Painting"));
        assertTrue(string.contains("Person"));
    }

    @Test
    void testStreamOfAnonymousClassesCollectedToListOfTypeAnonymous() {
        Map<String, Painting> nameToPaintingMap = TestSampleGenerator.createPaintingList().stream()
                .collect(Collectors.toUnmodifiableMap(Painting::name, Function.identity()));

        final var list = nameToPaintingMap.entrySet().stream()
                .map(entry -> new Object() {
                    final String name = entry.getKey();
                    final Painting painting = entry.getValue();

                    boolean isInMuseumAndIsOlderThan100Years() {
                        return painting.isInMuseum() && painting.ageInYears() > 100;
                    }
                })
                .filter(a -> a.name.toLowerCase(Locale.ROOT).contains("meisje"))
                .filter(a -> a.isInMuseumAndIsOlderThan100Years())
                .toList();

        list.forEach(anonymous -> System.out.println("Name: " + anonymous.name + " PaintingAge: " + anonymous.painting));

        assertFalse(list.isEmpty());

        final var anonymous = list.get(1);

        assertTrue((anonymous.name.startsWith("M")));
        assertTrue(anonymous.isInMuseumAndIsOlderThan100Years());
    }

    @Test
    void testPuttingAllKindsOfObjectsInBoundedWildCardList() {
        List<? super Comparable<?>> list = new ArrayList<>();
        list.add(LocalDate.of(2021, Month.OCTOBER, 4));
        list.add("Hello");
        list.add("This is weird");
        list.add(BigDecimal.valueOf(20));
        list.add(new Person("", null, null));

        list.forEach(System.out::println);

        assertTrue(list.contains("Hello"));
    }

    @Test
    void testModifyingUnderlingArrayViaArraysAsList() {
        final Integer[] expected = {1, 4, 10, 7, 3, 8};
        Integer[] actual = {1, 4, 5, 7, 3, 8};

        final var integerList = Arrays.asList(actual);

        // setting the value at index 2 of the created list to 10 also sets the value of the original array at index 2 to 10
        integerList.set(2, 10);

        assertArrayEquals(expected, actual);
    }

    @Test
    void testTypeWitnessNotNeededAnyMore() {
        //noinspection RedundantTypeArguments
        assertDoesNotThrow(() -> printStrings(Collections.<String>emptyList()));
    }

    public void printStrings(List<String> strings) {
        strings.forEach(System.out::println);
    }

}
