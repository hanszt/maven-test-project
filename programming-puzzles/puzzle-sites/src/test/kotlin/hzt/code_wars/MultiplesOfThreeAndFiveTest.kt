package hzt.code_wars

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory

class MultiplesOfThreeAndFiveTest {

    @TestFactory
    fun `multiples of three and five`(): List<DynamicTest> {
        fun test(expected: Int, input: Int) = dynamicTest("$input should yield an sum of $expected")
        { assertEquals(expected, solution(input)) }

        return listOf(
            test(23, 10),
            test(78, 20),
            test(9168, 200))
    }
}
