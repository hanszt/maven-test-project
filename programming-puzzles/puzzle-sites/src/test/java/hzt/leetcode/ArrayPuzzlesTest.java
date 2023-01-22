package hzt.leetcode;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

class ArrayPuzzlesTest {

    @TestFactory
    Stream<DynamicTest> testNrOfSubArraysLessThanNrK() {
        record Test(int expected, int k, int... ints) {
            DynamicTest toDynamicTest() {
                var displayName = "The nr of sub arrays of " + Arrays.toString(ints) + " where it's product is less than " + k + ", should be: " + expected;

                var result = ArrayPuzzles.subArrayProductLessThanNrK(ints, k);
                var resultFast = ArrayPuzzles.numSubarrayProductLessThanKFast(ints, k);

                return dynamicTest(displayName, () -> assertAll(
                        () -> assertEquals(expected, result),
                        () -> assertEquals(expected, resultFast)
                ));
            }
        }
        return Stream.of(
                new Test(1, 5, 4),
                new Test(0, 4, 4),
                new Test(8, 100, 10, 5, 2, 6),
                new Test(0, 0, 1, 2, 3),
                new Test(18, 19, 10, 9, 10, 4, 3, 8, 3, 3, 6, 2, 10, 10, 9, 3)).map(Test::toDynamicTest);
    }

    @TestFactory
    Stream<DynamicTest> testMinMovesToEqualElement() {
        record Test(int expected, int... ints) {
            DynamicTest toDynamicTest() {
                var result = ArrayPuzzles.minMoves2(ints);
                var displayName = "The minimum nr of moves to make all elements in " + Arrays.toString(ints) + " equal, should be: " + expected;
                return dynamicTest(displayName, () -> assertEquals(expected, result));
            }
        }
        return Stream.of(
                new Test(0, 4),
                new Test(898, 2, 900),
                new Test(2, 1, 2, 3),
                new Test(14, 1, 0, 0, 8, 6),
                new Test(2127271182, 386516853, 230332704, 203125577, 66379082, 50986744, 48321315, -212123143, -250908656, -349566234, -425653504),
                new Test(16, 1, 10, 2, 9)).map(Test::toDynamicTest);
    }

    @TestFactory
    Stream<DynamicTest> testNrOfBacklogOrders() {
        return Stream.of(
                nrOfBacklogOrdersTest(6, new int[][]{{10, 5, 0}, {15, 2, 1}, {25, 1, 1}, {30, 4, 0}}),
                nrOfBacklogOrdersTest(999_999_984, new int[][]{{7, 1000000000, 1}, {15, 3, 0}, {5, 999999995, 0}, {5, 1, 1}}),
                nrOfBacklogOrdersTest(999_999_926, new int[][]{{7, 2000000000, 0}, {15, 3, 1}, {5, 999999945, 0}, {5, 8, 1}}),
                nrOfBacklogOrdersTest(52, new int[][]{{10, 5, 1}, {20, 2, 1}, {25, 7, 0}, {34, 52, 1}})
        );
    }

    DynamicTest nrOfBacklogOrdersTest(int expected, int[]... orders) {
        var ordersList = Arrays.stream(orders).map(Arrays::toString).toList();
        var displayName = "With input: %s, the nr of backlog orders should be: %d".formatted(ordersList, expected);
        int actual = ArrayPuzzles.getNumberOfBacklogOrders(orders);
        return dynamicTest(displayName, () -> assertEquals(expected, actual));
    }

}
