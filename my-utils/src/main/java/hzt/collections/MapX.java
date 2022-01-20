package hzt.collections;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("ClassCanBeRecord")
public final class MapX<K, V> implements Map<K, V>,
        Iterable<Map.Entry<K, V>>, IndexedIterable<Map.Entry<K,V>>, Comparable<MapX<K, V>> {

    private final Map<K, V> map;

    private MapX(@NotNull Map<K, V> map) {
        this.map = map;
    }

    public static <K, V> MapX<K, V> of(Map<K, V> map) {
        return new MapX<>(map);
    }

    public <K1, V1> MapX<K1, V1> toMutableMap(Function<K, K1> keyMapper, Function<V, V1> valueMapper) {
        Map<K1, V1> resultMap = new HashMap<>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            K key = entry.getKey();
            if (key != null) {
                resultMap.put(keyMapper.apply(key), valueMapper.apply(entry.getValue()));
            }
        }
        return MapX.of(resultMap);
    }

    public <K1, V1> MapX<K1, V1> toMap(Function<K, K1> keyMapper, Function<V, V1> valueMapper) {
        return MapX.of(Map.copyOf(toMutableMap(keyMapper, valueMapper)));
    }

    public <K1, V1> MapX<K1, V1> map(Function<K, K1> keyMapper, Function<V, V1> valueMapper) {
        return MapX.of(toMutableMap(keyMapper, valueMapper));
    }

    public <K1, V1> MapX<K1, V> mapKeys(Function<K, K1> keyMapper) {
        return MapX.of(map(keyMapper, Function.identity()));
    }

    public <K1, V1> MapX<K1, V1> toInvertedMapOf(Function<K, V1> toValueMapper, Function<V, K1> toKeyMapper) {
        Map<K1, V1> resultMap = new HashMap<>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            V value = entry.getValue();
            if (value != null) {
                resultMap.put(toKeyMapper.apply(value), toValueMapper.apply(entry.getKey()));
            }
        }
        return MapX.of(resultMap);
    }

    public MapX<V, K> toInvertedMap() {
        return toInvertedMapOf(Function.identity(), Function.identity());
    }

    public <R> List<R> toListOf(BiFunction<K, V, R> mapper) {
        return IterX.of(map.entrySet()).toListOf(e -> mapper.apply(e.getKey(), e.getValue()));
    }

    public <R> List<R> toListOf(Function<Map.Entry<K, V>, R> mapper) {
        return IterX.of(map.entrySet()).toListOf(mapper);
    }

    public <R> List<R> valuesToListOf(Function<V, R> mapper) {
        return IterX.of(map.values()).toListOf(mapper);
    }

    public <R> Set<R> toSetOf(BiFunction<K, V, R> mapper) {
        return IterX.of(map.entrySet()).toSetOf(e -> mapper.apply(e.getKey(), e.getValue()));
    }

    public <R> Set<R> toSetOf(Function<Map.Entry<K, V>, R> mapper) {
        return IterX.of(map.entrySet()).toSetOf(mapper);
    }

    public <R> Set<R> keysToSetOf(Function<K, R> mapper) {
        return IterX.of(map.keySet()).toSetOf(mapper);
    }

    public <R> Set<R> valuesToSetOf(Function<V, R> mapper) {
        return IterX.of(map.values()).toSetOf(mapper);
    }

    public <R> IterX<R> toIterXOf(BiFunction<K, V, R> mapper) {
        return IterX.of(toListOf(mapper));
    }

    public <R> IterX<R> toIterXOf(Function<Map.Entry<K, V>, R> mapper) {
        return IterX.of(IterX.of(map.entrySet()).toMutableListOf(mapper));
    }

    public <R> IterX<R> keysToIterXOf(Function<K, R> mapper) {
        return IterX.of(keysToSetOf(mapper));
    }

    public <R> IterX<R> valuesToIterX(Function<V, R> mapper) {
        return IterX.of(valuesToListOf(mapper));
    }

    public <R, C extends Collection<R>> C valuesTo(Supplier<C> collectionFactory, Function<V, R> mapper) {
        return IterX.of(map.values()).mapTo(collectionFactory, mapper);
    }

    public <R, C extends Collection<R>> C flatMapTo(Supplier<C> collectionFactory, Function<Map.Entry<K, V>, C> mapper) {
        C destination = collectionFactory.get();
        for (var e : map.entrySet()) {
            var collection = mapper.apply(e);
            destination.addAll(collection);
        }
        return destination;
    }

    public <R, C extends Collection<R>> C flatMapValuesTo(Supplier<C> collectionFactory, Function<V, C> mapper) {
        C destination = collectionFactory.get();
        for (var e : map.values()) {
            var collection = mapper.apply(e);
            destination.addAll(collection);
        }
        return destination;
    }

    public <R, C extends Collection<R>> C flatMapKeysTo(Supplier<C> collectionFactory, Function<K, C> mapper) {
        C destination = collectionFactory.get();
        for (var e : keySet()) {
            var collection = mapper.apply(e);
            destination.addAll(collection);
        }
        return destination;
    }

    public <R, C extends Collection<R>> MapX<K, R> flatMapValues(Function<V, C> mapper) {
        Map<K, R> resultMap = new HashMap<>();
        for (var e : this) {
            var collection = mapper.apply(e.getValue());
            for (var v : collection) {
                resultMap.put(e.getKey(), v);
            }
        }
        return MapX.of(resultMap);
    }

    public <R> List<R> flatMapToMutableListOf(Function<Map.Entry<K, V>, Collection<R>> mapper) {
        return (List<R>) flatMapTo(ArrayList::new, mapper);
    }

    public <R> List<R> flatMapToListOf(Function<Map.Entry<K, V>, Collection<R>> mapper) {
        return List.copyOf(flatMapTo(ArrayList::new, mapper));
    }

    public <R> List<R> flatMapKeysToMutableListOf(Function<K, Collection<R>> mapper) {
        return (List<R>) flatMapKeysTo(ArrayList::new, mapper);
    }

    public <R> List<R> flatMapKeysToListOf(Function<K, Collection<R>> mapper) {
        return List.copyOf(flatMapKeysTo(ArrayList::new, mapper));
    }

    public <R> Set<R> flatMapKeysToSetOf(Function<K, Collection<R>> mapper) {
        return Set.copyOf(flatMapKeysTo(HashSet::new, mapper));
    }

    public <R> List<R> flatMapValuesToMutableListOf(Function<V, Collection<R>> mapper) {
        return (List<R>) flatMapValuesTo(ArrayList::new, mapper);
    }

    public <R> List<R> flatMapValuesToListOf(Function<V, Collection<R>> mapper) {
        return List.copyOf(flatMapValuesToMutableListOf(mapper));
    }

    public Map<K, V> toMap() {
        return map;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return map.get(key);
    }

    @Nullable
    @Override
    public V put(K key, V value) {
        return map.put(key, value);
    }

    @Override
    public V remove(Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(@NotNull Map<? extends K, ? extends V> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @NotNull
    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @NotNull
    @Override
    public Collection<V> values() {
        return map.values();
    }

    @NotNull
    @Override
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }

    @NotNull
    @Override
    public Iterator<Entry<K, V>> iterator() {
        return map.entrySet().iterator();
    }

    @Override
    public int compareTo(@NotNull MapX<K, V> o) {
        return map.entrySet().size() - o.entrySet().size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MapX<?, ?> mapX = (MapX<?, ?>) o;
        return map.equals(mapX.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(map);
    }

    @Override
    public String toString() {
        return "MapX{" +
                "map=" + map +
                '}';
    }

    @Override
    public Iterator<IndexedValue<Entry<K, V>>> indexedIterator() {
        return new Iterator<>() {
            private int index = 0;
            private final Iterator<Entry<K, V>> iterator = iterator();
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
            @Override
            public IndexedValue<Entry<K, V>> next() {
                var prevIndex = index;
                if (prevIndex < 0) {
                    throw new IllegalStateException("indexed iterator index overflow");
                }
                return new IndexedValue<>(index++, iterator.next());
            }
        };
    }
}
