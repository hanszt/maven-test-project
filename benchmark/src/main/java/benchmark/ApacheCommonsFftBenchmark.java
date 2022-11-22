package benchmark;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.jetbrains.annotations.NotNull;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Arrays;
import java.util.stream.DoubleStream;

public class ApacheCommonsFftBenchmark {

    private static final int MAX_SIZE = 8_192;

    static final Complex[] complexes = generateReals()
            .mapToObj(real -> new Complex(real, 0))
            .toArray(Complex[]::new);

    static final double[][] dataRi = new double[][]{generateReals().toArray(), new double[MAX_SIZE]};

    @NotNull
    private static DoubleStream generateReals() {
        return DoubleStream
                .iterate(0, d -> d + (Math.PI / 32))
                .map(Math::sin)
                .limit(MAX_SIZE);
    }

    @Benchmark
    public double[][] fastFourierTransformInPlace() {
        final var outputDataRi = Arrays.copyOf(dataRi, dataRi.length);
        FastFourierTransformer.transformInPlace(outputDataRi, DftNormalization.STANDARD, TransformType.FORWARD);
        return outputDataRi;
    }

    @Benchmark
    public Complex[] fastFourierTransform() {
        return new FastFourierTransformer(DftNormalization.STANDARD).transform(complexes, TransformType.FORWARD);
    }


    public static void main(String[] args) {
        Options options = new OptionsBuilder()
                .include(ApacheCommonsFftBenchmark.class.getSimpleName())
                .forks(2)
                .warmupIterations(2)
                .measurementIterations(3)
                .shouldFailOnError(true)
                .build();
        try {
            new Runner(options).run();
        } catch (RunnerException e) {
            throw new IllegalStateException(e);
        }
    }
}
