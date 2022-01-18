package hzt.collections;

import hzt.stream.collectors.MyCollectors;
import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.BankAccount;
import org.hzt.test.model.Book;
import org.hzt.test.model.Customer;
import org.hzt.test.model.Museum;
import org.hzt.test.model.Painter;
import org.hzt.test.model.Painting;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.Year;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class TransformerTest {

    @Test
    void testFlatMapFilterAndMapToList() {
        final var museumList = TestSampleGenerator.getMuseumList();

        final var expectedPainters = museumList.stream()
                .flatMap(museum -> museum.getPaintings().stream())
                .filter(Objects::nonNull)
                .filter(Painting::isInMuseum)
                .map(Painting::painter)
                .toList();

        final var actualPainters = Transformer.of(museumList)
                .flatMap(Museum::getPaintings)
                .filter(Painting::isInMuseum)
                .toListOf(Painting::painter);

        assertIterableEquals(actualPainters, expectedPainters);
    }

    @Test
    void testFilterAndMapToCollection() {
        final var museumList = TestSampleGenerator.getMuseumList();

        final Deque<LocalDate> actualLocalDates = Transformer.of(museumList)
                .filter(museum -> museum.getPaintings().size() > 3)
                .toCollectionOf(Museum::getDateOfOpening, ArrayDeque::new);

        final Deque<LocalDate> expectedLocalDates = museumList.stream()
                .filter(museum -> museum.getPaintings().size() > 3)
                .map(Museum::getDateOfOpening)
                .collect(toCollection(ArrayDeque::new));

        assertIterableEquals(expectedLocalDates, actualLocalDates);
    }

    @Test
    void testToMappedSet() {
        final var museumList = TestSampleGenerator.getMuseumList();

        final var expected = museumList.stream()
                .map(Museum::getDateOfOpening)
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableSet());

        final var actual = Transformer.of(museumList).toSetOf(Museum::getDateOfOpening);

        assertIterableEquals(expected, actual);
    }

    @Test
    void testToMap() {
        final var museumList = TestSampleGenerator.getMuseumList();

        final var actualMap = Transformer.of(museumList).toMapOf(Museum::getName, Museum::getPaintings);

        final var expectedMap = museumList.stream()
                .filter(m -> m.getPaintings() != null && m.getName() != null)
                .collect(toUnmodifiableMap(Museum::getName, Museum::getPaintings));

        assertEquals(expectedMap, actualMap);
    }

    @Test
    void testFromIntArrayToMappedList() {
        final int[] numbers = {1, 4, 3, 6, 7, 4, 3, 234};

        final var expected = IntStream.of(numbers).mapToObj(BigDecimal::valueOf).toList();

        final var actual = Transformer.of(numbers).toListOf(BigDecimal::valueOf);

        assertIterableEquals(expected, actual);
    }

    @Test
    void testGrouping() {
        final var paintings = TestSampleGenerator.createPaintingList();

        final var expectedMap = paintings.stream().collect(groupingBy(Painting::painter));

        final var actualMap = Transformer.of(paintings).groupBy(Painting::painter);

        assertEquals(expectedMap, actualMap);
    }

    @Test
    void testPartitionMapping() {
        final var paintings = TestSampleGenerator.createPaintingList();

        final var expectedMap = paintings.stream()
                .collect(partitioningBy(Painting::isInMuseum,
                        mapping(Painting::age, toList())));

        final var actualMap = Transformer.of(paintings).partitionMapping(Painting::isInMuseum, Painting::age);

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

        final var actualMap = Transformer.of(paintings).partition(Painting::isInMuseum);

        assertAll(
                () -> assertEquals(expectedMap.get(true), actualMap.first()),
                () -> assertEquals(expectedMap.get(false), actualMap.second())
        );
    }

    @Test
    void testToArray() {
        final var paintings = TestSampleGenerator.createPaintingList();

        final var expected = paintings.stream().map(Painting::getYearOfCreation).toArray(Year[]::new);

        final var actual = Transformer.of(paintings).mapToArray(Painting::getYearOfCreation, Year[]::new);

        assertArrayEquals(expected, actual);
    }

    @Test
    void testIntersect() {
        final var collections = List.of(
                List.of(1, 2, 3, 4, 5, 7),
                Set.of(2, 4, 5),
                Set.of(4, 5, 6)
        );

        final var intersect = Transformer.of(collections).intersectBy(Function.identity());

        assertEquals(Set.of(4, 5), intersect);
    }

    @Test
    void testIntersectMuseumPaintings() {
        final var museumList = TestSampleGenerator.getMuseumList();

        final var intersection = Transformer.of(museumList).intersectBy(Museum::getPaintings);

        assertEquals(Set.of(), intersection);
    }

    @Test
    void testFold() {
        var list = TestSampleGenerator.createSampleBankAccountList();

        final var actual = Transformer.of(list).fold(BigDecimal.ZERO, this::addAccountBalance);

        final var expected = list.stream()
                .map(BankAccount::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        assertEquals(expected, actual);
    }

    @Test
    void testMinBy() {
        var bankAccounts = TestSampleGenerator.createSampleBankAccountList().stream()
                .collect(Collectors.toUnmodifiableSet());

        final var expected = bankAccounts.stream().min(Comparator.comparing(BankAccount::getBalance));

        final var actual = Transformer.of(bankAccounts).minBy(BankAccount::getBalance);

        assertEquals(expected, actual);
    }

    @Test
    void testMaxBy() {
        var set = TestSampleGenerator.createSampleBankAccountList().stream()
                .collect(Collectors.toUnmodifiableSet());

        final var expected = set.stream().max(Comparator.comparing(BankAccount::getBalance));

        final var actual = Transformer.of(set).maxBy(BankAccount::getBalance);

        assertEquals(expected, actual);
    }

    @Test
    void testMaxOf() {
        var bankAccountList = TestSampleGenerator.createSampleBankAccountList();

        final var actual = Transformer.of(bankAccountList).maxOf(BankAccount::getBalance);

        final var expected = bankAccountList.stream()
                .max(Comparator.comparing(BankAccount::getBalance))
                .map(BankAccount::getBalance)
                .orElseThrow();

        System.out.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testMinOf() {
        var list = TestSampleGenerator.createSampleBankAccountList();

        final var actual = Transformer.of(list).minOf(BankAccount::getBalance);

        final var expected = list.stream()
                .min(Comparator.comparing(BankAccount::getBalance))
                .map(BankAccount::getBalance)
                .orElseThrow();

        System.out.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testCountBy() {
        var bankAccounts = TestSampleGenerator.createSampleBankAccountList();

        final var expected = bankAccounts.stream().filter(BankAccount::isDutchAccount).count();

        final var actual = Transformer.of(bankAccounts).countBy(BankAccount::isDutchAccount);

        System.out.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testSumOf() {
        var list = TestSampleGenerator.createSampleBankAccountList();

        final BigDecimal actual = Transformer.of(list).sumOf(BankAccount::getBalance);

        final var expected = list.stream()
                .map(BankAccount::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        System.out.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testIterXOfIterable() {
        var list = TestSampleGenerator.createSampleBankAccountList();

        final BigDecimal actual = Transformer.of(Transformer.of(list)).sumOf(BankAccount::getBalance);

        final var expected = list.stream()
                .map(BankAccount::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        System.out.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testSumOfInt() {
        var list = TestSampleGenerator.createPaintingList();

        final var actual = Transformer.of(list).sumOf(Painting::ageInYears);

        final var expected = list.stream().mapToInt(Painting::ageInYears).sum();

        System.out.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testAverageOf() {
        var list = TestSampleGenerator.createPaintingList();

        final var expected = list.stream().mapToInt(Painting::ageInYears).average().orElseThrow();

        final var actual = Transformer.of(list).averageOf(Painting::ageInYears);

        System.out.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testLargeTransform() {
        final var ints = IntStream.range(0, 100_000).toArray();

        final var bigDecimals = Transformer.of(ints)
                .filter(integer -> integer % 2 == 0)
                .toDescendingSortedListOf(BigDecimal::valueOf);

        assertEquals(50_000, bigDecimals.size());
    }

    @Test
    void testTransformForEach() {
        final var ints = IntStream.range(0, 100_000).toArray();

        Transformer.of(ints)
                .filter(integer -> integer % 2 == 0)
                .onEach(this::printEvery10_000stElement)
                .forEach(i -> assertEquals(0, i % 2));
    }

    @Test
    void testFirst() {
        final var paintings = TestSampleGenerator.createPaintingList();

        final var expected = Transformer.of(paintings).first();

        final var actual = paintings.stream()
                .findFirst()
                .orElseThrow();

        assertEquals(expected, actual);
    }

    @Test
    void testLast() {
        final var paintings = TestSampleGenerator.createPaintingList();

        final var expected = Transformer.of(paintings).last();

        final var actual = paintings.get(paintings.size() - 1);

        assertEquals(expected, actual);
    }

    @Test
    void testFindLast() {
        final var paintings = TestSampleGenerator.createPaintingList();

        final var actual = Transformer.of(paintings).findLast(not(Painting::isInMuseum));

        System.out.println("actual = " + actual);

        assertEquals("Lentetuin, de pastorietuin te Nuenen in het voorjaar", actual
                .map(Painting::name)
                .orElseThrow());
    }

    @Test
    void testEmptyTransformToArrayDeque() {
        final var deque = Transformer.empty().toCollection(ArrayDeque::new);
        final var expected = Stream.empty().collect(toCollection(ArrayDeque::new));
        assertIterableEquals(expected, deque);
    }

    @Test
    void testAny() {
        final var paintings = TestSampleGenerator.createPaintingList();

        final var expected = paintings.stream().anyMatch(Painting::isInMuseum);

        final var actual = Transformer.of(paintings).any(Painting::isInMuseum);

        assertEquals(expected, actual);
    }

    @Test
    void testJoinToString() {
        final var paintings = TestSampleGenerator.createPaintingList();

        final var expected = paintings.stream()
                .map(Painting::age)
                .map(Period::toString)
                .collect(Collectors.joining(", "));

        final var actual = Transformer.of(paintings).joinToStringBy(Painting::age, ", ");

        System.out.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testToString() {
        final var paintings = TestSampleGenerator.createPaintingList();

        final var expected = "IterX{iterable=[" +
                "Painting[name=Lentetuin, de pastorietuin te Nuenen in het voorjaar, " +
                "painter=Painter{firstName='Vincent', lastname='van Gogh', dateOfBirth=1853-03-20}, " +
                "yearOfCreation=1884, isInMuseum=false]]}";

        final var actual = Transformer.of(paintings).filterNot(Painting::isInMuseum);

        assertEquals(expected, actual.toString());
    }

    @Test
    void testSkippingToSet() {
        final var bookList = TestSampleGenerator.createBookList();

        final var expected = bookList.stream()
                .filter(not(Book::isAboutProgramming))
                .collect(Collectors.toUnmodifiableSet());

        final var actual = Transformer.of(bookList).toSetSkipping(Book::isAboutProgramming);

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

        final var actual = Transformer.of(bookList).countNotNullOf(Book::getCategory);

        System.out.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testAll() {
        final var paintings = TestSampleGenerator.createPaintingList();

        final var expected = paintings.stream().allMatch(Painting::isInMuseum);

        final var actual = Transformer.of(paintings).all(Painting::isInMuseum);

        assertEquals(expected, actual);
    }

    @Test
    void testNone() {
        final var paintings = TestSampleGenerator.createPaintingList();

        final var expected = paintings.stream().noneMatch(Painting::isInMuseum);

        final var actual = Transformer.of(paintings).none(Painting::isInMuseum);

        assertEquals(expected, actual);
    }

    @Test
    void testTeeing() {
        var array = TestSampleGenerator.createSampleBankAccountList().toArray(BankAccount[]::new);

        final var pair = Transformer.of(array)
                .teeing(toList(), reducing(BigDecimal.ZERO, BankAccount::getBalance, BigDecimal::add));

        final var expectedTotal = Transformer.of(array).fold(BigDecimal.ZERO, this::addAccountBalance);

        assertAll(
                () -> assertEquals(Arrays.stream(array).toList(), pair.first()),
                () -> assertEquals(expectedTotal, pair.second())
        );
    }

    private BigDecimal addAccountBalance(BigDecimal balance, BankAccount account) {
        return balance.add(account.getBalance());
    }

    @Test
    void testCollectingToTransformer() {
        var paintingList = TestSampleGenerator.createPaintingList();

        final var expected = paintingList.stream()
                .map(Painting::painter)
                .map(Painter::getDateOfBirth)
                .max(Comparator.naturalOrder())
                .orElseThrow();

        final var max = paintingList.stream()
                .map(Painting::painter)
                .collect(MyCollectors.toTransformer())
                .maxOf(Painter::getDateOfBirth);

        final var maxWithFullTransformer = Transformer.of(paintingList)
                .maxOf(painting -> painting.painter().getDateOfBirth());

        assertAll(
                () -> assertEquals(expected, max),
                () -> assertEquals(expected, maxWithFullTransformer)
        );
    }

    @Test
    void testMapNotNull() {
        var bankAccounts = TestSampleGenerator.createSampleBankAccountListContainingNulls();

        final var expected = bankAccounts.stream()
                .map(BankAccount::getCustomer)
                .filter(Objects::nonNull)
                .map(Customer::getId)
                .filter(Objects::nonNull)
                .max(Comparator.naturalOrder())
                .orElseThrow();

        final var actual = Transformer.of(bankAccounts)
                .mapNotNull(BankAccount::getCustomer)
                .maxOf(Customer::getId);

       assertEquals(expected, actual);
    }

    @Test
    void testFlatMapToSet() {
        var museumList = TestSampleGenerator.getMuseumList();

        final var expected = museumList.stream()
                .map(Museum::getPaintings)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .collect(toUnmodifiableSet());

        final var actual = Transformer.of(museumList).flatMapToSetOf(Museum::getPaintings);

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
}
