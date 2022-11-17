package demo.sequences;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * A base class to simplify implementing iterators so that implementations only have to implement [computeNext]
 * to implement the iterator, calling [done] when the iteration is complete.
 */
abstract class AbstractIterator<T> implements Iterator<T> {

    private State state = State.NEXT_UNKNOWN;
    private T nextValue = null;

    AbstractIterator() {
    }

    @Override
    public boolean hasNext() {
        return switch (state) {
            case FAILED -> throw new IllegalArgumentException();
            case DONE -> false;
            case CONTINUE -> true;
            default -> tryToComputeNext();
        };
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        state = State.NEXT_UNKNOWN;
        return Objects.requireNonNull(nextValue);
    }

    private boolean tryToComputeNext() {
        state = State.FAILED;
        computeNext();
        return state == State.CONTINUE;
    }

    abstract void computeNext();

    void setNext(T value) {
        nextValue = value;
        state = State.CONTINUE;
    }

    void done() {
        state = State.DONE;
    }

}
