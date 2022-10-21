package org.hzt.sequences.functional_iterator_sequences;

import java.util.function.Consumer;

public final class HoldingConsumer<T> implements Consumer<T> {

    private T value = null;

    @Override
    public void accept(T t) {
        this.value = t;
    }

    public T getAndClear() {
        final T next = value;
        value = null;
        return next;
    }

    public T getValue() {
        return value;
    }
}
