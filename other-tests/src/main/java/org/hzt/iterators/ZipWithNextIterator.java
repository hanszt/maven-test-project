package org.hzt.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;

public final class ZipWithNextIterator<R, T> implements Iterator<R> {

    private final Iterator<T> iterator;
    private final BiFunction<T, T, R> merger;
    boolean isFirstElement;
    private T first;
    private T second;

    public ZipWithNextIterator(Iterator<T> iterator, BiFunction<T, T, R> merger) {
        this.iterator = iterator;
        this.merger = merger;
        isFirstElement = true;
        first = null;
        second = null;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public R next() {
        nextPair();
        if (isFirstElement) {
            throw new NoSuchElementException("Not enough elements to zip with next (Only one element in iterable)");
        }
        return merger.apply(first, second);
    }

    void nextPair() {
        if (isFirstElement && iterator.hasNext()) {
            first = iterator.next();
            if (iterator.hasNext()) {
                second = iterator.next();
                isFirstElement = false;
                return;
            }
        }
        first = second;
        if (iterator.hasNext()) {
            second = iterator.next();
        }
    }
}
