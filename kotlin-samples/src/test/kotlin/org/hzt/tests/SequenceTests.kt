package org.hzt.tests

import io.kotest.matchers.collections.shouldContainInOrder
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class SequenceTests {

    @Test
    fun testScan() {
        val list = listOf("This", "is", "a", "test")
        val scan = list.scan(10) { l, a -> l + a.length }
        scan.shouldContainInOrder(10, 14, 16, 17, 21)
    }

    @ParameterizedTest
    @CsvSource("(1+(2*3)+((8)/4))+1, 3", "(1)+((2))+(((3))), 3")
    fun testMaximumNestingDepthUSingScan(s: String, expected: Int) {
        val actual = s.filter { it in "()"}
            .map{if (it == '(') 1 else -1 }
            .scan(0, Int::plus)
            .max()
        actual shouldBe expected
    }
}
