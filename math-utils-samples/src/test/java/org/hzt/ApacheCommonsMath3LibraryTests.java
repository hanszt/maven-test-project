package org.hzt;

import org.apache.commons.math3.util.ArithmeticUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ApacheCommonsMath3LibraryTests {

    @Test
    void testIsPowerOf2() {
       assertTrue(ArithmeticUtils.isPowerOfTwo(8));
    }
}
