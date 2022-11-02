package hzt.leetcode;

import org.hzt.utils.strings.StringX;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hzt.utils.Patterns.commaPattern;
import static org.hzt.utils.Patterns.pipePattern;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ArrayPuzzlesTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "4 k=5 -> 1",
            "10,5,2,6 k=100 -> 8",
            "1,2,3 k=0 -> 0",
            "10,9,10,4,3,8,3,3,6,2,10,10,9,3 k=19 -> 18"})
    void testNrOfSubArraysLessThanNrK(final String string) {
        final var split = StringX.of(string).split(" -> ");

        final var input = StringX.of(split.first()).split(" k=");
        final var arrayAsString = input.first();
        final var k = Integer.parseInt(input.last());

        final var ints = StringX.of(arrayAsString).split(",")
                .toIntArray(Integer::parseInt);

        final var result = ArrayPuzzles.subArrayProductLessThanNrK(ints, k);
        final var resultFast = ArrayPuzzles.numSubarrayProductLessThanKFast(ints, k);

        final int expected = Integer.parseInt(split.last());

        assertAll(
                () -> assertEquals(expected, result),
                () -> assertEquals(expected, resultFast)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "4 -> 0",
            "2,900 -> 898",
            "1,2,3 -> 2",
            "1,0,0,8,6 -> 14",
            "1,10,2,9 -> 16",
            "386516853,230332704,203125577,66379082,50986744,48321315,-212123143,-250908656,-349566234,-425653504 -> 2127271182"
    })
    void testMinMovesToEqualElement(final String s) {
        final var split = StringX.of(s).split(" -> ");
        final var ints = StringX.of(split.first()).split(",").toIntArray(Integer::parseInt);

        final var result = ArrayPuzzles.minMoves2(ints);

        final int expected = Integer.parseInt(split.last());

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "10,5,0| 15,2,1| 25,1,1| 30,4,0 -> 6",
            "7,1000000000,1| 15,3,0| 5,999999995,0| 5,1,1 -> 999999984",
            "7,2000000000,0| 15,3,1| 5,999999945,0| 5,8,1 -> 999999926"
    })
    void testNrOfBacklogOrders(String string) {
        final var splitByArrow = StringX.of(string).split(" -> ");

        final var orders = extractBackOrderArrayByStream(splitByArrow.first());

        int actualNr = ArrayPuzzles.getNumberOfBacklogOrders(orders);

        int expectedNr = Integer.parseInt(splitByArrow.last());

        assertEquals(expectedNr, actualNr);
    }

    private static int[][] extractBackOrderArrayByStream(@NotNull String backlogOrdersAsString) {
        return pipePattern.splitAsStream(backlogOrdersAsString)
                .map(s -> commaPattern.splitAsStream(s)
                        .mapToInt(Integer::parseInt)
                        .toArray())
                .toArray(int[][]::new);
    }

}
