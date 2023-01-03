package org.hzt.graph;

// Java Program to Implement Dijkstra's Algorithm
// Using Priority Queue

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

/**
 * A sample program of an implementation of dijkstra's algorithm using a priority queue
 * @see <a href="https://www.geeksforgeeks.org/dijkstras-shortest-path-algorithm-in-java-using-priorityqueue/">
 * *     Dijkstra’s shortest path algorithm in Java using PriorityQueue</a>
 */
public final class PathSearcher {

    private final int[] distances;
    private final Set<Node> settled;
    private final Queue<Node> priorityQueue;
    private final List<List<Node>> adjacencyList;

    public PathSearcher(List<List<Node>> adjacencyList) {
        this.adjacencyList = List.copyOf(adjacencyList);
        final var nrOfNodes = adjacencyList.size();
        distances = new int[nrOfNodes];
        settled = new HashSet<>();
        priorityQueue = new PriorityQueue<>(nrOfNodes, Comparator.comparingInt(Node::cost));
    }

    // Method 1
    // Dijkstra's Algorithm
    public void dijkstra(Node start) {
        Arrays.fill(distances, Integer.MAX_VALUE);
        priorityQueue.add(start);
        distances[start.index] = 0;
        while (!priorityQueue.isEmpty()) {
            Node node = priorityQueue.remove();
            if (settled.contains(node)) {
                continue;
            }
            settled.add(node);
            final var adjListIndex = node.index;
            final var neighbors = adjacencyList.get(adjListIndex);
            for (Node neighbor : neighbors) {
                // If current index hasn't already been processed
                if (settled.contains(neighbor)) {
                    continue;
                }
                int edgeDistance = neighbor.cost;
                int newDistance = distances[adjListIndex] + edgeDistance;

                // If new distance is cheaper in cost
                if (newDistance < distances[neighbor.index]) {
                    distances[neighbor.index] = newDistance;
                }
                // Add the current index to the queue
                priorityQueue.add(new Node(neighbor.index, distances[neighbor.index]));
            }
        }
    }

    public int[] getDistances() {
        return Arrays.copyOf(distances, distances.length);
    }

    record Node(int index, int cost) {
    }
}
