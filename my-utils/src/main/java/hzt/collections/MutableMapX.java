package hzt.collections;

import java.util.Map;
import java.util.function.BiConsumer;

public interface MutableMapX<K, V> extends Map<K, V>, MapX<K, V> {

    static <K, V> MutableMapX<K, V> of(Map<K, V> map) {
        return new HashMapX<>(map);
    }

    static <K, V> MutableMapX<K, V> empty() {
        return new HashMapX<>();
    }

    static <K, V> MutableMapX<K, V> of(Iterable<Map.Entry<K, V>> entries) {
        return new HashMapX<>(entries);
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
