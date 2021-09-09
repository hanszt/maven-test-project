package com.dnb.utils;

import com.dnb.model.Book;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.dnb.TestSampleGenerator.createBookList;
import static com.dnb.utils.HigherOrderFunctions.*;
import static com.dnb.utils.predicates.StringPredicates.contains;
import static com.dnb.utils.predicates.StringPredicates.hasEqualLength;
import static com.dnb.utils.predicates.StringPredicates.startsWith;
import static java.util.Comparator.comparing;
import static java.util.function.Predicate.isEqual;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toUnmodifiableList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HigherOrderFunctionsTest {

    /**
     * combineAll and composeAll have exactly the same effect
     */
    @Test
    void testHigherOrderFunctionCombinerAndComposerUsingColor() {
        //arrange
        final var originalColor = new Color(123, 32, 21);
        Function<Color, Color> combinedFilter = combineAll(
                Color::brighter,
                HigherOrderFunctionsTest::maxRed,
                HigherOrderFunctionsTest::removeBlue);

        Function<Color, Color> composedFilter = composeAll(
                Color::brighter,
                HigherOrderFunctionsTest::maxRed,
                HigherOrderFunctionsTest::removeBlue);
        //act
        final var colorByCombinedFilter = combinedFilter.apply(originalColor);
        final var colorByComposedFilter = composedFilter.apply(originalColor);
        System.out.println("originalColor = " + originalColor);
        System.out.println("colorByCombinedFilter = " + colorByCombinedFilter);
        System.out.println("colorByComposedFilter = " + colorByComposedFilter);
        //assert
        assertEquals(colorByCombinedFilter, colorByComposedFilter);
        assertEquals(0, colorByCombinedFilter.getBlue());
        assertEquals(255, colorByCombinedFilter.getRed());
        assertTrue(originalColor.getGreen() < colorByCombinedFilter.getGreen());
    }

    private static Color maxRed(Color t) {
        final var MAX_RGB = 255;
        return new Color(MAX_RGB, t.getGreen(), t.getBlue());
    }

    private static Color removeBlue(Color color) {
        return new Color(color.getRed(), color.getGreen(), 0);
    }

    @Test
    void testCombinePredicateUsingOrEvaluatesToTrue() {
        final var strings = new String[]{"hallo", "hoe", "gaat", "het", "met", "jou", "?"};
        assertEquals(strings.length, Stream.of(strings)
                .filter(anyMatch(
                        contains("h"),
                        contains("e"),
                        contains("g"),
                        isEqual("jou"),
                        hasEqualLength(1)))
                .count());
    }

    @Test
    void testCombinePredicateUsingOrEvaluatesToFalse() {
        assertEquals("", Stream.of("Dit is één fout", "?!", "Dan ", "is", "'t", "prima")
                .filter(anyMatch(
                        contains("h"),
                        contains("e"),
                        contains("g"),
                        isEqual("jou"),
                        hasEqualLength(1)))
                .findFirst().orElse(""));
    }

    @Test
    void testCombinePredicateUsingAndEvaluatesToEmptyString() {
        assertEquals("", Stream.of("Dit is één fout", "?!", "Dan ", "is", "'t", "prima")
                .filter(allMatch(
                        contains("h"),
                        contains("e"),
                        contains("g"),
                        isEqual("jou"),
                        hasEqualLength(1)))
                .findFirst().orElse(""));
    }

    @Test
    void testCombinePredicateUsingAndEvaluatesToFalse() {
        final var TEST = "Dit is één fout";
        assertEquals(TEST, Stream.of(TEST, "?!", "Dan ", "is", "'t", "prima")
                .filter(allMatch(
                        startsWith("D"),
                        contains("s"),
                        contains("é"),
                        not(contains("raar")),
                        hasEqualLength(TEST.length())))
                .findAny().orElse(""));
        assertFalse(isEqual("Hallo").or(isEqual("Raar")).test("f"));
    }

    @Test
    void testCombineComparators() {
        var bookList = createBookList();
        final var expected = bookList.stream()
                .sorted(comparing(Book::getCategory).reversed().thenComparing(Book::getTitle))
                .collect(toUnmodifiableList());
        final var actual = bookList.stream()
                .sorted(sequential(comparing(Book::getCategory).reversed(), comparing(Book::getTitle)))
                .collect(toUnmodifiableList());
        assertEquals(expected, actual);
    }

    @Test
    void testComparatorsThenComparing() {
        var bookList = createBookList();
        final var expected = bookList.stream()
                .sorted(comparing(book -> book.getCategory() + book.getTitle()))
                .collect(toUnmodifiableList());
        final var actual = bookList.stream()
                .sorted(comparing(Book::getCategory).thenComparing(Book::getTitle))
                .collect(toUnmodifiableList());
        assertEquals(expected, actual);
    }

    @Test
    void testMappingBeforeFiltering() {
        var books = createBookList();
        final var E = "e";
        final var A = "u";
        final var expected = books.stream()
                .filter(book -> !(book.getTitle().contains(E) || book.getTitle().contains(A)))
                .collect(toUnmodifiableList());
        final var filteredBookList = books.stream()
                .filter(by(Book::getTitle, not(contains(E).or(contains(A)))))
                .collect(toUnmodifiableList());
        filteredBookList.forEach(System.out::println);
        assertEquals(expected, filteredBookList);
    }

    @Test
    void testCompose() {
        var books = createBookList();
        final var fieldListOfObjectClass = books.stream()
                .map(asFun(Object::getClass)
                        .compose(Book::getCategory)
                        .andThen(Class::getDeclaredFields))
                .flatMap(Arrays::stream)
                .collect(toUnmodifiableList());
        fieldListOfObjectClass.forEach(System.out::println);
        assertFalse(fieldListOfObjectClass.isEmpty());
    }

    @Test
    void testChainingAPredicate() {
        var books = createBookList();
        final var filteredBookList = books.stream()
                .filter(by(Book::hasCopies).or(Book::isAboutProgramming))
                .collect(toUnmodifiableList());
        filteredBookList.forEach(System.out::println);
        assertEquals(2, filteredBookList.size());
    }

    @Test
    void testChainingMultipleBy() {
        var books = createBookList();
        final var expected = books.stream()
                .filter(book -> book.getTitle().startsWith("t") && book.getCategory().contains("2"))
                .collect(toUnmodifiableList());
        final var filteredBookList = books.stream()
                .filter(by(Book::getTitle, startsWith("t")).and(by(Book::getCategory, contains("2"))))
                .collect(toUnmodifiableList());
        filteredBookList.forEach(System.out::println);
        assertEquals(expected, filteredBookList);
    }

    @Test
    void testBiFunctionChainingAndThenUseCaseCalculateDifferenceElementsAtSameIndexAndThenConvertToBigDecimal() {
        //arrange
        List<Double> list1 = Arrays.asList(1.0d, 2.1d, 3.3d, 5.3);
        List<Double> list2 = Arrays.asList(0.1d, 0.2d, 4d);
        final var expected = Stream.of(.9, 1.9, -.7)
                .map(BigDecimal::valueOf)
                .collect(toUnmodifiableList());
        //act
        List<BigDecimal> result = listCombiner(list1, list2, asBiFun(HigherOrderFunctionsTest::difference)
                .andThen(BigDecimal::valueOf))
                .map(bigDecimal -> bigDecimal.setScale(1, RoundingMode.HALF_UP))
                .collect(toUnmodifiableList());
        //assert
        assertEquals(expected, result);
    }

    @Test
    void testComposedConsumer() {
        //arrange
        List<Double> list1 = Arrays.asList(1.0d, 2.1d, 3.3d, 5.3);
        List<Double> list2 = Arrays.asList(0.1d, 0.2d, 4d);
        final var expected = Stream.of(.9, 1.9, -.7)
                .map(BigDecimal::valueOf)
                .collect(toUnmodifiableList());
        //act
        listCombiner(list1, list2, asBiFun(HigherOrderFunctionsTest::difference)
                .andThen(BigDecimal::valueOf))
                .map(bigDecimal -> bigDecimal.setScale(1, RoundingMode.HALF_UP))
                .forEach(first(System.out::println).andThen(System.out::println));
        //assert
        assertFalse(expected.isEmpty());
    }

    @Test
    void testMappedConsumer() {
        //arrange
        final var bookList = createBookList();
        //act
        bookList.forEach(transformThen(Book::getCategory, System.out::println));
        //assert
        assertFalse(bookList.isEmpty());
    }

    private static double difference(double aDouble, double anotherDouble) {
        return aDouble - anotherDouble;
    }

}
