package hzt.only_jdk;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class VectorApiSamplesTest {

    @Test
    void testScalarAndVectorComputationYieldsSameResult() {
        final var length = 1_000;
        final var array1 = new float[length];
        final var array2 = new float[length];

        final var result1 = VectorApiSamples.vectorComputation(array1, array2);
        final var result2 = VectorApiSamples.scalarComputation(array1, array2);

        assertArrayEquals(result1, result2);
    }

}
