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
 * <a href="https://www.geeksforgeeks.org/dijkstras-shortest-path-algorithm-in-java-using-priorityqueue/">
 * *     Dijkstra’s shortest path algorithm in Java using PriorityQueue</a>
 */
@SuppressWarnings("squid:S2384")
public class PathSearcher {

    private final int[] distances;
    private final Set<Node> settled;
    private final Queue<Node> priorityQueue;
    private List<List<Node>> adjacencyList;

    public PathSearcher(int numberOfNodes) {
        distances = new int[numberOfNodes];
        settled = new HashSet<>();
        priorityQueue = new PriorityQueue<>(numberOfNodes, Comparator.comparingInt(Node::cost));
    }

    // Method 1
    // Dijkstra's Algorithm
    public void dijkstra(List<List<Node>> adjacencyList, Node start) {
        this.adjacencyList = adjacencyList;
        Arrays.fill(distances, Integer.MAX_VALUE);
        // Add source value to the priority queue
        priorityQueue.add(start);
        // Distance to the source is 0
        distances[start.value] = 0;
        while (settled.size() != distances.length) {
            // Terminating condition check when
            // the priority queue is empty, return
            if (priorityQueue.isEmpty()) {
                return;
            }
            // Removing the minimum distance value from the priority queue
            Node node = priorityQueue.remove();

            // Adding the value whose distance is finalized
            if (!settled.contains(node)) {
                // Continue keyword skips execution for following check
                // We don't have to call e_Neighbors(u)
                // if u is already present in the settled set.
                settled.add(node);
                processNeighbors(node);
            }
        }
    }

    private void processNeighbors(Node node) {
        final var adjListIndex = node.value;
        final var neighbors = adjacencyList.get(adjListIndex);
        for (Node neighbor : neighbors) {
            // If current value hasn't already been processed
            if (!settled.contains(neighbor)) {
                int edgeDistance = neighbor.cost;
                int newDistance = distances[adjListIndex] + edgeDistance;

                // If new distance is cheaper in cost
                if (newDistance < distances[neighbor.value]) {
                    distances[neighbor.value] = newDistance;
                }
                // Add the current value to the queue
                priorityQueue.add(new Node(neighbor.value, distances[neighbor.value]));
            }
        }
    }

    public int[] getDistances() {
        return distances;
    }

    record Node(int value, int cost) {
    }
}
