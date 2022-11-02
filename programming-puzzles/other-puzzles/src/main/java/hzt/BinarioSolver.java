package hzt;

import org.hzt.utils.It;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.sequences.primitives.IntSequence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;
import java.util.regex.Pattern;

public class BinarioSolver {

    private static final Pattern WHITE_SPACE_PATTERN = Pattern.compile(" ");

    public int[][] readAndSolve(final Path path) {
        try (final var lines = Files.lines(path)) {
            final var input = lines
                    .map(BinarioSolver::toIntArray)
                    .toArray(int[][]::new);
            return solvePuzzle(input);
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static int[][] solvePuzzle(final int[][] input) {
        final Function<int[], String> arrayToString = a -> IntSequence.of(a)
                .mapToObj(BinarioSolver::toChar)
                .joinToString();

        Sequence.of(input)
                .map(arrayToString)
                .forEach(It::println);
        return input;
    }

    private static int[] toIntArray(final String s) {
        return WHITE_SPACE_PATTERN.splitAsStream(s)
                .mapToInt(BinarioSolver::toInt)
                .toArray();
    }

    private static int toInt(final String s) {
        return switch (s) {
            case "x" -> -1;
            case "0" -> 0;
            case "1" -> 1;
            default -> throw new IllegalStateException(s + " not supported");
        };
    }

    private static char toChar(final int i) {
        return switch (i) {
            case -1 -> 'x';
            case 0 -> '0';
            case 1 -> '1';
            default -> throw new IllegalStateException(i + " not supported");
        };
    }
}
