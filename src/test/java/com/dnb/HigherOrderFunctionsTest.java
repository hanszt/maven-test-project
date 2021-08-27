package com.dnb;

import com.dnb.model.Book;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.dnb.HigherOrderFunctions.*;
import static com.dnb.TestSampleGenerator.createBookList;
import static java.util.Comparator.comparing;
import static java.util.function.Predicate.isEqual;
import static java.util.function.Predicate.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HigherOrderFunctionsTest {

    @Test
    void testHigherOrderFunctionCombinerUsingColor() {
        Function<Color, Color> func = combine(
                Color::brighter,
                HigherOrderFunctionsTest::maxRed,
                HigherOrderFunctionsTest::removeBlue);
        final var originalColor = Color.GRAY;
        final var result = func.apply(originalColor);
        assertEquals(0, result.getBlue());
        assertEquals(255, result.getRed());
        assertTrue(originalColor.getGreen() < result.getGreen());
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
                .collect(Collectors.toList());
        final var actual = bookList.stream()
                .sorted(sequential(comparing(Book::getCategory).reversed(), comparing(Book::getTitle)))
                .collect(Collectors.toList());
        assertEquals(expected, actual);
    }

    @Test
    void testComparatorsThenComparing() {
        var bookList = createBookList();
        final var expected = bookList.stream()
                .sorted(comparing(book -> book.getCategory() + book.getTitle()))
                .collect(Collectors.toList());
        final var actual = bookList.stream()
                .sorted(comparing(Book::getCategory).thenComparing(Book::getTitle))
                .collect(Collectors.toList());
        assertEquals(expected, actual);
    }

    @Test
    void testMappingBeforeFiltering() {
        var books = createBookList();
        final var E = "e";
        final var A = "u";
        final var expected = books.stream()
                .filter(book -> !(book.getTitle().contains(E) || book.getTitle().contains(A)))
                .collect(Collectors.toList());
        final var filteredBookList = books.stream()
                .filter(by(Book::getTitle, not(contains(E).or(contains(A)))))
                .collect(Collectors.toList());
        filteredBookList.forEach(System.out::println);
        assertEquals(expected, filteredBookList);
    }

    @Test
    void testCompose() {
        var books = createBookList();
        final var fieldListOfObjectClass = books.stream()
                .map(by(Object::getClass)
                        .compose(Book::getCategory)
                        .andThen(Class::getDeclaredFields))
                .flatMap(Arrays::stream)
                .collect(Collectors.toList());
        fieldListOfObjectClass.forEach(System.out::println);
        assertEquals(70, fieldListOfObjectClass.size());
    }

    @Test
    void testChainingAPredicate() {
        var books = createBookList();
        final var filteredBookList = books.stream()
                .filter(by(Book::hasCopies).or(Book::isAboutProgramming))
                .collect(Collectors.toList());
        filteredBookList.forEach(System.out::println);
        assertEquals(2, filteredBookList.size());
    }

    @Test
    void testChainingMultipleBy() {
        var books = createBookList();
        final var expected = books.stream()
                .filter(book -> book.getTitle().startsWith("t") && book.getCategory().contains("2"))
                .collect(Collectors.toList());
        final var filteredBookList = books.stream()
                .filter(by(Book::getTitle, startsWith("t")).and(by(Book::getCategory, contains("2"))))
                .collect(Collectors.toList());
        filteredBookList.forEach(System.out::println);
        assertEquals(expected, filteredBookList);
    }

    @Test
    void testBiFunctionChaining() {
        List<Double> list1 = Arrays.asList(1.0d, 2.1d, 3.3d, 5.3);
        List<Double> list2 = Arrays.asList(0.1d, 0.2d, 4d);
        List<Boolean> result = listCombiner(list1, list2,
                by(Double::compareTo).andThen(i -> i > 0));
        assertEquals(List.of(true, true, false), result);
    }

    private static <T, U, R> List<R> listCombiner(List<T> list1, List<U> list2, BiFunction<T, U, R> combiner) {
        final int endExclusive = Math.min(list1.size(), list2.size());
        return IntStream.range(0, endExclusive)
                .mapToObj(i -> combiner.apply(list1.get(i), list2.get(i)))
                .collect(Collectors.toList());
    }


}
