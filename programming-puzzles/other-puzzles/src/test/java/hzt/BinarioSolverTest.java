package hzt;

import org.hzt.utils.sequences.Sequence;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BinarioSolverTest {

    @Test
    void testSolveBinarioPuzzleEasy() {
        final var solution = new BinarioSolver()
                .readAndSolve(Path.of("input/binario_solver/puzzle-easy.txt"));

        assertEquals(6, solution.length);
    }

    @Test
    void testSolveBinarioPuzzleHard() {
        final var solution = new BinarioSolver()
                .readAndSolve(Path.of("input/binario_solver/puzzle1.txt"));

        Sequence.of(solution)
                .map(Arrays::toString)
                .forEach(System.out::println);

        assertEquals(14, solution.length);
    }
}
