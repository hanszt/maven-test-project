package org.hzt;

import org.hzt.utils.Timer;

import java.util.Arrays;
import java.util.stream.IntStream;

import static org.hzt.utils.It.println;

public final class PrimeNrCalculator {

    private PrimeNrCalculator() {
    }

    /**
     *
     *@see <a href="https://www.geeksforgeeks.org/java-program-for-sieve-of-eratosthenes/">Java Program for Sieve of Eratosthenes</a>
     * @param n the upperbound to search for primes
     */
    public static IntStream sieveOfEratosthenes(int n) {
        boolean[] sieve = new boolean[n + 1];
        Arrays.fill(sieve, true);
        int p = 2;
        while (true) {
            final var pSquared = p * p;
            if (pSquared > n) {
                break;
            }
            // If prime[p] is not changed, then it is a prime
            if (sieve[p]) {
                // Update all multiples of p
                for (int i = pSquared; i <= n; i += p) {
                    sieve[i] = false;
                }
            }
            p++;
        }
        // filter out all nrs that are prime
        return IntStream.rangeClosed(2, n)
                .filter(i -> sieve[i]);
    }

    public static void main(String... args) {
        final var n = 1_000_000_000;
        final var timer = Timer.timeAnIntFunction(n, PrimeNrCalculator::sieveOfEratosthenes);
        final var primes = timer.getResult().toArray();
        final int biggest = primes[primes.length - 1];
        println("primes length = " + primes.length);
        println("duration = " + timer.formattedDurationInSeconds());
        println("biggest = " + biggest);
    }
}
