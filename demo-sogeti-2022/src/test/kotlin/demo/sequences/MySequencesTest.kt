package demo.sequences

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class MySequencesTest {

    @Test
    fun `find first string with length greater than 4`() {
        val list = listOf("This", "is", "an", "awesome", "test", "for", "my", "sequences", "demo")

        println("By kotlin sequence")
        val expected = list.asSequence()
            .map(String::length)
            .filter { it >= 4 }
            .first()

        println("By stream")
        val actual = list.stream()
            .map(String::length)
            .filter { it <= 4 }
            .findFirst()
            .orElseThrow()

        println(actual)

        assertEquals(expected, actual)
    }
}
