package demo.sequences;

import java.util.Iterator;
import java.util.function.Predicate;

final class SkipWhileIterator<T> implements Iterator<T> {

    private final Iterator<T> iterator;
    private final Predicate<? super T> predicate;

    private T nextItem = null;
    private SkipState state = SkipState.SKIPPING;

    SkipWhileIterator(Iterator<T> iterator, Predicate<? super T> predicate) {
        this.iterator = iterator;
        this.predicate = predicate;
    }

    private void skip() {
        while (iterator.hasNext()) {
            T item = iterator.next();
            if (!predicate.test(item)) {
                nextItem = item;
                state = SkipState.NEXT_ITEM;
                return;
            }
        }
        state = SkipState.NORMAL_ITERATION;
    }

    @Override
    public boolean hasNext() {
        if (state == SkipState.SKIPPING) {
            skip();
        }
        return state == SkipState.NEXT_ITEM || iterator.hasNext();
    }

    @Override
    public T next() {
        if (state == SkipState.SKIPPING) {
            skip();
        }
        if (state == SkipState.NEXT_ITEM) {
            T result = nextItem;
            nextItem = null;
            state = SkipState.NORMAL_ITERATION;
            return result;
        }
        return iterator.next();
    }

    private enum SkipState {
        SKIPPING, NEXT_ITEM, NORMAL_ITERATION
    }
}
