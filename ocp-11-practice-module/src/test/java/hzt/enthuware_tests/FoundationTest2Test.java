package hzt.enthuware_tests;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FoundationTest2Test {

    @Test
    void testCallingNaiveNativeMethodThrowsUnsAtisfiedLinkError() {
        final var foundationTest2 = new FoundationTest2();
        assertThrows(UnsatisfiedLinkError.class, foundationTest2::getVariance);
    }

    @Test
    void testPutNullValInHashTableThrowsNullPointer() {
        assertThrows(NullPointerException.class, FoundationTest2::putInHashTable);
    }

    @Test
    void testLeftShiftAndRightShift() {
        int value = 10;
        int leftShifted = value << 2;
        int rightShifted = value >> 1;

        assertEquals(40, leftShifted);
        assertEquals(5, rightShifted);
    }
}
