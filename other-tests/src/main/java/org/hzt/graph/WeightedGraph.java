package org.hzt.graph;

import java.util.Arrays;

public final class WeightedGraph {

    private final int[][] adjacencyMatrix;

    public WeightedGraph(int nrOfNodes) {
        this.adjacencyMatrix = new int[nrOfNodes][nrOfNodes];
    }

    public WeightedGraph oneWayConnect(int vertex, int other, int weight) {
        if (vertex >= adjacencyMatrix.length || other >= adjacencyMatrix.length) {
            throw new IllegalArgumentException();
        }
        adjacencyMatrix[vertex][other] = weight;
        return this;
    }

    public WeightedGraph biDiConnect(int vertex, int other, int weight) {
        if (vertex >= adjacencyMatrix.length || other >= adjacencyMatrix.length) {
            throw new IllegalArgumentException();
        }
        adjacencyMatrix[vertex][other] = weight;
        adjacencyMatrix[other][vertex] = weight;
        return this;
    }

    public int[][] adjacencyMatrix() {
        return Arrays.stream(adjacencyMatrix)
                .map(row -> Arrays.copyOf(row, row.length))
                .toArray(int[][]::new);
    }
}
