package hzt.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public interface MapX<K, V> extends IterableX<Map.Entry<K, V>> {

    static <K, V> MapX<K, V> of(Map<K, V> map) {
        return new HashMapX<>(map);
    }

    static <K, V> MapX<K, V> of(Iterable<Map.Entry<K, V>> entries) {
        return new HashMapX<>(entries);
    }

    @SafeVarargs
    @SuppressWarnings("varargs")
    static <K, V> Map<K, V> ofEntries(Map.Entry<? extends K, ? extends V>... entries) {
        throw new UnsupportedOperationException();
    }

    <K1, V1> MapX<K1, V1> map(Function<K, K1> keyMapper, Function<V, V1> valueMapper);

    <K1> MapX<K1, V> mapKeys(Function<K, K1> keyMapper);

    <V1> MapX<K, V1> mapValues(Function<V, V1> valueMapper);

    default <K1, V1> MapX<K1, V1> toInvertedMapOf(Function<K, V1> toValueMapper, Function<V, K1> toKeyMapper) {
        Map<K1, V1> resultMap = new HashMap<>();
        for (Map.Entry<K, V> entry : this) {
            V value = entry.getValue();
            if (value != null) {
                resultMap.put(toKeyMapper.apply(value), toValueMapper.apply(entry.getKey()));
            }
        }
        return MapX.of(resultMap);
    }

    default MapX<V, K> toInvertedMap() {
        return toInvertedMapOf(Function.identity(), Function.identity());
    }

    default <R> ListX<R> toListOf(BiFunction<K, V, R> mapper) {
        return IterableX.of(this).toListXOf(e -> mapper.apply(e.getKey(), e.getValue()));
    }

    default <R> MutableListX<R> valuesToMutableListOf(Function<V, R> mapper) {
        return IterableX.of(valueIterable(this::iterator)).toMutableListOf(mapper);
    }

    default <R> ListX<R> valuesToListXOf(Function<V, R> mapper) {
        return valuesToMutableListOf(mapper);
    }

    default <R> List<R> valuesToListOf(Function<V, R> mapper) {
        return List.copyOf(valuesToMutableListOf(mapper));
    }

    default <R> SetX<R> toSetOf(BiFunction<K, V, R> mapper) {
        return IterableX.of(this).toSetXOf(e -> mapper.apply(e.getKey(), e.getValue()));
    }

    default <R> SetX<R> keysToSetOf(Function<K, R> mapper) {
        return IterableX.of(keyIterable(this::iterator)).toSetXOf(mapper);
    }

    default <R> SetX<R> valuesToSetOf(Function<V, R> mapper) {
        return IterableX.of(valueIterable(this::iterator)).toSetXOf(mapper);
    }

    default <R> IterableX<R> toIterXOf(BiFunction<K, V, R> mapper) {
        return IterableX.of(toListOf(mapper));
    }

    default <R> IterableX<R> toIterXOf(Function<Map.Entry<K, V>, R> mapper) {
        return IterableX.of(IterableX.of(this).toMutableListOf(mapper));
    }

    default <R> IterableX<R> keysToIterXOf(Function<K, R> mapper) {
        return IterableX.of(keysToSetOf(mapper));
    }

    default <R> IterableX<R> valuesToIterX(Function<V, R> mapper) {
        return IterableX.of(valuesToListXOf(mapper));
    }

    default <R, C extends Collection<R>> C valuesTo(Supplier<C> collectionFactory, Function<V, R> mapper) {
        return IterableX.of(valueIterable(this::iterator)).mapTo(collectionFactory, mapper);
    }

    default <R, C extends Collection<R>> C flatMapTo(Supplier<C> collectionFactory, Function<Map.Entry<K, V>, C> mapper) {
        C destination = collectionFactory.get();
        for (var e : this) {
            var collection = mapper.apply(e);
            destination.addAll(collection);
        }
        return destination;
    }

    default <R, C extends Collection<R>> C flatMapValuesTo(Supplier<C> collectionFactory, Function<V, C> mapper) {
        C destination = collectionFactory.get();
        for (var e : valueIterable(this::iterator)) {
            var collection = mapper.apply(e);
            destination.addAll(collection);
        }
        return destination;
    }

    default <R, C extends Collection<R>> C flatMapKeysTo(Supplier<C> collectionFactory, Function<K, C> mapper) {
        C destination = collectionFactory.get();
        for (var e : keyIterable(this::iterator)) {
            var collection = mapper.apply(e);
            destination.addAll(collection);
        }
        return destination;
    }

    default <R, C extends Collection<R>> MapX<K, R> flatMapValues(Function<V, C> mapper) {
        Map<K, R> resultMap = new HashMap<>();
        for (var e : this) {
            var collection = mapper.apply(e.getValue());
            for (var v : collection) {
                resultMap.put(e.getKey(), v);
            }
        }
        return MapX.of(resultMap);
    }

    default <R> List<R> flatMapKeysToMutableListOf(Function<K, Collection<R>> mapper) {
        return (List<R>) flatMapKeysTo(ArrayList::new, mapper);
    }

    default <R> List<R> flatMapKeysToListOf(Function<K, Collection<R>> mapper) {
        return List.copyOf(flatMapKeysTo(ArrayList::new, mapper));
    }

    default <R> Set<R> flatMapKeysToSetOf(Function<K, Collection<R>> mapper) {
        return Set.copyOf(flatMapKeysTo(HashSet::new, mapper));
    }

    default <R> List<R> flatMapValuesToMutableListOf(Function<V, Collection<R>> mapper) {
        return (List<R>) flatMapValuesTo(ArrayList::new, mapper);
    }

    default  <R> List<R> flatMapValuesToListOf(Function<V, Collection<R>> mapper) {
        return List.copyOf(flatMapValuesToMutableListOf(mapper));
    }

    private Iterable<V> valueIterable(Supplier<Iterator<Map.Entry<K, V>>> iteratorFactory) {
        return () -> valueIterator(iteratorFactory.get());
    }

    private Iterator<V> valueIterator(Iterator<Map.Entry<K, V>> iterator) {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
            @Override
            public V next() {
                return iterator.next().getValue();
            }
        };
    }

    private Iterable<K> keyIterable(Supplier<Iterator<Map.Entry<K, V>>> iteratorFactory) {
        return () -> keyIterator(iteratorFactory.get());
    }

    private Iterator<K> keyIterator(Iterator<Map.Entry<K, V>> iterator) {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
            @Override
            public K next() {
                return iterator.next().getKey();
            }
        };
    }

    int size();

    boolean isEmpty();

    boolean containsKey(Object key);

    boolean containsValue(Object value);

    V get(Object key);

    Set<K> keySet();

    Collection<V> values();

    Set<Map.Entry<K, V>> entrySet();

    boolean equals(Object o);

    int hashCode();

    default V getOrDefault(Object key, V defaultValue) {
        V v;
        return (((v = get(key)) != null) || containsKey(key))
                ? v
                : defaultValue;
    }

    default MutableMapX<K, V> toMutableMap() {
        return MutableMapX.ofIterable(this);
    }

    default void forEach(BiConsumer<? super K, ? super V> action) {
        Objects.requireNonNull(action);
        for (Map.Entry<K, V> entry : entrySet()) {
            K k;
            V v;
            try {
                k = entry.getKey();
                v = entry.getValue();
            } catch (IllegalStateException ise) {
                // this usually means the entry is no longer in the map.
                throw new ConcurrentModificationException(ise);
            }
            action.accept(k, v);
        }
    }

    static <K, V> Map<K, V> copyOf(Map<? extends K, ? extends V> map) {
        throw new UnsupportedOperationException();
    }
}
