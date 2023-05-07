package org.hzt

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

internal class OverloadsTest {

    @Test
    fun `overload with different list content possible when specifying jvm name`() {
        val intList = listOf(1, 2, 3, 4)
        val intValues = listOfValues(intList)
        println(intValues.joinToString())
        val longValues = listOfValues(listOf(1L, 2L, 3L, 4L))
        println(longValues.joinToString())
        val strings = listOfValues(listOf("this", "is", "a", "test"))

        assertAll(
            { intValues.size shouldBe longValues.size },
            { intValues.last() shouldBe longValues.last() * 2 },
            { strings.size shouldBe intValues.size }
        )
    }

    @Test
    fun `overload with different lambda's is possible`() {
        val string: String = callBackOverload<String> { "Hello" }
        val long = callBackOverload { 5L }
        string.length shouldBe long.toInt()
    }
}
