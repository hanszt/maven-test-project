package hzt.primitives_and_wrappers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PrimitiveTests {

    // Q
    @Test
    void testAddingCharsYieldsNumberTheCharIsRepresentedBy() {
        char a = 'a', b = 98; //1
        int a1 = a; //2
        int b1 = (int) b; //3
        assertEquals(195, (char) a1 + (char) b1); //4
    }

    // Q 11 test 6
    @Test
    void testPrimitivesAsExpressions() {
        int a = 10;
        int b = 20;
        a += (a = 4);
        b = b + (b = 5);
        assertEquals(14, a);
        assertEquals(25, b);
    }
}
