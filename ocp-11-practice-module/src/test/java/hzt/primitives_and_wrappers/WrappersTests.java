package hzt.primitives_and_wrappers;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class WrappersTests {

    @Test
    void testParseIntWithRadix() {
        final var radix = 8;
        for (int i = 0; i < radix; i++) {
            int result = Integer.parseInt(String.valueOf(i), radix);
            System.out.println("result = " + result);
        }
    }

    @Test
    void testComparingBoxedValsWithNonBoxed() {
        Integer a = 4;
        Double boxedD = 4.; // must have a dot at the end
//        if (a == d) { this is not allowed. Comparison of different Objects with == yields a compilation error
        double d = 4;
        if (a == d) {
            assertEquals((double) a, d);
        } else {
            fail();
        }
    }

    @Test
    void testIntCanNotBeBoxedToDouble() {
        int i = 2;
        double d = i;
        Double bd = d;
        assertEquals(2, bd);
    }

    @Test
    void testEffectivelyFinalForUseInLambdaOrInnerClass() {
        double payment = 32.9;
        double rate = 3;
        double defaultrate = 0.10;        //1
        if (rate > 10) defaultrate = rate;  //2

        class Implement {

            public int apply(double data) {
//                BiFunction<Integer, Double, Integer> f = (m, n) -> m + (int) (n * m);
//                return f.apply((int) data, defaultrate);
                Function<Integer, Integer> f = x -> x + (int) (x * rate);
                return f.apply((int) data);
            }

        }
        Implement i = new Implement();
        assertTrue(i.apply(payment) > 0);
    }

    //q16 test 5
    // There are multiple concepts at play here: 1. All the wrapper objects are immutable.
    // So when you do obj++, what actually happens is something like this: obj = new Integer( obj.intValue()  +
    // 1);  
    // 2.val1++ uses post-increment operator, which implies that you note down the current value of val1, increment it,
    // and then pass the original noted down value to the method testInts. Thus, the reference value of Integer 5
    // is passed to testInts. But val1 is set to point to a new Integer object containing 6.
    // ++val2 uses pre-increment operator, which implies that you first increment val2 and then pass the incremented value.
    //
    // Therefore, val2 is incremented to 10 and then 10 is passed to the method testInts.
    //
    // 3. Java uses pass by value semantics in method calls. In case of primitive variables,
    // their values are passed, while in case of Objects, their reference values are passed.  
    // Thus, when you assign a different object to reference variable in a method,
    // the original reference variable that was passed from the calling method still points to the same object
    // that it pointed to before the call. In this question, therefore, val1 in main still points to 6 after the call
    // to testInts returns.
    @Test
    void testBoxedVsPrimitiveIncrement() {
        Integer val1 = new Integer(5);
        int val2 = 9;
        testInts(val1++, ++val2);
        assertEquals(6, val1);
        assertEquals(10, val2);
    }

    public static void testInts(Integer obj, int var) {
        obj = var++;
        obj++;
    }

    // q42 test 6
    // left to right evaluation
    @Test
    void testStringLeftAssociativity() {
        assertEquals("123", "1" + 2 + 3);
        assertEquals("33", 1 + 2 + "3");
        assertEquals(98, 'a' + 1);
        assertEquals(5.0, 4 + 1.0f);
    }
}
