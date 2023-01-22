package org.hzt

import io.kotest.matchers.shouldBe
import org.apache.commons.math3.complex.Complex
import org.apache.commons.math3.transform.DftNormalization
import org.apache.commons.math3.transform.FastFourierTransformer
import org.apache.commons.math3.transform.TransformType
import org.hzt.utils.It
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.util.*
import kotlin.math.round
import kotlin.math.sin

internal class RecursiveSamplesKtTest {

    @Test
    fun `test fib with cash`() = fib(5_000) shouldBe RecursiveSamples.fibWithCash(5_000)

    @ParameterizedTest(name = "The greatest common divisor of `{0}` and `{1}` should be `{2}`")
    @CsvSource(
        value = [
            "2, 4, 2",
            "12, 36, 12",
            "35, 12, 1",
            "-5, 5, 5"]
    )
    fun `test greatest common divisor`(first: Long, second: Long, expected: Long) =
        gcdByEuclidesAlgorithm(first, second) shouldBe expected

    @Test
    fun `test fast fourier transform`() {
        val complexes = generateSequence(0.0) { it + Math.PI / 32 }
            .map(::sin)
            .map { real -> Complex(real, 0.0) }
            .take(8192)
            .toList()
            .toTypedArray()

        val fastFourierTransform = RecursiveSamples.fastFourierTransform(*complexes)
        val sequence = sequence { fft(complexes) }
        val expected = FastFourierTransformer(DftNormalization.STANDARD).transform(complexes, TransformType.FORWARD)

        val largerValueCount = fastFourierTransform
            .map(Complex::abs)
            .filter { it > 1 }
            .onEach(It::println)
            .count()

        val transformFromSuspendFun = sequence.last()
        sequence.count()
        println("sequence count = ${sequence.count()}")

        assertAll(
            { fastFourierTransform.size shouldBe complexes.size },
            { largerValueCount shouldBe 2 },
            { assertArrayEquals(fastFourierTransform, transformFromSuspendFun) },
            { assertArrayEquals(toRoundedAbsArray(expected), toRoundedAbsArray(transformFromSuspendFun)) }
        )
    }

    private fun toRoundedAbsArray(complexes: Array<Complex>): DoubleArray =
        complexes
            .map(Complex::abs)
            .map(::round)
            .toDoubleArray()
}
