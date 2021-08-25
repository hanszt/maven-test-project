package com.dnb;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public final class HigherOrderFunctions {

    private HigherOrderFunctions() {
    }

    /**
     * This only works when input and output type are the same. Using a unary operator does not work. Interesting...
     * @param functions the functions that will be combined
     * @param <T> the type for input and output
     * @return the new function that is a combination of the supplied functions
     * @see <a href="https://youtu.be/WN9kgdSVhDo?t=2529">
     *     Design Patterns in the Light of Lambda Expressions. Venkat Subramaniam, Agile developer, inc.</a>
     */
    @SafeVarargs
    static <T> Function<T, T> combine(Function<T, T>... functions) {
        return Stream.of(functions).reduce(Function.identity(), Function::andThen);
    }

    static <T, M, R> Function<T, R> combine(Function<T, M> function, Function<M, R> downStream) {
        Objects.requireNonNull(function);
        Objects.requireNonNull(downStream);
        return e -> downStream.apply(function.apply(e));
    }

    @SafeVarargs
    static <T> Predicate<T> allValid(Predicate<T>... predicates) {
        return Stream.of(predicates).reduce(predicate -> true, Predicate::and);
    }

    @SafeVarargs
    static <T> Predicate<T> oneOrMoreValid(Predicate<T>... predicates) {
        return Stream.of(predicates).reduce(predicate -> false, Predicate::or);
    }

    @SafeVarargs
    static <T> Comparator<T> firstAndThen(Comparator<T>... otherComparators) {
        return Stream.of(otherComparators).reduce(Comparator::thenComparing).orElseThrow();
    }

    public static  <T, R> boolean by(T t, Function<T, R> mapper, Predicate<R> predicate) {
        return predicate.test(mapper.apply(t));
    }

    public static Predicate<String> startsWith(String string) {
        return startsWith(string, Function.identity());
    }

    public static <T> Predicate<T> startsWith(String string, Function<T, String> toStringMapper) {
        return t -> toStringMapper.apply(t).startsWith(string);
    }

    public static Predicate<String> contains(String string) {
        return contains(string, Function.identity());
    }

    public static <T> Predicate<T> contains(String string, Function<T, String> toStringMapper) {
        return t -> toStringMapper.apply(t).contains(string);
    }
    /**
     * A function that first maps to some other type which can than be used to test with.
     *
     *  @param mapper the mapper that is applied before testing the predicate
     *  @param predicate the predicate to be tested
     *  @param <T> the incoming type
     *  @param <R> the type to test the predicate on
     *  @return a predicate for the incoming type
     *  @throws NullPointerException if the mapper or predicate is null
     *
     * @apiNote
     * This allows for easy filtering by some nested object while maintaining the original object in the stream
     * <p><b>Example:</b>
     * <pre>{@code
     * List<Book> filteredBookList = books.stream()
     *          .filter(by(Book::getAuthor, contains("a")
     *          .or(startsWith("j"))))
     *          .collect(Collectors.toList());
     * }</pre>
     * It can help clean up code
     * @see #contains(String)
     * @see #startsWith(String)
     */
    public static <T, R> Predicate<T> by(Function<T, R> mapper, Predicate<R> predicate) {
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(mapper);
        return t -> predicate.test(mapper.apply(t));
    }

    /**
     * Allows for easier use of combiner functions 'and()' and 'or()' in the Predicate class
     * @param predicate the input predicate
     * @param <T> the type
     * @return the input predicate
     * @throws NullPointerException if the predicate is null
     * <p><b>Example:</b>
     * <pre>{@code
     *  List<Painting> filteredPaintings = paintings.stream()
     *            .filter((by(Painting::isFromPicasso)
     *            .or(Painting::isFromRembrandt))
     *            .and(Painting::isInMuseum)))
     *            .collect(Collectors.toList());
     * }</pre>
     * @see java.util.function.Predicate#and(Predicate)
     * @see java.util.function.Predicate#or(Predicate)
     */
    public static <T> Predicate<T> by(Predicate<T> predicate) {
        Objects.requireNonNull(predicate);
        return predicate;
    }

    /**
     * Allows for easier use of combiner functions in the Functions class
     * @param function the input function
     * @param <T> the type
     * @param <R> the outputtype
     * @return the same as the input
     * @throws NullPointerException if the function is null
     * <pre>{@code
     *  List<LocalDate> datesOfBirth = paintings.stream()
     *            .map(by(Painting::getPainter)
     *            .andThen(Painter::getDateOfBirth)))
     *            .collect(Collectors.toList());
     * }</pre>
     * @see java.util.function.Function#andThen(Function)
     * @see java.util.function.Function#compose(Function)
     */
    public static <T, R> Function<T, R> by(Function<T, R> function) {
        Objects.requireNonNull(function);
        return function;
    }

    /**
     * Allows for easier use of combiner functions in the Functions class
     * @param function the input function
     * @param <T> input 1 type
     * @param <U> input 2 type
     * @param <R> the output type
     * @return the same as the input
     * @throws NullPointerException if the function is null
     * <pre>{@code
     *  List<LocalDate> datesOfBirth = paintings.stream()
     *            .map(by(Painting::getPainter)
     *            .andThen(Painter::getDateOfBirth)))
     *            .collect(Collectors.toList());
     * }</pre>
     * @see java.util.function.Function#andThen(Function)
     * @see java.util.function.Function#compose(Function)
     */
    public static <T, U, R> BiFunction<T, U, R> by(BiFunction<T, U, R> function) {
        Objects.requireNonNull(function);
        return function;
    }

}
