package org.hzt;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.ArithmeticUtils;
import org.apache.commons.math3.util.MathArrays;
import org.hzt.utils.collections.primitives.DoubleCollection;
import org.hzt.utils.collections.primitives.DoubleList;
import org.hzt.utils.sequences.primitives.DoubleSequence;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ApacheCommonsMath3LibraryTests {

    @Test
    void testIsPowerOf2() {
        assertTrue(ArithmeticUtils.isPowerOfTwo(8));
    }

    /**
     * @see <a href="https://www.youtube.com/watch?v=KuXjwB4LzSA">But what is concolution?</a>
     */
    @Nested
    class ConvolutionTests {
        @Test
        void testConvolve() {
            double[] doubles1 = {1, 2, 3};
            double[] doubles2 = {4, 5, 6};
            final var convolve = MathArrays.convolve(doubles1, doubles2);
            double[] expected = {4, 13, 28, 27, 18};
            assertArrayEquals(convolve, expected);
        }

        /**
         * When processing really large arrays, fast fourier transform convolve is the way to go.
         * This is not present in the apache commons math3 library
         */
        @Test
        void testFftConvolveVsRegularConvolve() {
            int chunkSize = 64;
            final var arrays = DoubleSequence.iterate(1, d -> d + 1)
                    .take(2 * chunkSize)
                    .chunked(chunkSize)
                    .map(DoubleCollection::toArray)
                    .toTypedArray(double[][]::new);

            final var doubles1 = arrays[0];
            final var doubles2 = arrays[1];
            final var expectedLength = doubles1.length * 2 - 1;

            final var convolve = MathArrays.convolve(doubles1, doubles2);
            final var fftConvolve = Arrays.stream(MyMathArrays.fftConvolveLineair(doubles1, doubles2))
                    .mapToDouble(Complex::getReal)
                    .map(Math::rint)
                    .limit(expectedLength)
                    .toArray();

            System.out.println("Arrays.toString(convolve) = " + Arrays.toString(convolve));
            System.out.println("Arrays.toString(fftConvolve) = " + Arrays.toString(fftConvolve));

            assertAll(
                    () -> assertEquals(convolve.length, expectedLength),
                    () -> assertArrayEquals(convolve, fftConvolve)
            );
        }

        @Test
        void testLargeArrayConvolveFeasibleWithFft() {
            int chunkSize = 262_144;

            final var arrays = getArrays(chunkSize);

            final var doubles1 = arrays[0];
            final var doubles2 = arrays[1];

            final var fftConvolve = MyMathArrays.fftConvolveLineair(doubles1, doubles2);

            final var v0 = doubles1[0] * doubles2[0];
            final var v1 = doubles1[1] * doubles2[0] + doubles1[0] * doubles2[1];
            final var v2 = doubles1[2] * doubles2[0] + doubles1[1] * doubles2[1] + doubles1[0] * doubles2[2];

            final var expectedLength = chunkSize * 2 - 1;

            assertAll(
                    () -> assertEquals(expectedLength, fftConvolve.length - 1),
                    () -> assertEquals(v0, fftConvolve[0].getReal(), .001),
                    () -> assertEquals(v1, fftConvolve[1].getReal(), .001),
                    () -> assertEquals(v2, fftConvolve[2].getReal(), .001)
            );
        }

        @Test
        void testFftConvolveInPlaceVsFftConvolve() {
            int chunkSize = 262_144;

            final var arrays = getArrays(chunkSize);

            final var doubles1 = arrays[0];
            final var doubles2 = arrays[1];

            final var doubles = MyMathArrays.fftConvolveLineairInPlace(doubles1, doubles2);
            final var complexes = MyMathArrays.fftConvolveLineair(doubles1, doubles2);

            final var expected = Arrays.stream(complexes).mapToDouble(Complex::getReal).toArray();

            assertArrayEquals(expected, doubles);
        }

        private double[][] getArrays(int chunkSize) {
            return DoubleSequence.iterate(1, d -> d + .0001)
                    .take(2L * chunkSize)
                    .chunked(chunkSize)
                    .map(DoubleList::toArray)
                    .toTypedArray(double[][]::new);
        }
    }

    @Test
    void testTransposeMatrix() {
        double[][] data = {
                {1, 2, 3},
                {4, 5, 6}
        };

        RealMatrix matrix = MatrixUtils.createRealMatrix(data);

        RealMatrix transpose = matrix.transpose();

        double[][] expected = {
                {1, 4},
                {2, 5},
                {3, 6}
        };

        assertArrayEquals(expected, transpose.getData());
    }
}
