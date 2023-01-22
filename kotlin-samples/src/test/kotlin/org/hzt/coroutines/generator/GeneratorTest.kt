package org.hzt.coroutines.generator

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class GeneratorTest {

    @Test
    fun `test generator returns null when depleted`() = generate<Int, Unit> {
        var index = 0
        while (index < 3)
            yield(index++)
    }.run {
        next(Unit) shouldBe 0
        next(Unit) shouldBe 1
        next(Unit) shouldBe 2
        next(Unit) shouldBe null
    }

    @Test
    fun `test generator can be infinite`() = generate<Int, Unit> {
        var index = 0
        while (true) yield(index++)
    }.run {
        val list = generateSequence { next(Unit) }
            .take(100)
            .toList()
        val slice = list.slice(0..3)

        slice shouldBe listOf(0, 1, 2, 3)
        list.size shouldBe 100
    }

    @Test
    fun `test nested nr generator`() = generate {
        yield(10)
        yieldAll(generate {
            yield(10 + 1)
            yield(10 + 2)
            yield(10 + 3)
        }, Unit)
        yield(10 + 10)
    }.run {
        next(Unit) shouldBe 10
        next(Unit) shouldBe 11
        next(Unit) shouldBe 12
        next(Unit) shouldBe 13
        next(Unit) shouldBe 20
        next(Unit) shouldBe null
    }

    @Test
    @Suppress("RemoveExplicitTypeArguments")
    fun `test log generator`(): Unit = generate<Unit, String> {
        ("Started with $it") shouldBe "Started with start"
        (yield(Unit)) shouldBe "pretzel"
        (yield(Unit)) shouldBe "california"
        (yield(Unit)) shouldBe "mayonnaise"
    }.run {
        next("start")
        next("pretzel")
        next("california")
        next("mayonnaise")
    }
}
