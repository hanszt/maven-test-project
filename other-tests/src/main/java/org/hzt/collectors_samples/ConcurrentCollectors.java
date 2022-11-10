package org.hzt.collectors_samples;

import org.hzt.collections.CollectorHelper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public final class ConcurrentCollectors {

    private static final Collector.Characteristics[] CH_CONCURRENT_ID = {
            Collector.Characteristics.IDENTITY_FINISH,
            Collector.Characteristics.CONCURRENT,
            Collector.Characteristics.UNORDERED};

    private static final Collector.Characteristics[] CH_CONCURRENT_NOID = {
            Collector.Characteristics.CONCURRENT,
            Collector.Characteristics.UNORDERED};

    private ConcurrentCollectors() {
    }

    public static <T> Collector<T, Set<T>, Set<T>> toSetConcurrent() {
        return Collector.of(ConcurrentHashMap::newKeySet, Set::add, ConcurrentCollectors::throwIfCombinerCalled,
                CH_CONCURRENT_ID);
    }

    public static <T, K, V> Collector<T, ConcurrentMap<K, V>, ConcurrentMap<K, V>> toConcurrentMap(
            Function<? super T, ? extends K> keyMapper,
            Function<? super T, ? extends V> valueMapper
    ) {
        return Collector.of(ConcurrentHashMap::new, (map, value) -> map.put(keyMapper.apply(value), valueMapper.apply(value)),
                ConcurrentCollectors::throwIfCombinerCalled,
                CH_CONCURRENT_ID);
    }

    public static <T, K> Collector<T, ConcurrentMap<K, Set<T>>, ConcurrentMap<K, Set<T>>> groupingByConcurrentToSet(
            Function<? super T, ? extends K> classifier) {
        final Collector<T, Set<T>, Set<T>> setCollector = toSetConcurrent();
        return Collector.of(ConcurrentHashMap::new, (m, t) -> {
                    K key = Objects.requireNonNull(classifier.apply(t), "element cannot be mapped to a null key");
                    setCollector.accumulator().accept(m.computeIfAbsent(key, k -> setCollector.supplier().get()), t);
                }, ConcurrentCollectors::throwIfCombinerCalled,
                CH_CONCURRENT_ID);
    }

    @NotNull
    public static <T> Collector<T, List<T>, List<T>> toUnorderedConcurrentList() {
        return concurrentUnorderedIdCollector(() -> Collections.synchronizedList(new ArrayList<>()), Collection::add);
    }

    /**
     * This collector maintains order but must implement the combine method to achieve this and is not actually
     * collecting in one collection from multiple threads,
     * therefore the synchronizedList wrapper is probably not necessary
     */
    @NotNull
    public static <T> Collector<T, ?, List<T>> toOrderedConcurrentList() {
        return Collector.of(() -> Collections.synchronizedList(new ArrayList<>()), Collection::add, CollectorHelper::combine,
                Collector.Characteristics.IDENTITY_FINISH,
                Collector.Characteristics.CONCURRENT);
    }

    @NotNull
    public static <T> Collector<T, ?, Queue<T>> toQueueConcurrent() {
        return concurrentUnorderedIdCollector(ConcurrentLinkedQueue::new, Collection::add);
    }

    @NotNull
    public static <T> Collector<T, ?, ConcurrentSkipListSet<T>> toConcurrentSkipListSet() {
        return concurrentUnorderedIdCollector(ConcurrentSkipListSet::new, Collection::add);
    }

    public static Collector<CharSequence, ?, String> concurrentJoining() {
        return Collector.of(StringBuffer::new,
                StringBuffer::append,
                ConcurrentCollectors::throwIfCombinerCalled,
                StringBuffer::toString, CH_CONCURRENT_NOID);
    }

    private static <A> A throwIfCombinerCalled(A left, A right) {
        throw new IllegalStateException("Combiner used in non concurrent non order preserving collector");
    }

    private static <T, A> Collector<T, A, A> concurrentUnorderedIdCollector(Supplier<A> supplier, BiConsumer<A, T> accumulator) {
        return Collector.of(supplier, accumulator, ConcurrentCollectors::throwIfCombinerCalled, CH_CONCURRENT_ID);
    }

    public static <T, K, V, M extends ConcurrentMap<K, V>> Collector<T, ?, M>
    toConcurrentMap(Function<? super T, ? extends K> keyMapper,
                    Function<? super T, ? extends V> valueMapper,
                    Supplier<M> supplier) {
        return Collectors.toConcurrentMap(keyMapper, valueMapper, ConcurrentCollectors::throwIfCombinerCalled, supplier);
    }

}
