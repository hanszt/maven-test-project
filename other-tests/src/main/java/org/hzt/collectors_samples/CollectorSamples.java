package org.hzt.collectors_samples;

import org.hzt.collections.CollectorHelper;

import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collector;

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
}
