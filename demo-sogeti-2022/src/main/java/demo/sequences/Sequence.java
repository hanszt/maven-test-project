package demo.sequences;

import demo.IndexedValue;
import demo.It;
import demo.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collector;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Spliterator.ORDERED;

@SuppressWarnings({"WhileLoopReplaceableByForEach", "squid:S1448"})
@FunctionalInterface
public interface Sequence<T> extends Iterable<T> {

    //factory methods
    @SafeVarargs
    static <T> Sequence<T> of(final T... values) {
        return () -> new ArrayIterator<>(values.length, index -> values[index]);
    }

    static <T> Sequence<T> of(final Iterable<T> iterable) {
        return iterable::iterator;
    }

    static <T> Sequence<T> empty() {
        return Collections::emptyIterator;
    }

    static <T> Sequence<T> generate(final Supplier<? extends T> nextValueGenerator) {
        return generate(nextValueGenerator, t -> nextValueGenerator.get());
    }

    static <T> Sequence<T> generate(final Supplier<? extends T> seedFunction,
                                    final UnaryOperator<T> nextFunction) {
        return () -> new GeneratorIterator<>(seedFunction, nextFunction);
    }

    static <T> Sequence<T> iterate(final T seed, final UnaryOperator<T> nextValueGenerator) {
        return generate(() -> seed, nextValueGenerator);
    }

    Iterator<T> iterator();

    //intermediate operations
    default Sequence<T> filter(final Predicate<? super T> predicate) {
        return () -> new FilteringIterator<>(iterator(), predicate, true);
    }

    default Sequence<T> filterNot(final Predicate<? super T> predicate) {
        return () -> new FilteringIterator<>(iterator(), predicate, false);
    }

    default Sequence<T> filterIndexed(final BiPredicate<Integer, ? super T> predicate) {
        return withIndex()
                .filter(indexedValue -> predicate.test(indexedValue.index(), indexedValue.value()))
                .map(IndexedValue::value);
    }

    default <R> Sequence<R> map(final Function<? super T, ? extends R> mapper) {
        return () -> new MappingIterator<>(iterator(), mapper);
    }

    default <R> Sequence<R> mapNotNull(final Function<? super T, ? extends R> mapper) {
        return () -> new FilteringIterator<>(
                new MappingIterator<>(
                        new FilteringIterator<>(iterator(),
                                Objects::nonNull, true),
                        mapper),
                Objects::nonNull, true);
    }

    default <R> Sequence<R> mapIndexed(final BiFunction<Integer, T, R> mapper) {
        return () -> new MappingIndexedIterator<>(iterator(), mapper);
    }

    default Sequence<T> onEach(final Consumer<T> consumer) {
        return map(t -> {
            consumer.accept(t);
            return t;
        });
    }

    default <R> Sequence<R> flatMap(final Function<? super T, ? extends Iterable<R>> toIterableMapper) {
        return () -> new FlatteningIterator<>(iterator(), t -> toIterableMapper.apply(t).iterator());
    }

    default <R> Sequence<R> mapMulti(final BiConsumer<? super T, ? super Consumer<R>> mapper) {
        return flatMap(t -> {
            final List<R> buffer = new ArrayList<>();
            mapper.accept(t, (Consumer<R>) buffer::add);
            return buffer;
        });
    }

    default Sequence<IndexedValue<T>> withIndex() {
        return this::indexingIterator;
    }

    private Iterator<IndexedValue<T>> indexingIterator() {
        return new Iterator<>() {

            private final Iterator<T> iterator = iterator();
            private int index = 0;

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public IndexedValue<T> next() {
                final var nextIndex = index++;
                if (nextIndex < 0) {
                    throw new IllegalStateException("Iterator index overflow...");
                }
                return new IndexedValue<>(nextIndex, iterator.next());
            }
        };
    }

    default Sequence<T> skip(final long count) {
        return () -> new SkipIterator<>(iterator(), count);
    }

    default Sequence<T> skipWhile(final Predicate<? super T> predicate) {
        return () -> new SkipWhileIterator<>(iterator(), predicate);
    }

    default Sequence<T> take(final long count) {
        return () -> new TakeIterator<>(iterator(), count);
    }

    default Sequence<T> takeWhile(final Predicate<? super T> predicate) {
        return () -> new TakeWhileIterator<>(iterator(), predicate);
    }

    default Sequence<List<T>> windowed(final int size, final int step, final boolean partialWindows) {
        if (size <= 0 || step <= 0) {
            throw new IllegalArgumentException("Size " + size + " and step " + step + " must be greater than zero.");
        }
        return () -> new WindowedIterator<>(iterator(), size, step, partialWindows);
    }

    default Sequence<List<T>> windowed(final int size, final int step) {
        return windowed(size, step, false);
    }

    default Sequence<List<T>> windowed(final int size) {
        return windowed(size, 1);
    }

    default Sequence<List<T>> chunked(final int size) {
        return windowed(size, size, true);
    }

    default <R> Sequence<R> zipWithNext(final BiFunction<? super T, ? super T, ? extends R> function) {
        return zipWithNext().map(list -> function.apply(list.first(), list.second()));
    }

    default Sequence<Pair<T, T>> zipWithNext() {
        return windowed(2).map(l -> Pair.of(l.get(0), l.get(1)));
    }

    default <R extends Comparable<? super R>> Sequence<T> sortedBy(final Function<? super T, ? extends R> selector) {
        return sorted(Comparator.comparing(selector));
    }

    default Sequence<T> sorted(final Comparator<? super T> comparator) {
        return () -> toSortedList(comparator).iterator();
    }

    private List<T> toSortedList(final Comparator<? super T> comparator) {
        final var list = new ArrayList<T>();
        forEach(list::add);
        list.sort(comparator);
        return list;
    }

    default Sequence<T> distinct() {
        return distinctBy(It::self);
    }

    default <R> Sequence<T> distinctBy(final Function<? super T, ? extends R> selector) {
        return () -> distinctIterator(selector);
    }

    private <K> Iterator<T> distinctIterator(final Function<? super T, ? extends K> selector) {
        return new AbstractIterator<>() {
            private final Iterator<T> iterator = iterator();
            private final Set<K> observed = new HashSet<>();

            @Override
            protected void computeNext() {
                while (iterator.hasNext()) {
                    final var next = iterator.next();
                    final var key = selector.apply(next);
                    if (observed.add(key)) {
                        setNext(next);
                        return;
                    }
                }
                done();
            }
        };
    }

    default Sequence<T> plus(final T value) {
        return Sequence.of(this, Sequence.of(value)).mapMulti(Iterable::forEach);
    }

    default Sequence<T> plus(final Iterable<? extends T> values) {
        return Sequence.of(this, Sequence.of(values)).mapMulti(Iterable::forEach);
    }

    default Sequence<T> minus(final T value) {
        return () -> removingIterator(value);
    }

    private Iterator<T> removingIterator(final T value) {
        final var removed = new AtomicBoolean();
        return filter(e -> {
            if (!removed.get() && e == value) {
                removed.set(true);
                return false;
            } else {
                return true;
            }
        }).iterator();
    }

    default Sequence<T> minus(final Iterable<T> values) {
        final var others = values instanceof Set<?> ? (Set<T>) values : Sequence.of(values).to(HashSet::new);
        return () -> others.isEmpty() ? iterator() : filterNot(others::contains).iterator();
    }

    default Stream<T> stream() {
        return StreamSupport.stream(() -> Spliterators.spliteratorUnknownSize(iterator(), ORDERED), ORDERED, false);
    }

    default Sequence<T> constrainOnce() {
        final AtomicBoolean consumed = new AtomicBoolean();
        return () -> {
            if (consumed.get()) {
                throw new IllegalStateException("Sequence is already consumed");
            }
            consumed.set(true);
            return iterator();
        };
    }

    //terminal operations

    default T reduce(final T identity, final BinaryOperator<T> operator) {
        var accumulator = identity;
        final var iterator = iterator();
        while (iterator.hasNext()) {
            accumulator = operator.apply(accumulator, iterator.next());
        }
        return accumulator;
    }

    default Optional<T> reduce(final BinaryOperator<T> operator) {
        final var iterator = iterator();
        if (iterator.hasNext()) {
            var accumulator = iterator.next();
            while (iterator.hasNext()) {
                accumulator = operator.apply(accumulator, iterator.next());
            }
            return Optional.of(accumulator);
        }
        return Optional.empty();
    }

    default <R> R fold(final R seed, final BiFunction<R, T, R> foldFunction) {
        var accumulator = seed;
        final var iterator = iterator();
        while (iterator.hasNext()) {
            accumulator = foldFunction.apply(accumulator, iterator.next());
        }
        return accumulator;
    }

    default <A, R> R collect(final Collector<? super T, A, R> collector) {
        final var accumulator = collector.accumulator();
        final var container = collector.supplier().get();
        final var iterator = iterator();
        while (iterator.hasNext()) {
            final var next = iterator.next();
            accumulator.accept(container, next);
        }
        return collector.finisher().apply(container);
    }

    default <C extends Collection<T>> C to(final Supplier<? extends C> collectionFactory) {
        final var collection = collectionFactory.get();
        final var iterator = iterator();
        while (iterator.hasNext()) {
            collection.add(iterator.next());
        }
        return collection;
    }

    default List<T> toList() {
        return List.copyOf(to(ArrayList::new));
    }

    default Set<T> toSet() {
        return Set.copyOf(to(LinkedHashSet::new));
    }

    default <K> Map<K, List<T>> groupBy(final Function<? super T, ? extends K> keySelector) {
        return groupBy(keySelector, It::self);
    }

    default <K, R> Map<K, List<R>> groupBy(final Function<? super T, ? extends K> keySelector,
                                           final Function<? super T, ? extends R> mapper) {
        final Map<K, List<R>> map = new HashMap<>();
        final var iterator = iterator();
        while (iterator.hasNext()) {
            final var next = iterator.next();
            map.computeIfAbsent(keySelector.apply(next), key -> new ArrayList<>()).add(mapper.apply(next));
        }
        return Map.copyOf(map);
    }

    default String joinToString() {
        return joinToStringBy(Object::toString);
    }

    default String joinToString(final String delimiter) {
        return joinToStringBy(Object::toString, delimiter);
    }

    default <R> String joinToStringBy(final Function<? super T, ? extends R> selector) {
        return joinToStringBy(selector, "");
    }

    default <R> String joinToStringBy(final Function<? super T, ? extends R> selector, final CharSequence delimiter) {
        final var sb = new StringBuilder();
        final var iterator = iterator();
        while (iterator.hasNext()) {
            final var r = selector.apply(iterator.next());
            sb.append(r).append(iterator.hasNext() ? delimiter : "");
        }
        return sb.toString().trim();
    }

    default long count() {
        long counter = 0;
        final var iterator = iterator();
        while (iterator.hasNext()) {
            iterator.next();
            counter++;
        }
        return counter;
    }

    //short-circuiting terminal operations
    default T first() {
        final var iterator = iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        }
        throw new NoSuchElementException();
    }

    default T first(Predicate<? super T> predicate) {
        final var iterator = iterator();
        while (iterator.hasNext()) {
            final var next = iterator.next();
            if (predicate.test(next)) {
                return next;
            }
        }
        throw new NoSuchElementException();
    }

    default Optional<T> findFirst(Predicate<? super T> predicate) {
        final var iterator = iterator();
        while (iterator.hasNext()) {
            final var next = iterator.next();
            if (predicate.test(next)) {
                return Optional.of(next);
            }
        }
        return Optional.empty();
    }

    default Optional<T> findFirst() {
        final var iterator = iterator();
        return iterator.hasNext() ? Optional.ofNullable(iterator.next()) : Optional.empty();
    }

    default boolean any(final Predicate<? super T> predicate) {
        final var iterator = iterator();
        while (iterator.hasNext()) {
            if (predicate.test(iterator.next())) {
                return true;
            }
        }
        return false;
    }

    default boolean any() {
        return iterator().hasNext();
    }

    default boolean all(final Predicate<? super T> predicate) {
        final var iterator = iterator();
        while (iterator.hasNext()) {
            if (!predicate.test(iterator.next())) {
                return false;
            }
        }
        return true;
    }

    default boolean none(final Predicate<? super T> predicate) {
        final var iterator = iterator();
        while (iterator.hasNext()) {
            if (predicate.test(iterator.next())) {
                return false;
            }
        }
        return true;
    }

    default boolean none() {
        return !iterator().hasNext();
    }
}
