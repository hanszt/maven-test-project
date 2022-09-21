package org.hzt.collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class IndexedDataStructure<T> {

    private final List<T> nodes = new ArrayList<>();

    @SafeVarargs
    public IndexedDataStructure(T... values) {
        Collections.addAll(nodes, values);
    }

    public int size() {
        return nodes.size();
    }

    public T get(int index) {
        return nodes.get(index);
    }
}
