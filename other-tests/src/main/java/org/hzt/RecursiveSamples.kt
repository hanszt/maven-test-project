package org.hzt

import java.math.BigInteger
import kotlin.math.abs
import kotlin.math.cos

fun factorial(n: Int): BigInteger = factorial(BigInteger.ONE, n.toBigInteger())

tailrec fun factorial(acc:  BigInteger, n: BigInteger): BigInteger =
    if (n <= BigInteger.ONE) acc else factorial(acc.multiply(n), n.subtract(BigInteger.ONE))

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
tailrec fun findFixPoint(x: Double = 1.0, eps: Double = 1E-15): Double = if (abs(x - cos(x)) < eps) x else findFixPoint(cos(x))
