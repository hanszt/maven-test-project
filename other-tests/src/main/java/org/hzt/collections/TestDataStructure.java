package org.hzt.collections;

import org.hzt.sequences.functional_iterator_sequences.FunctionalIterable;
import org.hzt.sequences.functional_iterator_sequences.FunctionalIterator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class TestDataStructure<T> implements FunctionalIterable<T> {

    private final List<T> nodes = new ArrayList<>();

    @SafeVarargs
    public TestDataStructure(T... values) {
        Collections.addAll(nodes, values);
    }
    @Override
    public FunctionalIterator<T> functionalIterator() {
        final var iterator = nodes.iterator();
        return action -> acceptIfHasNext(iterator, action);
    }

    private boolean acceptIfHasNext(Iterator<T> iterator, Consumer<? super T> action) {
        final var hasNext = iterator.hasNext();
        if (hasNext) {
            action.accept(iterator.next());
        }
        return hasNext;
    }
}
