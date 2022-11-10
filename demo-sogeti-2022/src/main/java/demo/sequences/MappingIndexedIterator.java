package demo.sequences;

import java.util.Iterator;
import java.util.function.BiFunction;

final class MappingIndexedIterator<T, R> implements Iterator<R> {

    private final Iterator<T> iterator;
    private final BiFunction<Integer, T, R> mapper;
    private int index;

    MappingIndexedIterator(Iterator<T> iterator, BiFunction<Integer, T, R> mapper) {
        this.iterator = iterator;
        this.mapper = mapper;
        this.index = 0;
    }

    public boolean hasNext() {
        return this.iterator.hasNext();
    }

    public R next() {
        if (index < 0) {
            throw new IllegalStateException("indexed iterator index overflow");
        }
        return mapper.apply(this.index++, this.iterator.next());
    }
}
