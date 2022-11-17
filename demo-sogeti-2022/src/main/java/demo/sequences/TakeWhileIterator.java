package demo.sequences;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

public final class TakeWhileIterator<T> implements Iterator<T> {

    private final Iterator<T> iterator;
    private final Predicate<? super T> predicate;
    private T nextItem;
    private State nextState = State.INIT_UNKNOWN;

    TakeWhileIterator(Iterator<T> iterator, Predicate<? super T> predicate) {
        this.iterator = iterator;
        this.predicate = predicate;
    }

    private void calculateNext() {
        if (iterator.hasNext()) {
            final T item = iterator.next();
            if (predicate.test(item)) {
                nextState = State.CONTINUE;
                nextItem = item;
                return;
            }
        }
        nextState = State.DONE;
    }

    @Override
    public boolean hasNext() {
        if (nextState.isUnknown()) {
            calculateNext();
        }
        return nextState == State.CONTINUE;
    }

    @Override
    public T next() {
        if (nextState.isUnknown()) {
            calculateNext();
        }
        if (nextState == State.DONE) {
            throw new NoSuchElementException();
        }
        T result = nextItem;
        // Clean next to avoid keeping reference on yielded instance
        nextItem = null;
        nextState = State.NEXT_UNKNOWN;
        return result;
    }
}
