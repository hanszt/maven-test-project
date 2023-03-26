package org.hzt

import io.kotest.matchers.collections.shouldContainInOrder
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class SequenceTests {

    @Test
    fun `test scan`() = listOf("This", "is", "a", "test")
        .scan(10) { result, string -> result + string.length }
        .shouldContainInOrder(10, 14, 16, 17, 21)

    @ParameterizedTest(name = "The maximum nesting depth of `{0}` should be `{1}`")
    @CsvSource(
        "(1+(2*3)+((8)/4))+1 -> 3",
        "(1)+((2))+(((3))) -> 3",
        "2 -> 0",
        "(2) -> 1",
        "(((((((((((((4 -> 13",
        "17)))))))) -> 0",
        delimiterString = " -> ")
    fun `test maximum nesting-depth using scan`(s: String, expected: Int) =
        s.filter { it in "()" }
            .map { if (it == '(') 1 else -1 }
            .scan(0, Int::plus)
            .max() shouldBe expected
}
