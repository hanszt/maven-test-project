package org.hzt.coroutines.sequences

import io.kotest.matchers.sequences.shouldContainInOrder
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.math.pow

class TowerOfHanoiTest {

    /**
     * Example tower of Hanoi game with 3 disks
     *
     *
     * Initial state:                        Goal state:
     * a      b      c                  a      b      c
     * 1    -|-     |      |                  |      |     -|-
     * 2   --|--    |      |                  |      |    --|--
     * 3  ---|---   |      |                  |      |   ---|---
     */
    @Test
    fun `tower of Hanoi with 3 disks`() {
        val nrOfDisks = 3
        val instructions = org.hzt.coroutines.sequences.poc.sequence { moveDisk(nrOfDisks, 'a', 'c', 'b') }
        val expectedNrOfMoves = (2.0.pow(nrOfDisks.toDouble()) - 1).toInt()
        val expected = sequenceOf(
            "Move disk  1 from rod a to rod c",
            "Move disk  2 from rod a to rod b",
            "Move disk  1 from rod c to rod b",
            "Move disk  3 from rod a to rod c",
            "Move disk  1 from rod b to rod a",
            "Move disk  2 from rod b to rod c",
            "Move disk  1 from rod a to rod c"
        )
        assertAll(
            { instructions.count() shouldBe expectedNrOfMoves },
            { instructions shouldContainInOrder expected }
        )
    }

    @ParameterizedTest(name = "When starting with {0} disks, the nr of moves to complete the game should be: {1}")
    @CsvSource(
        "15 -> 32767",
        "10 -> 1023",
        "1 -> 1",
        "2 -> 3",
        "3 -> 7",
        delimiterString = " -> "
    )
    fun `test tower of hanoi nr of moves`(nrOfDisks: Int, expectedNrOfMoves: Int) = assertAll(
        { org.hzt.coroutines.sequences.poc.sequence { moveDisk(nrOfDisks, 'a', 'c', 'b') }.count() shouldBe expectedNrOfMoves },
        { expectedNrOfMoves shouldBe (2.0.pow(nrOfDisks.toDouble()) - 1).toInt() }
    )
}
