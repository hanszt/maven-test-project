package org.hzt;

import org.apache.commons.math3.fraction.BigFraction;
import org.apache.commons.math3.fraction.Fraction;
import org.eclipse.collections.api.map.primitive.IntIntMap;
import org.eclipse.collections.api.tuple.primitive.IntIntPair;
import org.eclipse.collections.api.tuple.primitive.ObjectIntPair;
import org.eclipse.collections.impl.list.mutable.primitive.IntArrayList;
import org.hzt.collections.IndexedDataStructure;
import org.hzt.collections.TestDataStructure;
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
import org.hzt.utils.collections.MutableSetX;
import org.hzt.utils.collections.SetX;
import org.hzt.utils.collectors.CollectorsX;
import org.hzt.utils.sequences.Sequence;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.regex.Pattern;
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
import static org.hzt.StatisticsUtils.firstDigit;
import static org.hzt.Streams.*;
import static org.hzt.stream.StreamUtils.by;
import static org.hzt.stream.StreamUtils.function;
import static org.hzt.utils.It.println;
import static org.hzt.utils.collectors.CollectorsX.flatMappingToList;
import static org.hzt.utils.collectors.CollectorsX.mappingToSet;
import static org.hzt.utils.collectors.CollectorsX.multiMappingToList;
import static org.hzt.utils.function.predicates.ComparingPredicates.greaterThan;
import static org.hzt.utils.function.predicates.StringPredicates.containsAllOf;
import static org.hzt.utils.numbers.DoubleX.GOLDEN_RATIO;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

class StreamsTest {

    private static final List<Payment> PAYMENT_LIST = List.of(
            new Payment("1", BigDecimal.valueOf(1_000_000)),
            new Payment("2", BigDecimal.valueOf(4_000_000)),
            new Payment("3", BigDecimal.valueOf(2_000_000)),
            new Payment("4", BigDecimal.valueOf(2_000_000)));

    private static <K, V> void printKeyAndValue(K key, V value) {
        println("key = " + key + ", value = " + value);
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

        final var result = Streams.streamFromIterator(iterator)
                .map(String::valueOf)
                .toList();

        assertEquals(expected, result);
    }

    @Test
    void testReturnStreamFromIterableUSingMapMulti() {
        final List<Painting> list = TestSampleGenerator.createMuseumList().stream()
                .mapMulti(Museum::forEach)
                .toList();

        assertEquals(9, list.size());
    }

    @SuppressWarnings("SameParameterValue")
    private Stream<Integer> getIntegerStream(int endExclusive) {
        return IntStream.range(0, endExclusive).boxed();
    }

    @Test
    void testCalculatePiParallelUsingStreams() {
        Timer<BigDecimal> timer = Timer.timeALongFunction(10_000_000, amount -> Streams.leibnizBdStream(amount)
                .parallel()
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        final var result = timer.getResult();
        println("Duration: " + timer.getDuration());
        assertEquals(new BigDecimal("3.1415927694"), result);
    }

    @Test
    void testCalculatePiUsingLeibnizFractionStream() {
        final var timer = Timer.timeAnIntFunction(11, amount -> Streams.leibnizFractionStream(amount)
                .reduce(Fraction::add)
                .orElseThrow());
        final var result = timer.getResult();
        println("Duration: " + timer.getDuration());

        final var expected = new Fraction(1023461776, 334639305);
        println("result = " + result.doubleValue());

        assertEquals(expected, result);
    }

    @Test
    void testCalculatePiUsingLeibnizBigFractionStream() {
        final var timer = Timer.timeAnIntFunction(2_000, amount -> Streams.leibnizBigFractionStream(amount)
                .reduce(BigFraction::add)
                .orElseThrow());

        final var result = timer.getResult();
        final var duration = timer.getDuration();
        String formattedDuration = String.format("%2d:%02d s", duration.toSecondsPart(), duration.toMillisPart());
        println("Duration: " + formattedDuration);

        final var decimalValue = result.doubleValue();
        println("result = " + decimalValue);

        final var actual = BigDecimal.valueOf(decimalValue).setScale(2, RoundingMode.HALF_UP);
        assertEquals(BigDecimal.valueOf(3.14), actual);
    }

    @Test
    void testCalculatePiParallelUsingDoubleStream() {
        final var iterations = 10_000_000;
        //act
        Timer<Double> sequentialTimer = Timer.timeALongFunction(iterations,
                nr -> Streams.leibnizStream(nr).sum());
        Timer<Double> parallelTimer = Timer.timeALongFunction(iterations,
                nr -> Streams.leibnizStream(nr).parallel().sum());

        final var seqTimeInMillis = sequentialTimer.getDurationInMillis();
        final var parallelTimeInMillis = parallelTimer.getDurationInMillis();
        println("Sequential in milliseconds = " + seqTimeInMillis);
        println("Parallel in milliseconds = " + parallelTimeInMillis);
        println("parallelTimer.getResult() = " + parallelTimer.getResult());
        //assert
        assertTrue(parallelTimeInMillis < seqTimeInMillis);
        assertEquals(parallelTimer.getResult(), sequentialTimer.getResult());
    }

    @Nested
    class CollatzConjectureTests {

        @Test
        void testCollatzStream() {
            final var collatzNrs = Streams.collatzStream(BigInteger.valueOf(3))
                    .peek(It::println)
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
            final IntIntMap firstDigitToCounts = StatisticsUtils.groupedCounts(StatisticsUtils::firstDigit, LongStream.range(1, 500_000)
                    .flatMap(initNr -> Streams.collatzStream(initNr).takeWhile(nr -> nr != 1)));

            final var firstDigitPresenceInAscendingOrder = Sequence.of(firstDigitToCounts.keyValuesView())
                    .sorted(Comparator.comparingInt(IntIntPair::getTwo))
                    .mapToInt(IntIntPair::getOne)
                    .toArray();

            println("result = " + firstDigitToCounts);

            assertAll(
                    () -> assertArrayEquals(new int[]{9, 8, 7, 6, 5, 4, 3, 2, 1}, firstDigitPresenceInAscendingOrder),
                    () -> assertTrue(StatisticsUtils.obeysBenfordsLaw(firstDigitToCounts))
            );

        }

        @Test
        void testIntList() {
            final var list = IntStream.range(0, 10_000)
                    .collect(IntArrayList::new, IntArrayList::add, IntArrayList::addAll);

            assertEquals(10_000, list.size());
        }

        @ParameterizedTest
        @CsvSource({
                "123123, 1",
                "2353453, 2",
                "567567435, 5",
                "0, 0"})
        void testFirstDigit(int input, int expected) {
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
    void testBenfordsLawNotApplicableToLineairNrLine() {
        final var dataSet = LongStream.range(0, 100_000).toArray();
        final IntIntMap result = StatisticsUtils.groupedCounts(StatisticsUtils::firstDigit, Arrays.stream(dataSet));

        final var firstDigitPresenceInAscendingOrder = Sequence.of(result.keyValuesView())
                .sorted(Comparator.comparingInt(IntIntPair::getTwo))
                .mapToInt(IntIntPair::getOne)
                .toArray();

        println("result = " + result);

        assertAll(
                () -> assertArrayEquals(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, firstDigitPresenceInAscendingOrder),
                () -> assertFalse(StatisticsUtils.obeysBenfordsLaw(Arrays.stream(dataSet)))
        );
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
                .forEach(It::println);

        assertAll(
                () -> assertFalse(monthDayListMap.isEmpty()),
                () -> assertEquals(monthDayListMap1.size(), monthDayListMap.size())
        );
    }

    @Test
    void testStreamOnCloseMethod() {
        AtomicBoolean isClosedInTryWithResourcesBlock = new AtomicBoolean();
        AtomicBoolean isClosedOutsideTryWithResources = new AtomicBoolean();

        final var stream = TestSampleGenerator.createBookList().stream();
        final var expected = stream
                .map(Book::getTitle)
                .filter(containsAllOf("a", "e"))
                .distinct()
                .onClose(() -> setClosedAndPrintClosed("Stream", isClosedOutsideTryWithResources))
                .toList();

        try (final var bookStream = TestSampleGenerator.createBookList().stream()) {
            final var filteredBookTitles = bookStream
                    .map(Book::getTitle)
                    .filter(containsAllOf("a", "e"))
                    .distinct()
                    .onClose(() -> setClosedAndPrintClosed("Stream in try with resources block", isClosedInTryWithResourcesBlock))
                    .toList();

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

    private void setClosedAndPrintClosed(String message, AtomicBoolean closed) {
        closed.set(true);
        println(message + " is closed now");
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

        println(actual);
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

        println(actual);

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

    @Nested
    class FibonacciSequence {

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
            final var fibonacciList = fibonacciStream()
                    .skip(1)
                    .limit(500)
                    .reduce((first, second) -> second)
                    .orElseThrow();

            assertEquals(new BigInteger(
                    "139423224561697880139724382870407283950070256587697307264108962948325571622863290691557658876222521294125"
            ), fibonacciList);
        }

        @Test
        void testSumFibonacciNrUsingStreams() {
            final var fibonacciList = fibonacciStream()
                    .skip(1)
                    .limit(500)
                    .reduce(BigInteger.ZERO, BigInteger::add);

            assertEquals(new BigDecimal(
                    "365014740723634211012237077906479355996081581501455497852747829366800199361550174096573645929019489792750"
            ).toBigInteger(), fibonacciList);
        }

        @Test
        void testParallelFibonacciStream() {
            final var count = fibonacciStream()
                    .parallel()
                    .takeWhile(s -> s.toString().length() < 1_000)
                    .count();

            assertEquals(4782, count);
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
        void testFibonacciLongStream() {
            final var fibNrs = fibonacciLongStream()
                    .takeWhile(l -> l >= 0L)
                    .toArray();

            println(Arrays.toString(fibNrs));

            assertEquals(93, fibNrs.length);
        }

        @Nested
        class FibonacciAndGoldenRatioTests {

            private static final int SCALE = 5;

            @TestFactory
            Sequence<DynamicTest> testConsecutiveFibNrRatiosConvergeToGoldenRatio() {
                return Sequence.ofStream(fibonacciStream())
                        .skipWhile(n -> n.equals(BigInteger.ZERO))
                        .onEach(It::println)
                        .map(BigDecimal::new)
                        .zipWithNext((cur, next) -> next.divide(cur, SCALE, RoundingMode.HALF_UP))
                        .mapIndexed(this::fibRatioApproximatesGoldenRatio)
                        .skip(14)
                        .take(100);
            }

            @NotNull
            private DynamicTest fibRatioApproximatesGoldenRatio(int index, BigDecimal ratio) {
                final int ratioNr = index + 1;
                final var displayName = "Ratio " + ratioNr + ": " + ratio + " approximates golden ratio";
                final var expected = BigDecimal.valueOf(GOLDEN_RATIO).setScale(SCALE, RoundingMode.HALF_UP);
                return dynamicTest(displayName, () -> assertEquals(expected, ratio));
            }
        }

        @Test
        void testBenfordsLawFibonacciSequence() {
            final var stream = fibonacciStream()
                    .skip(1)
                    .limit(10_000)
                    .map(BigInteger::toString);

            final var result = StatisticsUtils.groupedCounts(s -> s.substring(0, 1), stream);

            final var firstDigitPresenceInAscendingOrder = Sequence.of(result.keyValuesView())
                    .sorted(Comparator.comparingInt(ObjectIntPair::getTwo))
                    .map(ObjectIntPair::getOne)
                    .mapToInt(Integer::parseInt)
                    .toArray();

            println("result = " + result);

            assertArrayEquals(new int[]{9, 8, 7, 6, 5, 4, 3, 2, 1}, firstDigitPresenceInAscendingOrder);
        }

        @Test
        void testBenfordsLawHoldsForFibonacciInHexadecimals() {
            final var longStream = fibonacciStream()
                    .skip(1)
                    .map(b -> b.toString(16))
                    .limit(2_000)
                    .mapToLong(s -> Long.parseLong(s.substring(0, 1), 16));

            final var result = StatisticsUtils.groupedCounts(value -> (int) value, longStream);

            final var firstDigitPresenceInAscendingOrder = Sequence.of(result.keyValuesView())
                    .sorted(Comparator.comparingInt(IntIntPair::getTwo))
                    .mapToInt(IntIntPair::getOne)
                    .toArray();

            println("result = " + result);

            assertArrayEquals(new int[]{15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1}, firstDigitPresenceInAscendingOrder);
        }

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
    void testIterableFromStream() {
        Iterable<String> strings = () -> Stream.of("Iterable", "from", "a", "stream").iterator();
        int counter = 0;
        for (String s : strings) {
            println("s = " + s);
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
                    .peek(It::println)
                    .mapToInt(Integer::intValue)
                    .toArray();

            println(Arrays.toString(array));

            assertArrayEquals(new int[]{1, 3, 5, 7, 9, 11, 13, 15, 17, 19}, array);
        }

        @Test
        void testZipWithNextWithOnlyOneThrowsException() {
            final var integers = List.of(0);

            final var integerStream = Streams.zipWithNext(integers, Integer::sum);
            assertThrows(NoSuchElementException.class, () -> integerStream.forEach(It::println));
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

            println("windows = " + windows);

            assertEquals(20, windows.size());
        }

        @Test
        void testWindowedNoPartialWindows() {
            final var range = IntStream.range(0, 2_000);

            final var windows = Streams
                    .windowed(range::iterator, 10, false)
                    .toList();

            println("windows = " + windows);

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

            println("grouping = " + grouping);

            assertEquals(7, grouping.size());
        }

        @Test
        void testToConcurrentMap() {
            final var fibonacciNrs = subtractingFibonacciStream()
                    .peek(It::println)
                    .limit(1_000)
                    .toList();

            final var map = fibonacciNrs.parallelStream()
                    .distinct()
                    .collect(Collectors.toConcurrentMap(s -> s, s -> s.isProbablePrime(100)));

            assertEquals(1_000, map.size());
        }
    }

    @Test
    void testStreamFromIndexedDataStructure() {
        final var indexedDataStructure = new IndexedDataStructure<>("This", "is", "a", "test");

        final var strings = IntStream.range(0, indexedDataStructure.size())
                .mapToObj(indexedDataStructure::get)
                .toList();

        assertEquals(List.of("This", "is", "a", "test"), strings);
    }

    @Test
    void testStreamFromIndexedDataStructureWithMapMulti() {
        final var indexedDataStructure = new IndexedDataStructure<>("This", "is", "a", "second", "test");

        final int[] ints = Stream.of(indexedDataStructure)
                .mapMulti(this::acceptEachString)
                .mapToInt(String::length)
                .toArray();

        assertArrayEquals(new int[]{4, 2, 1, 6, 4}, ints);
    }

    private void acceptEachString(IndexedDataStructure<String> stringIndexedDataStructure, Consumer<String> stringConsumer) {
        for (int i = 0; i < stringIndexedDataStructure.size(); i++) {
            stringConsumer.accept(stringIndexedDataStructure.get(i));
        }
    }

    @Test
    void testStreamFromNonCollectionDataStructureWithMapMulti() {
        final var dataStructure = new TestDataStructure<>("This", "is", "a", "third", "test");

        final int[] ints = Stream.of(dataStructure)
                .<String>mapMulti(TestDataStructure::forEach)
                .mapToInt(String::length)
                .toArray();

        assertArrayEquals(new int[]{4, 2, 1, 5, 4}, ints);
    }

    @Test
    void givenDataInSystemIn_whenCallingLinesMethod_thenHaveUserInputData() {
        final var stopKeyWord = "bye";
        final String[] inputLines = {
                "The first line.",
                "The second line.",
                "The last line.",
                stopKeyWord,
                "anything after 'bye' will be ignored"
        };
        final var expectedLines = Arrays.copyOf(inputLines, inputLines.length - 2);
        final var expected = Arrays.stream(expectedLines).toList();

        final InputStream stdin = System.in;
        try {
            System.setIn(new ByteArrayInputStream(String.join("\n", inputLines).getBytes()));
            final var actual = Streams.consoleLines(stopKeyWord).toList();
            assertEquals(expected, actual);
        } finally {
            System.setIn(stdin);
        }
    }

    @Test
    void givenDataInSystemIn_whenCallingLinesMethodWithStopPattern_thenHaveUserInputData() {
        final String[] inputLines = {
                "The first line.",
                "The second line.",
                "The last line.",
                "234234",
                "anything after a number will be ignored"
        };
        final var expectedLines = Arrays.copyOf(inputLines, inputLines.length - 2);
        final var expected = Arrays.stream(expectedLines).toList();


        final InputStream stdin = System.in;
        try {
            System.setIn(new ByteArrayInputStream(String.join("\n", inputLines).getBytes()));
            final var oneOrMoreDigits = Pattern.compile("\\d+");
            final var actual = Streams.consoleLines(oneOrMoreDigits).toList();
            assertEquals(expected, actual);
        } finally {
            System.setIn(stdin);
        }
    }

    @Test
    void testBufferedReaderLines() {
        final String[] inputLines = {
                "The first line.",
                "The second line.",
                "The last line."
        };
        final var expected = Arrays.stream(inputLines).toList();
        final var inputStream = new ByteArrayInputStream(String.join("\n", inputLines).getBytes());
        final var actual = new BufferedReader(new InputStreamReader(inputStream)).lines().toList();
        assertEquals(expected, actual);
    }

    @Nested
    class StreamReduceWrong {

        @Test
        void testStreamReduceSubtraction() {
            final var reduce = IntStream.range(0, 100_000)
                    .reduce(100, (i1, i2) -> i1 - i2);

            final var reduceParallel = IntStream.range(0, 100_000)
                    .parallel()
                    .reduce(100, (i1, i2) -> i1 - i2);

            println("reduce = " + reduce);
            println("reduceParallel = " + reduceParallel);

            assertNotEquals(reduce, reduceParallel);
        }

        @Test
        void testStreamReduceStringBuilder() {
            final var reduce = reduceWithStringBuilder(IntStream.range(0, 100)
                    .mapToObj(Objects::toString));

            final var parallelStream = IntStream.range(0, 100)
                    .mapToObj(Objects::toString)
                    .parallel();

            println("reduce = " + reduce);

            assertAll(
                    () -> assertThrows(IndexOutOfBoundsException.class, () -> reduceWithStringBuilder(parallelStream)),
                    () -> assertEquals(190, reduce.length())
            );
        }

        private StringBuilder reduceWithStringBuilder(Stream<String> stream) {
            return stream.reduce(new StringBuilder(), StringBuilder::append, StringBuilder::append);
        }
    }

}
