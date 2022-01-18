package hzt.collections;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
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
 *
 * Its use is very comparable to the streams api but with shorter syntax.
 * <p>
 * It is inspired by the functional methods provided for collections in Kotlin
 * <p>
 * The functions applied to a Transformer are eagerly evaluated as apposed to in a stream.
 * For smaller collections < 100_000 elements, the performance is similar to streams
 * <p>
 * For larger collections, or when a lot of transformation are applied, streams are preferred.
 *
 * For a Transformer, I suggest not to apply more than 3 subsequent transformations.
 *
 * @param <T> The Type of the Iterable in the Transform object
 * @author Hans Zuidervaart
 */
@SuppressWarnings("ClassCanBeRecord")
public final class Transformer<T> implements Iterable<T> {

    private final Iterable<T> iterable;

    private Transformer(@NotNull Iterable<T> iterable) {
        this.iterable = iterable;
    }

    public static <T> Transformer<T> of(Iterable<T> iterable) {
        return new Transformer<>(iterable);
    }

    public static <T> Transformer<T> empty() {
        return Transformer.of(() -> new Iterator<>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public T next() {
                throw noValuePresentException();
            }
        });
    }

    @SafeVarargs
    public static <T> Transformer<T> of(T... values) {
        return Transformer.of(List.of(values));
    }

    public static Transformer<Integer> of(int... values) {
        List<Integer> valueList = new ArrayList<>();
        for (var value : values) {
            valueList.add(value);
        }
        return Transformer.of(valueList);
    }

    public static Transformer<Long> of(long... values) {
        List<Long> valueList = new ArrayList<>();
        for (var value : values) {
            valueList.add(value);
        }
        return Transformer.of(valueList);
    }

    public static Transformer<Double> of(double... values) {
        List<Double> valueList = new ArrayList<>();
        for (var i : values) {
            valueList.add(i);
        }
        return Transformer.of(valueList);
    }

    public <R> Transformer<R> map(Function<T, R> mapper) {
        return Transformer.of(toCollectionOf(mapper, ArrayList::new));
    }

    public <R> Transformer<R> mapNotNull(Function<T, R> mapper) {
        return Transformer.of(toCollectionNotNullOf(mapper, ArrayList::new));
    }

    public <R> List<R> toListOf(Function<T, R> transform) {
        return List.copyOf(toMutableListNotNullOf(transform));
    }

    public <R> Set<R> toSetOf(Function<T, R> transform) {
        return Set.copyOf(toMutableSetNotNullOf(transform));
    }

    public <R> List<R> toMutableListOf(Function<T, R> transform) {
        return toCollectionOf(transform, ArrayList::new);
    }

    public <R> List<R> toMutableListNotNullOf(Function<T, R> transform) {
        return toCollectionNotNullOf(transform, ArrayList::new);
    }

    public <R> Set<R> toMutableSetOf(Function<T, R> transform) {
        return toCollectionOf(transform, HashSet::new);
    }

    public <R> Set<R> toMutableSetNotNullOf(Function<T, R> transform) {
        return toCollectionNotNullOf(transform, HashSet::new);
    }

    public <R, C extends Collection<R>> C toCollectionOf(Function<T, R> mapper, Supplier<C> collectionFactory) {
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

    public Transformer<T> filter(Predicate<T> predicate) {
        return Transformer.of(filterToCollection(predicate, new ArrayList<>()));
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

    public Transformer<T> filterNot(Predicate<T> predicate) {
        return filter(predicate.negate());
    }

    public List<T> toListSkipping(Predicate<T> predicate) {
        return List.copyOf(filterToMutableList(predicate.negate()));
    }

    public Set<T> toSetSkipping(Predicate<T> predicate) {
        return Set.copyOf(filterToMutableSet(predicate.negate()));
    }

    public <R, C extends Collection<R>> Transformer<R> flatMap(@NotNull Function<T, C> mapper) {
        return Transformer.of(flatMapToMutableListOf(mapper));
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
        final Set<R> set = new HashSet<>();
        for (T t : iterable) {
            if (t == null) {
                continue;
            }
            for (R r : mapper.apply(t)) {
                if (r != null) {
                    set.add(r);
                }
            }
        }
        return set;
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
        final var list = toMutableList();
        list.sort(Comparator.comparing(selector));
        return list;
    }

    public <R extends Comparable<R>> List<T> toListSortedDescendingBy(@NotNull Function<T, R> selector) {
        final var list = toMutableList();
        list.sort(Comparator.comparing(selector).reversed());
        return list;
    }

    public <R extends Comparable<R>> List<R> toSortedListOf(@NotNull Function<T, R> selector) {
        final var list = toMutableListOf(selector);
        list.sort(Comparator.naturalOrder());
        return list;
    }

    public <R extends Comparable<R>> List<R> toDescendingSortedListOf(@NotNull  Function<T, R> selector) {
        final var list = toMutableListOf(selector);
        final Comparator<R> tComparator = Comparator.naturalOrder();
        list.sort(tComparator.reversed());
        return list;
    }

    private List<T> toMutableList() {
        return toCollection(ArrayList::new);
    }

    public <R extends Comparable<R>> SortedSet<T> toSetSortedBy(Function<T, R> selector) {
        final SortedSet<T> sortedSet = new TreeSet<>(Comparator.comparing(selector));
        sortedSet.addAll(toCollection(HashSet::new));
        return sortedSet;
    }

    public <R extends Comparable<R>> SortedSet<R> toSortedSetOf(Function<T, R> selector) {
        return new TreeSet<>(toMutableListOf(selector));
    }

    public <C extends Collection<T>> C toCollection(@NotNull Supplier<C> collectionFactory) {
        return toCollectionOf(Function.identity(), collectionFactory);
    }

    public T[] toArray(@NotNull IntFunction<T[]> generator) {
        return toMutableList().toArray(generator);
    }

    public <R> R[] mapToArray(@NotNull Function<T, R> mapper, @NotNull IntFunction<R[]> generator) {
        return toMutableListOf(mapper).toArray(generator);
    }

    public int[] toIntArray(@NotNull ToIntFunction<T> mapper) {
        int counter = 0;
        int[] array = new int[countBy(t -> true)];
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
        long[] array = new long[countBy(t -> true)];
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
        double[] array = new double[countBy(t -> true)];
        for (T value : iterable) {
            if (value != null) {
                final var t = mapper.applyAsDouble(value);
                array[counter] = t;
                counter++;
            }
        }
        return array;
    }

    public <K, V> Map<K, V> toMutableMap(@NotNull Function<T, K> keyMapper, @NotNull Function<T, V> valueMapper) {
        Map<K, V> map = new HashMap<>();
        for (T t : iterable) {
            if (t != null) {
                final var key = keyMapper.apply(t);
                if (key != null) {
                    map.put(key, valueMapper.apply(t));
                }
            }
        }
        return map;
    }

    public <K, V> Map<K, V> toMapOf(@NotNull Function<T, K> keyMapper, @NotNull Function<T, V> valueMapper) {
        return Map.copyOf(toMutableMap(keyMapper, valueMapper));
    }

    public <R extends Comparable<R>> Transformer<T> sortedBy(@NotNull Function<T, R> selector) {
        return Transformer.of(toListSortedBy(selector));
    }

    public Transformer<T> sorted() {
        return Transformer.of(stream().sorted().toList());
    }

    public Stream<T> stream() {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    public Transformer<T> distinct() {
        return Transformer.of(toCollection(LinkedHashSet::new));
    }

    public <R> Transformer<T> distinctBy(Function<T, R> selector) {
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
        return Transformer.of(result);
    }

    public int countBy(@NotNull Predicate<T> predicate) {
        var counter = 0;
        for (T t : iterable) {
            if (t != null && predicate.test(t)) {
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

    public long sumOf(@NotNull ToIntFunction<T> selector) {
        long sum = 0;
        for (T t : iterable) {
            if (t != null) {
                final var value = selector.applyAsInt(t);
                sum += value;
            }
        }
        return sum;
    }

    public int sumOf(@NotNull ToLongFunction<T> selector) {
        var sum = 0;
        for (T t : iterable) {
            if (t != null) {
                final var value = selector.applyAsLong(t);
                sum += value;
            }
        }
        return sum;
    }

    public double sumOf(@NotNull ToDoubleFunction<T> selector) {
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
        final var iterator = iterable.iterator();
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
    public Transformer<T> onEach(@NotNull Consumer<T> consumer) {
        iterable.forEach(consumer);
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
        final var iterator = iterable.iterator();
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

    public <K> Map<K, List<T>> groupBy(@NotNull Function<T, K> classifier) {
        return groupMapping(classifier, Function.identity());
    }

    public <K, R> Map<K, List<R>> groupMapping(@NotNull Function<T, K> classifier, @NotNull Function<T, R> valueMapper) {
        Map<K, List<R>> groupedMap = new HashMap<>();
        for (T t : iterable) {
            groupedMap.computeIfAbsent(classifier.apply(t), key -> new ArrayList<>()).add(valueMapper.apply(t));
        }
        return groupedMap;
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
                    pair.first.add(r);
                } else {
                    pair.second.add(r);
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
        final var iterator = iterable.iterator();
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
        final var iterator = iterable.iterator();
        if (!iterator.hasNext()) {
            throw noValuePresentException();
        } else if (iterable instanceof List<T> list) {
            return findLastIfInstanceOfList(predicate, list);
        } else {
            return findLastIfUnknownIterable(predicate, iterator);
        }
    }

    public <R> Optional<R> findLastOf(@NotNull Function<T, R> mapper) {
        final var iterator = iterable.iterator();
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
        Iterator<T> iterator = iterable.iterator();
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Transformer<?> transformer = (Transformer<?>) o;
        return iterable.equals(transformer.iterable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iterable);
    }

    @Override
    public String toString() {
        return "IterX{" +
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

    public record Pair<A, B>(A first, B second) {
    }
}
