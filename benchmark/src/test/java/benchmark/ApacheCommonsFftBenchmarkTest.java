package benchmark;

import org.apache.commons.math3.complex.Complex;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class ApacheCommonsFftBenchmarkTest {

    private final ApacheCommonsFftBenchmark apacheCommonsFftBenchmark = new ApacheCommonsFftBenchmark();

    @Test
    void fftTransformInPlace() {
        final var dataRi = apacheCommonsFftBenchmark.fastFourierTransformInPlace();

        final var transform = apacheCommonsFftBenchmark.fastFourierTransform();

        final var transformedInPlace = IntStream.range(0, dataRi[0].length)
                .mapToObj(index -> new Complex(dataRi[0][index], dataRi[1][index]))
                .toArray(Complex[]::new);

        assertArrayEquals(transformedInPlace, transform);
    }

}
