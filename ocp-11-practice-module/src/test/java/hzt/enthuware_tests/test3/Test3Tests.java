package hzt.enthuware_tests.test3;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Test3Tests {

    // Q16 test 3
//    3 + 100/10*2-13 will be parsed as: 3 + (100/10)*2-13.
//    This is because the precedence of / and * is same (and is higher than + and -)
//    and since the expression is evaluated from left to right, the operands are grouped on first come first served basis.
//    [This is not the right terminology but you will be able to answer the questions if you remember this rule.]
    @Test
    void testIntegerDivision() {
        assertEquals(10, 3 + 100 / 10 * 2 - 13);
    }

}
