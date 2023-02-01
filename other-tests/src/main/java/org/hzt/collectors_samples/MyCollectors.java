package org.hzt.collectors_samples;

import org.hzt.collections.CollectorHelper;
import org.hzt.utils.iterables.Collectable;
import org.hzt.utils.sequences.Sequence;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class MyCollectors {

    private MyCollectors() {
    }

    public static <T, K extends Comparable<? super K>, V> Collector<T, ?, NavigableMap<K, V>>
    toNavigableMap(Function<? super T, ? extends K> keyMapper,
                   Function<? super T, ? extends V> valueMapper) {
        return Collector.of(TreeMap::new,
                (m, t) -> m.put(keyMapper.apply(t), valueMapper.apply(t)),
                CollectorHelper::combine);
    }

    public static <T> Collector<T, ?, NavigableSet<T>> toNavigableSet(Comparator<T> comparator) {
        return Collector.of(() -> new TreeSet<>(comparator),
                Set::add,
                CollectorHelper::combine);
    }

    public static <T> Collector<T, ?, NavigableSet<T>> toNavigableSet() {
        return Collector.of(TreeSet::new,
                Collection::add,
                CollectorHelper::combine);
    }

    /**
     * Intermediate streams
     *
     * @see <a href="https://github.com/JosePaumard/collectors-utils">collectors-utils</a>
     */
    public static <T, U> Collector<T, Stream.Builder<U>, Stream<U>> flattenToStream(Function<T, ? extends Iterable<U>> mapper) {
        Objects.requireNonNull(mapper);
        return Collector.of(
                Stream::builder,
                (builder, element) -> mapper.apply(element).forEach(builder),
                MyCollectors::combineBuilders,
                Stream.Builder::build
        );
    }

    @NotNull
    private static <U> Stream.Builder<U> combineBuilders(Stream.Builder<U> builder1, Stream.Builder<U> builder2) {
        builder2.build().forEach(builder1);
        return builder1;
    }

    public static <T> Collector<T, ?, Stream<T>> filterToStream(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        return Collectors.filtering(predicate, toStream());
    }

    public static <T, R> Collector<T, ?, Stream<R>> mapToStream(Function<? super T, ? extends R> mapper) {
        Objects.requireNonNull(mapper);
        return Collectors.mapping(mapper, toStream());
    }

    public static <T> Collector<T, Stream.Builder<T>, Stream<List<T>>> windowing(int windowSize) {
        return windowing(windowSize, 1, false);
    }

    public static <T> Collector<T, Stream.Builder<T>, Stream<List<T>>> windowing(int windowSize, int step) {
        return windowing(windowSize, step, false);
    }

    public static <T> Collector<T, Stream.Builder<T>, Stream<List<T>>> windowing(int windowSize, int step, boolean partialWindows) {
        return Collectors.collectingAndThen(toStream(), stream ->
                Sequence.of(stream::iterator)
                        .windowed(windowSize, step, partialWindows, Collectable::toList)
                        .stream());
    }

    public static <T> Collector<T, Stream.Builder<T>, Stream<List<T>>> chunking(int chunkSize) {
        return windowing(chunkSize, chunkSize, true);
    }

    public static <T, R> Collector<T, ?, R> peeking(Consumer<? super T> consumer, Collector<T, ?, R> downStream) {
        return Collectors.mapping(t -> {
            consumer.accept(t);
            return t;
        }, downStream);
    }

    /**
     * An equivalent of the forEach method on a stream. It does not collect into anything. It is a little silly therefore
     *
     * @param consumer The consumer
     * @param <T> the type of the consumer
     * @return The type as a null value
     */
    public static <T> Collector<T, ?, T> forEach(Consumer<? super T> consumer) {
        return Collector.of(() -> null, (_c, item) -> consumer.accept(item), (_c1, _c2) -> null);
    }

    /**
     * This collector triggers evaluation of the stream. It is not lazy
     * @param <T> the type of the created stream
     * @return a newly created stream.
     */
    private static <T> Collector<T, Stream.Builder<T>, Stream<T>> toStream() {
        return Collector.of(
                Stream::builder,
                Stream.Builder::accept,
                MyCollectors::combineBuilders,
                Stream.Builder::build
        );
    }

    /**
     * Implements a custom collector that converts a stream of
     * CompletableFuture objects into a single CompletableFuture that is
     * triggered when all the futures in the stream complete.
     *
     * @return A new StreamOfFuturesCollector()
     * @see <a href="https://github.com/douglascraigschmidt/LiveLessons/blob/master/ImageStreamGang/CommandLine/src/main/java/livelessons/utils/StreamOfFuturesCollector.java">
     * StreamOfFuturesCollector.java</a>
     * @see <a href="http://www.nurkiewicz.com/2013/05/java-8-completablefuture-in-action.html">Java 8: CompletableFuture in action</a>
     * @see <a href="https://www.youtube.com/watch?v=xzYV_2otlc4">The Java CompletableFuture ImageStreamGang Case Study: Applying Factory Methods</a>
     * for more info.
     * <p>
     * This static factory method creates a new StreamOfFuturesCollector.
     */
    public static <T> Collector<CompletableFuture<T>, List<CompletableFuture<T>>, CompletableFuture<Stream<T>>> toFuture() {
        return Collector.of(
                ArrayList::new,
                List::add,
                CollectorHelper::combine,
                MyCollectors::toStreamCompletableFuture,
                Collector.Characteristics.UNORDERED);
    }

    private static <T> CompletableFuture<Stream<T>> toStreamCompletableFuture(List<CompletableFuture<T>> futures) {
        return CompletableFuture
                // Use CompletableFuture.allOf() to obtain a
                // CompletableFuture that will itself complete when all
                // CompletableFutures in futures have completed.
                .allOf(futures.toArray(CompletableFuture[]::new))
                // When all futures have completed get a CompletableFuture
                // to an array of joined elements of type T.
                .thenApply(nothing -> toStream(futures));
    }

    @NotNull
    private static <T> Stream<T> toStream(List<CompletableFuture<T>> futures) {
        return futures
                // Convert futures into a stream of completable futures.
                .stream()
                // Use map() to join() all completable futures
                // and yield objects of type T.  Note that
                // join() should never block.
                .map(CompletableFuture::join);
    }


}
