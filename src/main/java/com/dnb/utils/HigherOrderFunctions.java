package com.dnb.utils;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;
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
     * @see java.util.function.Function#andThen(Function)
     */
    @SafeVarargs
    static <T> Function<T, T> combineAll(Function<T, T>... functions) {
        Objects.requireNonNull(functions);
        return Stream.of(functions).reduce(Function.identity(), Function::andThen);
    }

    /**
     * This only works when input and output type are the same. Using a unary operator does not work. Interesting...
     * @param functions the functions that will be composed
     * @param <T> the type for input and output
     * @return the new function that is a composition of the supplied functions
     * @see <a href="https://youtu.be/WN9kgdSVhDo?t=2529">
     *     Design Patterns in the Light of Lambda Expressions. Venkat Subramaniam, Agile developer, inc.</a>
     * @see java.util.function.Function#compose(Function)
     */
    @SafeVarargs
    static <T> Function<T, T> composeAll(Function<T, T>... functions) {
        Objects.requireNonNull(functions);
        return Stream.of(functions).reduce(Function.identity(), Function::compose);
    }

    static <T, M, R> Function<T, R> combineAll(Function<T, M> function, Function<M, R> downStream) {
        Objects.requireNonNull(function);
        Objects.requireNonNull(downStream);
        return e -> downStream.apply(function.apply(e));
    }

    @SafeVarargs
    static <T> Predicate<T> allMatch(Predicate<T>... predicates) {
        return Stream.of(predicates).reduce(predicate -> true, Predicate::and);
    }

    @SafeVarargs
    static <T> Predicate<T> anyMatch(Predicate<T>... predicates) {
        return Stream.of(predicates).reduce(predicate -> false, Predicate::or);
    }

    @SafeVarargs
    static <T> Comparator<T> sequential(Comparator<T> first, Comparator<T>... otherComparators) {
        return Stream.concat(Stream.of(first), Stream.of(otherComparators)).reduce(Comparator::thenComparing).orElse(first);
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
     *          .collect(Collectors.toUnmodifiableList());
     * }</pre>
     * It can help clean up code
     */
    public static <T, R> Predicate<T> by(Function<T, R> mapper, Predicate<R> predicate) {
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(mapper);
        return t -> {
            final R r = mapper.apply(t);
            return r != null && predicate.test(r);
        };
    }

    public static <T, U, R> Predicate<T> by(Function<T, U> toUMapper, Function<U, R> toRMapper, Predicate<R> predicate) {
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(toUMapper);
        Objects.requireNonNull(toRMapper);
        return t -> {
            final U u = toUMapper.apply(t);
            final R r = u != null ? toRMapper.apply(u) : null;
            return r != null && predicate.test(r);
        };
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
     *            .collect(Collectors.toUnmodifiableList());
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
     *            .collect(Collectors.toUnmodifiableList());
     * }</pre>
     * @see java.util.function.Function#andThen(Function)
     * @see java.util.function.Function#compose(Function)
     */
    public static <T, R> Function<T, R> function(Function<T, R> function) {
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
     *            .collect(Collectors.toUnmodifiableList());
     * }</pre>
     * @see java.util.function.Function#andThen(Function)
     * @see java.util.function.Function#compose(Function)
     */
    public static <T, U, R> BiFunction<T, U, R> asBiFun(BiFunction<T, U, R> function) {
        Objects.requireNonNull(function);
        return function;
    }

    public static <T> Consumer<T> first(Consumer<T> consumer) {
        Objects.requireNonNull(consumer);
        return consumer;
    }

    //shared mutability?
    public static <T, U, R> Stream<R> listCombiner(List<T> list1, List<U> list2, BiFunction<T, U, R> combiner) {
        return IntStream.iterate(0, i -> ++i)
                .mapToObj(i -> combiner.apply(list1.get(i), list2.get(i)))
                .limit(Math.min(list1.size(), list2.size()));
    }

    public static <T, R> Consumer<T> transformThen(Function<T, R> mappingFunction, Consumer<R> consumer) {
        return t -> consumer.accept(mappingFunction.apply(t));
    }

}
