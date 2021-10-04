package hzt.stream;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public final class EntryStreamUtils {

    private EntryStreamUtils() {
    }

    public static <K, V, R> Function<Map.Entry<K, V>, R> toSingle(BiFunction<K, V, R> entryMapper) {
        Objects.requireNonNull(entryMapper);
        return entry -> entryMapper.apply(entry.getKey(), entry.getValue());
    }

    public static <K, V, R1, R2> Function<Map.Entry<K, V>, Map.Entry<R2, R1>> toInvertedEntry(
            Function<K, R1> keyToValueMapper, Function<V, R2> valueToKeyMapper) {
        Objects.requireNonNull(keyToValueMapper);
        Objects.requireNonNull(valueToKeyMapper);
        return entry -> new AbstractMap.SimpleEntry<>(
                valueToKeyMapper.apply(entry.getValue()),
                keyToValueMapper.apply(entry.getKey()));
    }

    public static <K, V> Function<Map.Entry<K, V>, Map.Entry<V, K>> toInvertedEntry() {
        return toInvertedEntry(Function.identity(), Function.identity());
    }

    public static <K, V, R1, R2> Function<Map.Entry<K, V>, Map.Entry<R1, R2>> toEntry(
            Function<K, R1> keyMapper, Function<V, R2> valueMapper) {
        Objects.requireNonNull(keyMapper);
        Objects.requireNonNull(valueMapper);
        return entry -> new AbstractMap.SimpleEntry<>(
                keyMapper.apply(entry.getKey()),
                valueMapper.apply(entry.getValue()));
    }

    public static <K, V, R> Function<Map.Entry<K, V>, Map.Entry<R, V>> key(Function<K, R> keyMapper) {
        Objects.requireNonNull(keyMapper);
        return entry -> new AbstractMap.SimpleEntry<>(keyMapper.apply(entry.getKey()), entry.getValue());
    }

    public static <K, V, R> Function<Map.Entry<K, V>, Map.Entry<K, R>> value(Function<V, R> valueMapper) {
        Objects.requireNonNull(valueMapper);
        return entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), valueMapper.apply(entry.getValue()));
    }

    public static <K, V> Predicate<Map.Entry<K, V>> byKey(Predicate<K> keyPredicate) {
        Objects.requireNonNull(keyPredicate);
        return e -> e != null && e.getKey() != null && keyPredicate.test(e.getKey());
    }

    public static <K, V> Predicate<Map.Entry<K, V>> byValue(Predicate<V> valuePredicate) {
        Objects.requireNonNull(valuePredicate);
        return e -> e != null && e.getValue() != null && valuePredicate.test(e.getValue());
    }

    public static <K, V> Predicate<Map.Entry<K, V>> byEntry(BiPredicate<K, V> entryPredicate) {
        Objects.requireNonNull(entryPredicate);
        return e -> e != null && entryPredicate.test(e.getKey(), e.getValue());
    }

    public static <K, V> Consumer<Map.Entry<K, V>> entry(BiConsumer<K, V> biConsumer) {
        Objects.requireNonNull(biConsumer);
        return entry -> biConsumer.accept(entry.getKey(), entry.getValue());
    }

}
