package hzt.collections;

import hzt.stream.collectors.BigDecimalCollectors;
import hzt.stream.collectors.BigDecimalSummaryStatistics;
import hzt.stream.function.TriFunction;
import hzt.utils.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
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

/**
 * This is a convenience class to transform smaller iterables including collections or arrays to some other state.
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
@SuppressWarnings({"ClassCanBeRecord", "unused"})
public final class IterX<T> implements Iterable<T>, IndexedIterable<T>, Comparable<IterX<T>>  {

    private final Iterable<T> iterable;

    private IterX(@NotNull Iterable<T> iterable) {
        this.iterable = iterable;
    }

    public static <T> IterX<T> of(Iterable<T> iterable) {
        return new IterX<>(iterable);
    }

    @SafeVarargs
    public static <T> IterX<T> of(T... values) {
        return IterX.of(List.of(values));
    }

    public static IterX<Integer> ofInts(int... values) {
        List<Integer> valueList = new ArrayList<>();
        for (var value : values) {
            valueList.add(value);
        }
        return IterX.of(valueList);
    }

    public static IterX<Long> ofLongs(long... values) {
        List<Long> valueList = new ArrayList<>();
        for (var value : values) {
            valueList.add(value);
        }
        return IterX.of(valueList);
    }

    public static IterX<Double> ofDoubles(double... values) {
        List<Double> valueList = new ArrayList<>();
        for (var value : values) {
            valueList.add(value);
        }
        return IterX.of(valueList);
    }

    public static IterX<Integer> range(int start, int end, IntPredicate predicate) {
        List<Integer> valueList = new ArrayList<>();
        for (int i = start; i < end; i++) {
            if (predicate.test(i)) {
                valueList.add(i);
            }
        }
        return IterX.of(valueList);
    }

    public static IterX<Integer> range(int start, int end) {
        return range(start, end, i -> true);
    }

    public static IterX<Integer> rangeClosed(int start, int endInclusive, IntPredicate predicate) {
        List<Integer> valueList = new ArrayList<>();
        for (int i = start; i <= endInclusive; i++) {
            if (predicate.test(i)) {
                valueList.add(i);
            }
        }
        return IterX.of(valueList);
    }

    public static IterX<Integer> rangeClosed(int start, int endInclusive) {
        return rangeClosed(start, endInclusive, i -> true);
    }

    @SafeVarargs
    public final IterX<T> plus(T first, T... others) {
        return IterX.of(toMutableListPlus(first, others));
    }

    @SafeVarargs
    public final List<T> toListPlus(T first, T... others) {
        return List.copyOf(toMutableListPlus(first, others));
    }

    @SafeVarargs
    public final Set<T> toSetPlus(T first, T... others) {
        return Set.copyOf(toMutableListPlus(first, others));
    }

    @SafeVarargs
    public final List<T> toMutableListPlus(T first, T... others) {
        List<T> list = new ArrayList<>();
        for (T t : this) {
            list.add(t);
        }
        list.add(first);
        if (others.length > 0) {
            list.addAll(Arrays.asList(others));
        }
        return list;
    }

    public IterX<T> plus(Iterable<T> values) {
        return IterX.of(toMutableListPlus(values));
    }

    public List<T> toListPlus(Iterable<T> values) {
        return List.copyOf(toMutableListPlus(values));
    }

    public Set<T> toSetPlus(Iterable<T> values) {
        return Set.copyOf(toMutableListPlus(values));
    }

    public List<T> toMutableListPlus(Iterable<T> values) {
        List<T> list = new ArrayList<>();
        for (T t : this) {
            list.add(t);
        }
        for (T t : values) {
            list.add(t);
        }
        return list;
    }

    public static <T> IterX<T> empty() {
        return IterX.of(List.of());
    }

    public <R> IterX<R> map(Function<T, R> mapper) {
        return IterX.of(mapTo(ArrayList::new, mapper));
    }

    public <R> List<R> mapIndexedToMutableList(BiFunction<Integer, T, R> mapper) {
        return withIndex().mapTo(ArrayList::new, indexedValue -> mapper.apply(indexedValue.index(), indexedValue.value()));
    }

    public <R> List<R> mapIndexedToList(BiFunction<Integer, T, R> mapper) {
        return List.copyOf(mapIndexedToMutableList(mapper));
    }

    public <R> IterX<R> mapIndexed(BiFunction<Integer, T, R> mapper) {
        return IterX.of(mapIndexedToMutableList(mapper));
    }

    public <R> List<R> mapMultiToListOf(BiConsumer<? super T, ? super Consumer<R>> mapper) {
        return stream().mapMulti(mapper).toList();
    }

    public <R> IterX<R> mapMulti(BiConsumer<? super T, ? super Consumer<R>> mapper) {
        return IterX.of(mapMultiToListOf(mapper));
    }

    public <R> IterX<R> mapNotNull(Function<T, R> mapper) {
        return IterX.of(toCollectionNotNullOf(mapper, ArrayList::new));
    }

    public <R> List<R> toListOf(Function<T, R> transform) {
        return List.copyOf(toMutableListNotNullOf(transform));
    }

    public <R> Set<R> toSetOf(Function<T, R> transform) {
        return Set.copyOf(toMutableSetNotNullOf(transform));
    }

    public <R> List<R> toMutableListOf(Function<T, R> transform) {
        return mapTo(ArrayList::new, transform);
    }

    public <R> List<R> toMutableListNotNullOf(Function<T, R> transform) {
        return toCollectionNotNullOf(transform, ArrayList::new);
    }

    public <R> Set<R> toMutableSetOf(Function<T, R> transform) {
        return mapTo(HashSet::new, transform);
    }

    public <R> Set<R> toMutableSetNotNullOf(Function<T, R> transform) {
        return toCollectionNotNullOf(transform, HashSet::new);
    }

    public <R, C extends Collection<R>> C mapTo(Supplier<C> collectionFactory, Function<T, R> mapper) {
        C collection = collectionFactory.get();
        for (T t : iterable) {
            if (t != null) {
                collection.add(mapper.apply(t));
            }
        }
        return collection;
    }

    public <R, C extends Collection<R>> C toCollectionNotNullOf(Function<T, R> mapper, Supplier<C> collectionFactory) {
        C collection = collectionFactory.get();
        for (T t : iterable) {
            if (t != null) {
                final var r = mapper.apply(t);
                if (r != null) {
                    collection.add(r);
                }
            }
        }
        return collection;
    }

    public IterX<T> filter(Predicate<T> predicate) {
        return IterX.of(filterToCollection(predicate, new ArrayList<>()));
    }

    public List<T> filterIndexedToList(BiPredicate<Integer, T> predicate) {
        return filterIndexedToCollection(ArrayList::new, predicate);
    }

    public IterX<T> filterIndexed(BiPredicate<Integer, T> predicate) {
        return IterX.of(filterIndexedToList(predicate));
    }

    public List<T> filterToList(Predicate<T> predicate) {
        return List.copyOf(filterToMutableList(predicate));
    }

    public Set<T> filterToSet(Predicate<T> predicate) {
        return Set.copyOf(filterToMutableSet(predicate));
    }

    public List<T> filterToMutableList(Predicate<T> predicate) {
        return filterToCollection(predicate, new ArrayList<>());
    }

    public Set<T> filterToMutableSet(Predicate<T> predicate) {
        return filterToCollection(predicate, new HashSet<>());
    }

    public <C extends Collection<T>> C filterToCollection(Predicate<T> predicate, C collection) {
        for (T t : iterable) {
            if (t != null && predicate.test(t)) {
                collection.add(t);
            }
        }
        return collection;
    }

    public <C extends Collection<T>> C filterIndexedToCollection(Supplier<C> collectionFactory, BiPredicate<Integer, T> predicate) {
        C collection = collectionFactory.get();
        for (IndexedValue<T> item : IterX.of(iterable).withIndex()) {
            if (item != null && predicate.test(item.index(), item.value())) {
                collection.add(item.value());
            }
        }
        return collection;
    }

    public IterX<T> filterNot(Predicate<T> predicate) {
        return filter(predicate.negate());
    }

    public List<T> toListSkipping(Predicate<T> predicate) {
        return List.copyOf(filterToMutableList(predicate.negate()));
    }

    public Set<T> toSetSkipping(Predicate<T> predicate) {
        return Set.copyOf(filterToMutableSet(predicate.negate()));
    }

    public <R, C extends Collection<R>> IterX<R> flatMap(@NotNull Function<T, C> mapper) {
        return IterX.of(flatMapToMutableListOf(mapper));
    }

    public <R, C extends Collection<R>> List<R> flatMapToListOf(Function<T, C> mapper) {
        return List.copyOf(flatMapToMutableListOf(mapper));
    }

    public <R, C extends Collection<R>> List<R> flatMapToMutableListOf(@NotNull Function<T, C> mapper) {
        final List<R> list = new ArrayList<>();
        for (T t : iterable) {
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

    public <R, C extends Collection<R>> Set<R> flatMapToMutableSetOf(Function<T, C> mapper) {
        //noinspection unchecked
        return (Set<R>) flatMapToCollectionOf(mapper, () -> (C) new HashSet<R>());
    }

    public <R, C extends Collection<R>> Set<R> flatMapToSetOf(Function<T, C> mapper) {
        return Set.copyOf(flatMapToMutableSetOf(mapper));
    }

    public <R, C extends Collection<R>> C flatMapToCollectionOf(Function<T, C> mapper, Supplier<C> collectionFactory) {
        C collection = collectionFactory.get();
        for (T t : iterable) {
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

    public <R extends Comparable<R>> List<T> toListSortedBy(@NotNull Function<T, R> selector) {
        final var list = toMutableListOf(Function.identity());
        list.sort(Comparator.comparing(selector));
        return list;
    }

    public <R extends Comparable<R>> List<T> toListSortedDescendingBy(@NotNull Function<T, R> selector) {
        final var list = toMutableListOf(Function.identity());
        list.sort(Comparator.comparing(selector).reversed());
        return list;
    }

    public <R extends Comparable<R>> List<R> toSortedListOf(@NotNull Function<T, R> selector) {
        final var list = toMutableListOf(selector);
        list.sort(Comparator.naturalOrder());
        return list;
    }

    public <R extends Comparable<R>> List<R> toDescendingSortedListOf(@NotNull Function<T, R> selector) {
        final var list = toMutableListOf(selector);
        final Comparator<R> tComparator = Comparator.naturalOrder();
        list.sort(tComparator.reversed());
        return list;
    }

    public <R extends Comparable<R>> SortedSet<T> toSetSortedBy(Function<T, R> selector) {
        final SortedSet<T> sortedSet = new TreeSet<>(Comparator.comparing(selector));
        sortedSet.addAll(mapTo(HashSet::new, Function.identity()));
        return sortedSet;
    }

    public <R extends Comparable<R>> SortedSet<R> toSortedSetOf(Function<T, R> selector) {
        return new TreeSet<>(toMutableListOf(selector));
    }

    public <R> R[] mapToArray(@NotNull Function<T, R> mapper, @NotNull IntFunction<R[]> generator) {
        return toMutableListOf(mapper).toArray(generator);
    }

    public int[] toIntArray(@NotNull ToIntFunction<T> mapper) {
        int counter = 0;
        int[] array = new int[nonNullCount(t -> true)];
        for (T value : iterable) {
            if (value != null) {
                final var anInt = mapper.applyAsInt(value);
                array[counter] = anInt;
                counter++;
            }
        }
        return array;
    }

    public long[] toLongArray(@NotNull ToLongFunction<T> mapper) {
        int counter = 0;
        long[] array = new long[nonNullCount(t -> true)];
        for (T value : iterable) {
            if (value != null) {
                final var t = mapper.applyAsLong(value);
                array[counter] = t;
                counter++;
            }
        }
        return array;
    }

    public double[] toDoubleArray(@NotNull ToDoubleFunction<T> mapper) {
        int counter = 0;
        double[] array = new double[nonNullCount(t -> true)];
        for (T value : iterable) {
            if (value != null) {
                final var t = mapper.applyAsDouble(value);
                array[counter] = t;
                counter++;
            }
        }
        return array;
    }

    public IntSummaryStatistics statsOf(ToIntFunction<T> mapper) {
        return stream().mapToInt(mapper).summaryStatistics();
    }

    public LongSummaryStatistics statsOf(ToLongFunction<T> mapper) {
        return stream().mapToLong(mapper).summaryStatistics();
    }

    public DoubleSummaryStatistics statsOf(ToDoubleFunction<T> mapper) {
        return stream().mapToDouble(mapper).summaryStatistics();
    }

    public BigDecimalSummaryStatistics statsOf(Function<T, BigDecimal> mapper) {
        return stream()
                .filter(Objects::nonNull)
                .map(mapper)
                .filter(Objects::nonNull)
                .collect(BigDecimalCollectors.summarizingBigDecimal());
    }

    public <K, V> MapX<K, V> toMutableMap(@NotNull Function<T, K> keyMapper, @NotNull Function<T, V> valueMapper) {
        Map<K, V> map = new LinkedHashMap<>();
        for (T t : iterable) {
            if (t != null) {
                final var key = keyMapper.apply(t);
                if (key != null) {
                    map.put(key, valueMapper.apply(t));
                }
            }
        }
        return MapX.of(map);
    }

    public <K, V> MapX<K, V> toMap(@NotNull Function<T, K> keyMapper, @NotNull Function<T, V> valueMapper) {
        return MapX.of(toMutableMap(keyMapper, valueMapper));
    }

    public List<T> getListOrElseCompute() {
        return iterable instanceof List<T> list ? list : toListOf(Function.identity());
    }

    public Set<T> getSetOrElseCompute() {
        return iterable instanceof Set<T> set ? set : toSetOf(Function.identity());
    }

    public List<T> getListOrElseThrow() {
        if (iterable instanceof List<T> list) {
            return list;
        }
        throw new IllegalArgumentException(iterable.getClass().getSimpleName() + " is not an instance of List");
    }

    public Set<T> getSetOrElseThrow() {
        if (iterable instanceof Set<T> set) {
            return set;
        }
        throw new IllegalArgumentException(iterable.getClass().getSimpleName() + " is not an instance of Set");
    }

    public <K> MapX<K, T> associateBy(@NotNull Function<T, K> keyMapper) {
        return MapX.of(toMutableMap(keyMapper, Function.identity()));
    }

    public <V> MapX<T, V> associateWith(@NotNull Function<T, V> valueMapper) {
        return MapX.of(toMutableMap(Function.identity(), valueMapper));
    }

    public <R extends Comparable<R>> IterX<T> sortedBy(@NotNull Function<T, R> selector) {
        return IterX.of(toListSortedBy(selector));
    }

    public IterX<T> sorted() {
        return IterX.of(stream().sorted().toList());
    }

    public Stream<T> stream() {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    public IterX<Integer> indices() {
        return IterX.of(indexIterable(this::iterator));
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

    public IterX<IndexedValue<T>> withIndex() {
        return IterX.of(indexingIterable(this::iterator));
    }

    private Iterable<IndexedValue<T>> indexingIterable(Supplier<Iterator<T>> iteratorFactory) {
        return this::indexedIterator;
    }

    public IterX<T> distinct() {
        return IterX.of(mapTo(LinkedHashSet::new, Function.identity()));
    }

    public <R> List<T> distinctToListBy(Function<T, R> selector) {
        List<T> result = new ArrayList<>();
        Set<R> set = new HashSet<>();
        for (T t : iterable) {
            if (t != null) {
                final var r = selector.apply(t);
                if (set.add(r)) {
                    result.add(t);
                }
            }
        }
        return result;
    }

    public <R> IterX<T> distinctBy(Function<T, R> selector) {
        return IterX.of(distinctToListBy(selector));
    }

    public int nonNullCount(@NotNull Predicate<T> predicate) {
        var counter = 0;
        for (T t : iterable) {
            if (t != null && predicate.test(t)) {
                counter++;
            }
        }
        return counter;
    }

    public int count(@NotNull Predicate<T> predicate) {
        var counter = 0;
        for (T t : iterable) {
            if (predicate.test(t)) {
                counter++;
            }
        }
        return counter;
    }

    public <R> int countNotNullOf(@NotNull Function<T, R> mapper) {
        int counter = 0;
        for (T t : iterable) {
            if (t != null) {
                final var r = mapper.apply(t);
                if (r != null) {
                    counter++;
                }
            }
        }
        return counter;
    }

    public long sumOfInts(@NotNull ToIntFunction<T> selector) {
        long sum = 0;
        for (T t : iterable) {
            if (t != null) {
                final var value = selector.applyAsInt(t);
                sum += value;
            }
        }
        return sum;
    }

    public int sumOfLongs(@NotNull ToLongFunction<T> selector) {
        var sum = 0;
        for (T t : iterable) {
            if (t != null) {
                final var value = selector.applyAsLong(t);
                sum += value;
            }
        }
        return sum;
    }

    public double sumOfDoubles(@NotNull ToDoubleFunction<T> selector) {
        double sum = 0;
        for (T t : iterable) {
            if (t != null) {
                final var value = selector.applyAsDouble(t);
                sum += value;
            }
        }
        return sum;
    }

    public BigDecimal sumOf(@NotNull Function<T, BigDecimal> selector) {
        var sum = BigDecimal.ZERO;
        for (T t : iterable) {
            if (t != null) {
                final var value = selector.apply(t);
                if (value != null) {
                    sum = sum.add(value);
                }
            }
        }
        return sum;
    }

    public double averageOf(@NotNull ToIntFunction<T> selector) {
        double sum = 0;
        var counter = 0;
        for (T t : iterable) {
            if (t != null) {
                final var value = selector.applyAsInt(t);
                sum += value;
                counter++;
            }
        }
        return (counter != 0) ? (sum / counter) : 0;
    }

    public double averageOf(@NotNull ToLongFunction<T> selector) {
        double sum = 0;
        var counter = 0;
        for (T t : iterable) {
            if (t != null) {
                final var value = selector.applyAsLong(t);
                sum += value;
                counter++;
            }
        }
        return (counter != 0) ? (sum / counter) : 0;
    }

    public double averageOf(@NotNull ToDoubleFunction<T> selector) {
        double sum = 0;
        var counter = 0;
        for (T t : iterable) {
            if (t != null) {
                final var value = selector.applyAsDouble(t);
                sum += value;
                counter++;
            }
        }
        return (counter != 0) ? (sum / counter) : 0;
    }

    public BigDecimal averageOf(@NotNull Function<T, BigDecimal> selector) {
        return averageOf(selector, 2, RoundingMode.HALF_UP);
    }

    public BigDecimal averageOf(@NotNull Function<T, BigDecimal> selector, int scale, @NotNull RoundingMode roundingMode) {
        var sum = BigDecimal.ZERO;
        var counter = 0;
        for (T t : iterable) {
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
    public <R extends Comparable<R>> Optional<T> minBy(@NotNull Function<T, R> selector) {
        return compareBy(selector, (first, second) -> first != null && second != null && first.compareTo(second) > 0);
    }

    @NotNull
    public <R extends Comparable<R>> Optional<T> maxBy(@NotNull Function<T, R> selector) {
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
    public <R extends Comparable<R>> R minOf(@NotNull Function<T, R> selector) {
        return comparisonOf(selector, (first, second) -> first != null && second != null && first.compareTo(second) > 0);
    }

    @NotNull
    public <R extends Comparable<R>> R maxOf(@NotNull Function<T, R> selector) {
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
    public IterX<T> onEach(@NotNull Consumer<T> consumer) {
        forEach(consumer);
        return this;
    }

    public <R> R fold(@NotNull R initial, @NotNull BiFunction<R, T, R> operation) {
        var accumulator = initial;
        for (T t : iterable) {
            if (t != null) {
                accumulator = operation.apply(accumulator, t);
            }
        }
        return accumulator;
    }

    public <R> R foldRight(@NotNull R initial, @NotNull BiFunction<T, R, R> operation) {
        List<T> list = iterable instanceof List<T> current ? current : toListOf(Function.identity());
        var accumulator = initial;
        if (!list.isEmpty()) {
            final var listIterator = list.listIterator();
            while (listIterator.hasPrevious()) {
                accumulator = operation.apply(listIterator.previous(), accumulator);
            }
        }
        return accumulator;
    }

    private static void requireGreaterThanZero(int n) {
        if (n < 0) {
            throw new IllegalStateException("Requested element count $n is less than zero.");
        }
    }

    public T reduce(@NotNull T initial, @NotNull BinaryOperator<T> operation) {
        var accumulator = initial;
        for (T t : iterable) {
            if (t != null) {
                accumulator = operation.apply(accumulator, t);
            }
        }
        return accumulator;
    }

    public <R> R reduce(@NotNull R initial, @NotNull Function<T, R> mapper, @NotNull BinaryOperator<R> operation) {
        var accumulator = initial;
        for (T t : iterable) {
            if (t != null) {
                accumulator = operation.apply(accumulator, mapper.apply(t));
            }
        }
        return accumulator;
    }

    public Optional<T> reduce(@NotNull BinaryOperator<T> operation) {
        final var iterator = iterator();
        return iterator.hasNext() ? Optional.of(reduce(iterator.next(), operation)) : Optional.empty();
    }

    public <A1, R1, A2, R2> Pair<R1, R2> teeing(@NotNull Collector<T, A1, R1> downstream1,
                                                @NotNull Collector<T, A2, R2> downStream2) {
        return teeing(downstream1, downStream2, Pair::new);
    }

    public <A1, R1, A2, R2, R> R teeing(
            @NotNull Collector<T, A1, R1> downstream1,
            @NotNull Collector<T, A2, R2> downStream2,
            @NotNull BiFunction<R1, R2, R> merger) {
        return collect(Collectors.teeing(downstream1, downStream2, merger));
    }

    public <A, R> R collect(@NotNull Collector<T, A, R> collector) {
        A result = collector.supplier().get();
        iterable.forEach(t -> collector.accumulator().accept(result, t));
        return collector.finisher().apply(result);
    }

    public <K> MapX<K, List<T>> groupBy(@NotNull Function<T, K> classifier) {
        return MapX.of(groupMapping(classifier, Function.identity()));
    }

    public <K, R> MapX<K, List<R>> groupMapping(@NotNull Function<T, K> classifier, @NotNull Function<T, R> valueMapper) {
        Map<K, List<R>> groupedMap = new HashMap<>();
        for (T t : iterable) {
            groupedMap.computeIfAbsent(classifier.apply(t), key -> new ArrayList<>()).add(valueMapper.apply(t));
        }
        return MapX.of(groupedMap);
    }

    public Pair<List<T>, List<T>> partition(@NotNull Predicate<T> predicate) {
        return partitionMapping(predicate, Function.identity());
    }

    public <R> Pair<List<R>, List<R>> partitionMapping(@NotNull Predicate<T> predicate, @NotNull Function<T, R> resultMapper) {
        Pair<List<R>, List<R>> pair = new Pair<>(new ArrayList<>(), new ArrayList<>());
        for (T t : iterable) {
            if (t != null) {
                R r = resultMapper.apply(t);
                if (predicate.test(t)) {
                    pair.first().add(r);
                } else {
                    pair.second().add(r);
                }
            }
        }
        return pair;
    }

    public <R, C extends Collection<R>> Set<R> intersectBy(@NotNull Function<T, C> function) {
        Collection<Collection<R>> list = new ArrayList<>();
        for (T t : iterable) {
            list.add(function.apply(t));
        }
        Set<R> common = new HashSet<>();
        final var iterator = list.iterator();
        if (iterator.hasNext()) {
            common.addAll(iterator.next());
            while (iterator.hasNext()) {
                common.retainAll(iterator.next());
            }
        }
        return common;
    }

    public T first() {
        return firstOf(Function.identity());
    }

    @NotNull
    public T first(@NotNull Predicate<T> predicate) {
        for (T next : iterable) {
            if (next != null && predicate.test(next)) {
                return next;
            }
        }
        throw noValuePresentException();
    }

    public <R> R firstOf(@NotNull Function<T, R> mapper) {
        for (T next : iterable) {
            if (next != null) {
                final var result = mapper.apply(next);
                if (result != null) {
                    return result;
                }
            }
        }
        throw noValuePresentException();
    }

    public Optional<T> findFirst() {
        return findFirstOf(Function.identity());
    }

    public Optional<T> findFirst(Predicate<T> predicate) {
        for (T next : iterable) {
            if (next != null && predicate.test(next)) {
                return Optional.of(next);
            }
        }
        return Optional.empty();
    }

    public <R> Optional<R> findFirstOf(Function<T, R> mapper) {
        for (T next : iterable) {
            if (next != null) {
                return Optional.of(next).map(mapper);
            }
        }
        return Optional.empty();
    }

    public T last() {
        return lastOf(Function.identity());
    }

    public <R> R lastOf(@NotNull Function<T, R> mapper) {
        final var iterator = iterator();
        if (!iterator.hasNext()) {
            throw noValuePresentException();
        } else if (iterable instanceof List<T> list) {
            return findLastIfInstanceOfList(Objects::nonNull, list).map(mapper).orElseThrow();
        } else {
            return findLastIfUnknownIterable(Objects::nonNull, iterator).map(mapper).orElseThrow();
        }
    }

    public Optional<T> findLast() {
        return findLastOf(Function.identity());
    }

    public Optional<T> findLast(@NotNull Predicate<T> predicate) {
        final var iterator = iterator();
        if (!iterator.hasNext()) {
            throw noValuePresentException();
        } else if (iterable instanceof List<T> list) {
            return findLastIfInstanceOfList(predicate, list);
        } else {
            return findLastIfUnknownIterable(predicate, iterator);
        }
    }

    public <R> Optional<R> findLastOf(@NotNull Function<T, R> mapper) {
        final var iterator = iterator();
        if (!iterator.hasNext()) {
            return Optional.empty();
        } else if (iterable instanceof List<T> list) {
            return Optional.ofNullable(list.get(list.size() - 1)).map(mapper);
        } else {
            T result = iterator.next();
            while (iterator.hasNext()) {
                result = iterator.next();
            }
            return Optional.ofNullable(result).map(mapper);
        }
    }

    public boolean any(@NotNull Predicate<T> predicate) {
        for (var element : iterable) {
            if (predicate.test(element)) {
                return true;
            }
        }
        return false;
    }

    public boolean all(@NotNull Predicate<T> predicate) {
        for (T t : iterable) {
            if (!predicate.test(t)) {
                return false;
            }
        }
        return true;
    }

    public boolean none(@NotNull Predicate<T> predicate) {
        for (T t : iterable) {
            if (predicate.test(t)) {
                return false;
            }
        }
        return true;
    }

    public <R> List<R> zipWithNextToListOf(BiFunction<T, T, R> function) {
        final var iterator = iterator();
        if (!iterator.hasNext()) {
            return List.of();
        }
        final List<R> list = new ArrayList<>();
        T current = iterator.next();
        while (iterator.hasNext()) {
            final var next = iterator.next();
            list.add(function.apply(current, next));
            current = next;
        }
        return list;
    }

    public <R> IterX<R> zipWithNext(BiFunction<T, T, R> function) {
        return IterX.of(zipWithNextToListOf(function));
    }

    public <R> List<R> zipWithNext2ToListOf(TriFunction<T, T, T, R> function) {
        final var iterator = iterator();
        if (!iterator.hasNext()) {
            return List.of();
        }
        final List<R> list = new ArrayList<>();
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

    public <R> IterX<R> zipWithNext2(TriFunction<T, T, T, R> function) {
        return IterX.of(zipWithNext2ToListOf(function));
    }

    public <A, R> List<R> zipToListWith(Iterable<A> otherIterable, BiFunction<T, A, R> function) {
        final var otherIterator = otherIterable.iterator();
        final var iterator = iterator();
        final var resultListSize = Math.min(collectionSizeOrElse(10),
                IterX.of(otherIterable).collectionSizeOrElse(10));

        final List<R> list = new ArrayList<>(resultListSize);
        while (iterator.hasNext() && otherIterator.hasNext()) {
            final var next = iterator.next();
            final var otherNext = otherIterator.next();
            list.add(function.apply(next, otherNext));
        }
        return list;
    }

    public Set<T> union(Iterable<T> other) {
        Set<T> union = new HashSet<>();
        iterable.forEach(union::add);
        other.forEach(union::add);
        return union;
    }

    public <R> Set<R> union(Iterable<T> other, Function<T, R> mapper) {
        Set<R> union = toMutableSetOf(mapper);
        union.addAll(of(other).toSetOf(mapper));
        return union;
    }

    public <A, R> IterX<R> zipWith(Iterable<A> iterable, BiFunction<T, A, R> function) {
        return IterX.of(zipToListWith(iterable, function));
    }

    private int collectionSizeOrElse(@SuppressWarnings("SameParameterValue") int defaultSize) {
        return iterable instanceof Collection<T> c ? c.size() : defaultSize;
    }

    private int collectionSizeOrElseGet(IntSupplier supplier) {
        return iterable instanceof Collection<T> c ? c.size() : supplier.getAsInt();
    }

    public IterX<T> takeWhileInclusive(Predicate<T> predicate) {
        return IterX.of(takeToListWhileInclusive(predicate));
    }

    public List<T> takeToListWhileInclusive(Predicate<T> predicate) {
        List<T> list = new ArrayList<>();
        for (T item : this) {
            list.add(item);
            if (!predicate.test(item)) {
                break;
            }
        }
        return list;
    }

    public IterX<T> takeWhile(Predicate<T> predicate) {
        return IterX.of(takeToListWhile(predicate));
    }

    public List<T> takeToListWhile(Predicate<T> predicate) {
        List<T> list = new ArrayList<>();
        for (T item : this) {
            if (!predicate.test(item)) {
                break;
            }
            list.add(item);
        }
        return list;
    }

    public IterX<T> dropWhileExclusive(Predicate<T> predicate) {
        return IterX.of(dropToListWhileExclusive(predicate));
    }

    public List<T> dropToListWhileExclusive(Predicate<T> predicate) {
        return dropToListWhile(predicate, true);
    }

    private List<T> dropToListWhile(Predicate<T> predicate, boolean exclusive) {
        var yielding = false;
        List<T> list = new ArrayList<>();
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

    public IterX<T> dropWhile(Predicate<T> predicate) {
        return IterX.of(dropToListWhile(predicate));
    }

    public List<T> dropToListWhile(Predicate<T> predicate) {
        return  dropToListWhile(predicate, false);
    }

    public IterX<T> dropLastWhile(Predicate<T> predicate) {
        return IterX.of(dropLastToListWhile(predicate));
    }

    public List<T> dropLastToListWhile(Predicate<T> predicate) {
        List<T> list = iterable instanceof List<T> current ? current : toListOf(Function.identity());
        if (list.isEmpty()) {
            return List.of();
        }
        var iterator = list.listIterator();
        while (iterator.hasPrevious()) {
            if (!predicate.test(iterator.previous())) {
                return takeToList(iterator.nextIndex() + 1);
            }
        }
        return List.of();
    }

    public IterX<T> takeLastWhile(Predicate<T> predicate) {
        return IterX.of(takeLastToListWhile(predicate));
    }

    public List<T> takeLastToListWhile(Predicate<T> predicate) {
        List<T> input = iterable instanceof List<T> list ? list : toListOf(Function.identity());
        if (input.isEmpty()) {
            return List.of();
        }
        var iterator = input.listIterator(input.size());
        while (iterator.hasPrevious()) {
            if (!predicate.test(iterator.previous())) {
                iterator.next();
                var expectedSize = input.size() - iterator.nextIndex();
                if (expectedSize == 0) {
                    return List.of();
                }
                List<T> result = new ArrayList<>(expectedSize);
                while (iterator.hasNext()) {
                    result.add(iterator.next());
                }
                return List.copyOf(result);
            }
        }
        return input;
    }

    public IterX<T> take(int n) {
        return IterX.of(takeToList(n));
    }

    public List<T> takeToList(int n) {
        requireGreaterThanZero(n);
        if (n == 0) {
            return List.of();
        }
        if (iterable instanceof Collection<T> c) {
            if (n >= c.size()) {
                return new ArrayList<>(c);
            }
            if (n == 1) {
                return List.of(first());
            }
        }
        int count = 0;
        List<T> list = new ArrayList<>(n);
        for (T item : iterable) {
            list.add(item);
            if (++count == n) {
                break;
            }
        }
        return List.copyOf(list);
    }

    public IterX<T> takeLast(int n) {
        return IterX.of(takeLastToList(n));
    }

    public List<T> takeLastToList(int n) {
        requireGreaterThanZero(n);
        if (n == 0) {
            return List.of();
        }
        List<T> list = iterable instanceof List<T> current ? current : toListOf(Function.identity());
        int size = list.size();
        if (n >= size) {
            return list;
        }
        if (n == 1) {
            return List.of(last());
        }
        List<T> resultList = new ArrayList<>(n);
        for (int index = size - n; index < size; index++) {
            resultList.add(list.get(index));
        }
        return resultList;
    }

    public IterX<T> skip(int count) {
        return IterX.of(skipToList(count));
    }

    public List<T> skipToList(int n) {
        return stream().skip(n).toList();
    }

    public IterX<T> limit(int bound) {
        return IterX.of(limitToList(bound));
    }

    public List<T> limitToList(int bound) {
        return stream().limit(bound).toList();
    }

    public CharSequence joinToString() {
        return joinToStringBy(Object::toString);
    }

    public CharSequence joinToString(CharSequence delimiter) {
        return joinToStringBy(Object::toString, delimiter);
    }

    public <R> CharSequence joinToStringBy(@NotNull Function<T, R> selector) {
        return joinToStringBy(selector, "");
    }

    public <R> CharSequence joinToStringBy(@NotNull Function<T, R> selector, CharSequence delimiter) {
        var sb = new StringBuilder();
        Iterator<T> iterator = iterator();
        while (iterator.hasNext()) {
            final var string = selector.apply(iterator.next());
            sb.append(string).append(iterator.hasNext() ? delimiter : "");
        }
        return sb.toString().trim();
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return iterable.iterator();
    }

    @Override
    public Iterator<IndexedValue<T>> indexedIterator() {
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

    @Override
    public int compareTo(@NotNull IterX<T> o) {
        int thisSize = collectionSizeOrElseGet(() -> count(a -> true));
        int otherSize = collectionSizeOrElseGet(() -> count(a -> true));
        return thisSize - otherSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IterX<?> iterX = (IterX<?>) o;
        return iterable.equals(iterX.iterable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iterable);
    }

    @Override
    public String toString() {
        return "Transformer{" +
                "iterable=" + iterable +
                '}';
    }

    @NotNull
    private static NoSuchElementException noValuePresentException() {
        return new NoSuchElementException("No value present");
    }

    private static <T> Optional<T> findLastIfUnknownIterable(Predicate<T> predicate, Iterator<T> iterator) {
        T result = iterator.next();
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
