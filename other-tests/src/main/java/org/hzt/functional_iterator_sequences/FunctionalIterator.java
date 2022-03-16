package org.hzt.functional_iterator_sequences;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

/**
 * This iterator has exactly the same advancing method as spliterator.
 *
 * It replaces the 'boolean hasNext()' and 'T next()' methods in the familiair
 * @see java.util.Iterator by 'boolean tryAdvance(Consumer<T> consumer)'. The consumer accepts each next value
 * This way it is a Functional interface
 *
 * A spliterator is an advanced form of an iterator
 *
 * @param <T> the type of the elements iterated over by this iterator
 */
@FunctionalInterface
public interface FunctionalIterator<T> {

    boolean tryAdvance(Consumer<? super T> action);

    default void forEachRemaining(Consumer<? super T> action) {
        //noinspection StatementWithEmptyBody
        do {
        } while (tryAdvance(action));
    }

    default Iterator<T> asIterator() {
        return new Iterator<>() {

            private final HoldingConsumer<T> holdingConsumer = new HoldingConsumer<>();

            @Override
            public boolean hasNext() {
                return tryAdvance(holdingConsumer);
            }

            @Override
            public T next() {
                final var value = holdingConsumer.getAndClear();
                if (value == null) {
                    throw new NoSuchElementException();
                }
                return value;
            }
        };
    }
}
