package hzt

import hzt.leetcode.ArrayPuzzles
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.assertEquals

internal class StringPuzzlesTest {

    @ParameterizedTest
    @ValueSource(
        strings = [
            "AB, 1 -> AB",
            "PAYPALISHIRING, 3 -> PAHNAPLSIIGYIR",
            "PAYPALISHIRING, 4 -> PINALSIGYAHRPI",
            "PAYPALISHIRING, 5 -> PHASIYIRPLIGAN"]
    )
    fun testZigZagConversion(string: String) {
        val (inputString, nrOfRows, expected) = string.split(", ", " -> ")

        val actual = inputString.zigZagConversion(nrOfRows.toInt())

        assertEquals(expected, actual)
    }

    @ParameterizedTest
    @ValueSource(strings = [
        "10,5,0| 15,2,1| 25,1,1| 30,4,0 -> 6",
        "7,1000000000,1| 15,3,0| 5,999999995,0| 5,1,1 -> 999999984"
    ])
    fun testNrOfBacklogOrders(string: String) {
        val (ordersAsString, expected) = string.split(" -> ")

        val orders = ordersAsString.split("| ")
            .map { it.split(",").map(String::toInt).toIntArray() }
            .toTypedArray()

        val actualNr = ArrayPuzzles.getNumberOfBacklogOrders(orders)

        assertEquals(expected.toInt(), actualNr)
    }
}
