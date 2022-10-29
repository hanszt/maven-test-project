package org.hzt;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @see <a href="https://github.com/jenetics/colt">CERN Open Source Libraries for High Performance Scientific and Technical Computing in Java.</a>
 * @see org.hzt.colt.ColtMathLibrary
 */
class ColtMathLibraryTests {

    @Test
    void testColtDoubleMatrix2D() {
        double scalar_to_add  = 0.5;
        // creates an empty 10x10 matrix
        DoubleMatrix2D matrix = new DenseDoubleMatrix2D(10, 10);
        // adds the scalar to each cell
        matrix.assign(scalar_to_add);

        assertTrue(matrix.equals(0.5));
    }

}
