package org.hzt;

import org.hzt.collections.CollectorHelper;
import org.hzt.model.Bic;
import org.hzt.model.Person;
import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Book;
import org.hzt.test.model.Painting;
import org.hzt.utils.It;
import org.hzt.utils.iterables.Collectable;
import org.hzt.utils.sequences.Sequence;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Comparator.comparing;
import static org.hzt.collectors_samples.MyCollectors.chunking;
import static org.hzt.iterators.Enumerations.sizedEnumeration;
import static org.hzt.utils.It.println;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

class OtherTests {

    @Test
    void testChars() {
        IntStream.range(33, 900)
                .mapToObj(i -> (char) i)
                .forEach(It::println);

        println();

        "Hello".chars()
                .mapToObj(i -> (char) i)
                .forEach(It::println);

        String.format("Hello,%nI'm Hans").lines().forEach(It::printf);
        char c = 'ͽ';
        assertEquals(893, c);
    }

    @Test
    void testStreamFindFirstNotNullSafe() {
        final var bics = List.of(new Bic(null), new Bic(null));
        final var stringStream = bics.stream()
                .filter(Objects::nonNull)
                .map(Bic::getName);
        assertThrows(NullPointerException.class, stringStream::findAny);
    }

    @Test
    void testStreamFindFirstDoesNotThrowNull() {
        final var bics = List.of(new Bic(null), new Bic(null));
        assertTrue(getAnyName(bics).isEmpty());
    }

    @Test
    void testStreamFindFirstNotNullSafeButNotThrowing() {
        final var expected = "Sophie";

        final var anyName = Stream.of(new Bic(expected), new Bic(null))
                .map(Bic::getName)
                .findAny();

        anyName.ifPresentOrElse(actual -> assertEquals(expected, actual), () -> fail("Not present"));
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
        opt.ifPresentOrElse(actual -> assertEquals("Hallo", actual), () -> fail("Not present"));
    }

    @Test
    void testGenericsMethodToBic() {
        final var bic = new Bic("Hallo");
        final var opt = Optional.of(bic);
        opt.ifPresentOrElse(actual -> assertEquals(bic, actual), () -> fail("Not present"));
    }

    @Test
    void testCreatingBathes() {

        final int BATCH_SIZE = 100;

        List<String> data = IntStream.range(0, 950)
                .mapToObj(i -> "item " + i)
                .toList();

        final var expected = Sequence.of(data)
                .chunked(BATCH_SIZE)
                .map(Collectable::toList)
                .toList();

        List<List<String>> batches = IntStream.range(0, (data.size() + BATCH_SIZE - 1) / BATCH_SIZE)
                .mapToObj(i -> data.subList(i * BATCH_SIZE, Math.min(data.size(), (i + 1) * BATCH_SIZE)))
                .toList();

        List<List<String>> batches2 = data.stream()
                .collect(chunking(BATCH_SIZE))
                .toList();

        printBatches(batches);

        assertAll(
                () -> assertEquals(10, batches.size()),
                () -> assertEquals(expected, batches),
                () -> assertEquals(batches, batches2)
        );
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
        final var result = calcPiRandom(1_000_000);
        println(result);
        assertTrue(3.13 <= result && result <= 3.15);
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
     *
     * @see <a href="https://stackoverflow.com/questions/6802483/how-to-directly-initialize-a-hashmap-in-a-literal-way">
     * For up to Java Version 8</a>
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
        assertAll(
                () -> assertEquals(List.of("Hoi", "dit", "mag", "ook", "niet"), list),
                () -> assertEquals(Map.of("wat", "gek", "raar", "dit moet niet kunnen"), map)
        );
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
        // right way in the sense that it provides only local mutability.
        // But can be delegated to Collect(toUnmodifiableList()); //This does maintain order
        final var reducedList = TestSampleGenerator.createBookList().stream()
                .filter(Book::isAboutProgramming)
                .map(Book::getTitle)
                .reduce(new ArrayList<String>(), CollectorHelper::accumulate, CollectorHelper::combine);

        assertTrue(reducedList.containsAll(List.of("Pragmatic Programmer", "OCP 11 Volume 1")));
    }

    @Test
    void testCreatingSetUsingReduce() {
        //does not maintain order
        // right way in the sense that it provides only local mutability.
        // But can be delegated to Collect(toUnmodifiableList()); //This does maintain order
        final var reducedSet = TestSampleGenerator.createBookList().stream()
                .filter(Book::isAboutProgramming)
                .map(Book::getTitle)
                .reduce(new HashSet<String>(), CollectorHelper::accumulate, CollectorHelper::combine);

        assertTrue(reducedSet.containsAll(List.of("Pragmatic Programmer", "OCP 11 Volume 1")));
    }

    @Test
    void testCreatingMapUsingReduce() {
        //does not maintain order
        // right way in the sense that it provides only local mutability.
        // But can be delegated to Collect(toUnmodifiableList()); //This does maintain order
        final var bookList = TestSampleGenerator.createBookList();

        final var reducedMap = bookList.stream()
                .filter(Book::isAboutProgramming)
                .reduce(new HashMap<String, Book>(),
                        (map, book) -> CollectorHelper.accumulate(map, book.getTitle(), book),
                        CollectorHelper::combine);

        final var expected = bookList.stream()
                .filter(Book::isAboutProgramming)
                .collect(Collectors.toMap(Book::getTitle, It::self));

        assertEquals(expected, reducedMap);
    }

    @Test
    void testGenerateArrayContainingFiveHundredMillionElementsByStream() {
        final var amount = 100_000_000;
        var array = IntStream.range(0, amount).parallel().toArray();
        assertEquals(amount, array.length);
    }

    static void setAll(byte[] array, IntToByteFunction generator) {
        Objects.requireNonNull(generator);
        for (int i = 0; i < array.length; i++) {
            array[i] = generator.applyAsByte(i);
        }
    }

    @FunctionalInterface
    interface IntToByteFunction {

        byte applyAsByte(int value);
    }

    @Test
    void testIsPrimitive() {
        assertTrue(Stream.of(boolean.class, char.class, byte.class, short.class, int.class, long.class, float.class, double.class, void.class)
                .allMatch(Class::isPrimitive));
    }

    @Test
    void testIsNotPrimitive() {
        assertTrue(Stream.of(Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Void.class)
                .noneMatch(Class::isPrimitive));
    }

    @Test
    void testObjectToString() {
        final var painting = TestSampleGenerator.createPaintingList().stream()
                .findAny()
                .orElseThrow();

        final var person = Person.createTestPersonList()
                .stream()
                .findAny()
                .orElseThrow();

        List<Object> objects = List.of(painting, person, LocalDate.of(1989, 10, 18), new BigInteger("2000"));

        final var string = objects.stream()
                .map(o -> o.getClass().getSimpleName())
                .collect(Collectors.joining("\n"));

        println(string);

        assertAll(
                () -> assertTrue(string.contains("Painting")),
                () -> assertTrue(string.contains("Person"))
        );
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
        final List<? super Comparable<?>> list = List.of(
                LocalDate.of(2021, Month.OCTOBER, 4),
                "Hello",
                "This is weird",
                BigDecimal.valueOf(20),
                new Person("", null, null));

        list.forEach(It::println);

        assertTrue(list.contains("Hello"));
    }

    @Test
    void testExtendedBoundedWildCardList() {
        final Collection<? super Comparable<? extends Comparable<? extends Comparable<? extends Comparable<?>>>>> list = new ArrayList<>();
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

    public static void printStrings(List<String> strings) {
        strings.forEach(It::println);
    }

    @Test
    void testTypeWitnessNotNeededAnyMore() {
        //noinspection RedundantTypeArguments
        assertDoesNotThrow(() -> printStrings(Collections.<String>emptyList()));
    }

    @RepeatedTest(100)
    void testFlakyOrNot() {
        assertEquals(0.5, Math.random(), 0.5);
    }

    /**
     * @see <a href="https://youtu.be/EQPr7OhG768?t=1004">IntelliJ IDEA Conf 2022 | Evolving JUnit 5</a>
     * @return A stream of dynamic tests
     */
    @TestFactory
    Stream<DynamicTest> evenNumberDivisibleByTwo() {
        return IntStream.iterate(0, n -> n + 2)
                .limit(200)
                .mapToObj(n -> dynamicTest(n + " is divisible by 2",
                        () -> assertEquals(0, n % 2)));
    }

    /**
     * <a href="https://www.tabnine.com/code/java/methods/java.lang.Runtime/exec">How to use exec method in java.lang.Runtime</a>
     */
    @Test
    void testExecNvidiaSmi() throws IOException {
        final Process process;
        try {
            //noinspection UseOfProcessBuilder
            process = new ProcessBuilder("nvidia-smi").start();
        } catch (IOException e) {
            Assumptions.abort(e.getMessage());
            throw new IllegalStateException(e);
        }
        assertTrue(process.isAlive());

        try (final var inputStream = process.getInputStream()) {
            final var result = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            println(result);
        }
        assertFalse(process.isAlive());
    }

    /**
     * Filed a Stackoverflow question for test cases below.
     * <p>
     * A JDK bug is reported in response
     *
     * @see <a href="https://stackoverflow.com/questions/73654810/why-does-string-creation-using-newinstance-method-behave-different-when-usin">
     * Why does String creation using `newInstance()` method behave different when using `var` compared to using explicit type `String`?</a>
     * @see <a href="https://bugs.java.com/bugdatabase/view_bug.do?bug_id=JDK-8293578">JDK-8293578 : Duplicate ldc generated by javac</a>
     * @see <a href="https://bugs.openjdk.org/browse/JDK-8293578">Duplicate ldc generated by javac</a>
     *
     * This bug is now fixed so the original unexpected assertions are commented out
     */
    @Nested
    class StringNewInstanceUsingReflection {

        @Test
        void testClassNewInstance() throws Exception {
            final var input = "A string";
            final var theClass = input.getClass();
            final var constructor = theClass.getConstructor();
            final String newString = constructor.newInstance();

            assertEquals("", newString);
        }

        @Test
        @Disabled("In transition fase, sometimes fails, so ignored for now")
        void testClassNewInstanceWithVarOnly() throws Exception {
            final var input = "A string";
            final var theClass = input.getClass();
            final var constructor = theClass.getConstructor();
            final var newString = constructor.newInstance();

            // unexpected
            // assertEquals("A string", newString);
            // unexpected result should be fixed by now
            assertEquals("", newString);

        }

        @Test
        @Disabled("In transition fase, sometimes fails, so ignored for now")
        void testNewInstanceInlined() throws Exception {
            final var newString = "A string"
                    .getClass()
                    .getConstructor()
                    .newInstance();
            // unexpected
            // assertEquals("A string", newString);
            // unexpected result should be fixed by now
            assertEquals("", newString);
        }

        @Test
        void testStringReAssignmentToExplicitStringType() throws Exception {
            String string = "foo".getClass().getConstructor().newInstance();
            println("string = " + string);
            string = "bar";

            assertEquals("bar", string);
        }

        @Test
        @Disabled("In transition fase, sometimes fails, so ignored for now")
        void testStringReAssignmentToVar() throws Exception {
            var string = "foo".getClass().getConstructor().newInstance();
            println("string = " + string);
            string = "bar";
            // unexpected
            // assertEquals("foo", string);
            // unexpected result should be fixed by now
            assertEquals("bar", string);
        }

    }

}
