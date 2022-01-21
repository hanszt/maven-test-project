package hzt.collections;

import hzt.stream.collectors.BigDecimalCollectors;
import hzt.stream.collectors.BigDecimalSummaryStatistics;
import hzt.stream.function.PredicateX;
import hzt.stream.function.TriFunction;
import hzt.strings.StringX;
import hzt.utils.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.IntSummaryStatistics;
import java.util.Iterator;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static hzt.stream.function.IntPredicateX.noFilter;

/**
 * This is an interface to transform smaller iterables including collections or arrays to some other state.
 * <p>
 * Its use is very comparable to the streams api but with shorter syntax.
 * <p>
 * It is inspired by the functional methods provided for collections in Kotlin
 * <p>
 * The functions applied to a Transformer are eagerly evaluated as apposed to in a stream.
 * For smaller collections < 100_000 elements, the performance is similar to streams
 * <p>
 * For larger collections, or when a lot of transformation are applied, streams are preferred.
 * <p>
 * For a Transformer, I suggest not to apply more than 3 subsequent transformations.
 *
 * @param <T> The Type of the Iterable in the Transform object
 * @author Hans Zuidervaart
 */
@SuppressWarnings({"unused"})
@FunctionalInterface
public interface IterableX<T> extends Iterable<T>, IndexedIterable<T>  {

    Iterable<T> iterable();

    @NotNull
    static <T> IterableX<T> of(Iterable<T> iterable) {
        final class IterableXImpl<R> implements IterableX<R> {

            private final Iterable<R> iterable;

            private IterableXImpl(Iterable<R> iterable) {
                this.iterable = iterable;
            }

            @Override
            public Iterable<R> iterable() {
                return iterable;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (o == null || getClass() != o.getClass()) {
                    return false;
                }
                IterableX<?> iterableX = (IterableX<?>) o;
                return iterable().equals(iterableX.iterable());
            }

            @Override
            public int hashCode() {
                return Objects.hash(iterable);
            }

            @Override
            public String toString() {
                return "IterableX{" +
                        "elements=" + iterable +
                        '}';
            }
        }
        return new IterableXImpl<>(iterable);
    }

    static IterableX<Integer> range(int start, int end, IntPredicate predicate) {
        var valueList = MutableListX.<Integer>empty();
        for (int i = start; i < end; i++) {
            if (predicate.test(i)) {
                valueList.add(i);
            }
        }
        return valueList;
    }

    static IterableX<Integer> range(int start, int end) {
        return range(start, end, noFilter());
    }

    static IterableX<Integer> rangeClosed(int start, int endInclusive, IntPredicate predicate) {
        var valueList = MutableListX.<Integer>empty();
        for (int i = start; i <= endInclusive; i++) {
            if (predicate.test(i)) {
                valueList.add(i);
            }
        }
        return valueList;
    }

    static IterableX<Integer> rangeClosed(int start, int endInclusive) {
        return rangeClosed(start, endInclusive, noFilter());
    }

    default IterableX<T> plus(T value) {
        return toMutableListPlus(value);
    }

    default IterableX<T> plus(Iterable<T> values) {
        return toMutableListPlus(values);
    }

    default ListX<T> toListXPlus(Iterable<T> values) {
        return toMutableListPlus(values);
    }

    default SetX<T> toSetXPlus(Iterable<T> values) {
        return SetX.copyOf(toMutableListPlus(values));
    }

    default MutableListX<T> toMutableListPlus(T value) {
        final var list = getListOrElseCompute();
        list.add(value);
        return list;
    }

    default MutableListX<T> toMutableListPlus(Iterable<T> values) {
        var list = getListOrElseCompute();
        for (T t : values) {
            list.add(t);
        }
        return list;
    }

    default <R> IterableX<R> map(Function<T, R> mapper) {
        return IterableX.of(mapTo(MutableListX::empty, mapper));
    }

    default <R> IterableX<StringX> mapToStringX(Function<T, R> function) {
        return mapNotNull(t -> StringX.of((t != null ? function.apply(t) : "").toString()));
    }


    default <R> IterableX<R> mapIndexed(BiFunction<Integer, T, R> mapper) {
        return mapIndexedToMutableList(mapper);
    }

    default <R> MutableListX<R> mapIndexedToMutableList(BiFunction<Integer, T, R> mapper) {
        return withIndex().mapTo(MutableListX::empty, indexedValue -> mapper.apply(indexedValue.index(), indexedValue.value()));
    }

    default <R> ListX<R> mapIndexedToListX(BiFunction<Integer, T, R> mapper) {
        return mapIndexedToMutableList(mapper);
    }

    default <R> ListX<R> mapMultiToListXOf(BiConsumer<? super T, ? super Consumer<R>> mapper) {
        return ListX.of(stream().mapMulti(mapper).toList());
    }

    default <R> IterableX<R> mapMulti(BiConsumer<? super T, ? super Consumer<R>> mapper) {
        return IterableX.of(mapMultiToListXOf(mapper));
    }

    default <R> IterableX<R> mapNotNull(Function<T, R> mapper) {
        return IterableX.of(toCollectionNotNullOf(MutableListX::empty, mapper));
    }

    default <R> IterableX<T> notNullBy(Function<T, R> selector) {
        return filterNotNullBy(selector, PredicateX.noFilter());
    }

    default <R> IterableX<T> filterNotNullBy(Function<T, R> selector, Predicate<R> predicate) {
        return filterNotNullToMutableListBy(selector, predicate);
    }

    default <R> ListX<R> toListXOf(Function<T, R> transform) {
        return toMutableListNotNullOf(transform);
    }

    default <R> List<R> toListOf(Function<T, R> transform) {
        return List.copyOf(toMutableListNotNullOf(transform));
    }

    default <R> SetX<R> toSetXOf(Function<T, R> transform) {
        return toMutableSetNotNullOf(transform);
    }

    default <R> Set<R> toSetOf(Function<T, R> transform) {
        return Set.copyOf(toMutableSetNotNullOf(transform));
    }

    default <R> MutableListX<R> toMutableListOf(Function<T, R> transform) {
        return mapTo(MutableListX::empty, transform);
    }

    default <R> MutableListX<R> toMutableListNotNullOf(Function<? super T, ? extends R> transform) {
        return toCollectionNotNullOf(MutableListX::empty, transform);
    }

    default <R> MutableSetX<R> toMutableSetOf(Function<T, R> transform) {
        return mapTo(MutableSetX::empty, transform);
    }

    default <R> MutableSetX<R> toMutableSetNotNullOf(Function<T, R> transform) {
        return toCollectionNotNullOf(MutableSetX::empty, transform);
    }

    default <R, C extends Collection<R>> C mapTo(Supplier<C> collectionFactory, Function<T, R> mapper) {
        C collection = collectionFactory.get();
        for (T t : this) {
            if (t != null) {
                collection.add(mapper.apply(t));
            }
        }
        return collection;
    }

    default <R, C extends Collection<R>> C toCollectionNotNullOf(Supplier<C> collectionFactory, Function<? super T, ? extends R> mapper) {
        C collection = collectionFactory.get();
        for (T t : this) {
            if (t != null) {
                final var r = mapper.apply(t);
                if (r != null) {
                    collection.add(r);
                }
            }
        }
        return collection;
    }

    default IterableX<T> filter(Predicate<T> predicate) {
        return filterToCollection(MutableListX::empty, predicate);
    }

    default <R> IterableX<T> filterBy(Function<T, R> selector, Predicate<R> predicate) {
        return filterToMutableListBy(selector, predicate);
    }

    private  <R> MutableListX<T> filterToMutableListBy(Function<T, R> function, Predicate<R> predicate, Predicate<R> nullPredicate) {
        var list = MutableListX.<T>empty();
        for (var t : this) {
            if (t != null) {
                final var r = function.apply(t);
                if (nullPredicate.test(r) && predicate.test(r)) {
                    list.add(t);
                }
            }
        }
        return list;
    }

    default <R> MutableListX<T> filterToMutableListBy(Function<T, R> function, Predicate<R> predicate) {
        return filterToMutableListBy(function, predicate, PredicateX.noFilter());
    }

    default <R> MutableListX<T> filterNotNullToMutableListBy(Function<T, R> function, Predicate<R> predicate) {
       return filterToMutableListBy(function, predicate, Objects::nonNull);
    }

    default MutableListX<T> filterIndexedToMutableList(BiPredicate<Integer, T> predicate) {
        return filterIndexedToCollection(MutableListX::empty, predicate);
    }

    default ListX<T> filterIndexedToListX(BiPredicate<Integer, T> predicate) {
        return filterIndexedToCollection(MutableListX::empty, predicate);
    }

    default IterableX<T> filterIndexed(BiPredicate<Integer, T> predicate) {
        return IterableX.of(filterIndexedToListX(predicate));
    }

    default ListX<T> filterToListX(Predicate<T> predicate) {
        return filterToMutableList(predicate);
    }

    default SetX<T> filterToSetX(Predicate<T> predicate) {
        return SetX.copyOf(filterToMutableSet(predicate));
    }

    default MutableListX<T> filterToMutableList(Predicate<T> predicate) {
        return filterToCollection(MutableListX::empty, predicate);
    }

    default MutableSetX<T> filterToMutableSet(Predicate<T> predicate) {
        return filterToCollection(MutableSetX::empty, predicate);
    }

    default <C extends Collection<T>> C filterToCollection(Supplier<C> collectionFactory, Predicate<T> predicate) {
        C collection = collectionFactory.get();
        for (T t : this) {
            if (t != null && predicate.test(t)) {
                collection.add(t);
            }
        }
        return collection;
    }

    default <C extends Collection<T>> C filterIndexedToCollection(Supplier<C> collectionFactory, BiPredicate<Integer, T> predicate) {
        C collection = collectionFactory.get();
        for (IndexedValue<T> item : IterableX.of(this).withIndex()) {
            if (item != null && predicate.test(item.index(), item.value())) {
                collection.add(item.value());
            }
        }
        return collection;
    }

    default IterableX<T> filterNot(Predicate<T> predicate) {
        return filter(predicate.negate());
    }

    default ListX<T> toListXSkipping(Predicate<T> predicate) {
        return ListX.copyOf(filterToMutableList(predicate.negate()));
    }

    default SetX<T> toSetXSkipping(Predicate<T> predicate) {
        return SetX.copyOf(filterToMutableSet(predicate.negate()));
    }

    default <R, C extends Collection<R>> IterableX<R> flatMap(@NotNull Function<T, C> mapper) {
        return IterableX.of(flatMapToMutableListOf(mapper));
    }

    default <R, C extends Collection<R>> ListX<R> flatMapToListXOf(Function<T, C> mapper) {
        return flatMapToMutableListOf(mapper);
    }

    default <R, C extends Collection<R>> MutableListX<R> flatMapToMutableListOf(@NotNull Function<T, C> mapper) {
        final var list = MutableListX.<R>empty();
        for (T t : this) {
            final var c = mapper.apply(t);
            if (c == null) {
                continue;
            }
            for (R r : c) {
                if (r != null) {
                    list.add(r);
                }
            }
        }
        return list;
    }

    default <R, C extends Collection<R>> MutableSetX<R> flatMapToMutableSetOf(Function<T, C> mapper) {
        //noinspection unchecked
        return (MutableSetX<R>) flatMapToCollectionOf(mapper, () -> (C) MutableSetX.<R>empty());
    }

    default <R, C extends Collection<R>> SetX<R> flatMapToSetOf(Function<T, C> mapper) {
        return SetX.copyOf(flatMapToMutableSetOf(mapper));
    }

    default <R, C extends Collection<R>> C flatMapToCollectionOf(Function<T, C> mapper, Supplier<C> collectionFactory) {
        C collection = collectionFactory.get();
        for (T t : this) {
            if (t == null) {
                continue;
            }
            for (R r : mapper.apply(t)) {
                if (r != null) {
                    collection.add(r);
                }
            }
        }
        return collection;
    }

    default <R extends Comparable<R>> MutableListX<T> toListSortedBy(@NotNull Function<T, R> selector) {
        final var list = toMutableListOf(Function.identity());
        list.sort(Comparator.comparing(selector));
        return list;
    }

    default <R extends Comparable<R>> MutableListX<T> toListSortedDescendingBy(@NotNull Function<T, R> selector) {
        final var list = toMutableListOf(Function.identity());
        list.sort(Comparator.comparing(selector).reversed());
        return list;
    }

    default <R extends Comparable<R>> MutableListX<R> toSortedListOf(@NotNull Function<T, R> selector) {
        final var list = toMutableListOf(selector);
        list.sort(Comparator.naturalOrder());
        return list;
    }

    default <R extends Comparable<R>> MutableListX<R> toDescendingSortedListOf(@NotNull Function<T, R> selector) {
        final var list = toMutableListOf(selector);
        final Comparator<R> tComparator = Comparator.naturalOrder();
        list.sort(tComparator.reversed());
        return list;
    }

    default <R extends Comparable<R>> NavigableSetX<T> toSetSortedBy(Function<T, R> selector) {
        var navigableSetX = NavigableSetX.comparingBy(selector);
        navigableSetX.addAll(mapTo(MutableSetX::empty, Function.identity()));
        return navigableSetX;
    }

    default <R extends Comparable<R>> NavigableSetX<R> toNavigableSetOf(Function<? super T, ? extends R> selector) {
        return NavigableSetX.of(toMutableListNotNullOf(selector), Function.identity());
    }

    default <R> R[] toArrayOf(@NotNull Function<T, R> mapper, @NotNull IntFunction<R[]> generator) {
        return toMutableListOf(mapper).toArray(generator);
    }

    default int[] toIntArrayOf(@NotNull ToIntFunction<T> mapper) {
        int counter = 0;
        int[] array = new int[countNotNullBy(PredicateX.noFilter())];
        for (T value : this) {
            if (value != null) {
                final var anInt = mapper.applyAsInt(value);
                array[counter] = anInt;
                counter++;
            }
        }
        return array;
    }

    default long[] toLongArray(@NotNull ToLongFunction<T> mapper) {
        int counter = 0;
        long[] array = new long[countNotNullBy(PredicateX.noFilter())];
        for (T value : this) {
            if (value != null) {
                final var t = mapper.applyAsLong(value);
                array[counter] = t;
                counter++;
            }
        }
        return array;
    }

    default double[] toDoubleArray(@NotNull ToDoubleFunction<T> mapper) {
        int counter = 0;
        double[] array = new double[countNotNullBy(PredicateX.noFilter())];
        for (T value : this) {
            if (value != null) {
                final var t = mapper.applyAsDouble(value);
                array[counter] = t;
                counter++;
            }
        }
        return array;
    }

    default IntSummaryStatistics statsOf(ToIntFunction<T> mapper) {
        return stream().mapToInt(mapper).summaryStatistics();
    }

    default LongSummaryStatistics statsOf(ToLongFunction<T> mapper) {
        return stream().mapToLong(mapper).summaryStatistics();
    }

    default DoubleSummaryStatistics statsOf(ToDoubleFunction<T> mapper) {
        return stream().mapToDouble(mapper).summaryStatistics();
    }

    default BigDecimalSummaryStatistics statsOf(Function<T, BigDecimal> mapper) {
        return stream()
                .filter(Objects::nonNull)
                .map(mapper)
                .filter(Objects::nonNull)
                .collect(BigDecimalCollectors.summarizingBigDecimal());
    }

    default <K, V> MutableMapX<K, V> toMutableMap(
            @NotNull Function<? super T, ? extends K> keyMapper, @NotNull Function<? super T, ? extends V> valueMapper) {
        var map = MutableMapX.<K, V>empty();
        for (T t : this) {
            if (t != null) {
                final var key = keyMapper.apply(t);
                if (key != null) {
                    map.put(key, valueMapper.apply(t));
                }
            }
        }
        return map;
    }

    default <K, V> MapX<K, V> toMapX(@NotNull Function<T, K> keyMapper, @NotNull Function<T, V> valueMapper) {
        return toMutableMap(keyMapper, valueMapper);
    }

    default <K, V> Map<K, V> toMap(@NotNull Function<T, K> keyMapper, @NotNull Function<T, V> valueMapper) {
        return Map.copyOf(toMutableMap(keyMapper, valueMapper));
    }

    default MutableListX<T> getListOrElseCompute() {
        return iterable() instanceof List<T> list ? MutableListX.of(list) : MutableListX.of(this);
    }

    default MutableSetX<T> geSetOrElseCompute() {
        return iterable() instanceof Set<T> set ? MutableSetX.of(set) : MutableSetX.of(this);
    }

    default MutableListX<T> getListOrElseThrow() {
        if (iterable() instanceof List<T> list) {
            return MutableListX.of(list);
        }
        throw new IllegalArgumentException(iterable().getClass().getSimpleName() + " is not an instance of List");
    }

    default MutableSetX<T> getSetOrElseThrow() {
        if (iterable() instanceof Set<T> set) {
            return MutableSetX.of(set);
        }
        throw new IllegalArgumentException(iterable().getClass().getSimpleName() + " is not an instance of Set");
    }

    default NavigableSetX<T> getNavigableSetOrElseThrow() {
        if (iterable() instanceof NavigableSet<T> set) {
            return NavigableSetX.of(set);
        }
        throw new IllegalArgumentException(iterable().getClass().getSimpleName() + " is not an instance of NavigableSet");
    }

    default <K> MutableMapX<K, T> associateBy(@NotNull Function<T, K> keyMapper) {
        return toMutableMap(keyMapper, Function.identity());
    }

    default <K extends Comparable<K>> NavigableMapX<K, T> toNavigableMapAssociatedBy(
            @NotNull Function<? super T, ? extends K> keyMapper) {
        return NavigableMapX.ofMap(toMutableMap(keyMapper, Function.identity()), Function.identity());
    }

    default <V> MutableMapX<T, V> associateWith(@NotNull Function<T, V> valueMapper) {
        return toMutableMap(Function.identity(), valueMapper);
    }

    default <K extends Comparable<K>, V> NavigableMapX<K, V> toNavigableMapAssociatedWith(
            @NotNull Function<T, V> valueMapper) {
        Function<T, K> keyMapper = IterableX::asComparableOrThrow;
        final var entries = toMutableMap(keyMapper, valueMapper);
        return NavigableMapX.of(entries, Function.identity());
    }

    @NotNull
    private static <R, K extends Comparable<K>> K asComparableOrThrow(R key) {
        if (key instanceof Comparable<?> c) {
            //noinspection unchecked
            return (K) c;
        } else {
            throw new IllegalStateException(key.getClass().getSimpleName() + " is not of a comparable type");
        }
    }

    default <K> MapX<K, T> toMapXAssociatedBy(@NotNull Function<T, K> keyMapper) {
        return toMutableMap(keyMapper, Function.identity());
    }

    default <V> MapX<T, V> toMapXAssociatedWith(@NotNull Function<T, V> valueMapper) {
        return toMutableMap(Function.identity(), valueMapper);
    }

    default <R extends Comparable<R>> IterableX<T> sortedBy(@NotNull Function<T, R> selector) {
        return IterableX.of(toListSortedBy(selector));
    }

    default IterableX<T> sorted() {
        return IterableX.of(stream().sorted().toList());
    }

    default Stream<T> stream() {
        return StreamSupport.stream(iterable().spliterator(), false);
    }

    default IterableX<Integer> indices() {
        return IterableX.of(indexIterable(this::iterator));
    }

    private Iterable<Integer> indexIterable(Supplier<Iterator<T>> iteratorFactory) {
        return () -> indexIterator(iteratorFactory.get());
    }

    private Iterator<Integer> indexIterator(Iterator<T> iterator) {
        return new Iterator<>() {
            private int index = 0;
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
            @Override
            public Integer next() {
                var prevIndex = index;
                if (prevIndex < 0) {
                    throw new IllegalStateException("indexed iterator index overflow");
                }
                iterator.next();
                return index++;
            }
        };
    }

    default IterableX<IndexedValue<T>> withIndex() {
        return IterableX.of(indexingIterable());
    }

    private Iterable<IndexedValue<T>> indexingIterable() {
        return this::indexedIterator;
    }

    default IterableX<T> distinct() {
        return IterableX.of(mapTo(MutableLinkedSetX::empty, Function.identity()));
    }

    default <R> ListX<T> distinctToListBy(Function<T, R> selector) {
        return distinctToMutableListBy(selector);
    }

    default <R> MutableListX<T> distinctToMutableListBy(Function<T, R> selector) {
        var result = MutableListX.<T>empty();
        var set = MutableLinkedSetX.<R>empty();
        for (T t : this) {
            if (t != null) {
                final var r = selector.apply(t);
                if (set.add(r)) {
                    result.add(t);
                }
            }
        }
        return result;
    }

    default <R> IterableX<T> distinctBy(Function<T, R> selector) {
        return IterableX.of(distinctToMutableListBy(selector));
    }

    default int countNotNullBy(@NotNull Predicate<T> predicate) {
        var counter = 0;
        for (T t : this) {
            if (t != null && predicate.test(t)) {
                counter++;
            }
        }
        return counter;
    }

    default int count(@NotNull Predicate<T> predicate) {
        var counter = 0;
        for (T t : this) {
            if (predicate.test(t)) {
                counter++;
            }
        }
        return counter;
    }

    default <R> int countNotNullOf(@NotNull Function<T, R> mapper) {
        int counter = 0;
        for (T t : this) {
            if (t != null) {
                final var r = mapper.apply(t);
                if (r != null) {
                    counter++;
                }
            }
        }
        return counter;
    }

    default long sumOfInts(@NotNull ToIntFunction<T> selector) {
        long sum = 0;
        for (T t : this) {
            if (t != null) {
                final var value = selector.applyAsInt(t);
                sum += value;
            }
        }
        return sum;
    }

    default int sumOfLongs(@NotNull ToLongFunction<T> selector) {
        var sum = 0;
        for (T t : this) {
            if (t != null) {
                final var value = selector.applyAsLong(t);
                sum += value;
            }
        }
        return sum;
    }

    default double sumOfDoubles(@NotNull ToDoubleFunction<T> selector) {
        double sum = 0;
        for (T t : this) {
            if (t != null) {
                final var value = selector.applyAsDouble(t);
                sum += value;
            }
        }
        return sum;
    }

    default BigDecimal sumOfBigDecimals(@NotNull Function<T, BigDecimal> selector) {
        var sum = BigDecimal.ZERO;
        for (T t : this) {
            if (t != null) {
                final var value = selector.apply(t);
                if (value != null) {
                    sum = sum.add(value);
                }
            }
        }
        return sum;
    }

    default double averageOfInts(@NotNull ToIntFunction<T> selector) {
        double sum = 0;
        var counter = 0;
        for (T t : this) {
            if (t != null) {
                final var value = selector.applyAsInt(t);
                sum += value;
                counter++;
            }
        }
        return (counter != 0) ? (sum / counter) : 0;
    }

    default double averageOfLongs(@NotNull ToLongFunction<T> selector) {
        double sum = 0;
        var counter = 0;
        for (T t : this) {
            if (t != null) {
                final var value = selector.applyAsLong(t);
                sum += value;
                counter++;
            }
        }
        return (counter != 0) ? (sum / counter) : 0;
    }

    default double averageOfDoubles(@NotNull ToDoubleFunction<T> selector) {
        double sum = 0;
        var counter = 0;
        for (T t : this) {
            if (t != null) {
                final var value = selector.applyAsDouble(t);
                sum += value;
                counter++;
            }
        }
        return (counter != 0) ? (sum / counter) : 0;
    }

    default BigDecimal averageOfBigDecimals(@NotNull Function<T, BigDecimal> selector) {
        return averageOfBigDecimals(selector, 2, RoundingMode.HALF_UP);
    }

    default BigDecimal averageOfBigDecimals(@NotNull Function<T, BigDecimal> selector, int scale, @NotNull RoundingMode roundingMode) {
        var sum = BigDecimal.ZERO;
        var counter = 0;
        for (T t : this) {
            if (t != null) {
                final var value = selector.apply(t);
                if (value != null) {
                    sum = sum.add(value);
                }
                counter++;
            }
        }
        return sum.divide(BigDecimal.valueOf(counter), scale, roundingMode);
    }

    @NotNull
    default <R extends Comparable<R>> Optional<T> minBy(@NotNull Function<T, R> selector) {
        return compareBy(selector, (first, second) -> first != null && second != null && first.compareTo(second) > 0);
    }

    @NotNull
    default <R extends Comparable<R>> Optional<T> maxBy(@NotNull Function<T, R> selector) {
        return compareBy(selector, (first, second) -> first != null && second != null && first.compareTo(second) < 0);
    }

    @NotNull
    private <R extends Comparable<R>> Optional<T> compareBy(@NotNull Function<T, R> selector, @NotNull BiPredicate<R, R> biPredicate) {
        final var iterator = iterator();
        if (!iterator.hasNext()) {
            return Optional.empty();
        }
        var result = iterator.next();
        if (!iterator.hasNext()) {
            return Optional.ofNullable(result);
        }
        var value = result != null ? selector.apply(result) : null;
        do {
            final var next = iterator.next();
            final var nextToCompare = selector.apply(next);
            if (biPredicate.test(value, nextToCompare)) {
                result = next;
                value = nextToCompare;
            }
        } while (iterator.hasNext());
        return Optional.ofNullable(result);
    }

    @NotNull
    default <R extends Comparable<R>> R minOf(@NotNull Function<T, R> selector) {
        return comparisonOf(selector, (first, second) -> first != null && second != null && first.compareTo(second) > 0);
    }

    @NotNull
    default <R extends Comparable<R>> R maxOf(@NotNull Function<T, R> selector) {
        return comparisonOf(selector, (first, second) -> first != null && second != null && first.compareTo(second) < 0);
    }

    @NotNull
    private <R extends Comparable<R>> R comparisonOf(@NotNull Function<T, R> selector, @NotNull BiPredicate<R, R> biPredicate) {
        final var iterator = iterator();
        if (!iterator.hasNext()) {
            throw noValuePresentException();
        }
        final var first = iterator.next();
        var result = first != null ? selector.apply(first) : null;
        while (iterator.hasNext()) {
            final var next = iterator.next();
            if (next != null) {
                final var value = selector.apply(next);
                if (biPredicate.test(result, value)) {
                    result = value;
                }
            }
        }
        if (result != null) {
            return result;
        }
        throw noValuePresentException();
    }

    @NotNull
    default IterableX<T> onEach(@NotNull Consumer<? super T> consumer) {
        return onEachOf(Function.identity(), consumer);
    }

    @NotNull
    default <R> IterableX<T> onEachOf(@NotNull Function<? super T, ? extends R> selector, @NotNull Consumer<? super R> consumer) {
        for (T t : this) {
            consumer.accept(t != null ? selector.apply(t) : null);
        }
        return this;
    }

    default <R> R fold(@NotNull R initial, @NotNull BiFunction<R, T, R> operation) {
        var accumulator = initial;
        for (T t : this) {
            if (t != null) {
                accumulator = operation.apply(accumulator, t);
            }
        }
        return accumulator;
    }

    static void requireGreaterThanZero(int n) {
        if (n < 0) {
            throw new IllegalStateException("Requested element count $n is less than zero.");
        }
    }

    default T reduce(@NotNull T initial, @NotNull BinaryOperator<T> operation) {
        var accumulator = initial;
        for (T t : this) {
            if (t != null) {
                accumulator = operation.apply(accumulator, t);
            }
        }
        return accumulator;
    }

    default <R> R reduce(@NotNull R initial, @NotNull Function<T, R> mapper, @NotNull BinaryOperator<R> operation) {
        var accumulator = initial;
        for (T t : this) {
            if (t != null) {
                accumulator = operation.apply(accumulator, mapper.apply(t));
            }
        }
        return accumulator;
    }

    default Optional<T> reduce(@NotNull BinaryOperator<T> operation) {
        final var iterator = iterator();
        return iterator.hasNext() ? Optional.of(reduce(iterator.next(), operation)) : Optional.empty();
    }

    default <A1, R1, A2, R2> Pair<R1, R2> teeing(@NotNull Collector<T, A1, R1> downstream1,
                                                @NotNull Collector<T, A2, R2> downStream2) {
        return teeing(downstream1, downStream2, Pair::of);
    }

    default <A1, R1, A2, R2, R> R teeing(
            @NotNull Collector<T, A1, R1> downstream1,
            @NotNull Collector<T, A2, R2> downStream2,
            @NotNull BiFunction<R1, R2, R> merger) {
        return collect(Collectors.teeing(downstream1, downStream2, merger));
    }

    default <A, R> R collect(@NotNull Collector<T, A, R> collector) {
        A result = collector.supplier().get();
        iterable().forEach(t -> collector.accumulator().accept(result, t));
        return collector.finisher().apply(result);
    }

    default MutableMapX<T, MutableListX<T>> group() {
        return groupMapping(Function.identity(), Function.identity());
    }

    default <K> MutableMapX<K, MutableListX<T>> groupBy(@NotNull Function<T, K> classifier) {
        return groupMapping(classifier, Function.identity());
    }

    default <K, R> MutableMapX<K, MutableListX<R>> groupMapping(@NotNull Function<T, K> classifier,
                                                         @NotNull Function<T, R> valueMapper) {
        MutableMapX<K, MutableListX<R>> groupedMap = MutableMapX.empty();
        for (T t : this) {
            groupedMap.computeIfAbsent(classifier.apply(t), key -> MutableListX.empty()).add(valueMapper.apply(t));
        }
        return groupedMap;
    }

    default Pair<ListX<T>, ListX<T>> partition(@NotNull Predicate<T> predicate) {
        return partitionMapping(predicate, Function.identity());
    }

    default <R> Pair<ListX<R>, ListX<R>> partitionMapping(@NotNull Predicate<T> predicate, @NotNull Function<T, R> resultMapper) {
        var pair = Pair.of(MutableListX.<R>empty(), MutableListX.<R>empty());
        for (T t : this) {
            if (t != null) {
                R r = resultMapper.apply(t);
                if (predicate.test(t)) {
                    pair.first().add(r);
                } else {
                    pair.second().add(r);
                }
            }
        }
        return Pair.of(pair.first().toListX(), pair.second().toListX());
    }

    default <R, C extends Collection<R>> SetX<R> intersectBy(@NotNull Function<T, C> toCollectionMapper) {
        var list = MutableListX.<Collection<R>>empty();
        for (T t : this) {
            list.add(toCollectionMapper.apply(t));
        }
        var common = MutableSetX.<R>empty();
        final var iterator = list.iterator();
        if (iterator.hasNext()) {
            common.addAll(iterator.next());
            while (iterator.hasNext()) {
                common.retainAll(iterator.next());
            }
        }
        return common;
    }

    default T first() {
        return firstOf(Function.identity());
    }

    @NotNull
    default T first(@NotNull Predicate<T> predicate) {
        for (T next : this) {
            if (next != null && predicate.test(next)) {
                return next;
            }
        }
        throw noValuePresentException();
    }

    @NotNull
    default T firstNot(@NotNull Predicate<T> predicate) {
        return first(predicate.negate());
    }

    default <R> R firstOf(@NotNull Function<T, R> mapper) {
        for (T next : this) {
            if (next != null) {
                final var result = mapper.apply(next);
                if (result != null) {
                    return result;
                }
            }
        }
        throw noValuePresentException();
    }

    default Optional<T> findFirst() {
        return findFirstOf(Function.identity());
    }

    default Optional<T> findFirst(Predicate<T> predicate) {
        for (T next : this) {
            if (next != null && predicate.test(next)) {
                return Optional.of(next);
            }
        }
        return Optional.empty();
    }

    default <R> Optional<R> findFirstOf(Function<T, R> mapper) {
        for (T next : this) {
            if (next != null) {
                return Optional.of(next).map(mapper);
            }
        }
        return Optional.empty();
    }

    default T last() {
        return lastOf(Function.identity());
    }

    default <R> R lastOf(@NotNull Function<T, R> mapper) {
        final var iterator = iterator();
        if (!iterator.hasNext()) {
            throw noValuePresentException();
        } else if (iterable() instanceof List<T> list) {
            return findLastIfInstanceOfList(Objects::nonNull, list).map(mapper).orElseThrow();
        } else {
            return findLastIfUnknownIterable(Objects::nonNull, iterator).map(mapper).orElseThrow();
        }
    }

    default Optional<T> findLast() {
        return findLastOf(Function.identity());
    }

    default Optional<T> findLast(@NotNull Predicate<T> predicate) {
        final var iterator = iterator();
        if (!iterator.hasNext()) {
            throw noValuePresentException();
        } else if (iterable() instanceof List<T> list) {
            return findLastIfInstanceOfList(predicate, list);
        } else {
            return findLastIfUnknownIterable(predicate, iterator);
        }
    }

    default <R> Optional<R> findLastOf(@NotNull Function<T, R> mapper) {
        final var iterator = iterator();
        if (!iterator.hasNext()) {
            return Optional.empty();
        } else if (iterable() instanceof List<T> list) {
            return Optional.ofNullable(list.get(list.size() - 1)).map(mapper);
        } else {
            T result = iterator.next();
            while (iterator.hasNext()) {
                result = iterator.next();
            }
            return Optional.ofNullable(result).map(mapper);
        }
    }

    default boolean any(@NotNull Predicate<T> predicate) {
        for (var element : this) {
            if (predicate.test(element)) {
                return true;
            }
        }
        return false;
    }

    default boolean all(@NotNull Predicate<T> predicate) {
        for (T t : this) {
            if (!predicate.test(t)) {
                return false;
            }
        }
        return true;
    }

    default boolean none(@NotNull Predicate<T> predicate) {
        for (T t : this) {
            if (predicate.test(t)) {
                return false;
            }
        }
        return true;
    }

    default <R> MutableListX<R> zipWithNextToMutableListOf(BiFunction<T, T, R> function) {
        final var iterator = iterator();
        if (!iterator.hasNext()) {
            return MutableListX.empty();
        }
        final var list = MutableListX.<R>empty();
        T current = iterator.next();
        while (iterator.hasNext()) {
            final var next = iterator.next();
            list.add(function.apply(current, next));
            current = next;
        }
        return list;
    }

    default <R> IterableX<R> zipWithNext(BiFunction<T, T, R> function) {
        return zipWithNextToMutableListOf(function);
    }

    default <R> MutableListX<R> zipWithNext2ToMutableListOf(TriFunction<T, T, T, R> function) {
        final var iterator = iterator();
        if (!iterator.hasNext()) {
            return MutableListX.empty();
        }
        final var list = MutableListX.<R>empty();
        T current = iterator.next();
        if (iterator.hasNext()) {
            T next = iterator.next();
            while (iterator.hasNext()) {
                final var secondNext = iterator.next();
                list.add(function.apply(current, next, secondNext));
                current = next;
                next = secondNext;
            }
        }
        return list;
    }

    default <R> IterableX<R> zipWithNext2(TriFunction<T, T, T, R> function) {
        return IterableX.of(zipWithNext2ToMutableListOf(function));
    }

    default <A, R> MutableListX<R> zipToMutableListWith(Iterable<A> otherIterable, BiFunction<T, A, R> function) {
        final var otherIterator = otherIterable.iterator();
        final var iterator = iterator();
        final var resultListSize = Math.min(collectionSizeOrElse(10),
                IterableX.of(otherIterable).collectionSizeOrElse(10));

        final var list = MutableListX.<R>withInitCapacity(resultListSize);
        while (iterator.hasNext() && otherIterator.hasNext()) {
            final var next = iterator.next();
            final var otherNext = otherIterator.next();
            list.add(function.apply(next, otherNext));
        }
        return list;
    }

    default SetX<T> union(Iterable<T> other) {
        var union = MutableSetX.<T>empty();
        iterable().forEach(union::add);
        other.forEach(union::add);
        return union;
    }

    default <R> SetX<R> union(Iterable<T> other, Function<T, R> mapper) {
        var union = toMutableSetOf(mapper);
        final var setX = of(other).toSetXOf(mapper);
        setX.forEach(union::add);
        return union;
    }

    default <A, R> IterableX<R> zipWith(Iterable<A> iterable, BiFunction<T, A, R> function) {
        return IterableX.of(zipToMutableListWith(iterable, function));
    }

    private int collectionSizeOrElse(@SuppressWarnings("SameParameterValue") int defaultSize) {
        return collectionSizeOrElseGet(() -> defaultSize);
    }

    private int collectionSizeOrElseGet(IntSupplier supplier) {
        return iterable() instanceof Collection<T> c ? c.size() : supplier.getAsInt();
    }

    default IterableX<T> takeWhileInclusive(Predicate<T> predicate) {
        return IterableX.of(takeToMutableListWhileInclusive(predicate));
    }

    default MutableListX<T> takeToMutableListWhileInclusive(Predicate<T> predicate) {
        var list = MutableListX.<T>empty();
        for (T item : this) {
            list.add(item);
            if (!predicate.test(item)) {
                break;
            }
        }
        return list;
    }

    default IterableX<T> takeWhile(Predicate<T> predicate) {
        return IterableX.of(takeToListXWhile(predicate));
    }

    default MutableListX<T> takeToMutableListWhile(Predicate<T> predicate) {
        final var list = MutableListX.<T>empty();
        for (T item : this) {
            if (!predicate.test(item)) {
                break;
            }
            list.add(item);
        }
        return list;
    }
    default ListX<T> takeToListXWhile(Predicate<T> predicate) {
        return takeToMutableListWhile(predicate);
    }

    default IterableX<T> dropWhileExclusive(Predicate<T> predicate) {
        return IterableX.of(dropToListXWhileExclusive(predicate));
    }

    default ListX<T> dropToListXWhileExclusive(Predicate<T> predicate) {
        return dropToMutableListWhile(predicate, true);
    }

    private MutableListX<T> dropToMutableListWhile(Predicate<T> predicate, boolean exclusive) {
        var yielding = false;
        var list = MutableListX.<T>empty();
        for (T item : this) {
            if (yielding) {
                list.add(item);
                continue;
            }
            if (!predicate.test(item)) {
                if (!exclusive) {
                    list.add(item);
                }
                yielding = true;
            }
        }
        return list;
    }

    default IterableX<T> dropWhile(Predicate<T> predicate) {
        return IterableX.of(dropToListXWhile(predicate));
    }

    default ListX<T> dropToListXWhile(Predicate<T> predicate) {
        return  dropToMutableListWhile(predicate, false);
    }

    default IterableX<T> dropLastWhile(Predicate<T> predicate) {
        return IterableX.of(dropLastToMutableListWhile(predicate));
    }

    default MutableListX<T> dropLastToMutableListWhile(Predicate<T> predicate) {
        var list = getListOrElseCompute();
        if (list.isEmpty()) {
            return MutableListX.empty();
        }
        var iterator = list.listIterator();
        while (iterator.hasPrevious()) {
            if (!predicate.test(iterator.previous())) {
                return takeToMutableList(iterator.nextIndex() + 1);
            }
        }
        return MutableListX.empty();
    }

    default IterableX<T> takeLastWhile(Predicate<T> predicate) {
        return IterableX.of(takeLastToMutableListWhile(predicate));
    }

    default MutableListX<T> takeLastToMutableListWhile(Predicate<T> predicate) {
        var input = getListOrElseCompute();
        if (input.isEmpty()) {
            return MutableListX.empty();
        }
        var iterator = input.listIterator(input.size());
        while (iterator.hasPrevious()) {
            if (!predicate.test(iterator.previous())) {
                iterator.next();
                var expectedSize = input.size() - iterator.nextIndex();
                if (expectedSize == 0) {
                    return MutableListX.empty();
                }
                var result = MutableListX.<T>withInitCapacity(expectedSize);
                while (iterator.hasNext()) {
                    result.add(iterator.next());
                }
                return result;
            }
        }
        return input;
    }

    default IterableX<T> take(int n) {
        return IterableX.of(takeToMutableList(n));
    }

    default MutableListX<T> takeToMutableList(int n) {
        requireGreaterThanZero(n);
        if (n == 0) {
            return MutableListX.empty();
        }
        if (iterable() instanceof Collection<T> c) {
            if (n >= c.size()) {
                return MutableListX.of(c);
            }
            if (n == 1) {
                return MutableListX.of(first());
            }
        }
        int count = 0;
        var list = MutableListX.<T>empty();
        for (T t : this) {
            list.add(t);
            if (++count == n) {
                break;
            }
        }
        return list;
    }

    default IterableX<T> skip(int count) {
        return IterableX.of(skipToList(count));
    }

    default ListX<T> skipToList(int n) {
        return ListX.of(stream().skip(n).toList());
    }

    default IterableX<T> limit(int bound) {
        return IterableX.of(limitToList(bound));
    }

    default ListX<T> limitToList(int bound) {
        return ListX.of(stream().limit(bound).toList());
    }

    default String joinToString() {
        return joinToStringBy(Object::toString);
    }

    default String joinToString(CharSequence delimiter) {
        return joinToStringBy(Object::toString, delimiter);
    }

    default <R> String joinToStringBy(@NotNull Function<T, R> selector) {
        return joinToStringBy(selector, "");
    }

    default <R> String joinToStringBy(@NotNull Function<T, R> selector, CharSequence delimiter) {
        final var sb = new StringBuilder();
        final var iterator = iterator();
        while (iterator.hasNext()) {
            final var string = selector.apply(iterator.next());
            sb.append(string).append(iterator.hasNext() ? delimiter : "");
        }
        return sb.toString().trim();
    }

    default StringX joinToStringX() {
        return joinToStringXBy(Object::toString);
    }

    default StringX joinToStringX(CharSequence delimiter) {
        return joinToStringXBy(Object::toString, delimiter);
    }

    default <R> StringX joinToStringXBy(@NotNull Function<T, R> selector) {
        return joinToStringXBy(selector, "");
    }

    default <R> StringX joinToStringXBy(@NotNull Function<T, R> selector, CharSequence delimiter) {
        return StringX.of(joinToStringBy(selector, delimiter));
    }

    @NotNull
    @Override
    default Iterator<T> iterator() {
        return iterable().iterator();
    }

    @Override
    default @NotNull Iterator<IndexedValue<T>> indexedIterator() {
        return new Iterator<>() {
            private int index = 0;
            private final Iterator<T> iterator = iterator();
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
            @Override
            public IndexedValue<T> next() {
                var prevIndex = index;
                if (prevIndex < 0) {
                    throw new IllegalStateException("indexed iterator index overflow");
                }
                return new IndexedValue<>(index++, iterator.next());
            }
        };
    }

    @NotNull
    private static NoSuchElementException noValuePresentException() {
        return new NoSuchElementException("No value present");
    }

    private static <T> Optional<T> findLastIfUnknownIterable(Predicate<T> predicate, Iterator<T> iterator) {
        var result = iterator.next();
        if (!predicate.test(result) && !iterator.hasNext()) {
            return Optional.empty();
        }
        while (iterator.hasNext()) {
            var next = iterator.next();
            if (predicate.test(next)) {
                result = next;
            }
        }
        return Optional.ofNullable(result);
    }

    private static <T> Optional<T> findLastIfInstanceOfList(Predicate<T> predicate, List<T> list) {
        final var last = list.get(list.size() - 1);
        if (last != null && predicate.test(last)) {
            return Optional.of(last);
        }
        int index = list.size() - 2;
        while (index >= 0) {
            final var result = list.get(index);
            if (result != null && predicate.test(result)) {
                return Optional.of(result);
            }
            index--;
        }
        return Optional.empty();
    }
}
