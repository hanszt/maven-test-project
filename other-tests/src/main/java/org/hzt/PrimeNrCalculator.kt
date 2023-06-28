package org.hzt

import org.hzt.utils.Timer
import java.util.stream.IntStream

fun intStreamBySieveOfEratosthenes(@Suppress("SameParameterValue") n: Int): IntStream =
    sieveOfEratosthenes(n).let { sieve -> IntStream.rangeClosed(2, n).filter { sieve[it] } }

private tailrec fun sieveOfEratosthenes(
    n: Int,
    sieve: BooleanArray = BooleanArray(n + 1) { true },
    p: Int = 2
): BooleanArray {
    val pSquared = p * p
    if (pSquared !in p..n) return sieve
    // If prime[p] is not changed, then it is a prime
    if (sieve[p]) {
        // Update all multiples of p
        for (i in pSquared..n step p) sieve[i] = false
    }
    return sieveOfEratosthenes(n, sieve, p + 1)
}

fun main() {
    val n = Int.MAX_VALUE - 3
    val timer = Timer.timeAnIntFunction(n, ::intStreamBySieveOfEratosthenes)
    val primes = timer.result.toArray()
    val biggest = primes[primes.size - 1]
    println("primes length = " + primes.size)
    println("duration = " + timer.formattedDurationInSeconds())
    println("biggest = $biggest")
    require(biggest.isPrime())
    require(biggest == 2_147_483_629)
}

fun Int.isPrime(): Boolean = this > 1 && (2..this / 2).none { this % it == 0 }
