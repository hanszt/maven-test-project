package org.hzt

import io.kotest.matchers.shouldBe
import org.apache.commons.math3.complex.Complex
import org.apache.commons.math3.transform.DftNormalization
import org.apache.commons.math3.transform.FastFourierTransformer
import org.apache.commons.math3.transform.TransformType
import org.hzt.utils.It
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.math.round
import kotlin.math.sin

internal class RecursiveSamplesKtTest {

    @Test
    fun `test fib with cash`() = fibWithCash(5_000) shouldBe RecursiveSamples.fibWithCash(5_000)

    @Test
    fun `test memoized fib`() = memoizedFib(1_500) shouldBe RecursiveSamples.memoizedFib(1_500)

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
        fun toRoundedAbsArray(complexes: Array<Complex>) = complexes
                .map(Complex::abs)
                .map(::round)
                .toDoubleArray()

        val complexes = generateSequence(0.0) { it + Math.PI / 32 }
            .map(::sin)
            .map { real -> Complex(real, 0.0) }
            .take(8192)
            .toList()
            .toTypedArray()

        val fastFourierTransform = RecursiveSamples.fastFourierTransform(*complexes)
        val fft = fft(complexes)
        val expected = FastFourierTransformer(DftNormalization.STANDARD).transform(complexes, TransformType.FORWARD)

        val largerValueCount = fastFourierTransform
            .map(Complex::abs)
            .filter { it > 1 }
            .onEach(It::println)
            .count()

        assertAll(
            { fastFourierTransform.size shouldBe complexes.size },
            { largerValueCount shouldBe 2 },
            { assertArrayEquals(fastFourierTransform, fft) },
            { assertArrayEquals(toRoundedAbsArray(expected), toRoundedAbsArray(fft)) }
        )
    }
}
