package org.hzt;

import org.hzt.graph.Graph;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.hzt.utils.It.println;
import static org.junit.jupiter.api.Assertions.*;

class BacktrackingSamplesTest {

    @Nested
    class NQueenProblemTests {
        @Test
        void testNQueenProblem4By4() {
            final var n = 4;
            char[][] board = createSquareBoard(n);

            final var solutionFound = BacktrackingSamples.NQueenProblem.solveNQueen(board, n);

            BacktrackingSamples.NQueenProblem.printBoard(board);

            char[][] expected = {
                    {' ', ' ', 'Q', ' '},
                    {'Q', ' ', ' ', ' '},
                    {' ', ' ', ' ', 'Q'},
                    {' ', 'Q', ' ', ' '}};

            assertAll(
                    () -> assertTrue(solutionFound),
                    () -> assertArrayEquals(expected, board)
            );
        }

        @Test
        void testNoSolutionFor1RowGridWithTwoQueens() {
            char[][] board = {{' ', ' ', ' ', ' '}};
            final var solutionFound = BacktrackingSamples.NQueenProblem.solveNQueen(board, 2);
            assertFalse(solutionFound);
        }

        @Test
        void testNQueenProblem8By4() {
            char[][] board = {
                    {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                    {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                    {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                    {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}};

            final var solutionFound = BacktrackingSamples.NQueenProblem.solveNQueen(board, 4);

            BacktrackingSamples.NQueenProblem.printBoard(board);

            char[][] expected = {
                    {' ', ' ', 'Q', ' ', ' ', ' ', ' ', ' '},
                    {'Q', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                    {' ', ' ', ' ', 'Q', ' ', ' ', ' ', ' '},
                    {' ', 'Q', ' ', ' ', ' ', ' ', ' ', ' '}};

            assertAll(
                    () -> assertTrue(solutionFound),
                    () -> assertArrayEquals(expected, board)
            );
        }

        @Test
        void testNQueenProblem8By8() {
            final var n = 8;
            char[][] board = createSquareBoard(n);

            final var solutionFound = BacktrackingSamples.NQueenProblem.solveNQueen(board, n);

            BacktrackingSamples.NQueenProblem.printBoard(board);

            char[][] expected = {
                    {'Q', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                    {' ', ' ', ' ', ' ', ' ', ' ', 'Q', ' '},
                    {' ', ' ', ' ', ' ', 'Q', ' ', ' ', ' '},
                    {' ', ' ', ' ', ' ', ' ', ' ', ' ', 'Q'},
                    {' ', 'Q', ' ', ' ', ' ', ' ', ' ', ' '},
                    {' ', ' ', ' ', 'Q', ' ', ' ', ' ', ' '},
                    {' ', ' ', ' ', ' ', ' ', 'Q', ' ', ' '},
                    {' ', ' ', 'Q', ' ', ' ', ' ', ' ', ' '}};

            assertAll(
                    () -> assertTrue(solutionFound),
                    () -> assertArrayEquals(expected, board)
            );
        }

        @Test
        void testNQueenProblemLargeGrid() {
            final var n = 24;
            char[][] board = createSquareBoard(n);
            final var solutionFound = BacktrackingSamples.NQueenProblem.solveNQueen(board, n);

            BacktrackingSamples.NQueenProblem.printBoard(board);

            assertTrue(solutionFound);
        }

        private static char[][] createSquareBoard(int n) {
            char[][] board = new char[n][];
            for (int row = 0; row < board.length; row++) {
                final var aRow = new char[n];
                Arrays.fill(aRow, ' ');
                board[row] = aRow;
            }
            return board;
        }
    }

    @Nested
    class HamiltonianCycleTests {

        /**
         * The graph is represented by a 2d square matrix. 1 at index i, j means that there is an edge between
         * node i and node j
         * <p>
         * 0 at index ii, jj means that there is no  connection between node ii and node jj
         */
        @Test
        void testHamiltonCycleExists() {
        /* Let us create the following graph
           (0)--(1)--(2)
            |   / \   |
            |  /   \  |
            | /     \ |
           (3)-------(4)    */
            int[][] graph1 = {
                    {0, 1, 0, 1, 0},
                    {1, 0, 1, 1, 1},
                    {0, 1, 0, 0, 1},
                    {1, 1, 0, 0, 1},
                    {0, 1, 1, 1, 0},
            };
            // Print the solution
            final var cycle = BacktrackingSamples.HamiltonianCycle.hamCycle(graph1);

            println("Solution Exists: Following is one Hamiltonian Cycle");
            println(Arrays.toString(cycle));

            assertArrayEquals(new int[] {0, 1, 2, 4, 3, 0}, cycle);
        }

        @Test
        void testLargerHamiltonCycleExists() {
        /* Let us create the following graph
           (0)--(1)--(2)--(5)---------(7)
            |   / \   |   /             \
            |  /   \  |  /              (8)
            | /     \ | /               /
           (3)-------(4)--------------(6)
           */
            final var graph = new Graph(9)
                    .oneWayConnect(0, 1, 3)
                    .oneWayConnect(1, 0, 2, 3, 4)
                    .oneWayConnect(2, 1, 4, 5)
                    .biDiConnect(3, 0, 1, 4)
                    .oneWayConnect(4, 1, 2, 3, 5, 6)
                    .oneWayConnect(5, 2, 4, 7)
                    .oneWayConnect(6, 4, 8)
                    .oneWayConnect(7, 5, 8)
                    .oneWayConnect(8, 7)
                    .adjacencyMatrix();
            // Print the solution
            final var cycle = BacktrackingSamples.HamiltonianCycle.hamCycle(graph);

            println("Solution Exists: Following is one Hamiltonian Cycle");
            println(Arrays.toString(cycle));

            assertArrayEquals(new int[] {0, 3, 4, 6, 8, 7, 5, 2, 1, 0}, cycle);
        }

        @Test
        void testNoSolution() {
            /* Let us create the following graph
           (0)--(1)--(2)
            |   / \   |
            |  /   \  |
            | /     \ |
           (3)       (4)    */
            int[][] graph2 = {
                    {0, 1, 0, 1, 0},
                    {1, 0, 1, 1, 1},
                    {0, 1, 0, 0, 1},
                    {1, 1, 0, 0, 0},
                    {0, 1, 1, 0, 0},
            };
            final var exception = assertThrows(IllegalStateException.class, () -> BacktrackingSamples.HamiltonianCycle.hamCycle(graph2));
            final var message = exception.getMessage();
            assertEquals("Solution does not exist", message);
        }
    }

}
