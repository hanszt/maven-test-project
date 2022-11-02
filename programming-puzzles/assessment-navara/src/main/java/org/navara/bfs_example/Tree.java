package org.navara.bfs_example;

import java.util.ArrayList;
import java.util.List;

public final class Tree<T> {

    private final T value;
    private final List<Tree<T>> children;

    private Tree(final T value) {
        this.value = value;
        this.children = new ArrayList<>();
    }

    public static <T> Tree<T> of(final T value) {
        return new Tree<>(value);
    }

    public Tree<T> addChild(final T value) {
        final Tree<T> newChild = new Tree<>(value);
        children.add(newChild);
        return newChild;
    }

    public T getValue() {
        return value;
    }

    public List<Tree<T>> getChildren() {
        return List.copyOf(children);
    }
}
