package org.hzt.graph;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BreadthFirstSearchTest {

    @Test
    void testBfsByChatGpt() {
        // graph represented as an adjacency list
        int[][] graph = {
                {1, 2, 3}, // nodes connected to node 0
                {0, 4, 5}, // nodes connected to node 1
                {0, 6, 7}, // nodes connected to node 2
                {0},       // nodes connected to node 3
                {1},       // nodes connected to node 4
                {1},       // nodes connected to node 5
                {2},       // nodes connected to node 6
                {2}        // nodes connected to node 7
        };
        // perform breadth-first search starting from node 0
        final var visitedNodes = BreadthFirstSearch.bfs(graph, 0);
        assertEquals(List.of(0, 1, 2, 3, 4, 5, 6, 7), visitedNodes);
    }
}
