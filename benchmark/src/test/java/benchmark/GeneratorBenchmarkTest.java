package benchmark;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GeneratorBenchmarkTest {

    @Test
    void testFibonacciGeneratorAndConventionalYieldSame() {
        final var generatorBenchmark = new GeneratorBenchmark();
        final var generatorResult = generatorBenchmark.fibonacciFromGenerator();
        final var sequence = generatorBenchmark.fibonacciSequence();
        final var expected = generatorBenchmark.conventionalFibonacci();

        assertAll(
                () -> assertEquals(expected, generatorResult),
                () -> assertEquals(expected, sequence)
        );
    }

}
