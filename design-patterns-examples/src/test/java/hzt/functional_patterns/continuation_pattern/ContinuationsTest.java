package hzt.functional_patterns.continuation_pattern;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicReference;

import static hzt.functional_patterns.continuation_pattern.Continuations.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @see <a href="https://blog.marcinchwedczuk.pl/continuations-in-java">Continuations in Java</a>
 */
class ContinuationsTest {

    @Test
    void testAddInCspStyle() {
        addInts(1, 2, partialSum ->
                addInts(partialSum, 3, sum -> {
                    assertAll(
                            () -> assertEquals(6, sum),
                            () -> print(sum, unit -> {
                                System.out.println(unit + " accepted as last argument");
                                return null;
                            }));
                    return null;
                }));
    }

    static void print(int n, Continuation<Void> cont) {
        System.out.println(n);
        cont.apply(null);
    }

    @Test
    void testLoopWithContinuations() {
        final long start = 2;
        final long end = 10;
        sumCC(start, end, s -> {
            assertAll(
                    () -> System.out.println("s = " + s),
                    () -> assertEquals(s, sum(start, end)),
                    () -> assertEquals(s, sum_rec(start, end))
            );
            return null;
        });
    }

    private static long sum(long from, long to) {
        long sum = 0;
        for (long i = from; i <= to; i++) {
            sum += i;
        }
        return sum;
    }

    static long sum_rec(long from, long to) {
        return (from > to) ? 0 : from + sum_rec(from + 1, to);
    }

    @Test
    void testFactorialUsingContinuation() {
        final var n = 7;
        Continuations.factorial(n, result -> {
            assertAll(
                    () -> System.out.println("result = " + result),
                    () -> assertEquals(result, factorial(n))
            );
            return () -> null;
        });
    }

    @Test
    void testFactorialUsingTrampoline() {
        final var val = 400;

        final var result1 = new AtomicReference<>(BigInteger.valueOf(-1));
        Continuations.bigIntFactorial(BigInteger.valueOf(val), endCall(result1::set));

        final var result2 = new AtomicReference<>(BigInteger.valueOf(-1));
        trampoline(Continuations.trampolineBigIntFactorial(BigInteger.valueOf(val), endCall(result2::set)));

        assertEquals(result1.get(), result2.get());
    }

    static int factorial(int n) {
        if (n == 0) return 1;
        return factorial(n - 1) * n;
    }

    @Test
    void testFibonacci() {
        Continuations.fib(8, result -> {
            assertAll(
                    () -> System.out.println("result = " + result),
                    () -> assertEquals(fib1(8), result)
            );
            return null;
        });
    }

    @Test
    void testNeverReturningContinuation() {
        final var fib = fib(8, result -> {
            print(result, v -> null);
            return null;
        });
        assertNull(fib);
    }

    private static int fib1(int n) {
        return n > 1 ? fib1(n - 1) + fib1(n - 2) : 1;
    }
}
