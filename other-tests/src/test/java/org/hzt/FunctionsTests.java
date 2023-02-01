package org.hzt;

import org.hzt.Functions.MemIntFunction;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class FunctionsTests {

    @Test
    void testMemoizedJavaUtilFunction() {
        final MemIntFunction<BigInteger> memoizedFunction = MemIntFunction.of(BigInteger::valueOf).memoized();
        final var length = memoizedFunction.apply(5);

        assertAll(
                () -> assertTrue(memoizedFunction.isMemoized()),
                () -> assertEquals(BigInteger.valueOf(5), length)
        );
    }

    @Test
    void testNonMemoizedJavaUtilFunction() {
        final Functions.MemIntFunction<BigInteger> memoizedFunction = BigInteger::valueOf;
        final var length = memoizedFunction.apply(5);

        assertAll(
                () -> assertFalse(memoizedFunction.isMemoized()),
                () -> assertEquals(BigInteger.valueOf(5), length)
        );
    }

}
