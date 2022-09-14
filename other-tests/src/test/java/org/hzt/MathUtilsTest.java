package org.hzt;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hzt.utils.It.println;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MathUtilsTest {

    @ParameterizedTest
    @ValueSource(floats = {9, 16, 25, 50, 10_000, 34.32F, 345345.23423F})
    void testFloatFastInverseSqrt(float value) {
        final var ROUNDED_TO_THREE = "%.3f";
        final var expected = normalInverseSqrt(value);
        final var actual = MathUtils.fastInverseSqrt(value);

        println("expected = " + expected);
        println("actual = " + actual);

        final var expectedRounded = ROUNDED_TO_THREE.formatted(expected);
        final var actualRounded = ROUNDED_TO_THREE.formatted(actual);

        assertEquals(expectedRounded, actualRounded);
    }

    @ParameterizedTest
    @ValueSource(doubles = {9, 16, 25, 50, 10_000, 34.32F, 345345.23423D})
    void testDoubleFastInverseSqrt(double value) {
        final var ROUNDED_TO_FIVE = "%.5f";
        final var expected = normalInverseSqrt(value);
        final var actual = MathUtils.fastInverseSqrt(value);

        println("expected = " + expected);
        println("actual = " + actual);

        final var expectedRounded = ROUNDED_TO_FIVE.formatted(expected);
        final var actualRounded = ROUNDED_TO_FIVE.formatted(actual);

        assertEquals(expectedRounded, actualRounded);
    }

    private double normalInverseSqrt(double v) {
        return 1 / Math.sqrt(v);
    }

}
