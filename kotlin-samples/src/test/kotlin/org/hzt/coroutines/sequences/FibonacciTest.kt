package org.hzt.coroutines.sequences

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class FibonacciTest {

    @Test
    fun `fibonacci sequence using coroutines`() =
        fibonacci.take(10).joinToString() shouldBe "1, 1, 2, 3, 5, 8, 13, 21, 34, 55"
}

