package org.hzt.collectors_samples;

import org.hzt.collections.CollectorHelper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;

public final class CollectorSamples {

    private CollectorSamples() {
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
                Set::add,
                CollectorHelper::combine);
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
                CollectorSamples::toStreamCompletableFuture,
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
