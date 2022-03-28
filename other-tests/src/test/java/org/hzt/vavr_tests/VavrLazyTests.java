package org.hzt.vavr_tests;

import io.vavr.Lazy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VavrLazyTests {

    @Test
    void testLazyRandomNr() {
        Lazy<Double> lazy = Lazy.of(Math::random);

        assertFalse(lazy.isEvaluated());

        // (random generated)
        final var aDouble = lazy.get();

        assertTrue(lazy.isEvaluated());

        // (memoized)
        final var aDouble1 = lazy.get();

        assertEquals(aDouble, aDouble1);
    }
}
