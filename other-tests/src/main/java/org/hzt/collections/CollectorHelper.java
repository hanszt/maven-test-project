package org.hzt.collections;

import java.util.Collection;
import java.util.Map;

public final class CollectorHelper {

    private CollectorHelper() {
    }

    public static <T, C extends Collection<T>> C combine(C left, C right) {
        left.addAll(right);
        return left;
    }

    public static <K, V, M extends Map<K, V>> M combine(M left, M right) {
        left.putAll(right);
        return left;
    }

    public static <T, C extends Collection<? super T>> C accumulate(C collection, T t) {
        collection.add(t);
        return collection;
    }

    public static <K, V, M extends Map<? super K, ? super V>> M accumulate(M map, K k, V v) {
        map.put(k, v);
        return map;
    }
}
