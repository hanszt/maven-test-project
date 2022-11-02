package hzt.floodfill;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Q4IslandsTest {

    private static final Consumer<int[][]> printGrid = grid -> Arrays.stream(grid)
            .map(Arrays::toString)
            .forEach(System.out::println);

    private static final Function<String, int[]> lineToIntArray = s -> Arrays.stream(s.split(" "))
            .mapToInt(Integer::parseInt)
            .toArray();

    @Test
    void testFindNrOfIslands() {
        final var input = getInput();

        System.out.println("Before flood fill");
        printGrid.accept(input);

        final var nrOfIslands = Q4Islands.findNrOfIslands(input, Q4Islands::floodFill);

        System.out.println("After flood fill");
        printGrid.accept(input);

        assertEquals(4, nrOfIslands);
    }

    @Test
    void testFindNrsOfIslandsFloodFillRecursive() {
        final int expectedResult = 4;
        final int[][] grid = getInput();

        final int result = Q4Islands.findNrOfIslands(grid, Q4Islands::floodFillRecursive);
        assertEquals(expectedResult, result);
    }

    private static int[][] getInput() {
        try (final var lines = Files.lines(Path.of("input/IslandsInput.txt"))) {
            return lines
                    .map(lineToIntArray)
                    .toArray(int[][]::new);
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
