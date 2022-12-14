package org.hzt

import org.apache.commons.math3.complex.Complex
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import java.math.BigInteger as BigInt

fun factorial(n: Int): BigInt = factorial(BigInt.ONE, n.toBigInteger())

tailrec fun factorial(acc: BigInt, n: BigInt): BigInt =
    if (n <= BigInt.ONE) acc else factorial(acc.multiply(n), n.subtract(BigInt.ONE))

fun findFixPoint() = findFixPoint(1.0)

/**
 * This code calculates the fixpoint of cosine (Dottie number), which is a mathematical constant.
 *
 * In mathematics, the Dottie number is a constant that is the unique real root of the equation: cos(x) = x
 *
 * @see <a href="https://kotlinlang.org/docs/functions.html#tail-recursive-functions">Tail recursive functions</a>
 * @see <a href="https://en.m.wikipedia.org/wiki/Dottie_number">Dottie number</a>
 *
 * @param x the initial value
 * @return the fixed point
 */
tailrec fun findFixPoint(x: Double = 1.0, eps: Double = 1E-15): Double =
    if (abs(x - cos(x)) < eps) x else findFixPoint(cos(x))

/**
 * It is assumed the length of the input array is a power of two
 *
 * @see [Fast Fourier Transform](https://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/FFT.java.html)
 * @see [The Fast Fourier Transform (FFT): Most Ingenious Algorithm Ever?](https://youtu.be/h7apO7q16V0?t=1658)
 *
 * @param input the complex nr array to apply the fast fourier transform to
 * @return the fourier transform of the input
 */
fun fft(input: Array<Complex>): Array<Complex> {
    if (input.size == 1) return arrayOf(input[0])

    require(isPowerOfTwo(input.size)) { "The length: ${input.size} is not a power of 2" }

    val halfSize = input.size / 2

    val evenFFT = fft(Array(halfSize) { input[2 * it] })
    val oddFFT = fft(Array(halfSize) { input[2 * it + 1] })

    val fft = Array<Complex>(input.size) { Complex.ZERO }

    for (k in 0 until halfSize) {
        val phi = -(2 * PI) * k / input.size
        val complex = Complex(cos(phi), sin(phi)).multiply(oddFFT[k])
        val even = evenFFT[k]
        fft[k] = even.add(complex)
        fft[k + halfSize] = even.subtract(complex)
    }
    return fft
}

fun isPowerOfTwo(n: Int) = (n > 0) && ((n and (n - 1)) == 0)

fun fib(n: Int, cache: MutableMap<Int, BigInt> = HashMap()): BigInt {
    require(n >= 0) { "the position n in the fib sequence must be greater than 0 but was $n" }
    if (n == 0) {
        return BigInt.ZERO
    }
    if (n <= 2) {
        return BigInt.ONE
    }
    val fibNr = cache[n]
    if (fibNr != null) {
        return fibNr
    }
    val nextFib = fib(n - 1, cache).add(fib(n - 2, cache))
    cache[n] = nextFib
    return nextFib
}

