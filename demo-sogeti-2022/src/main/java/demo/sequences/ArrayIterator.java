package demo.sequences;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.IntFunction;

final class ArrayIterator<T> implements Iterator<T> {
    private int index = 0;
    private final int size;
    private final IntFunction<T> indexToValueMapper;

    ArrayIterator(int size, IntFunction<T> indexToValueMapper) {
        this.size = size;
        this.indexToValueMapper = indexToValueMapper;
    }

    @Override
    public boolean hasNext() {
        return index < size;
    }

    @Override
    public T next() {
        if (index >= size) {
            throw new NoSuchElementException();
        }
        return indexToValueMapper.apply(index++);
    }
}
