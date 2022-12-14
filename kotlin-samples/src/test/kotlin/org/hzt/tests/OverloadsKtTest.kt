package org.hzt.tests

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import kotlin.test.assertEquals

internal class OverloadsKtTest {

    @Test
    fun `overload with different list content possible when specifying jvm name`() {
        val intList = listOf(1, 2, 3, 4)
        val intValues = listOfValues(intList)
        val longValues = listOfValues(listOf(1L, 2L, 3L, 4L))
        val strings = listOfValues(listOf("this", "is", "a", "test"))

        assertAll(
            { assertEquals(intValues.size, longValues.size) },
            { assertEquals(strings.size, intValues.size) }
        )
    }

    @Test
    fun `overload with different lambda's is possible`() {
        val string: String = callBackOverload<String> { "Hello" }
        val long = callBackOverload { 5L }
        assertEquals(string.length, long.toInt())
    }
}
