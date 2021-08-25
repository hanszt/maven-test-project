package com.dnb;

import com.dnb.model.Book;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.dnb.HigherOrderFunctions.*;
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
                .filter(oneOrMoreValid(
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
                .filter(oneOrMoreValid(
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
                .filter(allValid(
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
                .filter(allValid(
                        contains("D"),
                        contains("s"),
                        contains("é"),
                        hasEqualLength(TEST.length())))
                .findAny().orElse(""));
        assertFalse(isEqual("Hallo").or(isEqual("Raar")).test("f"));
    }

    private Predicate<String> hasEqualLength(int length) {
        return s -> s.length() == length;
    }

    @Test
    void testCombineComparators() {
        Set<Book> set = createBookSet();
        final var expected = set.stream()
                .filter(book -> by(book, Book::getBookCategory, isEqual("raar").negate()))
                .sorted(comparing(Book::getBookCategory).reversed().thenComparing(Book::getBookName))
                .collect(Collectors.toList());
        final var actual = set.stream()
                .sorted(firstAndThen(comparing(Book::getBookCategory).reversed(), comparing(Book::getBookName)))
                .collect(Collectors.toList());
        assertEquals(expected, actual);
    }

    private static Set<Book> createBookSet() {
        return Set.of(new Book("hallo", "1"),
                new Book("wat", "1"),
                new Book("tester", "2"),
                new Book("een", "1"),
                new Book("ingewikkelde", "2"),
                new Book("test", "2"));
    }

    @Test
    void testMappingBeforeFiltering() {
        Set<Book> books = createBookSet();
        final var filteredBookList = books.stream()
                .filter(not(by(Book::getBookName, contains("a").or(contains("i")))))
                .collect(Collectors.toList());
        filteredBookList.forEach(System.out::println);
        assertEquals(3, filteredBookList.size());
    }

    @Test
    void testCompose() {
        Set<Book> books = createBookSet();
        final var filteredBookList = books.stream()
                .map(by(Object::getClass)
                        .compose(Book::getBookCategory)
                        .andThen(Class::getDeclaredFields))
                .flatMap(Arrays::stream)
                .collect(Collectors.toList());
        filteredBookList.forEach(System.out::println);
        assertEquals(60, filteredBookList.size());
    }

    @Test
    void testChainingAPredicate() {
        Set<Book> books = createBookSet();
        final var filteredBookList = books.stream()
                .filter(by(Book::hasCopies).or(Book::isAboutJava))
                .collect(Collectors.toList());
        filteredBookList.forEach(System.out::println);
        assertEquals(6, filteredBookList.size());
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
