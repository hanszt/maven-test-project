package org.hzt

import java.util.stream.IntStream

fun sieveOfEratosthenes(n: Int): IntStream {
    val sieve = BooleanArray(n + 1) { true }
    sieveOfEratosthenes(n, sieve)
    return IntStream.rangeClosed(2, n)
        .filter { sieve[it] }
}

private tailrec fun sieveOfEratosthenes(n: Int, sieve: BooleanArray, p: Int = 2) {
    val pSquared = p * p
    if (pSquared > n) {
        return
    }
    // If prime[p] is not changed, then it is a prime
    if (sieve[p]) {
        // Update all multiples of p
        for (i in pSquared..n step p) {
            sieve[i] = false
        }
    }
    sieveOfEratosthenes(n, sieve, p + 1)
}

fun main() {
    val n = 1_000_000_000
    val timer = Timer.timeAnIntFunction(n) { sieveOfEratosthenes(it) }
    val primes = timer.result.toArray()
    val biggest = primes[primes.size - 1]
    println("primes length = " + primes.size)
    println("duration = " + timer.formattedDurationInSeconds())
    println("biggest = $biggest")
}
