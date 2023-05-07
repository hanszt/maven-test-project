package org.hzt

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StringTests {

    @Nested
    inner class SlicingTests {

        @Test
        fun `a string sliced by only it's even indexes`() {
            val s = "This is the string to be sliced"
            val slice = s.slice(s.indices.filter { it % 2 == 0 })
            assertEquals("Ti stesrn ob lcd", slice)
        }

        @Test
        fun `a string sliced by an int range`() {
            val start = "This is "
            val s = "${start}the string to be sliced"
            val slice = s.slice(start.length until s.length)
            assertEquals("the string to be sliced", slice)
        }
    }

    @Nested
    inner class TakeAndDropWhileTests {

        @Test
        fun `take from a string while it does not contain 'a'`() {
            val result = "This string does not contain... O oops it stopped!".takeWhile { it != 'a' }
            assertEquals("This string does not cont", result)
        }

        @Test
        fun `drop last from a string while it does not contain 'a'`() {
            val result = "This string does not contain... O oops it stopped!".dropLastWhile { it != 'a' }
            assertEquals("This string does not conta", result)
        }

        @Test
        fun `take last from a string while it does not contain 'a'`() {
            val result = "This string does not contain... O oops it stopped!".takeLastWhile { it != 'a' }
            assertEquals("in... O oops it stopped!", result)
        }

        @Test
        fun `drop from a string while it does not contain 'a'`() {
            val result = "This string does not contain... O oops it stopped!".dropWhile { it != 'a' }
            assertEquals("ain... O oops it stopped!", result)
        }

        @Nested
        inner class OtherConvenienceMethodTests {

            @Test
            fun `string capitalized`() {
                val capitalized = "This Is SOmE String wItH randOm caps"
                    .lowercase()
                    .replaceFirstChar(Char::uppercase)
                assertEquals("This is some string with random caps", capitalized)
            }

            @Test
            fun `replace range in string`() {
                val replacedAtRange = "This string is a test string".replaceRange(2..4, "(The replacement)")
                assertEquals("Th(The replacement)string is a test string", replacedAtRange)
            }

            @Test
            fun `split a string by multiple delimiters`() {
                val (s1, s2, s3, s4, s5) =
                    "This is a text containing . . and ! and also *, what will happen next?".split('!', '*', '.')

                assertAll(
                    { assertEquals("This is a text containing ", s1) },
                    { assertEquals(" ", s2) },
                    { assertEquals(" and ", s3) },
                    { assertEquals(" and also ", s4) },
                    { assertEquals(", what will happen next?", s5) },
                )
            }

            @Test
            fun `scan an string`() {
                val scan = "Hello".scan("") { acc, s -> acc + s }

                assertAll(
                    { assertEquals(6, scan.size) },
                    { assertTrue { "He" in scan } }
                )
            }
        }
    }
}
