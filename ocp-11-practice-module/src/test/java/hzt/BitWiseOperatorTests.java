package hzt;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BitWiseOperatorTests {

    //    Binary OR Operator copies a bit if it exists in either operand.
    @Test
    void testBitWiseOrForInts() {
        int a = 99;
        int b = 100;

        int result = a | b;

        System.out.println("Integer.toBinaryString(a) = " + Integer.toBinaryString(a));
        System.out.println("Integer.toBinaryString(b) = " + Integer.toBinaryString(b));
        System.out.println("Integer.toBinaryString(result) = " + Integer.toBinaryString(result));

        assertEquals(103, result);
    }

    //    Binary AND Operator copies a bit to the result if it exists in both operands.
    @Test
    void testBitWiseAndForInts() {
        int a = 99;
        int b = 100;

        int result = a & b;

        System.out.println("Integer.toBinaryString(a) = " + Integer.toBinaryString(a));
        System.out.println("Integer.toBinaryString(b) = " + Integer.toBinaryString(b));
        System.out.println("Integer.toBinaryString(result) = " + Integer.toBinaryString(result));

        assertEquals(96, result);
    }

    //    Binary XOR Operator copies the bit if it is set in one operand but not both.
    @Test
    void testBitWiseXOrForInts() {
        int a = 99;
        int b = 100;

        int result = a ^ b;

        System.out.println("Integer.toBinaryString(a) = " + Integer.toBinaryString(a));
        System.out.println("Integer.toBinaryString(b) = " + Integer.toBinaryString(b));
        System.out.println("Integer.toBinaryString(result) = " + Integer.toBinaryString(result));

        assertEquals(7, result);
    }

    //    Binary Ones Complement Operator is unary and has the effect of 'flipping' bits.
    @Test
    void testBitWiseComplimentForInts() {
        int b = 100;

        int result = ~ b;

        System.out.println("Integer.toBinaryString(b) = " + Integer.toBinaryString(b));
        final var resultAsBinaryString = Integer.toBinaryString(result);
        System.out.println("resultAsBinaryString = " + resultAsBinaryString);
        System.out.println("resultAsBinaryString.length() = " + resultAsBinaryString.length());

        assertEquals(-101, result);
    }

//    Binary Left Shift Operator. The left operands value is moved left by the number of bits specified by the right operand.
    @Test
    void testBinaryLeftShiftOperator() {
        int a = 99;

        int result = a << 2;

        System.out.println("Integer.toBinaryString(a) = " + Integer.toBinaryString(a));
        System.out.println("Integer.toBinaryString(result) = " + Integer.toBinaryString(result));

        assertEquals(396, result);
    }

//    Binary Right Shift Operator. The left operands value is moved right by the number of bits specified by the right operand.
    @Test
    void testBinaryRightShiftOperator() {
        int a = 1000;

        int result = a >> 2;

        System.out.println("Integer.toBinaryString(a) = " + Integer.toBinaryString(a));
        System.out.println("Integer.toBinaryString(result) = " + Integer.toBinaryString(result));

        assertEquals(250, result);
    }

//    Shift right zero fill operator.
//    The left operands value is moved right by the number of bits specified by the right operand and
//    shifted values are filled up with zeros.
    @Test
    void testZeroFillRightShiftOperator() {
        int a = 1000;

        int result = a >>> 2;

        System.out.println("Integer.toBinaryString(a) = " + Integer.toBinaryString(a));
        System.out.println("Integer.toBinaryString(result) = " + Integer.toBinaryString(result));

        assertEquals(250, result);
    }

    @Test
    void testConvertLetterToLowerCaseUsingOr() {
        char a = 'A';
        char b = 'b';

        char aLowerCase = (char) (a | ' ');
        char bLowerCase = (char) (b | ' ');

        assertEquals('a', aLowerCase);
        assertEquals('b', bLowerCase);
    }

    @Test
    void testConvertLetterToUpperCaseUsingAnd() {
        char a = 'A';
        char b = 'b';

        char aUpperCase = (char) (a & '_');
        char bUpperCase = (char) (b & '_');

        assertEquals('A', aUpperCase);
        assertEquals('B', bUpperCase);
    }

    @TestFactory
    Stream<DynamicTest> testRightBitWiseOperatorSameAsDividingByTwo() {
        return IntStream.range(0, 100)
                .mapToObj(i -> DynamicTest.dynamicTest(String.format("%d divide by 2 same as right bitshift of %d to 1", i, i),
                        () -> assertEquals(i >>> 1, i / 2)));
    }

    @TestFactory
    Stream<DynamicTest> testLeftBitWiseOperatorSameAsMultiplyingByTwo() {
        return IntStream.range(0, 100)
                .mapToObj(i -> DynamicTest.dynamicTest(String.format("%d multiplied by 2 same as left bitshift of %d to 1", i, i),
                        () -> assertEquals(i << 1, i * 2)));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 4, 5, 6, 3, 7, 6, 8, 4, 89, 3, 563})
    void testMultiplyByFourUsingBitShift(int value) {
        assertEquals(value * 4, value << 2);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 4, 5, 6, 3, 7, 6, 8, 4, 89, 3, 563})
    void testDividingByFourUsingBitShift(int value) {
        assertEquals(value / 4, value >> 2);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 4, 5, 6, 3, 7, 6, 8, 4, 89, 3, 563})
    void testCheckOddNumber(int value) {
        assertEquals(value % 2 == 1, (value & 1) == 1);
    }
}
