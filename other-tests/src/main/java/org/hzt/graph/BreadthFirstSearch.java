package org.hzt.graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by chat GPT 16-12-2022
 */
public final class BreadthFirstSearch {

    private BreadthFirstSearch() {
    }

    public static List<Integer> bfs(int[][] graph, int startNode) {
        List<Integer> visitedNodes = new ArrayList<>();
        boolean[] visited = new boolean[graph.length];
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(startNode);
        visited[startNode] = true;

        while (!queue.isEmpty()) {
            int node = queue.poll();
            visitedNodes.add(node);

            // add all unvisited neighbors to the queue
            for (int neighbor : graph[node]) {
                if (!visited[neighbor]) {
                    queue.offer(neighbor);
                    visited[neighbor] = true;
                }
            }
        }
        return visitedNodes;
    }
}

