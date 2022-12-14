package org.hzt;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.apache.commons.math3.util.ArithmeticUtils;
import org.hzt.utils.sequences.Sequence;

import java.util.Arrays;

public final class FftConvolver {

    private FftConvolver() {
    }

    /**
     * Returns the circular convolution of the two specified arrays.
     *
     * @param array1 one complex array
     * @param array2 the other complex array
     * @return the circular convolution of {@code x} and {@code y}
     * @throws IllegalArgumentException if the length of {@code x} does not equal
     *                                  the length of {@code y} or if the length is not a power of 2
     * @see <a href="https://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/FFT.java.html">Fast Fourier Transform</a>
     */
    public static Complex[] fftConvolveCircular(double[] array1, double[] array2) {
        checkPreconditions(array1.length, array2.length);

        final var fastFourierTransformer = new FastFourierTransformer(DftNormalization.STANDARD);
        final var transform1 = fastFourierTransformer.transform(array1, TransformType.FORWARD);
        final var transform2 = fastFourierTransformer.transform(array2, TransformType.FORWARD);

        final Complex[] pointWiseMultiplied = Sequence.of(transform1)
                .zip(Sequence.of(transform2), Complex::multiply)
                .toTypedArray(Complex[]::new);

        return fastFourierTransformer.transform(pointWiseMultiplied, TransformType.INVERSE);
    }

    /**
     * @param array1 input array 1
     * @param array2 input array 2
     *
     * The result is stored in input array 1
     */
    public static void fftConvolveCircularInPlace(double[] array1, double[] array2) {
        checkPreconditions(array1.length, array2.length);

        final var dataRI1 = new double[][]{array1, new double[array1.length]};
        final var dataRI2 = new double[][]{array2, new double[array2.length]};

        FastFourierTransformer.transformInPlace(dataRI1, DftNormalization.STANDARD, TransformType.FORWARD);
        FastFourierTransformer.transformInPlace(dataRI2, DftNormalization.STANDARD, TransformType.FORWARD);

        for (int i = 0; i < dataRI1[0].length; i++) {
            final var real1 = dataRI1[0][i];
            final var imag1 = dataRI1[1][i];
            final var real2 = dataRI2[0][i];
            final var imag2 = dataRI2[1][i];
            dataRI1[0][i] = real1 * real2 - imag1 * imag2;
            dataRI1[1][i] = real1 * imag2 + imag1 * real2;
        }
        FastFourierTransformer.transformInPlace(dataRI1, DftNormalization.STANDARD, TransformType.INVERSE);
    }

    private static void checkPreconditions(int length1, int length2) {
        if (!ArithmeticUtils.isPowerOfTwo(length1)) {
            throw new ArithmeticException("Both arrays should have a length that is a power of two. Try padding with zero's");
        }
        if (length1 != length2) {
            throw new IllegalArgumentException("Dimensions don't agree");
        }
    }

    /**
     * Returns the linear convolution of the two specified complex arrays.
     *
     * @param array1 one array
     * @param array2 the other array
     * @return the linear convolution of {@code array1} and {@code array2}
     * @throws IllegalArgumentException if the length of {@code array1} does not equal
     *                                  the length of {@code array2} or if the length is not a power of 2
     */
    public static Complex[] fftConvolveLineair(double[] array1, double[] array2) {
        checkPreconditions(array1.length, array2.length);
        final var doubles1 = Arrays.copyOf(array1, array1.length * 2);
        final var doubles2 = Arrays.copyOf(array2, array2.length * 2);
        return fftConvolveCircular(doubles1, doubles2);
    }

    public static double[] fftConvolveLineairInPlace(double[] array1, double[] array2) {
        checkPreconditions(array1.length, array2.length);
        final var doubles1 = Arrays.copyOf(array1, array1.length * 2);
        final var doubles2 = Arrays.copyOf(array2, array2.length * 2);
        fftConvolveCircularInPlace(doubles1, doubles2);
        return doubles1;
    }
}
