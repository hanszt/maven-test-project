package org.hzt;

import org.hzt.collectors_samples.CollectorSamples;
import org.hzt.model.Bic;
import org.hzt.model.Person;
import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Book;
import org.hzt.test.model.Painting;
import org.hzt.utils.It;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Comparator.comparing;
import static org.hzt.iterators.Enumerations.sizedEnumeration;
import static org.hzt.utils.It.println;
import static org.junit.jupiter.api.Assertions.*;

class OtherTests {

    @Test
    void testChars() {
        IntStream.range(33, 900)
                .mapToObj(OtherTests::toCharacter)
                .forEach(It::println);

        println();

        "Hello".chars()
                .mapToObj(OtherTests::toCharacter)
                .forEach(It::println);

        String.format("Hello,%nI'm Hans").lines().forEach(It::printf);
        char c = 'ͽ';
        assertEquals(893, c);
    }

    private static Character toCharacter(int i) {
        return (char) i;
    }

    @Test
    void testStreamFindFirstNotNullSafe() {
        final var bics = List.of(new Bic(null), new Bic(null));
        assertThrows(NullPointerException.class, () -> getAnyNameThrowingNull(bics));
    }

    @Test
    void testStreamFindFirstDoesNotThrowNull() {
        final var bics = List.of(new Bic(null), new Bic(null));
        assertTrue(getAnyName(bics).isEmpty());
    }

    @Test
    void testStreamFindFirstNotNullSafeButNotThrowing() {
        final var expected = "Sophie";
        final var anyName = getAnyNameThrowingNull(List.of(new Bic(expected), new Bic(null)));
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

        Arrays.stream(actual).forEach(It::println);

        assertEquals(8, OCTAL);
        assertArrayEquals(expected, actual);
    }

    @Test
    void testHexValues() {
        final int[] expected = IntStream.rangeClosed(0, 256).toArray();
        final int[] actual = IntStream.rangeClosed(0x0, 0x100).toArray();

        Arrays.stream(actual).forEach(It::println);

        assertArrayEquals(expected, actual);
    }

    @Test
    void testBinaryValues() {
        final int[] expected = IntStream.rangeClosed(0, 8).toArray();
        final int[] actual = IntStream.rangeClosed(0b0, 0b1000).toArray();

        Arrays.stream(actual).forEach(It::println);

        assertArrayEquals(expected, actual);
    }

    @Test
    void testSetBehaviourContainingNull() {
        final Set<String> stringSet = new HashSet<>();
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
        final var opt = Optional.of("Hallo");
        opt.ifPresentOrElse(assertIsEqualTo("Hallo"), () -> fail("Not present"));
    }

    @Test
    void testGenericsMethodToBic() {
        final var bic = new Bic("Hallo");
        final var opt = Optional.of(bic);
        opt.ifPresentOrElse(assertIsEqualTo(bic), () -> fail("Not present"));
    }

    private static <T> Consumer<T> assertIsEqualTo(T other) {
        return t -> assertEquals(t, other);
    }

    @Test
    void testCreatingBathes() {
        List<String> data = IntStream.range(0, 950)
                .mapToObj(i -> "item " + i)
                .toList();

        final int BATCH_SIZE = 100;

        List<List<String>> batches = IntStream.range(0, (data.size() + BATCH_SIZE - 1) / BATCH_SIZE)
                .mapToObj(i -> data.subList(i * BATCH_SIZE, Math.min(data.size(), (i + 1) * BATCH_SIZE)))
                .toList();

        assertEquals(10, batches.size());
        printBatches(batches);
    }

    private void printBatches(List<List<String>> batches) {
        int counter = 0;
        for (List<String> batch : batches) {
            println("Batch " + counter);
            println("batch = " + batch);
            println();
            counter++;
        }
    }

    @Test
    void testThrowable() {
        final var throwable = new Throwable();
        println("throwable.getLocalizedMessage() = " + throwable.getLocalizedMessage());
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
        final var size = 100;
        final var list = Collections.list(sizedEnumeration(size, String::valueOf));
        final var spliterator = Spliterators.spliterator(sizedEnumeration(size, String::valueOf).asIterator(), size, Spliterator.ORDERED);
        final var strings = StreamSupport.stream(spliterator, false).toList();
        println(strings);
        assertEquals(strings, list);
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
        final long successCount = IntStream.rangeClosed(0, iterations)
                .filter(i -> sqrtTwoRandomNumbersSmallerEqualThanOne())
                .count();
        return (double) (4 * successCount) / iterations;
    }

    private static boolean sqrtTwoRandomNumbersSmallerEqualThanOne() {
        final double x = Math.random();
        final double y = Math.random();
        return x * x + y * y <= 1;
    }

    /**
     * Can lead to memory leaks:
     * @see <a href="https://stackoverflow.com/questions/6802483/how-to-directly-initialize-a-hashmap-in-a-literal-way">
     *     For up to Java Version 8</a>
     */
    @Test
    void testMapAndCollectionIllegalInitialisation() {
        final Map<String, String> map = new HashMap<>() {{
            put("wat", "gek");
            put("raar", "dit moet niet kunnen");
        }};
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
        var array = IntStream.range(0, ONE_BILLION).parallel().toArray();
        assertEquals(ONE_BILLION, array.length);
    }

    @Test
    void testStartingWithMaxRangeGetNumbersByDivisor() {
        final var DIVISOR = 20;
        var array = IntStream.range(0, Integer.MAX_VALUE)
                .parallel()
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

        println(string);
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
                .sorted(Comparator.comparing(a -> a.name))
                .filter(a -> a.name.toLowerCase(Locale.ROOT).contains("meisje"))
                .filter(a -> a.isInMuseumAndIsOlderThan100Years())
                .toList();

        list.forEach(anonymous -> println("Name: " + anonymous.name + " PaintingAge: " + anonymous.painting));

        assertFalse(list.isEmpty());

        final var anonymous = list.get(1);

        assertAll(
                () -> assertTrue((anonymous.name.startsWith("M"))),
                () -> assertTrue(anonymous.isInMuseumAndIsOlderThan100Years())
        );
    }

    @Test
    void testPuttingAllKindsOfObjectsInBoundedWildCardList() {
        final List<? super Comparable<?>> list = new ArrayList<>();
        list.add(LocalDate.of(2021, Month.OCTOBER, 4));
        list.add("Hello");
        list.add("This is weird");
        list.add(BigDecimal.valueOf(20));
        list.add(new Person("", null, null));

        list.forEach(It::println);

        assertTrue(list.contains("Hello"));
    }

    @Test
    void testExtendedBoundedWildCardList() {
        final List<? super Comparable<? extends Comparable<? extends Comparable<? extends Comparable<?>>>>> list = new ArrayList<>();
        list.add(LocalDate.of(2021, Month.OCTOBER, 4));
        list.add("Hello");
        list.add("This is weird");
        list.add(BigDecimal.valueOf(20));
        list.add(new Person("", null, null));

        list.forEach(It::println);

        assertTrue(list.contains("Hello"));
    }

    @Test
    void testModifyingUnderlingArrayViaArraysAsList() {
        final Integer[] expected = {1, 4, 10, 7, 3, 8};
        final Integer[] actual = {1, 4, 5, 7, 3, 8};

        final var integerList = Arrays.asList(actual);

        // setting the value at index 2 of the created list to 10 also sets the value of the original array at index 2 to 10
        integerList.set(2, 10);

        assertArrayEquals(expected, actual);
    }

    public void printStrings(List<String> strings) {
        strings.forEach(It::println);
    }

    @Test
    void testTypeWitnessNotNeededAnyMore() {
        //noinspection RedundantTypeArguments
        assertDoesNotThrow(() -> printStrings(Collections.<String>emptyList()));
    }

    @Test
    void testExec() throws IOException {
        Process process = Runtime.getRuntime().exec("docker exec rabbitmq bash");
        assertTrue(process.isAlive());
    }

    /**
     * Filed a Stackoverflow question for test cases below.
     * <p>
     * A JDK bug is reported in response
     *
     * @see <a href="https://stackoverflow.com/questions/73654810/why-does-string-creation-using-newinstance-method-behave-different-when-usin">
     *     Why does String creation using `newInstance()` method behave different when using `var` compared to using explicit type `String`?</a>
     * @see <a href="https://bugs.java.com/bugdatabase/view_bug.do?bug_id=JDK-8293578">JDK-8293578 : Duplicate ldc generated by javac</a>
     * @see <a href="https://bugs.openjdk.org/browse/JDK-8293578">Duplicate ldc generated by javac</a>
     */
    @Nested
    class NewInstanceUsingReflection {

        @Test
        void testClassNewInstance() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
            final var input = "A string";
            final var theClass = input.getClass();
            final var constructor = theClass.getConstructor();
            final String newString = constructor.newInstance();

            assertEquals("", newString);
        }

        @Test
        void testClassNewInstanceWithVarOnly() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
            final var input = "A string";
            final var theClass = input.getClass();
            final var constructor = theClass.getConstructor();
            final var newString = constructor.newInstance();

            // unexpected
            assertEquals("A string", newString);
        }

        @Test
        void testNewInstanceInlined() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
            final var newString = "A string"
                    .getClass()
                    .getConstructor()
                    .newInstance();
            // unexpected
            assertEquals("A string", newString);
        }

    }

}
