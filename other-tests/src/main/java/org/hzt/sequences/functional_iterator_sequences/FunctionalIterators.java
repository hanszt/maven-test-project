package org.hzt.sequences.functional_iterator_sequences;

import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public final class FunctionalIterators {

    private FunctionalIterators() {
    }

    @NotNull
    static <T, R> FunctionalIterator<R> mappingIterator(FunctionalIterator<T> iterator,
                                                        Function<? super T, ? extends R> mapper) {
        return new FunctionalIterator<>() {

            private final HoldingConsumer<T> holdingConsumer = new HoldingConsumer<>();

            @Override
            public boolean tryAdvance(Consumer<? super R> consumer) {
                final var isHoldingNext = iterator.tryAdvance(holdingConsumer);
                if (isHoldingNext) {
                    consumer.accept(mapper.apply(holdingConsumer.getAndClear()));
                }
                return isHoldingNext;
            }
        };
    }

    static <T> FunctionalIterator<T> filteringIterator(FunctionalIterator<T> iterator, Predicate<? super T> predicate) {
        return new FunctionalIterator<>() {

            private final HoldingConsumer<T> consumer = new HoldingConsumer<>();

            @Override
            public boolean tryAdvance(Consumer<? super T> action) {
                var hasNext = iterator.tryAdvance(consumer);
                while (hasNext && !predicate.test(consumer.getValue())) {
                    hasNext = iterator.tryAdvance(consumer);
                }
                if (hasNext) {
                    final var next = consumer.getAndClear();
                    if (next == null) {
                        throw new NoSuchElementException();
                    }
                    action.accept(next);
                }
                return hasNext;
            }
        };
    }
}
