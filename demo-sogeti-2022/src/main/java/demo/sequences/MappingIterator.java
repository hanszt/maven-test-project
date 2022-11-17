package demo.sequences;

import java.util.Iterator;
import java.util.function.Function;

final class MappingIterator<T, R> implements Iterator<R> {

    private final Iterator<T> iterator;
    private final Function<? super T, ? extends R> mapper;


    MappingIterator(Iterator<T> iterator, Function<? super T, ? extends R> mapper) {
        this.iterator = iterator;
        this.mapper = mapper;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public R next() {
        return mapper.apply(iterator.next());
    }
}
