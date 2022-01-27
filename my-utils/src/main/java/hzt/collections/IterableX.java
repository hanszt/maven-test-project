package hzt.collections;

import hzt.function.It;
import hzt.function.QuadFunction;
import hzt.function.TriFunction;
import hzt.stream.collectors.BigDecimalStatistics;
import hzt.stream.collectors.CollectorsX;
import hzt.stream.collectors.DoubleStatistics;
import hzt.strings.StringX;
import hzt.utils.Pair;
import hzt.utils.Triple;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.NavigableSet;
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
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
public interface IterableX<T> extends Iterable<T>, IndexedIterable<T> {

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
        MutableListX<Integer> valueList = MutableListX.empty();
        for (int i = start; i < end; i++) {
            if (predicate.test(i)) {
                valueList.add(i);
            }
        }
        return valueList;
    }

    static IterableX<Integer> range(int start, int end) {
        return range(start, end, It.noIntFilter());
    }

    static IterableX<Integer> rangeClosed(int start, int endInclusive, IntPredicate predicate) {
        MutableListX<Integer> valueList = MutableListX.empty();
        for (int i = start; i <= endInclusive; i++) {
            if (predicate.test(i)) {
                valueList.add(i);
            }
        }
        return valueList;
    }

    static IterableX<Integer> rangeClosed(int start, int endInclusive) {
        return rangeClosed(start, endInclusive, It.noIntFilter());
    }

    static <T> IterableX<T> iterate(Supplier<T> supplier, Predicate<MutableListX<T>> predicate) {
        MutableListX<T> list = MutableListX.empty();
        while (predicate.test(list)) {
            list.add(supplier.get());
        }
        return list;
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
        final MutableListX<T> list = getListOrElseCompute();
        list.add(value);
        return list;
    }

    default MutableListX<T> toMutableListPlus(Iterable<T> values) {
        MutableListX<T> list = getListOrElseCompute();
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


    default <R> IterableX<R> mapIndexed(BiFunction<Integer, ? super T, ? extends R> mapper) {
        return mapIndexedToMutableList(mapper);
    }

    default <R> MutableListX<R> mapIndexedToMutableList(BiFunction<Integer, ? super T, ? extends R> mapper) {
        return withIndex().mapTo(MutableListX::empty, indexedValue -> mapper.apply(indexedValue.index(), indexedValue.value()));
    }

    default <R> ListX<R> mapIndexedToListX(BiFunction<Integer, ? super T, ? extends R> mapper) {
        return mapIndexedToMutableList(mapper);
    }

    default <R> ListX<R> mapMultiToListXOf(BiConsumer<? super T, ? super Consumer<R>> mapper) {
        MutableListX<R> list = MutableListX.empty();
        for (T t : this) {
            mapper.accept(t, (Consumer<R>) list::add);
        }
        return list;
    }

    default <R> IterableX<R> mapMulti(BiConsumer<? super T, ? super Consumer<R>> mapper) {
        return IterableX.of(mapMultiToListXOf(mapper));
    }

    default <R> IterableX<R> mapNotNull(Function<? super T, ? extends R> mapper) {
        return IterableX.of(toCollectionNotNullOf(MutableListX::empty, mapper));
    }

    default <R> IterableX<T> notNullBy(Function<? super T, ? extends R> selector) {
        return filterNotNullBy(selector, It.noFilter());
    }

    default <R> IterableX<T> filterNotNullBy(Function<? super T, ? extends R> selector, Predicate<? super R> predicate) {
        return filterNotNullToMutableListBy(selector, predicate);
    }

    default <R> ListX<R> toListXOf(Function<T, R> transform) {
        return toMutableListNotNullOf(transform);
    }

    default <R> List<R> toListOf(Function<T, R> transform) {
        return Collections.unmodifiableList(toMutableListNotNullOf(transform));
    }

    default <R> SetX<R> toSetXOf(Function<T, R> transform) {
        return toMutableSetNotNullOf(transform);
    }

    default <R> Set<R> toSetOf(Function<T, R> transform) {
        return Collections.unmodifiableSet(toMutableSetNotNullOf(transform));
    }

    default <R> MutableListX<R> toMutableListOf(Function<T, R> transform) {
        return mapTo(MutableListX::empty, transform);
    }

    default <R> MutableListX<R> toMutableListNotNullOf(Function<? super T, ? extends R> transform) {
        return toCollectionNotNullOf(MutableListX::empty, transform);
    }

    default <R> MutableSetX<R> toMutableSetOf(Function<? super T, ? extends R> transform) {
        return mapTo(MutableSetX::empty, transform);
    }

    default <R> MutableSetX<R> toMutableSetNotNullOf(Function<? super T, ? extends R> transform) {
        return toCollectionNotNullOf(MutableSetX::empty, transform);
    }

    default <R, C extends Collection<R>> C mapTo(Supplier<C> collectionFactory, Function<? super T, ? extends R> mapper) {
        return mapFilteringToCollection(collectionFactory, Objects::nonNull, mapper, It.noFilter());
    }

    default <R, C extends Collection<R>> C toCollectionNotNullOf(Supplier<C> collectionFactory, Function<? super T, ? extends R> mapper) {
        return mapFilteringToCollection(collectionFactory, Objects::nonNull, mapper, Objects::nonNull);
    }

    default IterableX<T> filter(Predicate<T> predicate) {
        return filterToCollection(MutableListX::empty, predicate);
    }

    default <R> IterableX<T> filterBy(Function<T, R> selector, Predicate<R> predicate) {
        return filterToMutableListBy(selector, predicate);
    }

    default <R> MutableListX<T> filterToMutableListBy(Function<? super T, ? extends R> function, Predicate<R> predicate) {
        return IterableXHelper.filterToMutableListBy(this, function, predicate, It.noFilter());
    }

    default <R> MutableListX<T> filterNotNullToMutableListBy(Function<? super T, ? extends R> function, Predicate<R> predicate) {
       return IterableXHelper.filterToMutableListBy(this, function, predicate, Objects::nonNull);
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
        return filterToMutableSet(predicate);
    }

    default MutableListX<T> filterToMutableList(Predicate<T> predicate) {
        return filterToCollection(MutableListX::empty, predicate);
    }

    default <R> MutableListX<R> castToMutableListIfInstanceOf(Class<R> aClass) {
        return castToCollectionIfInstanceOf(MutableListX::of, aClass);
    }

    default <R> IterableX<R> castIfInstanceOf(Class<R> aClass) {
        return castToMutableListIfInstanceOf(aClass);
    }

    default MutableSetX<T> filterToMutableSet(Predicate<T> predicate) {
        return filterToCollection(MutableSetX::empty, predicate);
    }

    default <C extends Collection<T>> C filterToCollection(Supplier<C> collectionFactory, Predicate<T> predicate) {
        return mapFilteringToCollection(collectionFactory, predicate, It::self, It.noFilter());
    }

    default <R> IterableX<R> filterMapping(Predicate<T> predicate, Function<T, R> mapper) {
        return mapFiltering(predicate, mapper, It.noFilter());
    }

    default <R> IterableX<R> mapFiltering(Function<? super T, ? extends R> mapper, Predicate<R> resultFilter) {
        return mapFiltering(It.noFilter(), mapper, resultFilter);
    }

    default <R> IterableX<R> mapFiltering(Predicate<T> predicate, Function<? super T, ? extends R> mapper, Predicate<R> resultFilter) {
        return IterableX.of(mapFilteringToCollection(MutableListX::empty, predicate, mapper, resultFilter));
    }

    default <R, C extends Collection<R>> C mapFilteringToCollection(
            Supplier<C> collectionFactory, Predicate<T> predicate, Function<? super T, ? extends R> mapper, Predicate<R> resultFilter) {
        C collection = collectionFactory.get();
        for (T t : this) {
            if (t != null && predicate.test(t)) {
                final R r = mapper.apply(t);
                if (resultFilter.test(r)) {
                    collection.add(r);
                }
            }
        }
        return collection;
    }

    default <R, C extends Collection<R>> C castToCollectionIfInstanceOf(Supplier<C> collectionFactory, Class<R> aClass) {
        return mapFilteringToCollection(collectionFactory, aClass::isInstance, aClass::cast, It.noFilter());
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
        return filterToMutableList(predicate.negate());
    }

    default SetX<T> toSetXSkipping(Predicate<T> predicate) {
        return filterToMutableSet(predicate.negate());
    }

    default <R, I extends Iterable<R>> IterableX<R> flatMap(@NotNull Function<T, I> mapper) {
        return IterableX.of(flatMapToMutableListOf(mapper));
    }

    default <R, I extends Iterable<R>> ListX<R> flatMapToListXOf(Function<T, I> mapper) {
        return flatMapToMutableListOf(mapper);
    }

    default <R, I extends Iterable<R>> MutableListX<R> flatMapToMutableListOf(@NotNull Function<T, I> mapper) {
        final MutableListX<R> list = MutableListX.empty();
        for (T t : this) {
            final I c = mapper.apply(t);
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

    default <R, I extends Iterable<R>> MutableSetX<R> flatMapToMutableSetOf(Function<T, I> mapper) {
        return flatMapToCollectionOf(mapper, MutableSetX::empty);
    }

    default <R, I extends Iterable<R>> SetX<R> flatMapToSetXOf(Function<T, I> mapper) {
        return flatMapToMutableSetOf(mapper);
    }

    default <R, C extends Collection<R>, I extends Iterable<R>> C flatMapToCollectionOf(
            Function<T, I> mapper, Supplier<C> collectionFactory) {
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
        final MutableListX<T> list = toMutableListOf(It::self);
        list.sort(Comparator.comparing(selector));
        return list;
    }

    default <R extends Comparable<R>> MutableListX<T> toListSortedDescendingBy(@NotNull Function<T, R> selector) {
        final MutableListX<T> list = toMutableListOf(It::self);
        list.sort(Comparator.comparing(selector).reversed());
        return list;
    }

    default <R extends Comparable<R>> MutableListX<R> toSortedMutableListOf(@NotNull Function<T, R> selector) {
        final MutableListX<R> list = toMutableListOf(selector);
        list.sort(Comparator.naturalOrder());
        return list;
    }

    default <R extends Comparable<R>> MutableListX<R> toDescendingSortedMutableListOf(@NotNull Function<T, R> selector) {
        final MutableListX<R> list = toMutableListOf(selector);
        final Comparator<R> tComparator = Comparator.naturalOrder();
        list.sort(tComparator.reversed());
        return list;
    }

    default <R extends Comparable<R>> NavigableSetX<T> toSetSortedBy(Function<? super T, ? extends R> selector) {
        NavigableSetX<T> navigableSetX = NavigableSetX.comparingBy(selector);
        final MutableListX<T> c = filterNotNullToMutableListBy(selector, Objects::nonNull);
        navigableSetX.addAll(c);
        return navigableSetX;
    }

    default <R extends Comparable<R>> NavigableSetX<R> toNavigableSetOf(Function<? super T, ? extends R> selector) {
        return NavigableSetX.of(toMutableListNotNullOf(selector), It::self);
    }

    default <R> R[] toArrayOf(@NotNull Function<T, R> mapper, @NotNull IntFunction<R[]> generator) {
        return toMutableListOf(mapper).toArray(generator.apply(0));
    }

    default int[] toIntArrayOf(@NotNull ToIntFunction<T> mapper) {
        int counter = 0;
        int[] array = new int[countNotNullBy(It.noFilter())];
        for (T value : this) {
            if (value != null) {
                final int anInt = mapper.applyAsInt(value);
                array[counter] = anInt;
                counter++;
            }
        }
        return array;
    }

    default long[] toLongArray(@NotNull ToLongFunction<T> mapper) {
        int counter = 0;
        long[] array = new long[countNotNullBy(It.noFilter())];
        for (T value : this) {
            if (value != null) {
                final long t = mapper.applyAsLong(value);
                array[counter] = t;
                counter++;
            }
        }
        return array;
    }

    default double[] toDoubleArray(@NotNull ToDoubleFunction<T> mapper) {
        int counter = 0;
        double[] array = new double[countNotNullBy(It.noFilter())];
        for (T value : this) {
            if (value != null) {
                final double t = mapper.applyAsDouble(value);
                array[counter] = t;
                counter++;
            }
        }
        return array;
    }

    default IntSummaryStatistics statsOf(ToIntFunction<T> mapper) {
        return collect(IntSummaryStatistics::new,
                Objects::nonNull,
                mapper::applyAsInt,
                IntSummaryStatistics::accept);
    }

    default LongSummaryStatistics statsOf(ToLongFunction<T> mapper) {
        return collect(LongSummaryStatistics::new,
                Objects::nonNull,
                mapper::applyAsLong,
                LongSummaryStatistics::accept);
    }

    default DoubleStatistics statsOf(ToDoubleFunction<T> mapper) {
        return collect(DoubleStatistics::new,
                Objects::nonNull,
                mapper::applyAsDouble,
                DoubleStatistics::accept);
    }

    default BigDecimalStatistics statsOf(Function<T, BigDecimal> mapper) {
        return collect(BigDecimalStatistics::new,
                Objects::nonNull,
                mapper,
                Objects::nonNull,
                BigDecimalStatistics::accept,
                It::self);
    }

    default <K, V> MutableMapX<K, V> toMutableMap(
            @NotNull Function<? super T, ? extends K> keyMapper, @NotNull Function<? super T, ? extends V> valueMapper) {
        MutableMapX<K, V> map = MutableMapX.empty();
        for (T t : this) {
            if (t != null) {
                final K key = keyMapper.apply(t);
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
        return toMutableMap(keyMapper, valueMapper);
    }

    default MutableListX<T> getListOrElseCompute() {
        final Iterable<T> iterable = iterable();
        return iterable instanceof List ? MutableListX.of((List<T>) iterable) : MutableListX.of(this);
    }

    default MutableSetX<T> geSetOrElseCompute() {
        final Iterable<T> iterable = iterable();
        return iterable instanceof Set ? MutableSetX.of((Set<T>) iterable) : MutableSetX.of(this);
    }

    default MutableListX<T> getListOrElseThrow() {
        final Iterable<T> iterable = iterable();
        if (iterable instanceof List) {
            return MutableListX.of((List<T>) iterable);
        }
        throw new IllegalArgumentException(iterable.getClass().getSimpleName() + " is not an instance of List");
    }

    default MutableSetX<T> getSetOrElseThrow() {
        final Iterable<T> iterable = iterable();
        if (iterable instanceof Set) {
            return MutableSetX.of(((Set<T>) iterable));
        }
        throw new IllegalArgumentException(iterable.getClass().getSimpleName() + " is not an instance of Set");
    }

    default NavigableSetX<T> getNavigableSetOrElseThrow() {
        final Iterable<T> iterable = iterable();
        if (iterable instanceof NavigableSet) {
            return NavigableSetX.of((NavigableSet<T>) iterable);
        }
        throw new IllegalArgumentException(iterable.getClass().getSimpleName() + " is not an instance of NavigableSet");
    }

    default <K> MutableMapX<K, T> associateBy(@NotNull Function<T, K> keyMapper) {
        return toMutableMap(keyMapper, It::self);
    }

    default <K extends Comparable<K>> NavigableMapX<K, T> toNavigableMapAssociatedBy(
            @NotNull Function<? super T, ? extends K> keyMapper) {
        return NavigableMapX.ofMap(toMutableMap(keyMapper, It::self), It::self);
    }

    default <V> MutableMapX<T, V> associateWith(@NotNull Function<T, V> valueMapper) {
        return toMutableMap(It::self, valueMapper);
    }

    default <K extends Comparable<K>, V> NavigableMapX<K, V> toNavigableMapAssociatedWith(
            @NotNull Function<T, V> valueMapper) {
        Function<T, K> keyMapper = IterableXHelper::asComparableOrThrow;
        final MutableMapX<K, V> entries = toMutableMap(keyMapper, valueMapper);
        return NavigableMapX.of(entries, It::self);
    }

    default <K> MapX<K, T> toMapXAssociatedBy(@NotNull Function<T, K> keyMapper) {
        return toMutableMap(keyMapper, It::self);
    }

    default <V> MapX<T, V> toMapXAssociatedWith(@NotNull Function<T, V> valueMapper) {
        return toMutableMap(It::self, valueMapper);
    }

    default <R extends Comparable<R>> IterableX<T> sortedBy(@NotNull Function<T, R> selector) {
        return IterableX.of(toListSortedBy(selector));
    }

    default <R extends Comparable<R>> IterableX<T> sorted() {
        return sortedBy((Function<T, R>) IterableXHelper::asComparableOrThrow);
    }

    default Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    default IterableX<Integer> indices() {
        return IterableX.of(() -> IterableXHelper.indexIterator(iterator()));
    }

    default IterableX<IndexedValue<T>> withIndex() {
        return IterableX.of(this::indexedIterator);
    }

    default IterableX<T> distinct() {
        return IterableX.of(mapTo(MutableLinkedSetX::empty, It::self));
    }

    default <R> ListX<T> distinctToListBy(Function<T, R> selector) {
        return distinctToMutableListBy(selector);
    }

    default <R> MutableListX<T> distinctToMutableListBy(Function<T, R> selector) {
        MutableListX<T> result = MutableListX.empty();
        MutableSetX<R> set = MutableLinkedSetX.empty();
        for (T t : this) {
            if (t != null) {
                final R r = selector.apply(t);
                if (set.add(r)) {
                    result.add(t);
                }
            }
        }
        return result;
    }

    default <R> IterableX<T> distinctBy(Function<T, R> selector) {
        return distinctToMutableListBy(selector);
    }

    default int countNotNullBy(@NotNull Predicate<T> predicate) {
        int counter = 0;
        for (T t : this) {
            if (t != null && predicate.test(t)) {
                counter++;
            }
        }
        return counter;
    }

    default int count(@NotNull Predicate<T> predicate) {
        int counter = 0;
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
                final R r = mapper.apply(t);
                if (r != null) {
                    counter++;
                }
            }
        }
        return counter;
    }

    default int sumOfInts(@NotNull ToIntFunction<T> selector) {
        int sum = 0;
        for (T t : this) {
            if (t != null) {
                final int value = selector.applyAsInt(t);
                sum += value;
            }
        }
        return sum;
    }

    default long sumOfLongs(@NotNull ToLongFunction<T> selector) {
        long sum = 0;
        for (T t : this) {
            if (t != null) {
                final long value = selector.applyAsLong(t);
                sum += value;
            }
        }
        return sum;
    }

    default double sumOf(@NotNull ToDoubleFunction<T> selector) {
        double sum = 0;
        for (T t : this) {
            if (t != null) {
                final double value = selector.applyAsDouble(t);
                sum += value;
            }
        }
        return sum;
    }

    default BigDecimal sumOf(@NotNull Function<T, BigDecimal> selector) {
        BigDecimal sum = BigDecimal.ZERO;
        for (T t : this) {
            if (t != null) {
                final BigDecimal value = selector.apply(t);
                if (value != null) {
                    sum = sum.add(value);
                }
            }
        }
        return sum;
    }

    default double averageOfInts(@NotNull ToIntFunction<T> selector) {
        double sum = 0;
        int counter = 0;
        for (T t : this) {
            if (t != null) {
                final int value = selector.applyAsInt(t);
                sum += value;
                counter++;
            }
        }
        return (counter != 0) ? (sum / counter) : 0;
    }

    default double averageOfLongs(@NotNull ToLongFunction<T> selector) {
        double sum = 0;
        int counter = 0;
        for (T t : this) {
            if (t != null) {
                final long value = selector.applyAsLong(t);
                sum += value;
                counter++;
            }
        }
        return (counter != 0) ? (sum / counter) : 0;
    }

    default double averageOf(@NotNull ToDoubleFunction<T> selector) {
        double sum = 0;
        int counter = 0;
        for (T t : this) {
            if (t != null) {
                final double value = selector.applyAsDouble(t);
                sum += value;
                counter++;
            }
        }
        return (counter != 0) ? (sum / counter) : 0;
    }

    default BigDecimal averageOf(@NotNull Function<T, BigDecimal> selector) {
        return averageOf(selector, 2, RoundingMode.HALF_UP);
    }

    default BigDecimal averageOf(@NotNull Function<T, BigDecimal> selector, int scale, @NotNull RoundingMode roundingMode) {
        BigDecimal sum = BigDecimal.ZERO;
        int counter = 0;
        for (T t : this) {
            if (t != null) {
                final BigDecimal value = selector.apply(t);
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
        return IterableXHelper.compareBy(iterator(),
                selector, (first, second) -> first != null && second != null && first.compareTo(second) > 0);
    }

    @NotNull
    default <R extends Comparable<R>> Optional<T> maxBy(@NotNull Function<T, R> selector) {
        return IterableXHelper.compareBy(iterator(),
                selector, (first, second) -> first != null && second != null && first.compareTo(second) < 0);
    }

    @NotNull
    default <R extends Comparable<R>> R minOf(@NotNull Function<T, R> selector) {
        return IterableXHelper
                .comparisonOf(iterator(), selector, (first, second) -> first != null && second != null && first.compareTo(second) > 0);
    }

    @NotNull
    default <R extends Comparable<R>> R maxOf(@NotNull Function<T, R> selector) {
        return IterableXHelper
                .comparisonOf(iterator(), selector, (first, second) -> first != null && second != null && first.compareTo(second) < 0);
    }

    @NotNull
    default IterableX<T> onEach(@NotNull Consumer<? super T> consumer) {
        return onEachOf(It::self, consumer);
    }

    @NotNull
    default <R> IterableX<T> onEachOf(@NotNull Function<? super T, ? extends R> selector, @NotNull Consumer<? super R> consumer) {
        for (T t : this) {
            consumer.accept(t != null ? selector.apply(t) : null);
        }
        return this;
    }

    default <R> R fold(@NotNull R initial, @NotNull BiFunction<R, T, R> operation) {
        R accumulator = initial;
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
        T accumulator = initial;
        for (T t : this) {
            if (t != null) {
                accumulator = operation.apply(accumulator, t);
            }
        }
        return accumulator;
    }

    default <R> R reduce(@NotNull R initial, @NotNull Function<T, R> mapper, @NotNull BinaryOperator<R> operation) {
        R accumulator = initial;
        for (T t : this) {
            if (t != null) {
                accumulator = operation.apply(accumulator, mapper.apply(t));
            }
        }
        return accumulator;
    }

    default Optional<T> reduce(@NotNull BinaryOperator<T> operation) {
        final Iterator<T> iterator = iterator();
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
        return collect(CollectorsX.teeing(downstream1, downStream2, merger));
    }

    default <A1, R1, A2, R2, A3, R3, R> Triple<R1, R2, R3> branching(
            @NotNull Collector<T, A1, R1> downstream1,
            @NotNull Collector<T, A2, R2> downStream2,
            @NotNull Collector<T, A3, R3> downStream3) {
        return collect(CollectorsX.branching(downstream1, downStream2, downStream3, Triple::of));
    }

    default <A1, R1, A2, R2, A3, R3, R> R branching(
            @NotNull Collector<T, A1, R1> downstream1,
            @NotNull Collector<T, A2, R2> downStream2,
            @NotNull Collector<T, A3, R3> downStream3,
            @NotNull TriFunction<R1, R2, R3, R> merger) {
        return collect(CollectorsX.branching(downstream1, downStream2, downStream3, merger));
    }

    default <A1, R1, A2, R2, A3, R3, A4, R4, R> R branching(
            @NotNull Collector<T, A1, R1> downstream1,
            @NotNull Collector<T, A2, R2> downStream2,
            @NotNull Collector<T, A3, R3> downStream3,
            @NotNull Collector<T, A4, R4> downStream4,
            @NotNull QuadFunction<R1, R2, R3, R4, R> merger) {
        return collect(CollectorsX.branching(downstream1, downStream2, downStream3, downStream4, merger));
    }

    default <A, R> R collect(@NotNull Collector<T, A, R> collector) {
        A result = collector.supplier().get();
        final BiConsumer<A, T> accumulator = collector.accumulator();
        forEach(t -> accumulator.accept(result, t));
        return collector.finisher().apply(result);
    }

    default <A, R> R collect(Supplier<A> supplier,
                             BiConsumer<A, ? super T> accumulator,
                             Function<A, R> finisher) {
        A result = supplier.get();
        return collect(supplier, It.noFilter(), It::self, It.noFilter(), accumulator, finisher);
    }

    default <A, R> A collect(Supplier<A> supplier,
                             Predicate<T> filter,
                             Function<T, R> mapper,
                             BiConsumer<A, ? super R> accumulator) {
        A result = supplier.get();
        return collect(supplier, filter, mapper, It.noFilter(), accumulator, It::self);
    }

    default <A, U, R> R collect(Supplier<A> supplier,
                                Predicate<T> filter,
                                Function<T, U> mapper,
                                Predicate<U> resultFilter,
                                BiConsumer<A, ? super U> accumulator,
                                Function<A, R> finisher) {
        A result = supplier.get();
        for (T t : this) {
            if (filter.test(t)) {
                U u = mapper.apply(t);
                if (resultFilter.test(u)) {
                    accumulator.accept(result, u);
                }
            }
        }
        return finisher.apply(result);
    }

    default MutableMapX<T, MutableListX<T>> group() {
        return groupMapping(It::self, It::self);
    }

    default <K> MutableMapX<K, MutableListX<T>> groupBy(@NotNull Function<T, K> classifier) {
        return groupMapping(classifier, It::self);
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
        return partitionMapping(predicate, It::self);
    }

    default <R> Pair<ListX<R>, ListX<R>> partitionMapping(@NotNull Predicate<T> predicate, @NotNull Function<T, R> resultMapper) {
        Pair<MutableListX<R>, MutableListX<R>> pair = Pair.of(MutableListX.empty(), MutableListX.empty());
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

    default <S, C extends Collection<S>, R> SetX<R> intersectionOf(
            @NotNull Function<T, C> toCollectionMapper, Function<S, R> selector) {
        MutableSetX<R> common = MutableSetX.empty();
        for (T t : this) {
            final C collection = toCollectionMapper.apply(t);
            final MutableListX<R> resultList = MutableListX.empty();
            for (S s : collection) {
                final R r = selector.apply(s);
                resultList.add(r);
            }
            if (common.isEmpty()) {
                common.addAll(resultList);
            } else {
                common.retainAll(resultList);
            }
        }
        return common;
    }

    default <R, C extends Collection<R>> SetX<R> intersectionOf(@NotNull Function<T, C> toCollectionMapper) {
        return intersectionOf(toCollectionMapper, It::self);
    }

    default T first() {
        return firstOf(It::self);
    }

    @NotNull
    default T first(@NotNull Predicate<T> predicate) {
        for (T next : this) {
            if (next != null && predicate.test(next)) {
                return next;
            }
        }
        throw IterableXHelper.noValuePresentException();
    }

    @NotNull
    default T firstNot(@NotNull Predicate<T> predicate) {
        return first(predicate.negate());
    }

    default <R> R firstOf(@NotNull Function<T, R> mapper) {
        for (T next : this) {
            if (next != null) {
                final R result = mapper.apply(next);
                if (result != null) {
                    return result;
                }
            }
        }
        throw IterableXHelper.noValuePresentException();
    }

    default Optional<T> findFirst() {
        return findFirstOf(It::self);
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
        return lastOf(It::self);
    }

    default <R> R lastOf(@NotNull Function<T, R> mapper) {
        final Iterator<T> iterator = iterator();
        final Iterable<T> iterable = iterable();
        if (!iterator.hasNext()) {
            throw IterableXHelper.noValuePresentException();
        } else if (iterable instanceof List) {
            return IterableXHelper.findLastIfInstanceOfList(Objects::nonNull, (List<T>) iterable).map(mapper)
                    .orElseThrow(IllegalStateException::new);
        } else {
            return IterableXHelper.findLastIfUnknownIterable(Objects::nonNull, iterator).map(mapper)
                    .orElseThrow(IllegalStateException::new);
        }
    }

    default Optional<T> findLast() {
        return findLastOf(It::self);
    }

    default Optional<T> findLast(@NotNull Predicate<T> predicate) {
        final Iterator<T> iterator = iterator();
        final Iterable<T> iterable = iterable();
        if (!iterator.hasNext()) {
            throw IterableXHelper.noValuePresentException();
        } else if (iterable instanceof List) {
            return IterableXHelper.findLastIfInstanceOfList(predicate, ((List<T>) iterable));
        } else {
            return IterableXHelper.findLastIfUnknownIterable(predicate, iterator);
        }
    }

    default <R> Optional<R> findLastOf(@NotNull Function<T, R> mapper) {
        final Iterator<T> iterator = iterator();
        final Iterable<T> iterable = iterable();
        if (!iterator.hasNext()) {
            return Optional.empty();
        } else if (iterable instanceof List) {
            List<T> list = (List<T>) iterable;
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
        for (T element : this) {
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
        final Iterator<T> iterator = iterator();
        if (!iterator.hasNext()) {
            return MutableListX.empty();
        }
        final MutableListX<R> list = MutableListX.empty();
        T current = iterator.next();
        while (iterator.hasNext()) {
            final T next = iterator.next();
            list.add(function.apply(current, next));
            current = next;
        }
        return list;
    }

    default <R> ListX<R> zipWithNextToListXOf(BiFunction<T, T, R> function) {
        return zipWithNextToMutableListOf(function);
    }

    default <R> List<R> zipWithNextToListOf(BiFunction<T, T, R> function) {
        return zipWithNextToMutableListOf(function);
    }

    default <R> IterableX<R> zipWithNext(BiFunction<T, T, R> function) {
        return zipWithNextToMutableListOf(function);
    }

    default <R> MutableListX<R> zipWithNext2ToMutableListOf(TriFunction<T, T, T, R> function) {
        final Iterator<T> iterator = iterator();
        if (!iterator.hasNext()) {
            return MutableListX.empty();
        }
        final MutableListX<R> list = MutableListX.empty();
        T current = iterator.next();
        if (iterator.hasNext()) {
            T next = iterator.next();
            while (iterator.hasNext()) {
                final T secondNext = iterator.next();
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
        final Iterator<A> otherIterator = otherIterable.iterator();
        final Iterator<T> iterator = iterator();
        final int resultListSize = Math.min(IterableXHelper.collectionSizeOrElse(this,10),
                IterableXHelper.collectionSizeOrElse(IterableX.of(otherIterable), 10));

        final MutableListX<R> list = MutableListX.withInitCapacity(resultListSize);
        while (iterator.hasNext() && otherIterator.hasNext()) {
            final T next = iterator.next();
            final A otherNext = otherIterator.next();
            list.add(function.apply(next, otherNext));
        }
        return list;
    }

    default <A, R> ListX<R> zipToListXWith(Iterable<A> otherIterable, BiFunction<T, A, R> function) {
        return zipToMutableListWith(otherIterable, function);
    }

    default <A, R> List<R> zipToListWith(Iterable<A> otherIterable, BiFunction<T, A, R> function) {
        return zipToMutableListWith(otherIterable, function);
    }

    default SetX<T> union(Iterable<T> other) {
        MutableSetX<T> union = MutableSetX.empty();
        forEach(union::add);
        other.forEach(union::add);
        return union;
    }

    default <R> SetX<R> union(Iterable<T> other, Function<T, R> mapper) {
        MutableSetX<R> union = toMutableSetOf(mapper);
        final SetX<R> setX = of(other).toSetXOf(mapper);
        setX.forEach(union::add);
        return union;
    }

    default <A, R> IterableX<R> zipWith(Iterable<A> iterable, BiFunction<T, A, R> function) {
        return IterableX.of(zipToMutableListWith(iterable, function));
    }

    default IterableX<T> takeWhileInclusive(Predicate<T> predicate) {
        return IterableX.of(takeToMutableListWhileInclusive(predicate));
    }

    default MutableListX<T> takeToMutableListWhileInclusive(Predicate<T> predicate) {
        MutableListX<T> list = MutableListX.empty();
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
        final MutableListX<T> list = MutableListX.empty();
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
        return IterableXHelper.dropToMutableListWhile(this, predicate, true);
    }

    default IterableX<T> dropWhile(Predicate<T> predicate) {
        return IterableX.of(dropToListXWhile(predicate));
    }

    default ListX<T> dropToListXWhile(Predicate<T> predicate) {
        return  IterableXHelper.dropToMutableListWhile(this, predicate, false);
    }

    default IterableX<T> dropLastWhile(Predicate<T> predicate) {
        return IterableX.of(dropLastToMutableListWhile(predicate));
    }

    default MutableListX<T> dropLastToMutableListWhile(Predicate<T> predicate) {
        MutableListX<T> list = getListOrElseCompute();
        if (list.isEmpty()) {
            return MutableListX.empty();
        }
        ListIterator<T> iterator = list.listIterator(list.size());
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
        MutableListX<T> input = getListOrElseCompute();
        if (input.isEmpty()) {
            return MutableListX.empty();
        }
        ListIterator<T> iterator = input.listIterator(input.size());
        while (iterator.hasPrevious()) {
            if (!predicate.test(iterator.previous())) {
                iterator.next();
                int expectedSize = input.size() - iterator.nextIndex();
                if (expectedSize == 0) {
                    return MutableListX.empty();
                }
                MutableListX<T> result = MutableListX.withInitCapacity(expectedSize);
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
        final Iterable<T> iterable = iterable();
        if (iterable instanceof Collection) {
            Collection<T> c = (Collection<T>) iterable;
            if (n >= c.size()) {
                return MutableListX.of(c);
            }
            if (n == 1) {
                return MutableListX.of(first());
            }
        }
        int count = 0;
        MutableListX<T> list = MutableListX.empty();
        for (T t : this) {
            list.add(t);
            if (++count == n) {
                break;
            }
        }
        return list;
    }

    default IterableX<T> skip(int count) {
        return IterableX.of(skipToListX(count));
    }

    default ListX<T> skipToListX(int n) {
       return filterIndexedToMutableList((i, t) -> i >= n);
    }

    default IterableX<T> limit(int bound) {
        return IterableX.of(limitToListX(bound));
    }

    default ListX<T> limitToListX(int bound) {
        return filterIndexedToMutableList((i, t) -> i < bound);
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
        final StringBuilder sb = new StringBuilder();
        final Iterator<T> iterator = iterator();
        while (iterator.hasNext()) {
            final R r = selector.apply(iterator.next());
            sb.append(r).append(iterator.hasNext() ? delimiter : "");
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
        return new Iterator<IndexedValue<T>>() {
            private int index = 0;
            private final Iterator<T> iterator = iterator();
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
            @Override
            public IndexedValue<T> next() {
                int prevIndex = index;
                if (prevIndex < 0) {
                    throw new IllegalStateException("indexed iterator index overflow");
                }
                return new IndexedValue<>(index++, iterator.next());
            }
        };
    }
}
