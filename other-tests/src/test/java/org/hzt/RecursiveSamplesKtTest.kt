package org.hzt

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class RecursiveSamplesKtTest {

    @Test
    fun `test fib with cash`() {
        val n = 5_000
        val fib = fib(n)
        val expected = RecursiveSamples.fibWithCash(n)
        assertEquals(expected, fib)
    }
}
