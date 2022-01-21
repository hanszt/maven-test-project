package hzt.collections;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public interface MutableMapX<K, V> extends Map<K, V>, MapX<K, V> {

    static <K, V> MutableMapX<K, V> of(Map<K, V> map) {
        return new HashMapX<>(map);
    }

    static <K, V> MutableMapX<K, V> empty() {
        return new HashMapX<>();
    }

    static <K, V> MutableMapX<K, V> ofIterable(Iterable<Map.Entry<K, V>> entries) {
        return new HashMapX<>(entries);
    }

    @Override
    default <K1, V1> MutableMapX<K1, V1> map(Function<K, K1> keyMapper, Function<V, V1> valueMapper) {
        var resultMap = MutableMapX.<K1, V1>empty();
        for (Map.Entry<K, V> entry : this) {
            K key = entry.getKey();
            if (key != null) {
                resultMap.put(keyMapper.apply(key), valueMapper.apply(entry.getValue()));
            }
        }
        return resultMap;
    }

    @Override
    default <K1> MutableMapX<K1, V> mapKeys(Function<K, K1> keyMapper) {
        return map(keyMapper, Function.identity());
    }

    @Override
    default <V1> MutableMapX<K, V1> mapValues(Function<V, V1> valueMapper) {
        return map(Function.identity(), valueMapper);
    }

    @Override
    default V getOrDefault(Object key, V defaultValue) {
        return Map.super.getOrDefault(key, defaultValue);
    }

    @Override
    default void forEach(BiConsumer<? super K, ? super V> action) {
        Map.super.forEach(action);
    }
}
