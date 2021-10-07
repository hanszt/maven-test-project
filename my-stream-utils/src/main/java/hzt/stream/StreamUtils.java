package hzt.stream;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * A Utility class to write clean stream pipelines
 *
 * @author Hans Zuidervaart
 */
public final class StreamUtils {

    private StreamUtils() {
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
    public static <T> Function<T, T> combine(Function<T, T>... functions) {
        return Stream.of(functions)
                .filter(Objects::nonNull)
                .reduce(Function.identity(), Function::andThen);
    }

    @SafeVarargs
    public static <T> UnaryOperator<T> combine(UnaryOperator<T>... unaryOperators) {
        return Stream.of(unaryOperators)
                .filter(Objects::nonNull)
                .reduce(identity(), StreamUtils::combine);
    }

    private static <T> UnaryOperator<T> combine(UnaryOperator<T> before, UnaryOperator<T> after) {
        Objects.requireNonNull(before);
        Objects.requireNonNull(after);
        return t -> after.apply(before.apply(t));
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
    public static <T> Function<T, T> composeAll(Function<T, T>... functions) {
        return Stream.of(functions)
                .filter(Objects::nonNull)
                .reduce(Function.identity(), Function::compose);
    }

    @SafeVarargs
    public static <T> Function<T, T> composeAll(UnaryOperator<T>... unaryOperators) {
        return Stream.of(unaryOperators)
                .filter(Objects::nonNull)
                .reduce(identity(), StreamUtils::compose);
    }

    private static <T> UnaryOperator<T> compose(UnaryOperator<T> before, UnaryOperator<T> after) {
        Objects.requireNonNull(before);
        return t -> before.apply(after.apply(t));
    }

    static <T, M, R> Function<T, R> combine(Function<T, M> function, Function<M, R> downStream) {
        Objects.requireNonNull(function);
        Objects.requireNonNull(downStream);
        return e -> downStream.apply(function.apply(e));
    }

    @SafeVarargs
    public static <T> Predicate<T> allMatch(Predicate<T>... predicates) {
        return Stream.of(predicates)
                .filter(Objects::nonNull)
                .reduce(predicate -> true, Predicate::and);
    }

    @SafeVarargs
    public static <T> Predicate<T> anyMatch(Predicate<T>... predicates) {
        return Stream.of(predicates)
                .filter(Objects::nonNull)
                .reduce(predicate -> false, Predicate::or);
    }

    @SafeVarargs
    public static <T> Comparator<T> sequential(Comparator<T> first, Comparator<T>... otherComparators) {
        return Stream.concat(Stream.of(first), Stream.of(otherComparators))
                .filter(Objects::nonNull)
                .reduce(Comparator::thenComparing)
                .orElse(first);
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

    public static <T, R> Predicate<T> nonNull(Function<T, R> toRMapper) {
        Objects.requireNonNull(toRMapper);
        return t -> t != null && toRMapper.apply(t) != null;
    }

    public static <T, U, R> Predicate<T> nonNull(Function<T, U> toUMapper, Function<U, R> toRMapper) {
        Objects.requireNonNull(toUMapper);
        Objects.requireNonNull(toRMapper);
        return t -> {
            final U u = t != null ? toUMapper.apply(t) : null;
            final R r = u != null ? toRMapper.apply(u) : null;
            return r != null;
        };
    }

    public static <T, U, V, R> Predicate<T> nonNull(Function<T, U> toUMapper, Function<U, V> toVMapper, Function<V, R> toRMapper) {
        Objects.requireNonNull(toUMapper);
        Objects.requireNonNull(toVMapper);
        Objects.requireNonNull(toRMapper);
        return t -> {
            final U u = t != null ? toUMapper.apply(t) : null;
            final V v = u != null ? toVMapper.apply(u) : null;
            final R r = u != null ? toRMapper.apply(v) : null;
            return r != null;
        };
    }

    public static <T, R> Function<T, Stream<R>> iterableNullSafe(Function<? super T, ? extends Iterable<R>> toIterableMapper) {
        Objects.requireNonNull(toIterableMapper);
        return t -> {
            final Iterable<R> iterable = t != null ? toIterableMapper.apply(t) : null;
            return iterable != null ? StreamSupport.stream(iterable.spliterator(), false) : Stream.empty();
        };
    }

    public static <T, R> BiConsumer<T, Consumer<R>> iterableNullSafeBy(Function<? super T, ? extends Iterable<R>> toIterableMapper) {
        Objects.requireNonNull(toIterableMapper);
        return (t, consumer) -> {
             var iterable = t != null ? toIterableMapper.apply(t) : null;
             if (iterable != null) {
                 iterable.forEach(consumer);
             }
        };
    }

    public static <T, R> Function<T, Stream<R>> nullSafe(Function<? super T, ? extends R> mapper) {
        Objects.requireNonNull(mapper);
        return t -> Stream.ofNullable(t != null ? mapper.apply(t) : null);
    }

    public static <T, U, R> Function<T, Stream<R>> nullSafe(
            Function<? super T, ? extends U> toUMapper,
            Function<? super U, ? extends R> toRMapper) {
        Objects.requireNonNull(toUMapper);
        Objects.requireNonNull(toRMapper);
        return t -> {
            final U u = t != null ? toUMapper.apply(t) : null;
            final R r = u != null ? toRMapper.apply(u) : null;
            return Stream.ofNullable(r);
        };
    }

    public static <T, U, V, R> Function<T, Stream<R>> nullSafe(
            Function<? super T, ? extends U> toUMapper,
            Function<? super U, ? extends V> toVMapper,
            Function<? super V, ? extends R> toRMapper) {
        Objects.requireNonNull(toUMapper);
        Objects.requireNonNull(toVMapper);
        Objects.requireNonNull(toRMapper);
        return t -> {
            final U u = t != null ? toUMapper.apply(t) : null;
            final V v = u != null ? toVMapper.apply(u) : null;
            final R r = v != null ? toRMapper.apply(v) : null;
            return Stream.ofNullable(r);
        };
    }

    public static <T, U, V, W, R> Function<T, Stream<R>> nullSafe(
            Function<? super T, ? extends U> toUMapper,
            Function<? super U, ? extends V> toVMapper,
            Function<? super V, ? extends W> toWMapper,
            Function<? super W, ? extends R> toRMapper) {
        Objects.requireNonNull(toUMapper);
        Objects.requireNonNull(toVMapper);
        Objects.requireNonNull(toWMapper);
        Objects.requireNonNull(toRMapper);
        return t -> {
            final U u = t != null ? toUMapper.apply(t) : null;
            final V v = u != null ? toVMapper.apply(u) : null;
            final W w = v != null ? toWMapper.apply(v) : null;
            final R r = w != null ? toRMapper.apply(w) : null;
            return Stream.ofNullable(r);
        };
    }

    public static <T, R> BiConsumer<T, Consumer<R>> nullSafeBy(Function<? super T, ? extends R> mapper) {
        Objects.requireNonNull(mapper);
        return (t, consumer) -> Optional.ofNullable(t)
                .map(mapper)
                .ifPresent(consumer);
    }

    public static <T, U, R> BiConsumer<T, Consumer<R>> nullSafeBy(
            Function<? super T, ? extends U> toUMapper,
            Function<? super U, ? extends R> toRMapper) {
        Objects.requireNonNull(toUMapper);
        Objects.requireNonNull(toRMapper);
        return (t, consumer) -> Optional.ofNullable(t)
                .map(toUMapper)
                .map(toRMapper)
                .ifPresent(consumer);
    }

    public static <T, U, V, R> BiConsumer<T, Consumer<R>> nullSafeBy(
            Function<? super T, ? extends U> toUMapper,
            Function<? super U, ? extends V> toVMapper,
            Function<? super V, ? extends R> toRMapper) {
        Objects.requireNonNull(toUMapper);
        Objects.requireNonNull(toVMapper);
        Objects.requireNonNull(toRMapper);
        return (t, consumer) -> Optional.ofNullable(t)
                .map(toUMapper)
                .map(toVMapper)
                .map(toRMapper)
                .ifPresent(consumer);
    }

    public static <T, U, V, W, R> BiConsumer<T, Consumer<R>> nullSafeBy(
            Function<? super T, ? extends U> toUMapper,
            Function<? super U, ? extends V> toVMapper,
            Function<? super V, ? extends W> toWMapper,
            Function<? super W, ? extends R> toRMapper) {
        Objects.requireNonNull(toUMapper);
        Objects.requireNonNull(toVMapper);
        Objects.requireNonNull(toWMapper);
        Objects.requireNonNull(toRMapper);
        return (t, consumer) -> Optional.ofNullable(t)
                .map(toUMapper)
                .map(toVMapper)
                .map(toWMapper)
                .map(toRMapper)
                .ifPresent(consumer);
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
    public static <T, U, R> BiFunction<T, U, R> biFunction(BiFunction<T, U, R> function) {
        Objects.requireNonNull(function);
        return function;
    }

    public static <T> Consumer<T> first(Consumer<T> consumer) {
        Objects.requireNonNull(consumer);
        return consumer;
    }

    //shared mutability?
    public static <T, U, R> Stream<R> combineToStream(Iterable<T> iterable1, Iterable<U> iterable2, BiFunction<T, U, R> combiner) {
        final var iterator1 = iterable1.iterator();
        final var iterator2 = iterable2.iterator();
        return Stream.iterate(0, i -> iterator1.hasNext() && iterator2.hasNext(), identity())
                .map(i -> combiner.apply(iterator1.next(), iterator2.next()));
    }

    public static <T, R> Consumer<T> transformAndThen(Function<T, R> mappingFunction, Consumer<R> consumer) {
        return t -> consumer.accept(mappingFunction.apply(t));
    }

    public static <T> UnaryOperator<T> identity() {
        return t -> t;
    }

    @SafeVarargs
    public static <T> boolean allMatch(Predicate<T> matchPredicate, T... values) {
        return Stream.of(values)
                .filter(Objects::nonNull)
                .allMatch(matchPredicate);
    }

    @SafeVarargs
    public static <T> boolean anyMatch(Predicate<T> matchPredicate, T... values) {
        return Stream.of(values)
                .filter(Objects::nonNull)
                .anyMatch(matchPredicate);
    }

    @SafeVarargs
    public static <T> boolean noneMatch(Predicate<T> matchPredicate, T... values) {
        return Stream.of(values)
                .filter(Objects::nonNull)
                .noneMatch(matchPredicate);
    }

}
