package hzt

import hzt.leetcode.ArrayPuzzles
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.assertEquals

internal class StringPuzzlesTest {

    @ParameterizedTest(name = "The zigzag conversion of {0} with {1} rows, should be: {2}")
    @CsvSource(value = [
            "AB, 1, AB",
            "PAYPALISHIRING, 3, PAHNAPLSIIGYIR",
            "PAYPALISHIRING, 4, PINALSIGYAHRPI",
            "PAYPALISHIRING, 5, PHASIYIRPLIGAN"]
    )
    fun testZigZagConversion(inputString: String, nrOfRows: Int, expected: String) {
        val actual = inputString.zigZagConversion(nrOfRows)
        assertEquals(expected, actual)
    }

    @ParameterizedTest(name = "With input: {0}, the nr of backlog orders should be: {1}")
    @CsvSource(value = [
        "10, 5, 0 | 15, 2, 1 | 25, 1, 1 | 30, 4, 0 -> 6",
        "7, 1000000000, 1 | 15, 3, 0 | 5, 999999995, 0 | 5, 1, 1 -> 999999984"
    ], delimiterString = " -> ")
    fun testNrOfBacklogOrders(ordersAsString: String, expected: Int) {
        val orders = ordersAsString.split(" | ")
            .map { it.split(", ").map(String::toInt).toIntArray() }
            .toTypedArray()

        val actualNr = ArrayPuzzles.getNumberOfBacklogOrders(*orders)

        assertEquals(expected, actualNr)
    }

    @TestFactory
    fun `test nr of distinct sequences`(): List<DynamicTest> {
        fun test(s: String, t: String, expected: Int): DynamicTest {
            val displayName = "The nr of distinct subsequences of '$s' which equal '$t', should be: $expected"
            val actual = numDistinct(s, t)
            return dynamicTest(displayName) { assertEquals(expected, actual) }
        }
        return listOf(
            test("rabbbit", "rabbit", 3),
            test("babgbag", "bag", 5),
            test("loop", "ga", 0),
            test("pennenlikker", "pen", 4)
        )
    }
}
