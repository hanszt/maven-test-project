package org.hzt;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TimeComplexityPlotterTest {

    @ParameterizedTest(name = "when applying {0} to shor and the classic function, shor's result should be smaller")
    @ValueSource(doubles = {50 * Math.E, 90 * Math.E, 10 * Math.PI, 8, 200})
    void testShorIsFasterThanClassical(double value) {
        final var classicTime = TimeComplexityPlotter.classic.applyAsDouble(value);
        final var shorTime = TimeComplexityPlotter.shor.applyAsDouble(value);
        assertTrue(classicTime > shorTime);
    }

}
