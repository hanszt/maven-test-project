package org.hzt.vavr_tests;

import io.vavr.Function2;
import io.vavr.Function4;
import io.vavr.Tuple;
import io.vavr.control.Option;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VavrFunctionsTests {

    @Test
    void testCurriedFunction() {
        Function4<Integer, Integer, Integer, Integer, String> toStringFunction =
                (i1, i2, i3, i4) -> String.format("%d%d%d%d", i1, i2, i3, i4);

        final var result = toStringFunction.apply(1, 2, 3, 4);

        final var curriedFunction = toStringFunction.curried();

        final var curriedResult = curriedFunction.apply(1).apply(2).apply(3).apply(4);

        assertAll(
                () -> assertEquals("1234", result),
                () -> assertEquals(curriedResult, result)
        );
    }

    @Test
    void testMemoizedFunction() {
        Function4<Integer, Integer, Integer, Integer, String> toStringFunction =
                (i1, i2, i3, i4) -> String.format("%d%d%d%d", i1, i2, i3, i4);

        final var memoizedFunction = toStringFunction.memoized();

        assertAll(
                () -> assertTrue(memoizedFunction.isMemoized()),
                () -> assertEquals("1234", memoizedFunction.apply(1, 2, 3, 4))
        );
    }

    @Test
    void testTupledFunction() {
        Function4<Integer, Integer, Integer, Integer, String> toStringFunction =
                (i1, i2, i3, i4) -> String.format("%d%d%d%d", i1, i2, i3, i4);

        final var tupledFunction = toStringFunction.tupled();

        assertAll(
                () -> assertFalse(tupledFunction.isMemoized()),
                () -> assertEquals("1234", tupledFunction.apply(Tuple.of(1, 2, 3, 4)))
        );
    }

    @Test
    void testPartiallyAppliedFunction() {
        Function4<Integer, Integer, Integer, Integer, String> toStringFunction =
                (i1, i2, i3, i4) -> String.format("%d%d%d%d", i1, i2, i3, i4);

        final var triFunction = toStringFunction.apply(1);

        assertAll(
                () -> assertFalse(triFunction.isMemoized()),
                () -> assertEquals("1234", triFunction.apply(2, 3, 4))
        );
    }

    @Test
    void testReversedFunction() {
        Function4<Integer, Integer, Integer, Integer, String> toStringFunction =
                (i1, i2, i3, i4) -> String.format("%d%d%d%d", i1, i2, i3, i4);

        final var reversedFunction = toStringFunction.reversed();

        assertAll(
                () -> assertFalse(reversedFunction.isMemoized()),
                () -> assertEquals("4321", reversedFunction.apply(1, 2, 3, 4))
        );
    }

    @Test
    void partialFunctionToTotalFunctionUsingLift() {
        Function2<Integer, Integer, Integer> divide = (a, b) -> a / b;

        Function2<Integer, Integer, Option<Integer>> safeDivide = Function2.lift(divide);

        assertAll(
                () -> assertThrows(ArithmeticException.class, () -> divide.apply(1, 0)),
                () -> assertEquals(Option.none(), safeDivide.apply(1, 0)),
                () -> assertEquals(Option.some(2), safeDivide.apply(4, 2))
        );
    }
}
