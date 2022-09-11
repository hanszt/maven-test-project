package org.hzt;

import org.eclipse.collections.api.tuple.primitive.IntIntPair;
import org.eclipse.collections.impl.map.mutable.primitive.IntIntHashMap;
import org.hzt.model.Payment;
import org.hzt.model.Person;
import org.hzt.sequences.primitve_sequences.IntSequence;
import org.hzt.sequences.primitve_sequences.PrimitiveIterable;
import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Book;
import org.hzt.test.model.Museum;
import org.hzt.test.model.Painter;
import org.hzt.test.model.Painting;
import org.hzt.utils.It;
import org.hzt.utils.PreConditions;
import org.hzt.utils.collections.MutableSetX;
import org.hzt.utils.collections.SetX;
import org.hzt.utils.collectors.CollectorsX;
import org.hzt.utils.sequences.Sequence;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.valueOf;
import static java.util.Map.Entry.comparingByKey;
import static java.util.function.Predicate.isEqual;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toUnmodifiableSet;
import static org.hzt.Streams.fibonacciStream;
import static org.hzt.Streams.fibonacciStreamV2;
import static org.hzt.Streams.subtractingFibonacciStream;
import static org.hzt.stream.StreamUtils.by;
import static org.hzt.stream.StreamUtils.function;
import static org.hzt.utils.collectors.CollectorsX.flatMappingToList;
import static org.hzt.utils.collectors.CollectorsX.mappingToSet;
import static org.hzt.utils.collectors.CollectorsX.multiMappingToList;
import static org.hzt.utils.function.predicates.ComparingPredicates.greaterThan;
import static org.hzt.utils.function.predicates.StringPredicates.containsAllOf;
import static org.junit.jupiter.api.Assertions.*;

class StreamsTest {

    private static final List<Payment> PAYMENT_LIST = List.of(
            new Payment("1", BigDecimal.valueOf(1_000_000)),
            new Payment("2", BigDecimal.valueOf(4_000_000)),
            new Payment("3", BigDecimal.valueOf(2_000_000)),
            new Payment("4", BigDecimal.valueOf(2_000_000)));

    private static <K, V> void printKeyAndValue(K key, V value) {
        System.out.println("key = " + key + ", value = " + value);
    }

    @Test
    void testGettingSumUsingStramReduction() {
        BigDecimal sum = PAYMENT_LIST.stream()
                .map(Payment::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        assertEquals(BigDecimal.valueOf(9_000_000), sum);
    }

    @Test
    void testGroupingByAndMapping() {
        var map = PAYMENT_LIST.stream()
                .collect(groupingBy(Payment::amount, mappingToSet(Payment::id)));

        map.forEach(StreamsTest::printKeyAndValue);
        assertEquals(3, map.size());
    }

    @Test
    void testCounting() {
        var summaryStatistics = PAYMENT_LIST.stream()
                .map(Payment::id)
                .mapToInt(Integer::parseInt)
                .summaryStatistics();

        assertEquals(2.5, summaryStatistics.getAverage());
    }

    @Test
    void testStreamLimit() {
        List<Integer> list = getIntegerStream(100).collect(Collectors.toList());
        List<Integer> list2 = getIntegerStream(100).collect(Collectors.toList());
        Collections.shuffle(list);
        Collections.shuffle(list2);

        list.sort(Comparator.naturalOrder());

        final var expectedLimitedList = list.subList(0, 10);

        final var limitedList = list2.stream()
                .sorted()
                .limit(10)
                .toList();

        assertEquals(expectedLimitedList, limitedList);
    }

    @Test
    void testReturnStreamFromIterator() {
        final var iterator = getIntegerStream(100).iterator();
        final var expected = getIntegerStream(100)
                .map(String::valueOf)
                .toList();

        final var result = Streams.returnStreamFromIterator(iterator)
                .map(String::valueOf)
                .toList();

        assertEquals(expected, result);
    }

    @SuppressWarnings("SameParameterValue")
    private Stream<Integer> getIntegerStream(int endExclusive) {
        return IntStream.range(0, endExclusive).boxed();
    }

    @Test
    void testFibonacciUsingStreams() {
        final var expected = Stream.of(0, 1, 1, 2, 3, 5, 8, 13, 21, 34)
                .map(BigInteger::valueOf)
                .toList();

        final var fibonacciList = Streams.fibonacciStream().limit(10).toList();
        assertEquals(expected, fibonacciList);
    }

    @Test
    void testNthFibonacciNrUsingStreams() {
        final var fibonacciList = getNthFibonacciNumber(500);
        assertEquals(new BigDecimal(
                "139423224561697880139724382870407283950070256587697307264108962948325571622863290691557658876222521294125"
        ).toBigInteger(), fibonacciList);
    }

    @SuppressWarnings("SameParameterValue")
    private static BigInteger getNthFibonacciNumber(int n) {
        return fibonacciStream()
                .skip(1)
                .limit(n)
                .reduce((first, second) -> second)
                .orElseThrow();
    }

    @Test
    void testSumFibonacciNrUsingStreams() {
        final var fibonacciList = getSumFibonacciNumbers(500);
        assertEquals(new BigDecimal(
                "365014740723634211012237077906479355996081581501455497852747829366800199361550174096573645929019489792750"
        ).toBigInteger(), fibonacciList);
    }

    private static BigInteger getSumFibonacciNumbers(@SuppressWarnings("SameParameterValue") int n) {
        return fibonacciStream()
                .skip(1)
                .limit(n)
                .reduce(BigInteger.ZERO, BigInteger::add);
    }

    @Test
    void testCalculatePiParallelUsingStreams() {
        Timer<BigDecimal> timer = Timer.timeAFunction(10_000_000, amount -> Streams.parallelLeibnizBdStream(amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .multiply(BigDecimal.valueOf(4)));
        final var result = timer.getResult();
        System.out.println("Duration: " + timer.getDuration());
        assertEquals(new BigDecimal("3.1415927972"), result);
    }

    @Test
    void testCalculatePiParallelUsingDoubleStream() {
        final var iterations = 10_000_000;
        //act
        Timer<Double> sequentialTimer = Timer.timeAFunction(iterations,
                nr -> Streams.leibnizStream(nr).sum() * 4);
        Timer<Double> parallelTimer = Timer.timeAFunction(iterations,
                nr -> Streams.parallelLeibnizStream(nr).sum() * 4);

        final var seqTimeInMillis = sequentialTimer.getDurationInMillis();
        final var parallelTimeInMillis = parallelTimer.getDurationInMillis();
        System.out.println("Sequential in milliseconds = " + seqTimeInMillis);
        System.out.println("Parallel in milliseconds = " + parallelTimeInMillis);
        System.out.println("parallelTimer.getResult() = " + parallelTimer.getResult());
        //assert
        assertTrue(parallelTimeInMillis < seqTimeInMillis);
        assertEquals(parallelTimer.getResult(), sequentialTimer.getResult());
    }

    @Nested
    class CollatzConjectureTests {

        @Test
        void testCollatzStream() {
            final var collatzNrs = Streams.collatzStream(BigInteger.valueOf(3))
                    .peek(System.out::println)
                    .takeWhile(not(ONE::equals))
                    .toList();

            final var expected = IntStream.of(3, 10, 5, 16, 8, 4, 2)
                    .mapToObj(BigInteger::valueOf)
                    .toList();

            assertEquals(expected, collatzNrs);
        }

        @Test
        void testCollatzStreamLong() {
            final var collatzNrs = Streams.collatzStream(valueOf(27))
                    .takeWhile(not(ONE::equals))
                    .count();

            assertEquals(111L, collatzNrs);
        }

        @Test
        void testCollatzStreamLongVsBd() {
            final var collatzNrsBdStream = Streams.collatzStream(valueOf(27))
                    .takeWhile(not(ONE::equals))
                    .mapToLong(BigInteger::longValue)
                    .toArray();

            final var collatzNrs = Streams.collatzStream(27L)
                    .peek(It::println)
                    .takeWhile(nr -> nr != 1L)
                    .toArray();

            assertArrayEquals(collatzNrs, collatzNrsBdStream);
        }

        /**
         * Benford's law, also known as the Newcomb–Benford law, the law of anomalous numbers, or the first-digit law,
         * is an observation that in many real-life sets of numerical data, the leading digit is likely to be small.
         *
         * @see <a href="https://youtu.be/094y1Z2wpJg?t=322">Benford's law</a>
         * @see <a href="https://en.wikipedia.org/wiki/Benford%27s_law">Benford's law wikipedia</a>
         */
        @Test
        void testBenfordsLawCollatzNrs() {
            final var result = LongStream.range(1, 200_000)
                    .flatMap(initNr -> Streams.collatzStream(initNr).takeWhile(nr -> nr != 1))
                    .collect(IntIntHashMap::new,
                            (map, value) -> map.addToValue(firstDigit(value), 1),
                            IntIntHashMap::putAll);

            final var firstDigitPresenceInAscendingOrder = Sequence.of(result.keyValuesView())
                    .sortedBy(IntIntPair::getTwo)
                    .mapToInt(IntIntPair::getOne)
                    .toArray();

            System.out.println("result = " + result);

            assertArrayEquals(new int[]{9, 8, 7, 6, 5, 4, 3, 2, 1}, firstDigitPresenceInAscendingOrder);
        }

        int firstDigit(final long input) {
            PreConditions.require(input >= 0, () -> "x must be greater ot equal to 0. (was " + input + ")");
            long x = input;
            while (x > 9) {
                x /= 10;
            }
            return (int) x;
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "123123 -> 1",
                "2353453 -> 2",
                "567567435 -> 5",
                "0 -> 0"})
        void testFirstDigit(String string) {
            final String[] split = string.split(" -> ");
            final var input = Long.parseLong(split[0]);
            final var expected = Integer.parseInt(split[1]);

            assertEquals(expected, firstDigit(input));
        }

        @Test
        void testMaxNrInCollatzSequencesTill200000() {
            final var maxNr = LongStream.range(1, 200_000)
                    .flatMap(initNr -> Streams.collatzStream(initNr).takeWhile(nr -> nr != 1))
                    .max()
                    .orElseThrow();

            assertEquals(17_202_377_752L, maxNr);
        }
    }

    @Test
    void testBirthDayStream() {
        Month curMonth = Month.OCTOBER;

        final var monthDayListMap1 = Person.createTestPersonList().stream()
                .filter(person -> person.getDateOfBirth().getMonth().equals(curMonth))
                .collect(groupingBy(person -> MonthDay.from(person.getDateOfBirth())));

        final var monthDayListMap = Person.createTestPersonList().stream()
                .filter(by(Person::getDateOfBirth, LocalDate::getMonth, isEqual(curMonth)))
                .collect(groupingBy(function(Person::getDateOfBirth).andThen(MonthDay::from)));

        monthDayListMap.entrySet().stream()
                .filter(by(Entry::getValue, List::size, greaterThan(0)))
                .sorted(comparingByKey())
                .forEach(System.out::println);

        assertFalse(monthDayListMap.isEmpty());
        assertEquals(monthDayListMap1.size(), monthDayListMap.size());
    }

    @Test
    void testStreamOnCloseMethod() {
        AtomicBoolean isClosedInTryWithResourcesBlock = new AtomicBoolean();
        AtomicBoolean isClosedOutsideTryWithResources = new AtomicBoolean();

        final var stream = TestSampleGenerator.createBookList().stream();
        final var expected = getFilteredBookTitleList("Stream", isClosedOutsideTryWithResources, stream);

        try (final var bookStream = TestSampleGenerator.createBookList().stream()) {
            final var filteredBookTitles = getFilteredBookTitleList(
                    "Stream in try with resources block", isClosedInTryWithResourcesBlock, bookStream);

            assertAll(
                    () -> assertEquals(3, filteredBookTitles.size()),
                    () -> assertEquals(expected, filteredBookTitles)
            );
        }
        assertAll(
                () -> assertTrue(isClosedInTryWithResourcesBlock.get()),
                () -> assertFalse(isClosedOutsideTryWithResources.get())
        );
    }

    private List<String> getFilteredBookTitleList(String message, AtomicBoolean isClosed, Stream<Book> bookStream) {
        return bookStream
                .map(Book::getTitle)
                .filter(containsAllOf("a", "e"))
                .distinct()
                .onClose(() -> setClosedAndPrintClosed(message, isClosed))
                .toList();
    }

    private void setClosedAndPrintClosed(String message, AtomicBoolean closed) {
        closed.set(true);
        System.out.println(message + " is closed now");
    }

    @Test
    void testStreamToListDoesNotAllowsNulls() {
        final var strings = Stream.of("This", "is", "a", null, "test").toList();
        final var expected = Arrays.asList("This", "is", "a", null, "test");
        assertEquals(expected, strings);
    }

    @Test
    void testMultiMappingOptionals() {
        final List<Painting> paintingList = TestSampleGenerator.createPaintingList();

        final Set<LocalDate> expected = paintingList.stream()
                .map(Painting::painter)
                .map(Painter::getDateOfDeath)
                .flatMap(Optional::stream)
                .collect(toUnmodifiableSet());

        final Set<LocalDate> actual = paintingList.stream()
                .map(Painting::painter)
                .map(Painter::getDateOfDeath)
                .collect(CollectorsX.multiMapping(Optional::ifPresent, toUnmodifiableSet()));

        System.out.println(actual);
        assertEquals(expected, actual);
    }

    @Test
    void testMultiMappingMuseums() {
        final List<Museum> museumList = TestSampleGenerator.getMuseumListContainingNulls();

        final var expected = museumList.stream()
                .collect(groupingBy(museum -> museum.getPaintings().size(),
                        flatMappingToList(Museum::toPainterDateOfBirthStream)));

        final var actual = museumList.stream()
                .collect(groupingBy(museum -> museum.getPaintings().size(),
                        multiMappingToList(Museum::toDatesOfBirthPainters)));

        System.out.println(actual);

        assertEquals(expected, actual);
    }

    @Test
    void testIntStreamFromIntIterable() {
        PrimitiveIterable.OfInt intIterable = IntSequence.of(1, 2, 3, 4, 5).map(i -> i * 2);

        final var actual = StreamSupport.intStream(intIterable.spliterator(), false).toArray();

        assertArrayEquals(new int[]{2, 4, 6, 8, 10}, actual);
    }

    @Test
    void testCollectFromIntStream() {
        final SetX<Integer> integers = IntStream.iterate(0, i -> i < 100, i -> ++i)
                .collect(MutableSetX::empty, Set::add, Set::addAll);

        final SetX<Integer> set = Sequence.generate(0, i -> ++i).takeWhile(i -> i < 100).toSetX();

        assertAll(
                () -> assertEquals(100, integers.size()),
                () -> assertEquals(integers.toSetX(), set)
        );
    }

    @Test
    void testStreamBuilder() {
        final Stream<String> stream = Stream.<String>builder()
                .add("Hallo").add("Dit").add("is").add("een").add("test").build();

        final String[] actual = stream.toArray(String[]::new);
        final String[] expected = {"Hallo", "Dit", "is", "een", "test"};

        assertArrayEquals(expected, actual);
    }

    @Test
    void testParallelFibonacciStream() {
        final var count = fibonacciStream()
                .parallel()
                .takeWhile(s -> s.toString().length() < 10_000)
                .count();

        assertEquals(47847, count);
    }

    @Test
    void testParallelFibonacciStreamBehavesDifferentThenSequential() {
        final var parallelStream = fibonacciStream()
                .parallel()
                .dropWhile(s -> s.toString().length() < 10)
                .filter(s -> s.isProbablePrime(100));

        final var sequentialStream = fibonacciStream()
                .dropWhile(s -> s.toString().length() < 10)
                .filter(s -> s.isProbablePrime(100));

        //noinspection ResultOfMethodCallIgnored
        assertAll(
                () -> assertEquals(Optional.of(valueOf(2971215073L)), sequentialStream.findAny()),
                () -> assertThrows(OutOfMemoryError.class, parallelStream::findAny)
        );
    }

    @Test
    void testFirst10Primes() {
        final var firstTenPrimes = Streams.primes()
                .limit(10)
                .toArray();

        assertArrayEquals(new long[]{2L, 3L, 5L, 7L, 11L, 13L, 17L, 19L, 23L, 29L}, firstTenPrimes);
    }

    @Test
    void testFirstPrimeLargerThan10Digits() {
        final var primeTenDigits = Streams
                .primes()
                .dropWhile(l -> Long.toString(l).length() < 5)
                .findFirst();

        assertEquals(OptionalLong.of(10007), primeTenDigits);
    }

    @Test
    void testFibPrimeAfter100Terms() {
        final var fibPrime = fibonacciStreamV2()
//                .parallel()
                .skip(100)
                .filter(s -> s.isProbablePrime(100))
                .findFirst();

        assertEquals(Optional.of(new BigInteger("1066340417491710595814572169")), fibPrime);
    }

    @Test
    void testIterableFromStream() {
        Iterable<String> strings = () -> Stream.of("Iterable", "from", "a", "stream").iterator();
        int counter = 0;
        for (String s : strings) {
            System.out.println("s = " + s);
            counter++;
        }
        assertEquals(4, counter);
    }

    @Test
    void testDifferenceBetweenFindAnyAndFindFirst() {
        final var any = IntStream.iterate(-100, i -> ++i)
                .limit(100_000)
                .findAny()
                .orElseThrow();

        final var first = IntStream.iterate(-100, i -> ++i)
                .limit(100_000)
                .findFirst()
                .orElseThrow();

        assertEquals(any, first);
    }

    @Test
    void testDifferenceBetweenFindAnyAndFindFirstInParallel() {
        final var any = IntStream.iterate(-100, i -> ++i)
                .limit(100_000)
                .parallel()
                .findAny()
                .orElseThrow();

        final var first = IntStream.iterate(-100, i -> ++i)
                .limit(100_000)
                .parallel()
                .findFirst()
                .orElseThrow();

        assertNotEquals(any, first);
    }

    @Test
    void testCustomEmptyStream() {
        final var empty = Streams.empty();
        assertEquals(0L, empty.count());
    }

    @Nested
    class ZipWitNextTests {
        /**
         * gcd: Greatest common divisor
         */
        @Test
        void testGreatestCommonDivisorTwoAdjFibNrsAllOne() {
            final var gcdList = Streams.zipWithNext(fibonacciStream()::iterator, BigInteger::gcd)
                    .limit(1_000)
                    .toList();

            assertAll(
                    () -> assertEquals(1_000, gcdList.size()),
                    () -> assertTrue(gcdList.stream().allMatch(ONE::equals))
            );
        }

        @Test
        void testZipWithNext() {
            final var integers = List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

            final var array = Streams.zipWithNext(integers, Integer::sum)
                    .peek(System.out::println)
                    .mapToInt(Integer::intValue)
                    .toArray();

            System.out.println(Arrays.toString(array));

            assertArrayEquals(new int[]{1, 3, 5, 7, 9, 11, 13, 15, 17, 19}, array);
        }

        @Test
        void testZipWithNextWithOnlyOneThrowsException() {
            final var integers = List.of(0);

            final var integerStream = Streams.zipWithNext(integers, Integer::sum);
            assertThrows(NoSuchElementException.class, () -> integerStream.forEach(System.out::println));
        }

        @Test
        void testZipWithNextWithTwoElementsYieldsResult() {
            final var integers = List.of(1, 3);

            final var integerStream = Streams.zipWithNext(integers, Integer::sum);
            final var integer = integerStream.findFirst().orElseThrow();

            assertEquals(4, integer);
        }
    }

    @Nested
    class WindowedTests {

        @Test
        void testWindowedWithPartialWindows() {
            final var range = IntStream.range(0, 20);

            final var windows = Streams.windowed(range::iterator, 10).toList();

            System.out.println("windows = " + windows);

            assertEquals(20, windows.size());
        }

        @Test
        void testWindowedNoPartialWindows() {
            final var range = IntStream.range(0, 2_000);

            final var windows = Streams
                    .windowed(range::iterator, 10, false)
                    .toList();

            System.out.println("windows = " + windows);

            assertEquals(1991, windows.size());
        }
    }

    @Nested
    class ParallelStreamTests {

        @Test
        void testGroupByConcurrent() {
            final var fibonacciNrs = fibonacciStream()
                    .limit(1_000)
                    .toList();

            final var grouping = fibonacciNrs.parallelStream()
                    .collect(Collectors.groupingByConcurrent(s -> s.mod(valueOf(7L))));

            System.out.println("grouping = " + grouping);

            assertEquals(7, grouping.size());
        }

        @Test
        void testToConcurrentMap() {
            final var fibonacciNrs = subtractingFibonacciStream()
                    .peek(System.out::println)
                    .limit(1_000)
                    .toList();

            final var map = fibonacciNrs.parallelStream()
                    .distinct()
                    .collect(Collectors.toConcurrentMap(s -> s, s -> s.isProbablePrime(100)));

            assertEquals(1_000, map.size());
        }
    }
}
