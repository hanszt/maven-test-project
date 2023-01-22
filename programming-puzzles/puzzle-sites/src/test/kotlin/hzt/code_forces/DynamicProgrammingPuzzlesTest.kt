package hzt.code_forces

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class DynamicProgrammingPuzzlesTest {

    @TestFactory
    fun `telepanting ants test`(): List<DynamicTest> {
        fun test(x: IntArray, y: IntArray, s: IntArray, expected: Int): DynamicTest {
            val distance = telepanting(x, y, s)
            return DynamicTest.dynamicTest("The traveled distance when the nr of portals is ${x.size} should be: $distance")
            { distance shouldBe expected }
        }
        return listOf(
            test(intArrayOf(3, 6, 7, 8), intArrayOf(2, 5, 4, 1), intArrayOf(0, 1, 0, 1), 23),
            test(intArrayOf(454971987), intArrayOf(406874902), intArrayOf(1), 503069073),
            test(
                intArrayOf(243385510, 644426565, 708622105, 786625660, 899754846),
                intArrayOf(42245605, 574769163, 208990040, 616437691, 382774619),
                intArrayOf(0, 0, 0, 0, 0),
                899754847
            ),
            test(
                intArrayOf(200000000, 600000000, 800000000, 900000000, 1000000000),
                intArrayOf(100000000, 400000000, 300000000, 700000000, 500000000),
                intArrayOf(1, 0, 0, 1, 0),
                3511295
            )
        )
    }


}
