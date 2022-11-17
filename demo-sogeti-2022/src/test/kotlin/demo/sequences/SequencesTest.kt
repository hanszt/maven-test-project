package demo.sequences

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class SequencesTest {

    @Test
    fun `find first string length greater than 4`() {
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

    /**
     * Grouping by and then aggregate
     *
     * @see <a href="https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/aggregate.html">aggregate</a>
     */
    @Test
    fun `grouping by and then aggregate`() {
        val numbers = listOf(3, 4, 5, 6, 7, 8, 9)

        val aggregated = numbers
            .groupingBy { it % 3 }
            .aggregate(::toStringBuilder)
            .mapValues { (_, stringBuilder) -> stringBuilder.toString() }

        println(aggregated)

        val expected = mapOf(0 to "0:3-6-9", 1 to "1:4-7", 2 to "2:5-8")

        assertEquals(expected, aggregated)
    }

    private fun toStringBuilder(key: Int, accumulator: StringBuilder?, element: Int, first: Boolean): StringBuilder {
        // first element
        return if (first) StringBuilder().append(key).append(":").append(element)
        else accumulator!!.append("-").append(element)
    }
}
