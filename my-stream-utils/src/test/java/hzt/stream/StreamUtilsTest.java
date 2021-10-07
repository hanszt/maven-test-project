package hzt.stream;

import org.hzt.TestSampleGenerator;
import org.hzt.model.Book;
import org.hzt.model.Museum;
import org.hzt.model.Painter;
import org.hzt.model.Painting;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import static hzt.stream.StreamUtils.*;
import static hzt.stream.predicates.StringPredicates.contains;
import static hzt.stream.predicates.StringPredicates.containsNoneOf;
import static hzt.stream.predicates.StringPredicates.hasEqualLength;
import static hzt.stream.predicates.StringPredicates.startsWith;
import static java.util.Comparator.comparing;
import static java.util.function.Predicate.isEqual;
import static java.util.function.Predicate.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StreamUtilsTest {

    /**
     * combineAll and composeAll have exactly the same effect
     */
    @Test
    void testHigherOrderFunctionCombinerAndComposerUsingColor() {
        //arrange
        final var originalColor = new Color(123, 32, 21);
        Function<Color, Color> combinedFilter = combine(
                Color::brighter,
                StreamUtilsTest::maxRed,
                StreamUtilsTest::removeBlue);

        Function<Color, Color> composedFilter = composeAll(
                Color::brighter,
                StreamUtilsTest::maxRed,
                StreamUtilsTest::removeBlue);
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
        var bookList = TestSampleGenerator.createBookList();
        final var expected = bookList.stream()
                .sorted(comparing(Book::getCategory).reversed().thenComparing(Book::getTitle))
                .toList();
        final var actual = bookList.stream()
                .sorted(sequential(comparing(Book::getCategory).reversed(), comparing(Book::getTitle)))
                .toList();
        assertEquals(expected, actual);
    }

    @Test
    void testComparatorsThenComparing() {
        var bookList = TestSampleGenerator.createBookList();
        final var expected = bookList.stream()
                .sorted(comparing(book -> book.getCategory() + book.getTitle()))
                .toList();
        final var actual = bookList.stream()
                .sorted(comparing(Book::getCategory).thenComparing(Book::getTitle))
                .toList();
        assertEquals(expected, actual);
    }

    @Test
    void testMappingBeforeFiltering() {
        var books = TestSampleGenerator.createBookList();
        final var E = "e";
        final var A = "u";
        final var expected = books.stream()
                .filter(book -> !(book.getTitle().contains(E) || book.getTitle().contains(A)))
                .toList();
        final var filteredBookList = books.stream()
                .filter(by(Book::getTitle, containsNoneOf(E, A)))
                .toList();
        filteredBookList.forEach(System.out::println);
        assertEquals(expected, filteredBookList);
    }

    @Test
    void testCompose() {
        var books = TestSampleGenerator.createBookList();
        final var fieldListOfObjectClass = books.stream()
                .map(function(Object::getClass)
                        .compose(Book::getCategory)
                        .andThen(Class::getDeclaredFields))
                .flatMap(Arrays::stream)
                .toList();
        fieldListOfObjectClass.forEach(System.out::println);
        assertFalse(fieldListOfObjectClass.isEmpty());
    }

    @Test
    void testChainingAPredicate() {
        var books = TestSampleGenerator.createBookList();
        final var filteredBookList = books.stream()
                .filter(by(Book::hasCopies).or(Book::isAboutProgramming))
                .toList();
        filteredBookList.forEach(System.out::println);
        assertEquals(2, filteredBookList.size());
    }

    @Test
    void testChainingMultipleBy() {
        var books = TestSampleGenerator.createBookList();
        final var expected = books.stream()
                .filter(book -> book.getTitle().startsWith("t") && book.getCategory().contains("2"))
                .toList();
        final var filteredBookList = books.stream()
                .filter(by(Book::getTitle, startsWith("t")).and(by(Book::getCategory, contains("2"))))
                .toList();
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
                .toList();
        //act
        List<BigDecimal> result = combineToStream(list1, list2, StreamUtilsTest::difference)
                .map(BigDecimal::valueOf)
                .map(bigDecimal -> bigDecimal.setScale(1, RoundingMode.HALF_UP))
                .toList();
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
                .toList();
        //act
        combineToStream(list1, list2, biFunction(StreamUtilsTest::difference)
                .andThen(BigDecimal::valueOf))
                .map(bigDecimal -> bigDecimal.setScale(1, RoundingMode.HALF_UP))
                .forEach(first(System.out::println).andThen(System.out::println));
        //assert
        assertFalse(expected.isEmpty());
    }

    @Test
    void testMappedConsumer() {
        //arrange
        final var bookList = TestSampleGenerator.createBookList();
        //act
        bookList.forEach(transformAndThen(Book::getCategory, System.out::println));
        //assert
        assertFalse(bookList.isEmpty());
    }

    @Test
    void testNestedNonNullCheck() {
        var listContainingNestedNulls = Arrays.asList(
                new Painting("", null, null, false),
                null);

        final var paintingList = TestSampleGenerator.createPaintingList();
        final var concatenatedList = Stream.concat(listContainingNestedNulls.stream(), paintingList.stream())
                .toList();

        System.out.println("concatenatedList = " + concatenatedList);

        final var paintings = concatenatedList.stream()
                .filter(nonNull(Painting::painter))
                .toList();

        assertEquals(paintingList, paintings);
    }

    @Test
    void testNestedTwoLevelsDeepNonNullCheck() {
        var listContainingNestedNulls = getPaintingListContainingNestedNulls();

        final var paintingList = TestSampleGenerator.createPaintingList();
        final var concatenatedList = Stream.concat(listContainingNestedNulls.stream(), paintingList.stream())
                .toList();

        System.out.println("concatenatedList = " + concatenatedList);

        final var containingNullsFilteredOutList = concatenatedList.stream()
                .filter(nonNull(Painting::painter, Painter::getDateOfBirth))
                .toList();

        assertEquals(paintingList, containingNullsFilteredOutList);
    }

    private static List<Painting> getPaintingListContainingNestedNulls() {
        return Arrays.asList(
                new Painting("", new Painter("Hans", "Knipedol", null),
                        null, false),
                new Painting("", null, null, false),
                null);
    }

    private static double difference(double aDouble, double anotherDouble) {
        return aDouble - anotherDouble;
    }

    @Test
    void testNestedThreeLevelsDeepNonNullCheck() {
        var paintingContainingNulls = new Painting("", new Painter("Hans", "Knipedol", null),
                null, false);

        var listContainingNestedNulls = Arrays.asList(
                new Museum("", null, List.of(paintingContainingNulls)),
                null);

        final var museums = TestSampleGenerator.createMuseumList();
        final var concatenatedList = Stream.concat(listContainingNestedNulls.stream(), museums.stream())
                .toList();

        System.out.println("concatenatedList = " + concatenatedList);

        final var containingNullsFilteredOutList = concatenatedList.stream()
                .filter(nonNull(Museum::getMostPopularPainting, Painting::painter, Painter::getDateOfBirth))
                .toList();

        assertEquals(museums, containingNullsFilteredOutList);
    }

    @Test
    void testMapTwoLevelsDeepNullSafe() {
        final var paintingListContainingNestedNulls = getPaintingListContainingNestedNulls();
        final var paintingList = TestSampleGenerator.createPaintingList();

        final var paintings = Stream.concat(paintingListContainingNestedNulls.stream(), paintingList.stream())
                .toList();

        System.out.println("paintings = " + paintings);

        final var expected = paintings.stream()
                .filter(Objects::nonNull)
                .map(Painting::painter)
                .filter(Objects::nonNull)
                .map(Painter::getDateOfBirth)
                .filter(Objects::nonNull)
                .toList();

        final var painterDateOfBirthList = paintings.stream()
                .flatMap(nullSafe(Painting::painter, Painter::getDateOfBirth))
                .toList();

        assertEquals(expected, painterDateOfBirthList);
    }

    @Test
    void testMapThreeLevelsDeepNullSafe() {
        final List<Museum> concatenatedMuseumList = getMuseumsContainingNulls();

        System.out.println("concatenatedList = " + concatenatedMuseumList);

        final var expected = concatenatedMuseumList.stream()
                .filter(Objects::nonNull)
                .flatMap(museum -> museum.getPaintingList().stream())
                .filter(Objects::nonNull)
                .map(Painting::painter)
                .filter(Objects::nonNull)
                .map(Painter::getDateOfBirth)
                .filter(Objects::nonNull)
                .map(LocalDate::getDayOfWeek)
                .toList();

        final var listOfDayOfWeekBirthDateMostPopularPaintingPainters = concatenatedMuseumList.stream()
                .flatMap(iterableNullSafe(Museum::getPaintingList))
                .flatMap(nullSafe(
                        Painting::painter,
                        Painter::getDateOfBirth,
                        LocalDate::getDayOfWeek))
                .toList();

        System.out.println("listOfDayOfWeekBirthDateMostPopularPaintingPainters = " + listOfDayOfWeekBirthDateMostPopularPaintingPainters);

        assertEquals(expected, listOfDayOfWeekBirthDateMostPopularPaintingPainters);
    }

    @Test
    void testMapFourLevelsDeepNullSafe() {
        final List<Museum> concatenatedMuseumList = getMuseumsContainingNulls();

        System.out.println("concatenatedList = " + concatenatedMuseumList);

        final var expected = concatenatedMuseumList.stream()
                .filter(Objects::nonNull)
                .map(Museum::getMostPopularPainting)
                .map(Painting::painter)
                .filter(Objects::nonNull)
                .map(Painter::getDateOfBirth)
                .filter(Objects::nonNull)
                .map(LocalDate::getDayOfWeek)
                .toList();

        final var listOfDayOfWeekBirthDateMostPopularPaintingPainters = concatenatedMuseumList.stream()
                .flatMap(nullSafe(
                        Museum::getMostPopularPainting,
                        Painting::painter,
                        Painter::getDateOfBirth,
                        LocalDate::getDayOfWeek))
                .toList();

        final var expectedDaysOfWeek = List.of(
                LocalDate.of(1632, Month.OCTOBER, 31).getDayOfWeek(),
                LocalDate.of(1853, Month.MARCH, 20).getDayOfWeek(),
                LocalDate.of(1881, Month.OCTOBER, 25).getDayOfWeek());

        System.out.println("expectedDaysOfWeek = " + expectedDaysOfWeek);

        assertEquals(expectedDaysOfWeek, listOfDayOfWeekBirthDateMostPopularPaintingPainters);
        assertEquals(expected, listOfDayOfWeekBirthDateMostPopularPaintingPainters);
    }

    @Test
    void testMapFourLevelsDeepNullSafeWithMapMulti() {
        final List<Museum> concatenatedMuseumList = getMuseumsContainingNulls();

        System.out.println("concatenatedList = " + concatenatedMuseumList);

        final var expected = concatenatedMuseumList.stream()
                .filter(Objects::nonNull)
                .map(Museum::getMostPopularPainting)
                .map(Painting::painter)
                .filter(Objects::nonNull)
                .map(Painter::getDateOfBirth)
                .filter(Objects::nonNull)
                .map(LocalDate::getDayOfWeek)
                .toList();

        final var listOfDayOfWeekBirthDateMostPopularPaintingPainters = concatenatedMuseumList.stream()
                .mapMulti(nullSafeBy(
                        Museum::getMostPopularPainting,
                        Painting::painter,
                        Painter::getDateOfBirth,
                        LocalDate::getDayOfWeek))
                .toList();

        final var expectedDaysOfWeek = List.of(
                LocalDate.of(1632, Month.OCTOBER, 31).getDayOfWeek(),
                LocalDate.of(1853, Month.MARCH, 20).getDayOfWeek(),
                LocalDate.of(1881, Month.OCTOBER, 25).getDayOfWeek());

        System.out.println("expectedDaysOfWeek = " + expectedDaysOfWeek);

        assertEquals(expectedDaysOfWeek, listOfDayOfWeekBirthDateMostPopularPaintingPainters);
        assertEquals(expected, listOfDayOfWeekBirthDateMostPopularPaintingPainters);
    }

    private List<Museum> getMuseumsContainingNulls() {
        var paintingContainingNulls = new Painting("", new Painter("Hans", "Knipedol", null),
                null, false);

        var listContainingNestedNulls = Arrays.asList(
                new Museum("", null, List.of(paintingContainingNulls)),
                null);

        final var museums = TestSampleGenerator.createMuseumList();
        return Stream.concat(listContainingNestedNulls.stream(), museums.stream())
                .toList();
    }

}
