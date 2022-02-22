package org.hzt.primitve_sequences;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.function.IntPredicate;

public final class IntFilteringIterator implements PrimitiveIterator.OfInt {

    private final PrimitiveIterator.OfInt iterator;
    private final IntPredicate predicate;
    private final boolean sendWhen;
    private State nextState = State.NEXT_UNKNOWN;
    private int nextItem = 0;

    private IntFilteringIterator(PrimitiveIterator.OfInt iterator, IntPredicate predicate, boolean sendWhen) {
        this.iterator = iterator;
        this.predicate = predicate;
        this.sendWhen = sendWhen;
    }

    public static IntFilteringIterator of(PrimitiveIterator.OfInt iterator, IntPredicate predicate, boolean sendWhen) {
        return new IntFilteringIterator(iterator, predicate, sendWhen);
    }

    public static IntFilteringIterator of(PrimitiveIterator.OfInt iterator, IntPredicate predicate) {
        return new IntFilteringIterator(iterator, predicate, true);
    }

    @Override
    public boolean hasNext() {
        if (nextState == State.NEXT_UNKNOWN) {
            calculateNext();
        }
        return nextState == State.CONTINUE;
    }

    @Override
    public int nextInt() {
        if (nextState == State.NEXT_UNKNOWN) {
            calculateNext();
        }
        if (nextState == State.DONE) {
            throw new NoSuchElementException();
        }
        final int result = nextItem;
        nextItem = 0;
        nextState = State.NEXT_UNKNOWN;
        return result;
    }

    private void calculateNext() {
        while (iterator.hasNext()) {
            int item = iterator.next();
            if (predicate.test(item) == sendWhen) {
                nextItem = item;
                nextState = State.CONTINUE;
                return;
            }
        }
        nextState = State.DONE;
    }
}
