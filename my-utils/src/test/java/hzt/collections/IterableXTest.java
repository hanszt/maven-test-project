package hzt.collections;

import hzt.stream.collectors.BigDecimalCollectors;
import hzt.stream.collectors.CollectorsX;
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
import test.IterXImplGenerator;

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
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        var museumList = TestSampleGenerator.getMuseumListContainingNulls();

        final var expected = museumList.stream()
                .map(Museum::getPaintings)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .collect(toUnmodifiableSet());

        final var actual = ListX.of(museumList).flatMapToSetOf(Museum::getPaintings);

        final var streamX = StreamEx.of(museumList).flatMap(museum -> museum.getPaintings().stream()).toSet();

        assertAll(
                () -> assertEquals(expected, actual),
                () -> assertEquals(expected, streamX)
        );
    }

    @Test
    void testToNonNullKeyMap() {
        final var museumList = TestSampleGenerator.getMuseumListContainingNulls();

        final var expectedMap = museumList.stream()
                .filter(m -> m.getName() != null)
                .collect(toUnmodifiableMap(Museum::getName, Museum::getPaintings));

        final var actualMap = ListX.of(museumList).toMapX(Museum::getName, Museum::getPaintings);

        assertEquals(expectedMap, actualMap);
    }

    @Test
    void testToNonNullKeyMapWithValues() {
        final var museumList = TestSampleGenerator.getMuseumListContainingNulls();

        final var expectedMap = museumList.stream()
                .collect(toUnmodifiableMap(Function.identity(), Museum::getMostPopularPainting));

        final var actualMap = ListX.of(museumList).associateWith(Museum::getMostPopularPainting);

        assertEquals(expectedMap, actualMap);
    }

    @Test
    void testWithIndicesZipWithNext2() {
        final var museums = TestSampleGenerator.getMuseumListContainingNulls();

        final var sumsOfThree = ListX.of(museums)
                .flatMap(Museum::getPaintings)
                .indices()
                .zipWithNext2(IntStream::of)
                .toListXOf(IntStream::sum);

        System.out.println("sumsOfThree = " + sumsOfThree);

        assertEquals(List.of(3, 6, 9, 12, 15, 18, 21), sumsOfThree);
    }

    @Test
    void testMapIndexed() {
        final var museums = TestSampleGenerator.getMuseumListContainingNulls();

        final var sumsOfThree = ListX.of(museums)
                .mapIndexed((index, value) -> index)
                .zipWithNext2(IntStream::of)
                .toListXOf(IntStream::sum);

        System.out.println("sumsOfThree = " + sumsOfThree);

        assertEquals(List.of(3, 6), sumsOfThree);
    }

    @Test
    void testMaxOf() {
        var bankAccountList = TestSampleGenerator.createSampleBankAccountList();

        final var expected = bankAccountList.stream()
                .max(Comparator.comparing(BankAccount::getBalance))
                .map(BankAccount::getBalance)
                .orElseThrow();

        final var actual = ListX.of(bankAccountList).maxOf(BankAccount::getBalance);

        System.out.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testMinOf() {
        var list = TestSampleGenerator.createSampleBankAccountList();

        final var expected = list.stream()
                .min(Comparator.comparing(BankAccount::getBalance))
                .map(BankAccount::getBalance)
                .orElseThrow();

        final var actual = ListX.of(list).minOf(BankAccount::getBalance);

        System.out.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testAuctionImplementingIterableX() {
        var auctions = IterXImplGenerator.createAuctions();

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
        var set = TestSampleGenerator.createSampleBankAccountList().stream()
                .collect(Collectors.toUnmodifiableSet());

        final var expected = set.stream().max(Comparator.comparing(BankAccount::getBalance));

        final var actual = ListX.of(set).maxBy(BankAccount::getBalance);

        assertEquals(expected, actual);
    }

    @Test
    void testGrouping() {
        final var paintings = TestSampleGenerator.createPaintingList();

        final var expectedMap = paintings.stream().collect(groupingBy(Painting::painter));

        final var actualMap = ListX.of(paintings).groupBy(Painting::painter);

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
        final var paintings = TestSampleGenerator.createPaintingList();

        final var expectedMap = paintings.stream()
                .collect(partitioningBy(Painting::isInMuseum));

        final var actualMap = ListX.of(paintings).partition(Painting::isInMuseum);

        assertAll(
                () -> assertEquals(expectedMap.get(true), actualMap.first()),
                () -> assertEquals(expectedMap.get(false), actualMap.second())
        );
    }

    @Test
    void testToSummaryStatistics() {
        final var paintings = TestSampleGenerator.createPaintingList();

        final var expected = paintings.stream().mapToInt(Painting::ageInYears).summaryStatistics();

        final var actual = ListX.of(paintings).statsOf(Painting::ageInYears);

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
        final var bankAccounts = TestSampleGenerator.createSampleBankAccountListContainingNulls();

        final var expected = bankAccounts.stream()
                .map(BankAccount::getBalance)
                .collect(BigDecimalCollectors.summarizingBigDecimal());

        final var actual = ListX.of(bankAccounts).statsOf(BankAccount::getBalance);

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
        final var museumList = TestSampleGenerator.getMuseumListContainingNulls();

        final var expectedPainters = museumList.stream()
                .flatMap(museum -> museum.getPaintings().stream())
                .filter(Objects::nonNull)
                .filter(Painting::isInMuseum)
                .map(Painting::painter)
                .toList();

        final var actualPainters = ListX.of(museumList)
                .flatMap(Museum::getPaintings)
                .filter(Painting::isInMuseum)
                .toListXOf(Painting::painter);

        assertIterableEquals(actualPainters, expectedPainters);
    }

    @Test
    void testFilterAndMapToCollection() {
        final var museumList = TestSampleGenerator.getMuseumListContainingNulls();

        final Deque<LocalDate> expectedLocalDates = museumList.stream()
                .filter(museum -> museum.getPaintings().size() > 3)
                .map(Museum::getDateOfOpening)
                .collect(toCollection(ArrayDeque::new));

        final Deque<LocalDate> actualLocalDates = ListX.of(museumList)
                .filter(museum -> museum.getPaintings().size() > 3)
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
    void testSkip() {
        final var museumList = TestSampleGenerator.getMuseumListContainingNulls();

        final var expected = museumList.stream().skip(3).toList();

        final var actual = ListX.of(museumList).skipToList(3);

        System.out.println("actual = " + actual);

        assertIterableEquals(expected, actual);
    }

    @Test
    void testLimit() {
        final var museumList = TestSampleGenerator.getMuseumListContainingNulls();

        final var expected = museumList.stream().limit(3).toList();

        final var actual = ListX.of(museumList).limitToList(3);

        System.out.println("actual = " + actual);

        assertIterableEquals(expected, actual);
    }

    @Test
    void testTakeLastWhile() {
        final var museumList = TestSampleGenerator.getMuseumListContainingNulls();

        final var strings = """
                De sterrennacht, Het melkmeisje, Les Demoiselles d'Avignon,
                Meisje met de parel, Le Rêve, Meisje met de rode hoed, Guernica
                """.stripIndent()
                .split(",");

        final var expected = ListX.of(strings).toSetXOf(String::trim);

        final var actual = ListX.of(museumList)
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

        final var actual = ListX.ofInts(numbers).toListXOf(BigDecimal::valueOf);

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

        final var intersect = collections.intersectBy(Function.identity());

        assertEquals(Set.of(4, 5), intersect);
    }

    @Test
    void testIntersectMuseumPaintings() {
        final var museumList = ListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final var intersection = museumList.intersectBy(Museum::getPaintings);

        assertEquals(Set.of(), intersection);
    }

    @Test
    void testSumBigDecimals() {
        var list = ListX.of(TestSampleGenerator.createSampleBankAccountList());

        final var expected = list.stream()
                .map(BankAccount::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        final var actual = list.sumOfBigDecimals(BankAccount::getBalance);

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
    void testListIteratorGetPreviousOnlyWorksBeforeWhenIsAtEnd() {
        var li = List.of(22, 44, 88, 11 ,33);
        var itr = li.listIterator();

        assertFalse(itr.hasPrevious());

        System.out.println("In actual order :");
        while (itr.hasNext()) {
            System.out.println(itr.next());
        }

        assertTrue(itr.hasPrevious());

        System.out.println("In reverse order :");
        while (itr.hasPrevious()) {
            System.out.println(itr.previous());
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
        var list = TestSampleGenerator.createSampleBankAccountList();

        final BigDecimal actual = ListX.of(list).sumOfBigDecimals(BankAccount::getBalance);

        final var expected = list.stream()
                .map(BankAccount::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        System.out.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testSumOfInt() {
        var list = TestSampleGenerator.createPaintingList();

        final var actual = ListX.of(list).sumOfInts(Painting::ageInYears);

        final var expected = list.stream().mapToInt(Painting::ageInYears).sum();

        System.out.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testAverageOf() {
        var list = TestSampleGenerator.createPaintingList();

        final var expected = list.stream().mapToInt(Painting::ageInYears).average().orElseThrow();

        final var actual = ListX.of(list).averageOfInts(Painting::ageInYears);

        System.out.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testIterXOfIterable() {
        var list = TestSampleGenerator.createSampleBankAccountList();

        final BigDecimal actual = ListX.of(ListX.of(list)).sumOfBigDecimals(BankAccount::getBalance);

        final var expected = list.stream()
                .map(BankAccount::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        System.out.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testLargeTransform() {
        final var bigDecimals = IterableX.range(0, 100_000)
                .filter(integer -> integer % 2 == 0)
                .toDescendingSortedListOf(BigDecimal::valueOf);

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
        final var paintings = TestSampleGenerator.createPaintingList();

        final var expected = ListX.of(paintings).first();

        final var actual = paintings.stream()
                .findFirst()
                .orElseThrow();

        assertEquals(expected, actual);
    }

    @Test
    void testLast() {
        final var paintings = TestSampleGenerator.createPaintingList();

        final var expected = ListX.of(paintings).last();

        final var actual = paintings.get(paintings.size() - 1);

        assertEquals(expected, actual);
    }

    @Test
    void testFindLast() {
        final var paintings = TestSampleGenerator.createPaintingList();

        final var actual = ListX.of(paintings).findLast(not(Painting::isInMuseum));

        System.out.println("actual = " + actual);

        assertEquals("Lentetuin, de pastorietuin te Nuenen in het voorjaar", actual
                .map(Painting::name)
                .orElseThrow());
    }

    @Test
    void testAny() {
        final var paintings = TestSampleGenerator.createPaintingList();

        final var expected = paintings.stream().anyMatch(Painting::isInMuseum);

        final var actual = ListX.of(paintings).any(Painting::isInMuseum);

        assertEquals(expected, actual);
    }

    @Test
    void testJoinToString() {
        final var paintings = TestSampleGenerator.createPaintingList();

        final var expected = paintings.stream()
                .map(Painting::age)
                .map(Period::toString)
                .collect(Collectors.joining(", "));

        final var actual = ListX.of(paintings).joinToStringBy(Painting::age, ", ");

        System.out.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testToString() {
        final var paintings = TestSampleGenerator.createPaintingList();

        final var expected = "ArrayListX{elements=[" +
                "Painting[name=Lentetuin, de pastorietuin te Nuenen in het voorjaar, " +
                "painter=Painter{firstName='Vincent', lastname='van Gogh', dateOfBirth=1853-03-20}, " +
                "yearOfCreation=1884, isInMuseum=false]]}";

        final var actual = ListX.of(paintings).filterNot(Painting::isInMuseum);

        assertEquals(expected, actual.toString());
    }

    @Test
    void testSkippingToSet() {
        final var bookList = TestSampleGenerator.createBookList();

        final var expected = bookList.stream()
                .filter(not(Book::isAboutProgramming))
                .collect(Collectors.toUnmodifiableSet());

        final var actual = ListX.of(bookList).toSetXSkipping(Book::isAboutProgramming);

        System.out.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testCountingByNullableValue() {
        final var bookList = TestSampleGenerator.createBookList();

        final var expected = bookList.stream()
                .map(Book::getCategory)
                .filter(Objects::nonNull)
                .count();

        final var actual = ListX.of(bookList).countNotNullOf(Book::getCategory);

        System.out.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testAll() {
        final var paintings = TestSampleGenerator.createPaintingList();

        final var expected = paintings.stream().allMatch(Painting::isInMuseum);

        final var actual = ListX.of(paintings).all(Painting::isInMuseum);

        assertEquals(expected, actual);
    }

    @Test
    void testNone() {
        final var paintings = TestSampleGenerator.createPaintingList();

        final var expected = paintings.stream().noneMatch(Painting::isInMuseum);

        final var actual = ListX.of(paintings).none(Painting::isInMuseum);

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
        var paintingList = TestSampleGenerator.createPaintingList();

        final var expected = paintingList.stream()
                .map(Painting::painter)
                .map(Painter::getDateOfBirth)
                .max(Comparator.naturalOrder())
                .orElseThrow();

        final var max = paintingList.stream()
                .map(Painting::painter)
                .collect(CollectorsX.toIterX())
                .maxOf(Painter::getDateOfBirth);

        final var maxWithFullTransformer = ListX.of(paintingList)
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
                .collect(CollectorsX.toMapX(Painter::getDateOfBirth, Painter::getLastname, (a, b) -> a));

        final var localDates = actual.flatMapKeysToSetOf(date ->
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
        var museumList = TestSampleGenerator.createPaintingList();

        final var integers = ListX.of(museumList)
                .mapNotNull(Painting::name)
                .zipWithNextToMutableListOf(String::compareTo);

        assertEquals(List.of(-5, 83, -1, 5, -5, 1, 8), integers);
    }

    @Test
    void testZipTwoCollections() {
        var values = List.of(0, 1, 2, 3, 4, 5, 6, 7);
        var others = List.of(6, 5, 4, 3, 2, 1, 0);

        final var integers = ListX.of(values).zipToMutableListWith(others, Integer::compareTo);

        assertIterableEquals(List.of(-1, -1, -1, 0, 1, 1, 1), integers);
    }

    @Test
    void testToUnion() {
        var values = List.of(0, 1, 2, 1, 3, 4, 5, 6, 7);
        var other = List.of(6, 5, 4, 3, 2, 1, 0);

        final var union = ListX.of(values).union(other, String::valueOf);

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
    void testGetListOrElseThrow() {
        final var bigDecimals = IntStream.range(0, 100_000)
                .filter(integer -> integer % 2 == 0)
                .mapToObj(BigDecimal::valueOf)
                .toList();

        final var expected = IntStream.rangeClosed(0, 254)
                .filter(integer -> integer % 2 == 0)
                .mapToObj(BigDecimal::valueOf)
                .toList();

        final var list = ListX.of(bigDecimals).distinctBy(BigDecimal::byteValue).getListOrElseThrow();

        System.out.println("list = " + list);

        assertEquals(expected, list);
    }

    @Test
    void testCreateAnEmptyIterableX() {
        final var strings = ListX.<String>empty()
                .toCollectionNotNullOf(ArrayDeque::new, Function.identity());

        assertTrue(strings.isEmpty());
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
}
