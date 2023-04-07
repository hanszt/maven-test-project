package org.hzt

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Extension fun tests
 *
 * [5 fun ways to use extension functions combined by operator functions in Kotlin](https://www.youtube.com/watch?v=Q0RYVV9rZBI)
 */
class ExtensionFunTests {

    @Test
    fun `test deconstructed localDate`() {
        operator fun LocalDate.component1() = year
        operator fun LocalDate.component2() = monthValue
        operator fun LocalDate.component3() = dayOfMonth

        val localDate = LocalDate.of(2023, 4, 1)
        val (year, month, day) = localDate
        assertEquals(LocalDate.of(year, month, day), localDate)
    }

    @Test
    fun `in operator used to check if a date contains a month or a month and a year`() {

        infix fun Month.of(year: Int) = YearMonth.of(year, this)

        operator fun LocalDate.contains(month: Month) = month == this.month
        operator fun LocalDate.contains(yearMonth: YearMonth) = month == yearMonth.month && year == yearMonth.year

        val date = LocalDate.parse("2023-03-02")

        assertAll(
            { assertTrue(Month.MARCH of 2023 in date) },
            { assertTrue(Month.MARCH in date) },
            { assertFalse(Month.MARCH of 2021 in date) },
            { assertFalse(Month.APRIL in date) }
        )
    }

    @TestFactory
    fun `make any object invokable`(): List<DynamicTest> {

        infix fun <T> T.`should resolve to`(expected: String) =
            dynamicTest("invoking `${this}()` should return: $expected") {
                operator fun T?.invoke() = this?.toString() ?: "null"
                assertEquals(expected, this())
            }

        return listOf(
            "This is a callable string" `should resolve to` "This is a callable string",
            null `should resolve to` "null",
            true `should resolve to` "true",
            1 `should resolve to` "1"
        )
    }

    @Test
    fun `algebraic multiplication`() {
        operator fun Int.invoke(other: Int) = this * other
        assertEquals(27, 3(3 + 6))
    }

    @Test
    fun `digit by index`() {
        operator fun Int.get(index: Int) = toString().getOrNull(index)?.digitToInt() ?: 0

        val nr = 5978

        assertAll(
            { assertEquals(5, nr[0]) },
            { assertEquals(9, nr[1]) },
            { assertEquals(7, nr[2]) },
            { assertEquals(8, nr[3]) },
            { assertEquals(0, nr[4]) }
        )
    }

    @ParameterizedTest(name = "{0} will be multiplied by 2 to yield {1}")
    @CsvSource(value = ["4, 8", "5, 10", "6, 12", "3, 6"])
    fun `invoke method by braces brackets and curly braces`(input: Int, expected: Int) {

        operator fun <T, R> ((T) -> R).get(param: T) = this(param)
        operator fun <T, R> ((T) -> R).invoke(paramProvider: () -> T) = this(paramProvider())

        val timesTwo: (Int) -> Int = { it * 2 }

        val result1 = timesTwo(input)
        val result2 = timesTwo[input]
        val result3 = timesTwo { input }

        println("result1 = $result1")

        assertAll(
            { assertEquals(expected, result1) },
            { assertEquals(expected, result2) },
            { assertEquals(expected, result3) }
        )
    }

    @Test
    fun `augment existing operator fun for map`() {
        operator fun <K, V> TreeMap<K, V>.get(index: Int) = values.elementAt(index)

        val treeMap = TreeMap<String, String>()
        treeMap["first"] = "apple"
        treeMap["second"] = "pear"
        treeMap["third"] = "bowl"

        val actual = treeMap[1]

        assertEquals(treeMap["second"], actual)
    }

    @Test
    fun `array different syntax`() {
        operator fun Int.Companion.get(vararg values: Int) = intArrayOf(*values)
        operator fun Double.Companion.get(vararg values: Double) = doubleArrayOf(*values)

        val specialNrs = Double[Math.PI, Math.E]
        val evenNrs = Int[2, 4, 6, 8, 10]

        assertAll(
            { assertArrayEquals(doubleArrayOf(Math.PI, Math.E), specialNrs) },
            { assertArrayEquals(intArrayOf(2, 4, 6, 8, 10), evenNrs) }
        )
    }
}
