package hzt.leetcode;

import org.hzt.utils.strings.StringX;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BinaryPuzzlesTest {
    @ParameterizedTest
    @ValueSource(strings = {
            "1 -> 0",
            "10 -> 1",
            "1101 -> 6",
            "1111011110000011100000110001011011110010111001010111110001 -> 85",
            "1111110011101010110011100100101110010100101110111010111110110010 -> 89"
    })
    void testNrOfStepsToReduceToOne(final String string) {
        final var split = StringX.of(string).split(" -> ");
        final var input = split.first();

        final var numSteps = BinaryPuzzles.numStepsToReduceToOne(input);

        final var expected = Integer.parseInt(split.last());

        assertEquals(expected, numSteps);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "11,1 -> 100",
            "1010,1011 -> 10101"
    })
    void testAddBinary(final String string) {
        final var split = StringX.of(string).split(" -> ");
        final var nrs = StringX.of(split.first()).split(",");

        final var sum = BinaryPuzzles.addBinary(nrs.first(), nrs.last());

        final var expectedSum = split.last();
        assertEquals(expectedSum, sum);
    }


}
