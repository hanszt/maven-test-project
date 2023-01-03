package org.hzt.graph;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hzt.graph.PathSearcher.*;
import static org.hzt.utils.It.println;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
class PathSearcherTest {

    @Test
    @Tag("UnitTest")
    void testDijkstraUsingPriorityQueue() {
        int numberOfNodes = 5;
        int source = 0;
        List<List<Node>> adjacencyList = buildAdjacencyList(numberOfNodes);
        // Calculating the single-source-shortest-path
        PathSearcher pathSearcher = new PathSearcher(adjacencyList);
        pathSearcher.dijkstra(new Node(source, 0));
        final var distances = pathSearcher.getDistances();

        printResult(source, distances);
        final int[] expected = {0, 8, 6, 5 ,3};

        assertArrayEquals(distances, expected);
    }

    private static void printResult(int source, int[] distances) {
        println("The shortest path from node 0 to each node:");

        for (int i = 0; i < distances.length; i++) {
            println(source + " to " + i + " is " + distances[i]);
        }
    }

    @NotNull
    private static List<List<Node>> buildAdjacencyList(int numberOfNodes) {
        List<List<Node>> adjacencyList = new ArrayList<>();

        // Initialize list for every index
        for (int i = 0; i < numberOfNodes; i++) {
            adjacencyList.add(new ArrayList<>());
        }
        // create connections
        final var nodes = adjacencyList.get(0);
        nodes.add(new Node(1, 9));
        nodes.add(new Node(2, 6));
        nodes.add(new Node(3, 5));
        nodes.add(new Node(4, 3));

        final var nodes1 = adjacencyList.get(2);
        nodes1.add(new Node(1, 2));
        nodes1.add(new Node(3, 4));
        return adjacencyList;
    }

}
