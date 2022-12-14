package hzt.leetcode;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BinaryPuzzlesTest {
    @ParameterizedTest(name = "The nr of steps to reduce `{0}` to one should be: {1}")
    @CsvSource(value = {
            "1 -> 0",
            "10 -> 1",
            "1101 -> 6",
            "1111011110000011100000110001011011110010111001010111110001 -> 85",
            "1111110011101010110011100100101110010100101110111010111110110010 -> 89"
    }, delimiterString = " -> ")
    void testNrOfStepsToReduceToOne(final String input, int expected) {
        final var numSteps = BinaryPuzzles.numStepsToReduceToOne(input);
        assertEquals(expected, numSteps);
    }

    @ParameterizedTest(name = "The binary sum of `{0}` and `{1}`, should be: `{2}`")
    @CsvSource(value = {
            "11, 1, 100",
            "1010, 1011, 10101"
    })
    void testAddBinary(final String first, String second, String expectedSum) {
        final var sum = BinaryPuzzles.addBinary(first, second);
        assertEquals(expectedSum, sum);
    }


}
