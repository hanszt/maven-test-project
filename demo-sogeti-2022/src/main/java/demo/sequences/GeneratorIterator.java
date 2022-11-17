package demo.sequences;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

final class GeneratorIterator<T> implements Iterator<T> {

    private final Supplier<? extends T> initSupplier;
    private final UnaryOperator<T> nextValueSupplier;

    private T nextItem = null;
    private State nextState = State.INIT_UNKNOWN;

    GeneratorIterator(Supplier<? extends T> initSupplier, UnaryOperator<T> nextValueSupplier) {
        this.initSupplier = initSupplier;
        this.nextValueSupplier = nextValueSupplier;
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
        final T result = nextItem;
        // Do not clean nextItem (set item to 'null' to avoid keeping reference on yielded instance)
        // -- need to keep state for getNextValue
        nextState = State.NEXT_UNKNOWN;
        return result;
    }

    private void calculateNext() {
        nextItem = nextState == State.INIT_UNKNOWN ? initSupplier.get() : nextValueSupplier.apply(nextItem);
        nextState = nextItem == null ? State.DONE : State.CONTINUE;
    }
}
