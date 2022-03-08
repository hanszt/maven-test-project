package hzt;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SwitchStatementTest {

    // q 10 test 4
//    'c'|'d' produces a char value that is the result of the bitwise OR operation on 'c' and 'd'.
//    It does not mean that the case will be executed when the value of c is 'c' or 'd'.

//    This case will be executed when the value of c matches the value of the bitwise OR of 'c' and 'd',
//    which is actually same as the char value 'g'.
//    The following is how 'c'|'d' is computed: 'c' = 9 = 1100011 'd' = 100 = 1100100 'c'|'d'= 1100111 <== 100, same as 'g'.
//    Given the above information, it is easy to see that i will be incremented three times when c is 'a'
//
//    (because there is no break statement in the case blocks), two times when c is 'b'
//    (because there is no break statement in the case block) and zero times when c is 'c' and 'd'.
//    Therefore, the final value of i will be 5.
    @Test
    void testSwitchWithoutBreakAndUsingPipeOperator() {
        var ca = new char[]{'a', 'b', 'c', 'd'};
        var i = 0;
        for (var c : ca) {
            switch (c) {
                case 'a':
                    i++;
                case 'b':
                    ++i;
                case 'c' | 'd'://This evaluates to a char! It is not an or operator
                    i++;
            }
        }
        printBitWiseOperatorExplanationAbove();
        System.out.println("i = " + i);
        assertEquals(5, i);
    }

    private void printBitWiseOperatorExplanationAbove() {
        System.out.println("BinaryString c = " + Integer.toBinaryString('c'));
        System.out.println("BinaryString d = " + Integer.toBinaryString('d'));
        final var binaryString = Integer.toBinaryString('c' | 'd');
        System.out.println("BinaryString c | d = " + binaryString);
        System.out.println("Char after bitwise or: " + (char) Integer.parseInt(binaryString, 2));
    }

    //The following is how 'c'|'d' is computed: 'c' = 9= 1100011 'd' = 100 = 1100100 'c'|'d'= 1100111 <== 100, same as 'g'.
    @Test
    void testCharPipeOperatorYieldsNewChar() {
        assertEquals('g', 'c' | 'd');
    }
}
