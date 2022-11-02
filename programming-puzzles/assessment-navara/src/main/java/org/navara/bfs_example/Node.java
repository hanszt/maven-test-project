package org.navara.bfs_example;

import java.util.HashSet;
import java.util.Set;

public final class Node<T> {

    private final T value;
    private final Set<Node<T>> neighbors;

    private Node(final T value) {
        this.value = value;
        this.neighbors = new HashSet<>();
    }

    public static <T> Node<T> of(final T t) {
        return new Node<>(t);
    }

    public void connect(final Node<T> node) {
        if (this == node) {
            throw new IllegalArgumentException("Can't connect node to itself");
        }
        this.neighbors.add(node);
        node.neighbors.add(this);
    }

    public T getValue() {
        return value;
    }

    public Set<Node<T>> getNeighbors() {
        return Set.copyOf(neighbors);
    }
}
