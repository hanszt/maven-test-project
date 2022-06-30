package org.hzt;

import org.hzt.model.Payment;
import org.hzt.model.Person;
import org.hzt.sequences.primitve_sequences.IntSequence;
import org.hzt.sequences.primitve_sequences.PrimitiveIterable;
import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Book;
import org.hzt.test.model.Museum;
import org.hzt.test.model.Painter;
import org.hzt.test.model.Painting;
import org.hzt.utils.collections.MutableSetX;
import org.hzt.utils.collections.SetX;
import org.hzt.utils.collectors.CollectorsX;
import org.hzt.utils.sequences.Sequence;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Map.Entry.comparingByKey;
import static java.util.function.Predicate.isEqual;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toUnmodifiableSet;
import static org.hzt.stream.StreamUtils.by;
import static org.hzt.stream.StreamUtils.function;
import static org.hzt.utils.collectors.CollectorsX.flatMappingToList;
import static org.hzt.utils.collectors.CollectorsX.mappingToSet;
import static org.hzt.utils.collectors.CollectorsX.multiMappingToList;
import static org.hzt.utils.function.predicates.ComparingPredicates.greaterThan;
import static org.hzt.utils.function.predicates.StringPredicates.containsAllOf;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StreamsSampleTest {

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

        map.forEach(StreamsSampleTest::printKeyAndValue);
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
        List<Integer> list  = getIntegerStream(100).collect(Collectors.toList());
        List<Integer> list2  = getIntegerStream(100).collect(Collectors.toList());
        Collections.shuffle(list);
        Collections.shuffle(list2);

        list.sort(Comparator.naturalOrder());

        final var expectedLimitedList = list.subList(0, 10);

        final var limitedList = list2.stream()
                .sorted()
                .limit(10)
                .collect(Collectors.toUnmodifiableList());

        assertEquals(expectedLimitedList, limitedList);
    }

    @Test
    void testReturnStreamFromIterator() {
        final var iterator = getIntegerStream(100).iterator();
        final var expected = getIntegerStream(100)
                .map(String::valueOf)
                .collect(Collectors.toUnmodifiableList());

        final var result = StreamsSample.returnStreamFromIterator(iterator)
                .map(String::valueOf)
                .collect(Collectors.toUnmodifiableList());

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
                .collect(Collectors.toUnmodifiableList());

        final var fibonacciList = StreamsSample.getFibonacci(10);
        assertEquals(expected, fibonacciList);
    }

    @Test
    void testNthFibonacciNrUsingStreams() {
        final var fibonacciList = StreamsSample.getNthFibonacciNumber(500);
        assertEquals(new BigDecimal(
                "139423224561697880139724382870407283950070256587697307264108962948325571622863290691557658876222521294125"
        ).toBigInteger(), fibonacciList);
    }

    @Test
    void testSumFibonacciNrUsingStreams() {
        final var fibonacciList = StreamsSample.getSumFibonacciNumbers(500);
        assertEquals(new BigDecimal(
                "365014740723634211012237077906479355996081581501455497852747829366800199361550174096573645929019489792750"
        ).toBigInteger(), fibonacciList);
    }

    @Test
    void testCalculatePiParallelUsingStreams() {
        Timer<BigDecimal> timer = Timer.timeAFunction(10_000_000, StreamsSample::calculatePi);
        final var result = timer.getResult();
        System.out.println("Milliseconds = " + timer.getTimeInMillis());
        assertEquals(new BigDecimal("3.1415927972"), result);
    }

    @Test
    void testCalculatePiParallelUsingDoubleStream() {
        final var iterations = 10_000_000;
        //act
        Timer<Double> sequentialTimer = Timer.timeAFunction(iterations,
                nrOfIterations -> StreamsSample.calculatePiAsDouble(nrOfIterations, false));
        Timer<Double> parallelTimer = Timer.timeAFunction(iterations,
                nrOfIterations -> StreamsSample.calculatePiAsDouble(nrOfIterations, true));

        final var seqTimeInMillis = sequentialTimer.getTimeInMillis();
        final var parallelTimeInMillis = parallelTimer.getTimeInMillis();
        System.out.println("Sequential in milliseconds = " + seqTimeInMillis);
        System.out.println("Parallel in milliseconds = " + parallelTimeInMillis);
        System.out.println("parallelTimer.getResult() = " + parallelTimer.getResult());
        //assert
        assertTrue(parallelTimeInMillis < seqTimeInMillis);
        assertEquals(parallelTimer.getResult(), sequentialTimer.getResult());
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
                .collect(Collectors.toUnmodifiableList());
    }

    private void setClosedAndPrintClosed(String message, AtomicBoolean closed) {
        closed.set(true);
        System.out.println(message + " is closed now");
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

        assertArrayEquals(new int[] {2, 4, 6, 8, 10}, actual);
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
    void testIterableFromStream() {
        Iterable<String> strings = () -> Stream.of("Iterable", "from", "a", "stream").iterator();
        int counter = 0;
        for (String s : strings) {
            System.out.println("s = " + s);
            counter++;
        }
        assertEquals(4, counter);
    }

}
