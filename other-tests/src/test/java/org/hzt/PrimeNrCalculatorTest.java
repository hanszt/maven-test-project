package org.hzt;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.hzt.utils.It.println;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class PrimeNrCalculatorTest {

    @Test
    void testSieveOfEratosthenes() {
        final var primes = PrimeNrCalculator.sieveOfEratosthenes(50).toArray();

        println(Arrays.toString(primes));

        assertArrayEquals(new int[]{2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47}, primes);
    }

    @Test
    void testSieveOfEratosthenesTailRecursive() {
        final var primes = PrimeNrCalculatorKt.sieveOfEratosthenes(50).toArray();

        println(Arrays.toString(primes));

        assertArrayEquals(new int[]{2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47}, primes);
    }

}
