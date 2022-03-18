package org.hzt.functional_iterator_sequences;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@FunctionalInterface
public interface FunctionalSequence<T> extends FunctionalIterable<T> {

    static <T> FunctionalSequence<T> of(@NotNull Iterable<T> iterable) {
        return getFunctionalSequence(iterable.iterator());
    }

    static <T> FunctionalSequence<T> of(@NotNull Stream<T> stream) {
        return getFunctionalSequence(stream.iterator());
    }

    static <T> FunctionalSequence<T> of(@NotNull FunctionalIterable<T> iterable) {
        return iterable::functionalIterator;
    }

    default <R> FunctionalSequence<R> map(@NotNull Function<? super T, ? extends R> mapper) {
        return () -> FunctionalIterators.mappingIterator(functionalIterator(), mapper);
    }

    default FunctionalSequence<T> filter(@NotNull Predicate<? super T> predicate) {
        return () -> FunctionalIterators.filteringIterator(functionalIterator(), predicate);
    }

    default FunctionalSequence<T> onEach(@NotNull Consumer<T> consumer) {
        return map(value -> {
            consumer.accept(value);
            return value;
        });
    }

    default T reduce(@NotNull T init, @NotNull BinaryOperator<T> binaryOperator) {
        T result = init;
        FunctionalIterator<T> iterator = functionalIterator();
        HoldingConsumer<T> consumer = new HoldingConsumer<>();
        while (iterator.tryAdvance(consumer)) {
            result = binaryOperator.apply(result, consumer.getAndClear());
        }
        return result;
    }

    default <C extends Collection<T>> C to(@NotNull Supplier<C> collectionSupplier) {
        final var collection = collectionSupplier.get();
        FunctionalIterator<T> iterator = functionalIterator();
        HoldingConsumer<T> consumer = new HoldingConsumer<>();
        while (iterator.tryAdvance(consumer)) {
            collection.add(consumer.getAndClear());
        }
        return collection;
    }

    default T[] toArray(@NotNull IntFunction<T[]> arrayFactory) {
        return toList().toArray(arrayFactory);
    }

    default List<T> toList() {
        final List<T> list = to(ArrayList::new);
        return Collections.unmodifiableList(list);
    }

    default Set<T> toSet() {
        final Set<T> set = to(HashSet::new);
        return Collections.unmodifiableSet(set);
    }

    default Iterator<T> iterator() {
        return functionalIterator().asIterator();
    }

    default Spliterator<T> spliterator() {
        return Spliterators.spliteratorUnknownSize(iterator(), 0);
    }

    default Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    @NotNull
    private static <T> FunctionalSequence<T> getFunctionalSequence(@NotNull Iterator<T> downStream) {
        return new FunctionalSequence<>() {

            @Override
            public FunctionalIterator<T> functionalIterator() {
                return consumer -> consumeIfHasNext(consumer, downStream);
            }

            private boolean consumeIfHasNext(Consumer<? super T> consumer, Iterator<T> downStream) {
                final var hasNext = downStream.hasNext();
                if (hasNext) {
                    consumer.accept(downStream.next());
                }
                return hasNext;
            }
        };
    }
}
