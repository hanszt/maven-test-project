package org.hzt

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class RecursiveSamplesKtTest {

    @Test
    fun `test fib with cash`() {
        val n = 5_000
        fib(n) shouldBe RecursiveSamples.fibWithCash(n)
    }
}
