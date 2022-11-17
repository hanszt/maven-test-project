package demo.sequences;

import java.util.Iterator;

final class SkipIterator<T> implements Iterator<T> {
    private final Iterator<T> iterator;
    private long left;

    SkipIterator(Iterator<T> iterator, long count) {
        this.iterator = iterator;
        this.left = count;
    }

    private void skip() {
        while (left > 0 && iterator.hasNext()) {
            iterator.next();
            left--;
        }
    }

    @Override
    public boolean hasNext() {
        skip();
        return iterator.hasNext();
    }

    @Override
    public T next() {
        skip();
        return iterator.next();
    }
}
