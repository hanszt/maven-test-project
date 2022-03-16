package org.hzt.functional_iterator_sequences;

import java.util.function.Consumer;

@FunctionalInterface
public interface FunctionalIterable<T> {

    FunctionalIterator<T> functionalIterator();

    default void forEach(Consumer<T> consumer) {
        FunctionalIterator<T> iterator = functionalIterator();
        //noinspection StatementWithEmptyBody
        do {
        } while (iterator.tryAdvance(consumer));
    }
}
