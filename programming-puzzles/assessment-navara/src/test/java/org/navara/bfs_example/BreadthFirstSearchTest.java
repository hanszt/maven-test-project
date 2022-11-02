package org.navara.bfs_example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BreadthFirstSearchTest {

    @Test
    void testSearchWithBfs() {
        final Node<Integer> start = Node.of(10);
        final Node<Integer> firstNeighbor = Node.of(2);
        start.connect(firstNeighbor);

        final Node<Integer> firstNeighborNeighbor = Node.of(3);
        firstNeighbor.connect(firstNeighborNeighbor);
        firstNeighborNeighbor.connect(start);

        final Node<Integer> secondNeighbor = Node.of(4);
        start.connect(secondNeighbor);

        final var result = BreadthFirstSearch
                .withMessageConsumer(System.out::println)
                .search(4, firstNeighborNeighbor)
                .orElseThrow();

        assertEquals(secondNeighbor, result);
    }

    @Test
    void testTreeSearchWithBfs() {
        final Tree<Integer> root = Tree.of(10);
        final Tree<Integer> rootFirstChild = root.addChild(2);
        rootFirstChild.addChild(3);
        final Tree<Integer> rootSecondChild = root.addChild(4);

        final var result = BreadthFirstSearch
                .withMessageConsumer(System.out::println)
                .search(4, root)
                .orElseThrow();

        assertEquals(rootSecondChild, result);

    }
}
