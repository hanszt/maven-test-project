package org.hzt.graph;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PathSearcherTest {

    @Test
    void testDijkstraUsingPriorityQueue() {
        int numberOfNodes = 5;
        int source = 0;
        List<List<PathSearcher.Node>> adjacencyList = buildAdjacencyList(numberOfNodes);
        // Calculating the single-source-shortest-path
        PathSearcher pathSearcher = new PathSearcher(numberOfNodes);
        pathSearcher.dijkstra(adjacencyList, new PathSearcher.Node(source, 0));
        final var distances = pathSearcher.getDistances();

        printResult(source, distances);
        final int[] expected = {0, 8, 6, 5 ,3};

        assertArrayEquals(distances, expected);
    }

    private static void printResult(int source, int[] distances) {
        System.out.println("The shortest path from node 0 to each node:");

        for (int i = 0; i < distances.length; i++) {
            System.out.println(source + " to " + i + " is " + distances[i]);
        }
    }

    @NotNull
    private static List<List<PathSearcher.Node>> buildAdjacencyList(int numberOfNodes) {
        // Adjacency list representation of the
        // connected edges by declaring List class object
        // Declaring object of type List<Node>
        List<List<PathSearcher.Node>> adjacencyList = new ArrayList<>();

        // Initialize list for every value
        for (int i = 0; i < numberOfNodes; i++) {
            List<PathSearcher.Node> nodes = new ArrayList<>();
            adjacencyList.add(nodes);
        }

        // create connections
        final var nodes = adjacencyList.get(0);
        nodes.add(new PathSearcher.Node(1, 9));
        nodes.add(new PathSearcher.Node(2, 6));
        nodes.add(new PathSearcher.Node(3, 5));
        nodes.add(new PathSearcher.Node(4, 3));

        final var nodes1 = adjacencyList.get(2);
        nodes1.add(new PathSearcher.Node(1, 2));
        nodes1.add(new PathSearcher.Node(3, 4));
        return adjacencyList;
    }

}
