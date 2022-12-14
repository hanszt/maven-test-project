package org.hzt.graph;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertFalse;

class WeightedGraphTest {

    @Test
    void testWeightedGraphReturnsDeepCopy() {
        final var weightedGraph = new WeightedGraph(5)
                .oneWayConnect(0, 2, 4)
                .oneWayConnect(0, 3, 5)
                .oneWayConnect(1, 2, 3)
                .oneWayConnect(2, 3, 10)
                .oneWayConnect(2, 4, 8);

        final var matrix = weightedGraph.adjacencyMatrix();

        matrix[3][2] = 100;

        Arrays.stream(matrix).map(Arrays::toString).forEach(System.out::println);
        System.out.println();
        final var oriMatrix = weightedGraph.adjacencyMatrix();
        Arrays.stream(oriMatrix).map(Arrays::toString).forEach(System.out::println);

        assertFalse(Arrays.deepEquals(matrix, oriMatrix));
    }
}
