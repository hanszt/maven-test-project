package demo.sequences;

import demo.Car;
import demo.CarDemo;
import demo.It;
import demo.Pair;
import org.hzt.test.ReplaceCamelCaseBySentence;
import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.BankAccount;
import org.hzt.test.model.Museum;
import org.hzt.test.model.Painting;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.time.Instant;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static demo.It.printf;
import static demo.It.println;
import static java.util.stream.Collectors.groupingBy;
import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(ReplaceCamelCaseBySentence.class)
class SequenceTest {

    @Test
    void testFlatMap() {
        final var museumList = TestSampleGenerator.createMuseumList();

        final var paintings = Sequence.of(museumList)
                .flatMap(Museum::getPaintings)
                .toList();

        println(paintings);

        assertEquals(9, paintings.size());
    }

    @Test
    void testGenerate() {
        final var atomicInteger = new AtomicInteger();

        final var integers = Sequence.generate(atomicInteger::getAndIncrement)
                .take(10)
                .toList();

        assertEquals(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), integers);
    }

    @Test
    void testGenerateWithSeedGenerator() {
        final var atomicInteger = new AtomicInteger();

        final var integers = Sequence.generate(atomicInteger::getAndIncrement, i -> i + 1)
                .take(10)
                .toList();

        assertEquals(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), integers);
    }

    @Test
    void testGenerateLucasNrs() {
        record Pair(BigInteger first, BigInteger second) {
            Pair next() {
                return new Pair(second, first.add(second));
            }
        }
        final var lucasNrs = Sequence.iterate(new Pair(BigInteger.TWO, BigInteger.ONE), Pair::next)
                .map(Pair::first)
                .take(10)
                .toList();

        final var expected = Sequence.of(2, 1, 3, 4, 7, 11, 18, 29, 47, 76)
                .map(BigInteger::valueOf)
                .toList();

        assertEquals(expected, lucasNrs);
    }

    @Test
    void testDistinct() {
        final var list = List.of(1, 2, 3, 4, 4, 4, 5, 5, 6, 7, 8, 9, 9, 5, 3);

        final var integers = Sequence.of(list)
                .distinct()
                .toList();

        final var set = new HashSet<>(list);

        assertEquals(set.size(), integers.size());
    }

    @Test
    void testSimpleStreamWithMapYieldsIteratorWithNext() {
        final var list = List.of("Hallo", "dit", "is", "een", "test");

        final var sequence = Sequence.of(list)
                .map(SequenceTest::lengthMappingNotCalledWhenNotConsumed);

        assertTrue(sequence.iterator()::hasNext);
    }

    private static int lengthMappingNotCalledWhenNotConsumed(final String s) {
        fail("Should only be called when consumed with terminal operation");
        return s.length();
    }

    @Test
    void testSequenceIsLazy() {
        final var list = List.of(1, 2, 3, 4, 5, 6);

        final var sequence = Sequence.of(list)
                .filter(SequenceTest::filterNotCalledWhenNotConsumed)
                .withIndex()
                .windowed(4)
                .sortedBy(List::size)
                .distinctBy(List::hashCode);

        println(sequence);

        assertNotNull(sequence);
    }

    private static boolean filterNotCalledWhenNotConsumed(final int i) {
        fail("Should only be called when consumed with terminal operation");
        return i < 3;
    }

    @Test
    void testFilterReduce() {
        final var list = List.of("Hallo", "dit", "is", "een", "test");
        final int sum = Sequence.of(list)
                .map(String::length)
                .filter(l -> l > 3)
                .reduce(Integer::sum)
                .orElseThrow(NoSuchElementException::new);

        assertEquals(9, sum);
    }

    @Test
    void testMapFilterReduce() {
        final var list = List.of("Hallo", "dit", "is", "een", "test");

        final double sum = Sequence.of(list)
                .map(String::length)
                .onEach(It::println)
                .filter(length -> length < 5)
                .map(Double::valueOf)
                .reduce(Double::sum)
                .orElseThrow(NoSuchElementException::new);

        assertEquals(12, sum);
    }

    @Test
    void testFilterIndexed() {
        final var list = List.of("Hallo", "dit", "is", "een", "test");

        final long sum = Sequence.of(list)
                .map(String::length)
                .filterIndexed((index, length) -> length > 2 && index % 2 != 0)
                .map(Integer::longValue)
                .reduce(0L, Long::sum);

        assertEquals(6, sum);
    }

    @Test
    void testMapNotNull() {
        final var list = TestSampleGenerator.createSampleBankAccountListContainingNulls();

        final var sum = Sequence.of(list)
                .mapNotNull(BankAccount::getBalance)
                .to(ArrayList::new);

        assertFalse(sum.contains(null));
    }

    @Test
    void testMapFilterReduceToList() {
        final var list = List.of("Hallo", "dit", "is", "een", "test");
        final var result = Sequence.of(list)
                .map(String::length)
                .filter(l -> l > 3)
                .toList();

        assertEquals(List.of(5, 4), result);
    }

    @Test
    void testMapFilterReduceToSet() {
        final var list = List.of("Hallo", "dit", "is", "een", "test");

        final var result = Sequence.of(list)
                .map(String::length)
                .toSet();

        assertEquals(Set.of(2, 3, 4, 5), result);
    }

    @Test
    void testFlatMapToList() {
        final List<Iterable<String>> list = List.of(List.of("Hallo", "dit"), Set.of("is", "een"),
                new ArrayDeque<>(Collections.singleton("test")));

        //noinspection Convert2MethodRef
        final var result = Sequence.of(list)
                .flatMap(t -> It.self(t))
                .filter(s -> s.length() > 3)
                .filterNot(String::isEmpty)
                .toList();

        println("result = " + result);

        assertIterableEquals(List.of("Hallo", "test"), result);
    }

    @Test
    void testMapMultiToList() {
        final List<Iterable<String>> list = List.of(List.of("Hallo", "dit"), Set.of("is", "een"),
                new ArrayDeque<>(Collections.singleton("test")));

        final var result = Sequence.of(list)
                .<String>mapMulti(Iterable::forEach)
                .filter(s -> s.length() > 3)
                .filterNot(String::isEmpty)
                .toList();

        println("result = " + result);

        assertEquals(List.of("Hallo", "test"), result);
    }

    @Test
    void testCollectGroupingBy() {
        final var museumList = TestSampleGenerator.getMuseumListContainingNulls();

        final var expected = museumList.stream()
                .flatMap(m -> m.getPaintings().stream())
                .collect(Collectors.groupingBy(Painting::painter));

        final var actual = Sequence.of(museumList)
                .flatMap(Museum::getPaintings)
                .collect(Collectors.groupingBy(Painting::painter));

        final var actual2 = Sequence.of(museumList)
                .flatMap(Museum::getPaintings)
                .groupBy(Painting::painter);

        assertAll(
                () -> assertEquals(expected, actual),
                () -> assertEquals(expected, actual2)
        );
    }

    @Test
    void testFlatMapStream() {
        final var charInts = Sequence.of("hallo", "test")
                .map(String::chars)
                .flatMap(stream -> stream::iterator)
                .toList();

        assertEquals(List.of(104, 97, 108, 108, 111, 116, 101, 115, 116), charInts);
    }


    @Test
    void testTakeWhile() {
        final var strings = Sequence.iterate(0, i -> ++i)
                .takeWhile(i -> i < 10)
                .filter(l -> l % 2 == 0)
                .onEach(It::println)
                .map(String::valueOf)
                .map(String::trim)
                .toList();

        assertEquals(5, strings.size());
    }

    @Test
    void testSkipWhile() {
        final var list = List.of(1, 2, 10, 4, 5, 10, 6, 5, 3, 5, 6);

        final var integers = Sequence.of(list)
                .skipWhile(i -> i != 5)
                .toList();

        final var expected = list.stream()
                .dropWhile(i -> i != 5)
                .toList();

        println("integers = " + integers);

        assertAll(
                () -> assertEquals(List.of(5, 10, 6, 5, 3, 5, 6), integers),
                () -> assertEquals(expected, integers)
        );
    }

    @Test
    void testDistinctBy1() {
        final var integers = Sequence.of("hallo", "hoe", "is", "het");

        final var strings = integers
                .distinctBy(String::length)
                .map(StringBuilder::new)
                .map(StringBuilder::reverse)
                .map(StringBuilder::toString)
                .toList();

        println("strings = " + strings);

        assertEquals(List.of("ollah", "eoh", "si"), strings);
    }

    @Test
    void testDistinctBy2() {
        final var list = List.of("This", "test", "is", "a", "test", "for", "distinct", "by");

        final var words = Sequence.of(list)
                .distinctBy(String::length)
                .toList();

        final var distinctLengths = list.stream()
                .map(String::length)
                .distinct()
                .toList();

        assertEquals(distinctLengths.size(), words.size());
    }

    @Test
    void testSequenceWithConstrainOnceMethodCanOnlyBeUsedOnce() {
        Sequence<List<Integer>> windowedSequence = Sequence.of(Arrays.asList(1, 2, 3, 4, 5, 3, -1, 6, 12))
                .onEach(It::println)
                .filter(i -> i % 2 == 0)
                .windowed(2)
                .constrainOnce();

        final List<List<Integer>> windows = windowedSequence.toList();

        System.out.println("nameList = " + windows);

        final List<List<Integer>> expected = Arrays.asList(List.of(2, 4), List.of(4, 6), List.of(6, 12));

        assertAll(
                () -> assertEquals(expected, windows),
                () -> assertThrows(IllegalStateException.class, windowedSequence::first)
        );
    }

    @Test
    void testSequenceWithConstrainOnceMethodCanOnlyBeUsedOncAfterShortCircuitingOperations() {
        Sequence<Integer> integers = Sequence.of(Arrays.asList(1, 2, 3, 4, 5, 3, -1, 6, 12))
                .filter(i -> i % 2 == 0)
                .constrainOnce();

        final int first = integers.first();

        System.out.println("first = " + first);

        assertAll(
                () -> assertEquals(2, first),
                () -> assertThrows(IllegalStateException.class, integers::first)
        );
    }

    @Test
    void testSequenceWithConstrainOnceMethodCanOnlyBeUsedOncAfterShortCircuitingOperationsAndThen() {
        Sequence<Integer> integers = Sequence.of(Arrays.asList(1, 2, 3, 4, 5, 3, -1, 6, 12))
                .constrainOnce()
                .filter(i -> i % 2 == 0);

        final var first = integers.first(i -> i % 3 == 0);

        System.out.println("first = " + first);

        assertAll(
                () -> assertEquals(6, first),
                () -> assertThrows(IllegalStateException.class, integers::toList)
        );
    }

    @Nested
    class WindowedSequenceTests {

        @Test
        void testGenerateWindowedThenMapMultiToList() {
            final List<List<Integer>> windows = new ArrayList<>();

            final var result = Sequence.iterate(0, i -> ++i)
                    .windowed(8, 3)
                    .onEach(windows::add)
                    .takeWhile(s -> s.stream().reduce(0, Integer::sum) < 1_000_000)
                    .<Integer>mapMulti(Iterable::forEach)
                    .toList();

            Sequence.of(windows).filterIndexed((i, v) -> i % 10_000 == 0).forEach(It::println);

            println("windows.last() = " + windows.get(windows.size() - 1));

            assertEquals(333_328, result.size());
        }

        @Test
        void testWindowedThrowsExceptionWhenStepSizeNegative() {
            final var sequence = Sequence.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
            assertThrows(IllegalArgumentException.class, () -> sequence.windowed(-4));
        }

        @Test
        void testWindowedStepGreaterThanWindowSizeWithPartialWindow() {
            final var windows = Sequence.iterate(0, i -> i + 1)
                    .onEach(It::println)
                    .take(98)
                    .windowed(5, 6, true)
                    .toList();

            println("windows = " + windows);

            new HashMap<>(0);

            assertAll(
                    () -> assertEquals(List.of(0, 1, 2, 3, 4), windows.get(0)),
                    () -> assertEquals(List.of(96, 97), windows.get(windows.size() - 1))
            );
        }

        @Test
        void testWindowedStepGreaterThanWindowSizeNoPartialWindow() {
            final var windows = Sequence.iterate(0, i -> i + 1)
                    .take(98)
                    .onEach(It::println)
                    .windowed(5, 6)
                    .toList();

            println("windows = " + windows);

            assertAll(
                    () -> assertEquals(16, windows.size()),
                    () -> assertEquals(List.of(0, 1, 2, 3, 4), windows.get(0)),
                    () -> assertEquals(List.of(90, 91, 92, 93, 94), windows.get(windows.size() - 1))
            );
        }

        @Test
        void testWindowedStepSmallerThanWindowSizeWithPartialWindow() {
            final var windows = Sequence.iterate(0, i -> i + 1)
                    .take(11)
                    .windowed(5, 2, true)
                    .toList();

            println("windows = " + windows);

            assertAll(
                    () -> assertEquals(6, windows.size()),
                    () -> assertEquals(List.of(0, 1, 2, 3, 4), windows.get(0)),
                    () -> assertEquals(List.of(10), windows.get(windows.size() - 1))
            );
        }

        @Test
        void testWindowedStepSmallerThanWindowSizeNoPartialWindow() {
            final var windows = Sequence.iterate(0, i -> i + 1)
                    .take(8)
                    .windowed(4, 2)
                    .toList();

            println("windows = " + windows);

            assertAll(
                    () -> assertEquals(3, windows.size()),
                    () -> assertEquals(List.of(0, 1, 2, 3), windows.get(0)),
                    () -> assertEquals(List.of(4, 5, 6, 7), windows.get(windows.size() - 1))
            );
        }

        @Test
        void testWindowedSizeGreaterThanSequenceSizeNoPartialWindowGivesEmptyList() {
            final var windows = Sequence.of(0, 8)
                    .windowed(10)
                    .toList();

            println("windows = " + windows);

            assertTrue(windows.isEmpty());
        }

        @Test
        void testSequenceWindowedTransformed() {
            final var sizes = Sequence.iterate(0, i -> i + 1)
                    .take(1_000)
                    .filter(i -> i % 5 == 0)
                    .windowed(51, 7)
                    .map(List::size)
                    .toList();

            println("sizes = " + sizes);

            println("windows.first() = " + sizes.get(0));
            println("windows.last() = " + sizes.get(sizes.size() - 1));

            assertEquals(22, sizes.size());
        }

        @Test
        void testChunked() {
            final var chunkSize = 5;

            final var chunked = Sequence.iterate(0, i -> i + 1)
                    .take(1_000)
                    .chunked(chunkSize);

            chunked.forEach(It::println);

            assertAll(
                    () -> assertTrue(chunked.all(l -> l.size() == chunkSize)),
                    () -> assertTrue(chunked.zipWithNext().all(this::lastOfFirstOneLessThanFirstOfSecond)));
        }

        private boolean lastOfFirstOneLessThanFirstOfSecond(final Pair<List<Integer>, List<Integer>> lists) {
            final var first = lists.first();
            final var lastOfFirst = first.get(first.size() - 1);
            final var second = lists.second();
            final var firstOfSecond = second.get(0);
            return lastOfFirst == firstOfSecond - 1;
        }
    }

    @Test
    void testEmpty() {
        final var list = Sequence.empty().toList();
        assertTrue(list.isEmpty());
    }

    @Test
    void testSequenceFromStream() {
        final var stream = IntStream.range(0, 100).boxed();

        final var list = Sequence.of(stream::iterator)
                .filter(i -> i % 2 == 0)
                .sortedBy(It::self)
                .windowed(3, 1, true)
                .flatMap(It::self)
                .withIndex()
                .toList();

        println("list = " + list);

        assertEquals(147, list.size());
    }

    @Test
    void testSequenceToStream() {
        final var sum = Sequence.iterate(0, i -> i + 1)
                .take(20)
                .stream()
                .reduce(0, Integer::sum);

        assertEquals(190, sum);
    }

    @Test
    void testSequenceCanBeConsumedMultipleTimes() {
        final var names = Sequence.of(List.of(1, 2, 3, 4, 5, 3, -1, 6, 12))
                .onEach(It::println)
                .mapNotNull(Year::of)
                .sortedBy(It::self);

        final var first = names.findFirst().orElseThrow();
        println("first = " + first);
        println("first leap year = " + names.findFirst(Year::isLeap).orElseThrow());
        final var nameList = names.toList();
        final var last = names.reduce((acc, value) -> value).orElseThrow();


        println("last = " + last);

        assertAll(
                () -> assertEquals(Year.of(-1), first),
                () -> assertEquals(Year.of(12), last),
                () -> assertEquals(9, nameList.size()),
                () -> assertEquals(3, names.windowed(2, 3).count())
        );
    }

    @Test
    void testFoldToArrayDeque() {
        final var deque = Sequence.iterate(0, i -> i + 2)
                .take(20)
                .filter(i -> i % 4 == 0)
                .fold(new ArrayDeque<Integer>(), SequenceTest::plusElement);

        println(deque);

        assertIterableEquals(List.of(0, 4, 8, 12, 16, 20, 24, 28, 32, 36), deque);
    }

    private static <T, C extends Collection<T>> C plusElement(final C collection, final T element) {
        collection.add(element);
        return collection;
    }

    @Test
    void testSequenceOfZoneIds() {
        final var now = Instant.now();
        final var current = now.atZone(ZoneId.systemDefault());
        printf("Current time is %s%n%n", current);

        final var noneWholeHourZoneOffsetSummaries = getTimeZoneSummaries(now, id -> nonWholeHourOffsets(now, id));

        noneWholeHourZoneOffsetSummaries.forEach(It::println);

        assertEquals(23, noneWholeHourZoneOffsetSummaries.count());
    }

    private boolean nonWholeHourOffsets(final Instant instant, final ZoneId id) {
        return instant.atZone(id).getOffset().getTotalSeconds() % 3600 != 0;
    }

    @Test
    void testTimeZonesAntarctica() {
        final var now = Instant.now();
        final var current = now.atZone(ZoneId.systemDefault());
        printf("Current time is %s%n%n", current);

        final var timeZonesAntarctica = getTimeZoneSummaries(now, id -> id.getId().contains("Antarctica"));

        timeZonesAntarctica.forEach(It::println);

        assertEquals(12, timeZonesAntarctica.count());
    }

    private Sequence<String> getTimeZoneSummaries(final Instant now, @NotNull final Predicate<ZoneId> predicate) {
        return Sequence.of(ZoneId.getAvailableZoneIds())
                .map(ZoneId::of)
                .filter(predicate)
                .map(now::atZone)
                .sortedBy(It::self)
                .map(this::toZoneSummary);
    }

    private String toZoneSummary(final ZonedDateTime zonedDateTime) {
        return String.format("%10s %-25s %10s", zonedDateTime.getOffset(), zonedDateTime.getZone(),
                zonedDateTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)));
    }

    @Test
    void testSequenceMinus() {
        final var list = Sequence.iterate(0, i -> i + 1)
                .take(10)
                .minus(2)
                .toList();

        assertEquals(List.of(0, 1, 3, 4, 5, 6, 7, 8, 9), list);
    }

    @Test
    void testSequenceMinusOtherIterable() {
        final var intsToRemove = List.of(1, 34, 3, 5);

        final var list = Sequence.iterate(0, i -> i + 1)
                .take(10)
                .minus(intsToRemove)
                .toList();

        assertEquals(List.of(0, 2, 4, 6, 7, 8, 9), list);
    }

    @Nested
    class TerminalOperationTests {

        @Test
        void testGroupBy() {
            final var expected = CarDemo.cars.stream().collect(groupingBy(Car::brand));

            final var actual = Sequence.of(CarDemo.cars).groupBy(Car::brand);

            assertEquals(expected, actual);
        }

        @Test
        void testMapReduce() {
            final var list = List.of("Hallo", "dit", "is", "een", "test");

            final double sum = Sequence.of(list)
                    .map(String::length)
                    .reduce(0, Integer::sum);

            assertEquals(17, sum);
        }

        @Test
        void testAllEven() {
            final var allEven = Sequence.of(2, 4, 8, 14, 12, 10_254_678, 13_528).all(i -> i % 2 == 0);
            assertTrue(allEven);
        }

        @Test
        void testNoneOdd() {
            final var noneOdd = Sequence.of(2, 4, 8, 14, 12, 10_254_678, 13_528).none(i -> i % 2 != 0);
            assertTrue(noneOdd);
        }

        @Test
        void testAnyOdd() {
            final var anyOdd = Sequence.of(2, 4, 8, 15, 12, 10_254_678, 13_528).any(i -> i % 2 != 0);
            assertTrue(anyOdd);
        }
    }
}
