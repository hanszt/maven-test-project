package org.hzt.graph;

import java.util.Arrays;

public final class Graph {

    private final int[][] adjacencyMatrix;

    public Graph(int nrOfNodes) {
        this.adjacencyMatrix = new int[nrOfNodes][nrOfNodes];
    }

    public Graph oneWayConnect(int vertex, int... others) {
        if (vertex >= adjacencyMatrix.length || others.length > adjacencyMatrix.length) {
            throw new IllegalArgumentException();
        }
        for (int other : others) {
            adjacencyMatrix[vertex][other] = 1;
        }
        return this;
    }

    public Graph biDiConnect(int vertex, int... others) {
        if (vertex >= adjacencyMatrix.length || others.length > adjacencyMatrix.length) {
            throw new IllegalArgumentException();
        }
        for (int other : others) {
            adjacencyMatrix[vertex][other] = 1;
            adjacencyMatrix[other][vertex] = 1;
        }
        return this;
    }

    public int[][] adjacencyMatrix() {
        return Arrays.stream(adjacencyMatrix)
                .map(row -> Arrays.copyOf(row, row.length))
                .toArray(int[][]::new);
    }
}
