package hzt;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TowerOfHanoiTest {

    /**
     * Example tower of Hanoi game with 3 disks
     * <p>
     * Initial state:                        Goal state:
     *       a      b      c                  a      b      c
     * 1    -|-     |      |                  |      |     -|-
     * 2   --|--    |      |                  |      |    --|--
     * 3  ---|---   |      |                  |      |   ---|---
     */
    @Test
    void towerOfHanoiWith3Disks() {
        final var nrOfDisks = 3;

        final var instructions = TowerOfHanoi.playTowerOfHanoiGame(nrOfDisks);

        final var expectedNrOfMoves = (int) (Math.pow(2, nrOfDisks) - 1);

        final var expected = List.of(
                "Move disk  1 from rod a to rod c",
                "Move disk  2 from rod a to rod b",
                "Move disk  1 from rod c to rod b",
                "Move disk  3 from rod a to rod c",
                "Move disk  1 from rod b to rod a",
                "Move disk  2 from rod b to rod c",
                "Move disk  1 from rod a to rod c");

        assertAll(
                () -> assertEquals(expectedNrOfMoves, instructions.size()),
                () -> assertEquals(expected, instructions)
        );
    }

    @Test
    void towerOfHanoiWith15Disks() {
        final var nrOfDisks = 15;

        final var instructions = TowerOfHanoi.playTowerOfHanoiGame(nrOfDisks);

        final var expected = (int) (Math.pow(2, nrOfDisks) - 1);
        final var expectedNrOfMoves = 32767;

        assertAll(
                () -> assertEquals(expectedNrOfMoves, instructions.size()),
                () -> assertEquals(expected, expectedNrOfMoves)
        );
    }

}
