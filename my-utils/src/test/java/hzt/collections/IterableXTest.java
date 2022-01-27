package hzt.collections;

import hzt.function.It;
import hzt.stream.collectors.BigDecimalCollectors;
import hzt.strings.StringX;
import one.util.streamex.StreamEx;
import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.BankAccount;
import org.hzt.test.model.Book;
import org.hzt.test.model.Customer;
import org.hzt.test.model.Museum;
import org.hzt.test.model.Painter;
import org.hzt.test.model.Painting;
import org.junit.jupiter.api.Test;
import test.Generator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.Year;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static hzt.stream.collectors.CollectorsX.branching;
import static hzt.stream.collectors.CollectorsX.intersectingBy;
import static hzt.stream.collectors.CollectorsX.mappingToList;
import static hzt.stream.collectors.CollectorsX.toListX;
import static hzt.stream.collectors.CollectorsX.toMapX;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.*;
import static org.junit.jupiter.api.Assertions.*;

class IterableXTest {

    @Test
    void testMappingToSet() {
        final var museumList = TestSampleGenerator.getMuseumListContainingNulls();

        final var expected = museumList.stream()
                .map(Museum::getDateOfOpening)
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableSet());

        final var actual = ListX.of(museumList).toSetXOf(Museum::getDateOfOpening);

        assertEquals(expected, actual);
    }

    @Test
    void testFlatMapToSet() {
        var museumList = MutableListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final var expected = museumList.stream()
                .map(Museum::getPaintings)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .collect(toUnmodifiableSet());

        final var actual = museumList.flatMapToSetXOf(Museum::getPaintings);

        final var streamX = StreamEx.of(museumList).flatMap(museum -> museum.getPaintings().stream()).toSet();

        assertAll(
                () -> assertEquals(expected, actual),
                () -> assertEquals(expected, streamX)
        );
    }

    @Test
    void testToNonNullKeyMap() {
        final var museumList = ListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final var expectedMap = museumList.stream()
                .filter(m -> m.getName() != null)
                .collect(toUnmodifiableMap(Museum::getName, Museum::getPaintings));

        final var actualMap = museumList.toMapX(Museum::getName, Museum::getPaintings);

        assertEquals(expectedMap, actualMap);
    }

    @Test
    void testToNonNullKeyMapWithValues() {
        final var museumList = ListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final var expectedMap = museumList.stream()
                .collect(toUnmodifiableMap(It::self, Museum::getMostPopularPainting));

        final var actualMap = museumList.associateWith(Museum::getMostPopularPainting);

        assertEquals(expectedMap, actualMap);
    }

    @Test
    void testWithIndicesZipWithNext2() {
        final var museums = ListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final var sumsOfThree = museums
                .flatMap(Museum::getPaintings)
                .indices()
                .zipWithNext2(IntStream::of)
                .toListXOf(IntStream::sum);

        System.out.println("sumsOfThree = " + sumsOfThree);

        assertEquals(List.of(3, 6, 9, 12, 15, 18, 21), sumsOfThree);
    }

    @Test
    void testMapIndexed() {
        final var museums = ListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final var sumsOfThree = museums
                .mapIndexed((index, value) -> index)
                .zipWithNext2(IntStream::of)
                .toListXOf(IntStream::sum);

        System.out.println("sumsOfThree = " + sumsOfThree);

        assertEquals(List.of(3, 6), sumsOfThree);
    }

    @Test
    void testSortedIfOfComparableType() {
        var list = MutableListX.of(1, 3, 5, 4, 2, 7, 214, 5, 8, 3, 4, 123);
        final var sorted = list.sorted();
        assertIterableEquals(ListX.of(1, 2, 3, 3, 4, 4, 5, 5, 7, 8, 123, 214), sorted);
    }

    @Test
    void testSortedThrowsExceptionWhenNotOfComparableType() {
        var bankAccountList = ListX.of(TestSampleGenerator.createSampleBankAccountList());
        assertThrows(IllegalStateException.class, bankAccountList::sorted);
    }

    @Test
    void testMaxOf() {
        var bankAccountList = ListX.of(TestSampleGenerator.createSampleBankAccountList());

        final var expected = bankAccountList.stream()
                .max(Comparator.comparing(BankAccount::getBalance))
                .map(BankAccount::getBalance)
                .orElseThrow();

        final var actual = bankAccountList.maxOf(BankAccount::getBalance);

        System.out.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testMinOf() {
        var list = ListX.of(TestSampleGenerator.createSampleBankAccountList());

        final var expected = list.stream()
                .min(Comparator.comparing(BankAccount::getBalance))
                .map(BankAccount::getBalance)
                .orElseThrow();

        final var actual = list.minOf(BankAccount::getBalance);

        System.out.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testAuctionImplementingIterableX() {
        var auctions = Generator.createAuctions();

        final var auction = auctions.first();

        final var expected = auction.getPaintings().stream()
                .filter(Predicate.not(Painting::isInMuseum))
                .findFirst()
                .orElseThrow();

        final var firstPaintingNotInMuseum = auction.firstNot(Painting::isInMuseum);

        assertEquals(firstPaintingNotInMuseum, expected);
    }

    @Test
    void testMinBy() {
        var bankAccounts = TestSampleGenerator.createSampleBankAccountList().stream()
                .collect(Collectors.toUnmodifiableSet());

        final var expected = bankAccounts.stream().min(Comparator.comparing(BankAccount::getBalance));

        final var actual = ListX.of(bankAccounts).minBy(BankAccount::getBalance);

        assertEquals(expected, actual);
    }

    @Test
    void testMaxBy() {
        var set = ListX.of(TestSampleGenerator.createSampleBankAccountList().stream()
                .collect(Collectors.toUnmodifiableSet()));

        final var expected = set.stream().max(Comparator.comparing(BankAccount::getBalance));

        final var actual = set.maxBy(BankAccount::getBalance);

        assertEquals(expected, actual);
    }

    @Test
    void testGrouping() {
        final var paintings = ListX.of(TestSampleGenerator.createPaintingList());

        final var expectedMap = paintings.stream().collect(groupingBy(Painting::painter));

        final var actualMap = paintings.groupBy(Painting::painter);

        assertEquals(expectedMap, actualMap);
    }

    @Test
    void testGroupMapping() {
        final var paintings = TestSampleGenerator.createPaintingList();

        final var expectedMap = paintings.stream()
                .collect(groupingBy(Painting::painter, Collectors.mapping(Painting::getYearOfCreation, toList())));

        final var actualMap = ListX.of(paintings)
                .groupMapping(Painting::painter, Painting::getYearOfCreation);

        assertEquals(expectedMap, actualMap);
    }

    @Test
    void testPartitionMapping() {
        final var paintings = TestSampleGenerator.createPaintingList();

        final var expectedMap = paintings.stream()
                .collect(partitioningBy(Painting::isInMuseum,
                        mapping(Painting::age, toList())));

        final var actualMap = ListX.of(paintings)
                .partitionMapping(Painting::isInMuseum, Painting::age);

        assertAll(
                () -> assertEquals(expectedMap.get(true), actualMap.first()),
                () -> assertEquals(expectedMap.get(false), actualMap.second())
        );
    }

    @Test
    void testPartitioning() {
        final var paintings = ListX.of(TestSampleGenerator.createPaintingList());

        final var expectedMap = paintings.stream()
                .collect(partitioningBy(Painting::isInMuseum));

        final var actualMap = paintings.partition(Painting::isInMuseum);

        assertAll(
                () -> assertEquals(expectedMap.get(true), actualMap.first()),
                () -> assertEquals(expectedMap.get(false), actualMap.second())
        );
    }

    @Test
    void testToSummaryStatistics() {
        final var paintings = ListX.of(TestSampleGenerator.createPaintingList());

        final var expected = paintings.stream().mapToInt(Painting::ageInYears).summaryStatistics();

        final var actual = paintings.statsOf(Painting::ageInYears);

        assertAll(
                () -> assertEquals(expected.getMin(), actual.getMin()),
                () -> assertEquals(expected.getMax(), actual.getMax()),
                () -> assertEquals(expected.getAverage(), actual.getAverage()),
                () -> assertEquals(expected.getSum(), actual.getSum()),
                () -> assertEquals(expected.getCount(), actual.getCount())
        );
    }

    @Test
    void testToBigDecimalSummaryStatistics() {
        final var bankAccounts = ListX.of(TestSampleGenerator.createSampleBankAccountListContainingNulls());

        final var expected = bankAccounts.stream()
                .map(BankAccount::getBalance)
                .collect(BigDecimalCollectors.summarizingBigDecimal());

        final var actual = bankAccounts.statsOf(BankAccount::getBalance);

        assertAll(
                () -> assertEquals(expected.getMin(), actual.getMin()),
                () -> assertEquals(expected.getMax(), actual.getMax()),
                () -> assertEquals(expected.getAverage(), actual.getAverage()),
                () -> assertEquals(expected.getSum(), actual.getSum()),
                () -> assertEquals(expected.getCount(), actual.getCount())
        );
    }

    @Test
    void testFlatMapFiltersNullsFilterAndMapToList() {
        final var museumList = ListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final var expectedPainters = museumList.stream()
                .flatMap(museum -> museum.getPaintings().stream())
                .filter(Objects::nonNull)
                .filter(Painting::isInMuseum)
                .map(Painting::painter)
                .toList();

        final var actualPainters = museumList
                .flatMap(Museum::getPaintings)
                .filter(Painting::isInMuseum)
                .toListXOf(Painting::painter);

        assertIterableEquals(actualPainters, expectedPainters);
    }

    @Test
    void testFilterAndMapToCollection() {
        final var museumList = ListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final Deque<LocalDate> expectedLocalDates = museumList.stream()
                .filter(museum -> museum.getPaintings().size() > 3)
                .map(Museum::getDateOfOpening)
                .collect(toCollection(ArrayDeque::new));

        final Deque<LocalDate> actualLocalDates = museumList
                .filterBy(Museum::getPaintings, paintings -> paintings.size() > 3)
                .mapTo(ArrayDeque::new, Museum::getDateOfOpening);

        assertIterableEquals(expectedLocalDates, actualLocalDates);
    }

    @Test
    void testTakeWhile() {
        final var museumList = ListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final var expected = museumList.stream()
                .takeWhile(museum -> museum.getPaintings().size() < 3).toList();

        final var actual = museumList.takeWhile(museum -> museum.getPaintings().size() < 3);

        System.out.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testTakeWhileInclusive() {
        var list = ListX.of(1, 2, 10, 4, 5, 10, 6, 5, 3, 5, 6);

        list.forEach(System.out::println);

        final var takeWhileInclusive = list.takeWhileInclusive(i -> i != 5);
        final var takeWhile = list.takeWhile(i -> i != 5);

        System.out.println("integers = " + takeWhileInclusive);

        assertAll(
                () -> assertIterableEquals(List.of(1, 2, 10, 4, 5), takeWhileInclusive),
                () -> assertIterableEquals(List.of(1, 2, 10, 4), takeWhile)
        );
    }

    @Test
    void testSkip() {
        final var museumList = ListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final var expected = museumList.stream().skip(3).toList();

        final var actual = museumList.skip(3);

        System.out.println("actual = " + actual);

        assertIterableEquals(expected, actual);
    }

    @Test
    void testLimit() {
        final var museumList = ListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final var expected = museumList.stream().limit(3).toList();

        final var actual = museumList.limit(3);

        System.out.println("actual = " + actual);

        assertIterableEquals(expected, actual);
    }

    @Test
    void testTakeLastWhile() {
        final var museumList = ListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final var strings = """
                De sterrennacht, Het melkmeisje, Les Demoiselles d'Avignon,
                Meisje met de parel, Le Rêve, Meisje met de rode hoed, Guernica
                """.stripIndent()
                .split(",");

        final var expected = ListX.of(strings).toSetXOf(String::trim);

        final var actual = museumList
                .flatMap(Museum::getPaintings)
                .takeLastWhile(Painting::isInMuseum)
                .toSetXOf(Painting::name);

        System.out.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testFromIntArrayToMappedList() {
        final int[] numbers = {1, 4, 3, 6, 7, 4, 3, 234};

        final var expected = IntStream.of(numbers).mapToObj(BigDecimal::valueOf).toList();

        final var actual = ListX.ofInts(numbers).toListOf(BigDecimal::valueOf);

        assertIterableEquals(expected, actual);
    }

    @Test
    void testToArray() {
        final var paintings = MutableListX.of(TestSampleGenerator.createPaintingList());

        final var expected = paintings.stream().map(Painting::getYearOfCreation).toArray(Year[]::new);

        final var actual = paintings.toArrayOf(Painting::getYearOfCreation, Year[]::new);

        assertArrayEquals(expected, actual);
    }

    @Test
    void testIntersect() {
        final var collections = ListX.of(
                List.of(1, 2, 3, 4, 5, 7),
                Set.of(2, 4, 5),
                Set.of(4, 5, 6)
        );

        final var intersect = collections.intersectionOf(It::self);

        assertEquals(Set.of(4, 5), intersect);
    }

    @Test
    void testIntersectMuseumPaintings() {
        final var museums = ListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        var expected = museums.stream()
                .map(Museum::getPaintings)
                .collect(intersectingBy(Painting::getMilleniumOfCreation));

        final var intersection = museums.intersectionOf(Museum::getPaintings, Painting::getMilleniumOfCreation);

        System.out.println("intersection = " + intersection);

        assertEquals(expected, intersection);
    }

    @Test
    void testSumBigDecimals() {
        var list = ListX.of(TestSampleGenerator.createSampleBankAccountList());

        final var expected = list.stream()
                .map(BankAccount::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        final var actual = list.sumOf(BankAccount::getBalance);

        assertEquals(expected, actual);
    }

    @Test
    void testFold() {
        final var integers = ListX.of(1, 3, 3, 4, 5, 2, 6).toSetX();

        final var expectedSum = integers.stream()
                .map(BigDecimal::valueOf)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        final var sum = integers
                .fold(BigDecimal.ZERO, (bigDecimal, integer) -> bigDecimal.add(BigDecimal.valueOf(integer)));

        System.out.println("sum = " + sum);

        assertEquals(expectedSum, sum);
    }

    @Test
    void testFoldRight() {
        var list = ListX.of(TestSampleGenerator.createSampleBankAccountList());

        list.forEach(System.out::println);

        final var bigDecimal = list.foldRight(BigDecimal.ZERO, (b, a) -> a.add(b.getBalance()));

        assertEquals(BigDecimal.valueOf(232511.34), bigDecimal);
    }

    @Test
    void testDropLastWhile() {
        var list = ListX.of(1, 2, 10, 4, 5, 10, 6, 5, 3, 5, 6);

        list.forEach(System.out::println);

        final var integers = list.dropLastToMutableListWhile(i -> i != 10);

        System.out.println("integers = " + integers);

        assertEquals(List.of(1, 2, 10, 4, 5, 10), integers);
    }

    @Test
    void testDropWhile() {
        var list = ListX.of(1, 2, 10, 4, 5, 10, 6, 5, 3, 5, 6);

        list.forEach(System.out::println);

        final var integers = list.dropWhile(i -> i != 5);

        System.out.println("integers = " + integers);

        assertIterableEquals(List.of(5, 10, 6, 5, 3, 5, 6), integers);
    }

    @Test
    void testListIteratorGetPreviousOnlyWorksBeforeWhenIsAtEnd() {
        var list = List.of(22, 44, 88, 11, 33);
        var listIterator = list.listIterator();

        assertFalse(listIterator.hasPrevious());

        System.out.println("In actual order :");
        while (listIterator.hasNext()) {
            System.out.println(listIterator.next());
        }

        assertTrue(listIterator.hasPrevious());

        System.out.println("In reverse order :");
        while (listIterator.hasPrevious()) {
            System.out.println(listIterator.previous());
        }
    }

    @Test
    void testCountBy() {
        var bankAccounts = ListX.of(TestSampleGenerator.createSampleBankAccountList());

        final var expected = bankAccounts.stream().filter(BankAccount::isDutchAccount).count();

        final var actual = bankAccounts.countNotNullBy(BankAccount::isDutchAccount);

        System.out.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testSumOf() {
        var list = ListX.of(TestSampleGenerator.createSampleBankAccountList());

        final var expected = list.stream()
                .map(BankAccount::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        final BigDecimal actual = list.sumOf(BankAccount::getBalance);

        System.out.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testSumOfInt() {
        var list = ListX.of(TestSampleGenerator.createPaintingList());

        final var expected = list.stream().mapToInt(Painting::ageInYears).sum();

        final var actual = list.sumOfInts(Painting::ageInYears);

        System.out.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testAverageOf() {
        var list = ListX.of(TestSampleGenerator.createPaintingList());

        final var expected = list.stream().mapToInt(Painting::ageInYears).average().orElseThrow();

        final var actual = list.averageOfInts(Painting::ageInYears);

        System.out.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testIterXOfIterable() {
        var listX = ListX.of(TestSampleGenerator.createSampleBankAccountList());

        final var expected = listX.stream()
                .map(BankAccount::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        final BigDecimal actual = ListX.of(listX).sumOf(BankAccount::getBalance);

        System.out.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testLargeTransform() {
        final var bigDecimals = IterableX.range(0, 100_000)
                .filter(integer -> integer % 2 == 0)
                .toDescendingSortedMutableListOf(BigDecimal::valueOf);

        assertEquals(50_000, bigDecimals.size());
    }

    @Test
    void testRange() {
        assertArrayEquals(
                IntStream.range(5, 10).toArray(),
                IterableX.range(5, 10).toIntArrayOf(Integer::intValue));
    }

    @Test
    void testRangeClosed() {
        assertArrayEquals(
                IntStream.rangeClosed(5, 10).toArray(),
                IterableX.rangeClosed(5, 10).toIntArrayOf(Integer::intValue));
    }

    @Test
    void testTransformForEach() {
        final var ints = IntStream.range(0, 100_000).toArray();

        ListX.ofInts(ints)
                .filter(integer -> integer % 2 == 0)
                .onEach(this::printEvery10_000stElement)
                .forEach(i -> assertEquals(0, i % 2));
    }

    @Test
    void testFirst() {
        final var paintings = ListX.of(TestSampleGenerator.createPaintingList());

        final var expected = paintings.first();

        final var actual = paintings.stream()
                .findFirst()
                .orElseThrow();

        assertEquals(expected, actual);
    }

    @Test
    void testLast() {
        final var paintings = ListX.of(TestSampleGenerator.createPaintingList());

        final var expected = paintings.last();

        final var actual = paintings.get(paintings.size() - 1);

        assertEquals(expected, actual);
    }

    @Test
    void testFindLast() {
        final var paintings = ListX.of(TestSampleGenerator.createPaintingList());

        final var actual = paintings.findLast(not(Painting::isInMuseum));

        System.out.println("actual = " + actual);

        assertEquals("Lentetuin, de pastorietuin te Nuenen in het voorjaar", actual
                .map(Painting::name)
                .orElseThrow());
    }

    @Test
    void testAny() {
        final var paintings = ListX.of(TestSampleGenerator.createPaintingList());

        final var expected = paintings.stream().anyMatch(Painting::isInMuseum);

        final var actual = paintings.any(Painting::isInMuseum);

        assertEquals(expected, actual);
    }

    @Test
    void testJoinToString() {
        final var paintings = ListX.of(TestSampleGenerator.createPaintingList());

        final var expected = paintings.stream()
                .map(Painting::age)
                .map(Period::toString)
                .collect(Collectors.joining(", "));

        final var actual = paintings.joinToStringBy(Painting::age, ", ");

        System.out.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testToString() {
        final var paintings = ListX.of(TestSampleGenerator.createPaintingList());

        final var expected = "ArrayListX{elements=[" +
                "Painting[name=Lentetuin, de pastorietuin te Nuenen in het voorjaar, " +
                "painter=Painter{firstName='Vincent', lastname='van Gogh', dateOfBirth=1853-03-20}, " +
                "yearOfCreation=1884, isInMuseum=false]]}";

        final var actual = paintings.filterNot(Painting::isInMuseum);

        assertEquals(expected, actual.toString());
    }

    @Test
    void testSkippingToSet() {
        final var bookList = ListX.of(TestSampleGenerator.createBookList());

        final var expected = bookList.stream()
                .filter(not(Book::isAboutProgramming))
                .collect(Collectors.toUnmodifiableSet());

        final var actual = bookList.toSetXSkipping(Book::isAboutProgramming);

        System.out.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testCountingByNullableValue() {
        final var bookList = ListX.of(TestSampleGenerator.createBookList());

        final var expected = bookList.stream()
                .map(Book::getCategory)
                .filter(Objects::nonNull)
                .count();

        final var actual = bookList.countNotNullOf(Book::getCategory);

        System.out.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testAll() {
        final var paintings = ListX.of(TestSampleGenerator.createPaintingList());

        final var expected = paintings.stream().allMatch(Painting::isInMuseum);

        final var actual = paintings.all(Painting::isInMuseum);

        assertEquals(expected, actual);
    }

    @Test
    void testNone() {
        final var paintings = ListX.of(TestSampleGenerator.createPaintingList());

        final var expected = paintings.stream().noneMatch(Painting::isInMuseum);

        final var actual = paintings.none(Painting::isInMuseum);

        assertEquals(expected, actual);
    }

    @Test
    void testTeeing() {
        var array = TestSampleGenerator.createSampleBankAccountList().toArray(BankAccount[]::new);

        final var pair = ListX.of(array)
                .teeing(toList(), reducing(BigDecimal.ZERO, BankAccount::getBalance, BigDecimal::add));

        final var expectedTotal = ListX.of(array).fold(BigDecimal.ZERO, this::addAccountBalance);

        assertAll(
                () -> assertEquals(Arrays.stream(array).toList(), pair.first()),
                () -> assertEquals(expectedTotal, pair.second())
        );
    }

    private BigDecimal addAccountBalance(BigDecimal balance, BankAccount account) {
        return balance.add(account.getBalance());
    }

    @Test
    void testCollectingToIterX() {
        var paintingList = ListX.of(TestSampleGenerator.createPaintingList());

        final var expected = paintingList.stream()
                .map(Painting::painter)
                .map(Painter::getDateOfBirth)
                .max(Comparator.naturalOrder())
                .orElseThrow();

        final var max = paintingList.stream()
                .map(Painting::painter)
                .collect(toListX())
                .maxOf(Painter::getDateOfBirth);

        final var maxWithFullTransformer = paintingList
                .mapNotNull(Painting::painter)
                .maxOf(Painter::getDateOfBirth);

        assertAll(
                () -> assertEquals(expected, max),
                () -> assertEquals(expected, maxWithFullTransformer)
        );
    }

    @Test
    void testCollectingToMapX() {
        var paintingList = TestSampleGenerator.createPaintingList();

        final var expected = paintingList.stream()
                .map(Painting::painter)
                .collect(Collectors.toMap(Painter::getDateOfBirth, Painter::getLastname, (a, b) -> a));

        final var expectedLocalDates = expected.keySet().stream()
                .flatMap(date -> date.datesUntil(LocalDate.of(2000, Month.JANUARY, 1)))
                .collect(toSet());

        final MapX<LocalDate, String> actual = ListX.of(paintingList)
                .map(Painting::painter)
                .collect(toMapX(Painter::getDateOfBirth, Painter::getLastname, (a, b) -> a));

        final var localDates = actual.flatMapKeysToMutableSetOf(date ->
                date.datesUntil(LocalDate.of(2000, Month.JANUARY, 1)).toList());

        System.out.println("localDates.size() = " + localDates.size());

        assertAll(
                () -> assertEquals(expected, actual),
                () -> assertEquals(expectedLocalDates, localDates)
        );
    }

    @Test
    void testMapNotNull() {
        var bankAccounts = MutableListX
                .of(TestSampleGenerator.createSampleBankAccountListContainingNulls());

        final var expected = bankAccounts.stream()
                .map(BankAccount::getCustomer)
                .filter(Objects::nonNull)
                .map(Customer::getId)
                .filter(Objects::nonNull)
                .max(Comparator.naturalOrder())
                .orElseThrow();

        final var streamXResult = StreamEx.of(bankAccounts)
                .map(BankAccount::getCustomer)
                .nonNull()
                .maxBy(Customer::getId)
                .map(Customer::getId)
                .orElseThrow();

        final var actual = bankAccounts
                .mapNotNull(BankAccount::getCustomer)
                .maxOf(Customer::getId);

        assertAll(
                () -> assertEquals(expected, actual),
                () -> assertEquals(expected, streamXResult)
        );
    }

    @Test
    void testZipWithNext() {
        var museumList = ListX.of(TestSampleGenerator.createPaintingList());

        final var integers = museumList
                .mapNotNull(Painting::name)
                .zipWithNextToListOf(String::compareTo);

        assertEquals(List.of(-5, 83, -1, 5, -5, 1, 8), integers);
    }

    @Test
    void testZipTwoCollections() {
        var values = ListX.of(0, 1, 2, 3, 4, 5, 6, 7);
        var others = List.of(6, 5, 4, 3, 2, 1, 0);

        final var integers = values.zipToListWith(others, Integer::compareTo);

        assertIterableEquals(List.of(-1, -1, -1, 0, 1, 1, 1), integers);
    }

    @Test
    void testToUnion() {
        var values = ListX.of(0, 1, 2, 1, 3, 4, 5, 6, 7);
        var other = List.of(6, 5, 4, 3, 2, 1, 0);

        final var union = values.union(other, String::valueOf);

        assertEquals(Set.of("0", "1", "2", "3", "4", "5", "6", "7"), union);
    }

    @Test
    void mapToStringX() {
        final var bookList = ListX.of(TestSampleGenerator.createBookList());

        final var expected = bookList.stream()
                .map(Book::getCategory)
                .flatMapToInt(String::chars)
                .mapToObj(c -> (char) c)
                .toList();


        final var actual = bookList
                .mapToStringX(Book::getCategory)
                .flatMapToMutableListOf(StringX::toMutableList);

        System.out.println("stringXES = " + actual);

        assertEquals(expected, actual);
    }

    private void printEvery10_000stElement(int i) {
        if (i % 10_000 == 0) {
            System.out.println(i);
        }
    }

    @Test
    void testLargeStream() {
        final var bigDecimals = IntStream.range(0, 100_000)
                .filter(integer -> integer % 2 == 0)
                .mapToObj(BigDecimal::valueOf)
                .toList();

        assertEquals(50_000, bigDecimals.size());
    }

    @Test
    void testDistinctBy() {
        final var bigDecimals = ListX.of(IntStream.range(0, 100_000)
                .filter(integer -> integer % 2 == 0)
                .mapToObj(BigDecimal::valueOf)
                .toList());

        final var expected = IntStream.rangeClosed(0, 254)
                .filter(integer -> integer % 2 == 0)
                .mapToObj(BigDecimal::valueOf)
                .toList();

        final var list = bigDecimals.distinctBy(BigDecimal::byteValue);

        System.out.println("list = " + list);

        assertEquals(expected, list);
    }

    @Test
    void castIfInstanceOf() {
        final var list = ListX.of(3.0, 2, 4, 3, BigDecimal.valueOf(10), 5L, 'a', "String");

        final var integers = list.castIfInstanceOf(Integer.class);

        System.out.println("integers = " + integers);

        assertEquals(ListX.ofInts(2, 4, 3), integers);
    }

    @Test
    void testCreateAnEmptyIterableX() {
        final var strings = ListX.<String>empty()
                .toCollectionNotNullOf(ArrayDeque::new, It::self);

        assertTrue(strings.isEmpty());
    }

    @Test
    void testStreamCollectingAndThenEquivalent() {
        final var integers = MutableListX.of(1, 4, 5, 3, 7, 4, 2);

        final var expected = integers.stream()
                .filter(n -> n > 4)
                .collect(collectingAndThen(toListX(), IterableXTest::calculateProduct));

        final var product = integers
                .filter(n -> n > 4)
                .let(IterableXTest::calculateProduct);

        System.out.println("product = " + product);

        assertEquals(expected, product, () -> "Something went wrong. Did you know, you can also crate dates from ints? " +
                integers.toListOf(day -> LocalDate.of(2020, Month.JANUARY, day)));
    }

    private static int calculateProduct(ListX<Integer> list) {
        return list.reduce((result, integer) -> result * integer).orElse(0);
    }

    @Test
    void testJava11AndBeforeCompilerTripperWorkFineInJava17() {
        assertDoesNotThrow(() -> empty());
    }

    // causes compiler error on java 11 and older:
    // java: Compilation failed: internal java compiler error
    public static <T> IterableX<T> empty() {
        return ListX.of(() -> new Iterator<>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public T next() {
                throw new NoSuchElementException();
            }
        });
    }

    @Test
    void testBranchingPaintingDataToThreeValues() {
        //arrange
        final var paintingList = ListX.of(TestSampleGenerator.createPaintingList());

        final var expected = paintingList.stream()
                .collect(branching(
                        partitioningBy(Painting::isInMuseum, mappingToList(Painting::painter)),
                        summarizingInt(Painting::ageInYears),
                        mappingToList(Painting::name)));

        final var actual = paintingList.branching(
                partitioningBy(Painting::isInMuseum, mappingToList(Painting::painter)),
                summarizingInt(Painting::ageInYears),
                mappingToList(Painting::name));

        final var expectedStats = expected.second();
        final var actualStats = actual.second();

        assertAll(
                () -> assertEquals(expected.first(), actual.first()),
                () -> assertEquals(expectedStats.getAverage(), actualStats.getAverage()),
                () -> assertEquals(expectedStats.getMax(), actualStats.getMax()),
                () -> assertEquals(expected.third(), actual.third())
        );
    }
}
