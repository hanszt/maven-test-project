package demo.sequences;

import java.util.Iterator;
import java.util.NoSuchElementException;

final class TakeIterator<T> implements Iterator<T> {
    private final Iterator<T> iterator;
    private long left;

    TakeIterator(Iterator<T> iterator, long count) {
        this.iterator = iterator;
        this.left = count;
    }

    @Override
    public boolean hasNext() {
        return left > 0 && iterator.hasNext();
    }
    @Override
    public T next() {
        if (left == 0) {
            throw new NoSuchElementException();
        }
        left--;
        return iterator.next();
    }
}
