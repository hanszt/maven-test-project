package org.hzt;

import java.util.Arrays;
import java.util.stream.IntStream;

import static org.hzt.utils.It.print;
import static org.hzt.utils.It.println;

public final class BacktrackingSamples {

    private BacktrackingSamples() {
    }

    /**
     * @see <a href="https://www.geeksforgeeks.org/java-program-for-n-queen-problem-backtracking-3/#:~:text=The%20N%20Queen%20is%20the,blocks%20where%20queens%20are%20placed.">
     *     Java Program for N Queen Problem | Backtracking-3</a>
     */
    public static final class NQueenProblem {

        private NQueenProblem() {
        }

        /* A utility function to check if a queen can
           be placed on board[row][col]. Note that this
           function is called when "col" queens are only
           placed in columns from 0 to col -1. So we need
           to check only left side for attacking queens */
        private static boolean isSafe(char[][] board, int row, int col) {
            /* Check this row on left side */
            for (int x = 0; x < col; x++) {
                if (board[row][x] == 'Q') {
                    return false;
                }
            }
            /* Check upper diagonal on left side */
            for (int y = row, x = col; y >= 0 && x >= 0; y--, x--) {
                if (board[y][x] == 'Q') {
                    return false;
                }
            }
            /* Check lower diagonal on left side */
            for (int y = row, x = col; x >= 0 && y < board.length; y++, x--) {
                if (board[y][x] == 'Q') {
                    return false;
                }
            }
            return true;
        }

        /**
         * This function solves the N Queen problem using
         * Backtracking. It mainly uses solveNQueen() to
         * solve the problem. It returns false if queens
         * cannot be placed, otherwise, return true and
         * places queens in the form of the character Q.
         * Please note that there may be more than one
         * solution, this function computes one of the
         * feasible solutions.
         *
         * @param board the board to populate
         * @param n     the nr of queens to be placed
         * @return whether a solution was found or not
         */
        static boolean solveNQueen(final char[][] board, final int n) {
            /* start at the first column */
            return solveNQueen(board, 0, n);
        }

        /* A recursive utility function to solve N Queen problem */
        static boolean solveNQueen(final char[][] board, final int col, final int n) {
            /* base case: If all queens are placed, then return true */
            if (col >= n) {
                return true;
            }
            /* Consider this column and try placing, this queen in all rows one by one */
            for (int row = 0; row < board.length; row++) {
                /* Check if the queen can be placed on board[row][col] */
                if (isSafe(board, row, col)) {
                    /* Place this queen in board[row][col] */
                    board[row][col] = 'Q';
                    /* recur to place rest of the queens */
                    if (solveNQueen(board, col + 1, n)) {
                        return true;
                    }
                    /* If placing queen in board[row][col] doesn't lead to a solution then remove queen from board[row][col] */
                    board[row][col] = ' '; // BACKTRACK
                }
            }
            /* If the queen can not be placed in any row in this column col, then return false */
            return false;
        }

        static void printBoard(char[][] board) {
            for (var row : board) {
                for (int i = 0; i < row.length; i++) {
                    char value = row[i];
                    print("|" + value + (i == row.length - 1 ? "|" : ""));
                }
                println();
            }
        }
    }

    /**
     * Hamiltonian Path in an undirected graph is a path that visits each vertex exactly once.
     * A Hamiltonian cycle (or Hamiltonian circuit) is a Hamiltonian Path such that there is an edge (in the graph)
     * from the last vertex to the first vertex of the Hamiltonian Path.
     *
     * @see <a href="https://www.geeksforgeeks.org/hamiltonian-cycle-backtracking-6/">Hamiltonian Cycle | Backtracking-6</a>
     */
    static final class HamiltonianCycle {

        private HamiltonianCycle() {
        }

        /**
         *  This function solves the Hamiltonian Cycle problem using
         *  Backtracking. It throws an exception if there is no Hamiltonian Cycle
         *  possible, otherwise return the path.
         * <p>
         *  Please note that there may be more than one solution,
         *  this function returns one of the feasible solutions.
         *
         * @param graph the undirected graph represented by a 2d square matrix
         * @return the hamilton cycle represented by an int array
         */
        static int[] hamCycle(int[][] graph) {
            if (graph.length != graph[0].length) {
                throw new IllegalArgumentException("Malformed graph, it should be square");
            }
            int[] path = new int[graph.length];
            Arrays.fill(path, -1);
            /* Let us put vertex 0 as the first vertex in the path.
            If there is a Hamiltonian Cycle, then the path can be started from any point of the cycle as the graph is undirected */
            path[0] = 0;
            if (hamCycle(graph, path, 1)) {
                final var result = new int[path.length + 1];
                //add another 0 at the end to complete the cycle
                System.arraycopy(path, 0, result, 0, path.length);
                return result;
            }
            throw new IllegalStateException("Solution does not exist");
        }

        static boolean hamCycle(final int[][] graph, final int[] path, final int pos) {
            /* base case: If all vertices are included in Hamiltonian Cycle */
            if (pos == path.length) {
                // And if there is an edge from the last included vertex to the first vertex
                return graph[path[pos - 1]][path[0]] == 1;
            }
            // Try different vertices as a next candidate in Hamiltonian Cycle.
            // We don't try for 0 as we included 0 as starting point in hamCycle()
            for (int v = 1; v < path.length; v++) {
                /* Check if this vertex can be added to HamiltonianCycle */
                if (vertexCanBeAdded(v, graph, path, pos)) {
                    path[pos] = v;
                    /* recur to construct rest of the path */
                    if (hamCycle(graph, path, pos + 1)) {
                        return true;
                    }
                    /* If adding vertex v doesn't lead to a solution, then remove it. (backtracking) */
                    path[pos] = -1;
                }
            }
            /* If no vertex can be added to Hamiltonian Cycle constructed so far, then return false */
            return false;
        }

        static boolean vertexCanBeAdded(int v, int[][] graph, int[] path, int pos) {
            /* Check if this vertex is an adjacent vertex of the previously added vertex. */
            if (graph[path[pos - 1]][v] == 0) {
                return false;
            }
            /* Check if the vertex has already been included. This step can be optimized by creating an array of size V */
            return IntStream.range(0, pos).noneMatch(i -> path[i] == v);
        }
    }
}
