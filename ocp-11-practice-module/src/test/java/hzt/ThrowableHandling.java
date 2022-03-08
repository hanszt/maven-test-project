package hzt;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ThrowableHandling {

    //q27 test 5
//    A NullPointerException never occurs because the index expression must be completely evaluated before any part of
//    the indexing operation occurs, and that includes the check whether the value of the left-hand operand is null.
//    If the array reference expression produces null instead of a reference to an array, then a NullPointerException is
//    thrown at runtime, but only after all parts of the array reference expression have been evaluated and only if
//    these evaluations completed normally.
//
//    In an array access, the expression to the left of the brackets appears to be
//    fully evaluated before any part of the expression within the brackets is evaluated. Note that if evaluation of
//    the expression to the left of the brackets completes abruptly, no part of the expression within the brackets will
//    appear to have been evaluated.  Here, m1() is called first, which throws Exception and so 'a' is never accessed and
//    NullPointerException is never thrown.  Meaning of "the expression to the left of the brackets appears to be fully evaluated":
//
//    Consider this line of code: Object o = getArray()[getIndex()];
//
//    (Assume that the method getArray returns an object array and getIndex returns an int)
//
//    1. The expression to the left of the brackets is evaluated first. So, first getArray() will be called.
//    This gives the reference to the array. It could be null as well. (But if the getArray() method throws an exception,
//    the rest of the statement will not be evaluated i.e. getIndex() will not be called.)
//    2. Now, the expression in the brackets is evaluated, therefore, getIndex() will be called, which will give you the index.
//    If getIndex() throws an exception then step 3 will not happen.
//    3. Finally, the index will be applied on the array (whose reference was returned in step 1).
//
//    At this time, if the array reference is null, you will get a NullPointerException.
    @Test
    void testExceptionHandling() {
        assertThrows(MyException.class, ThrowableHandling::printArrayElement);
    }

    private static void printArrayElement() {
        int[] a = null;
        System.out.println(a[method()]);
    }

    public static int method() {
        throw new MyException("Some Exception");
    }

    private static class MyException extends RuntimeException {

        public MyException(String message) {
            super(message);
        }
    }

    //q28 test 5
//
//    1  Note that the program ends with ExceptionInInitializerError because any exception thrown in a static block
//    is wrapped into ExceptionInInitializerError and then that ExceptionInInitializerError is thrown.
//    Remember that a static or instance initializer can only throw a RuntimeException.

    //    If you try to throw a checked exception from a static block to the outside, the code will not compile.
//    An instance block is allowed to throw a checked exception but only if that exception is declared in the throws
//    clause of all the constructors of the class.
    @Test
    void testExceptionInInitializerErrorThrownWhenExceptionThrownInStaticBlock() {
        final var exceptionInInitializerError = assertThrows(ExceptionInInitializerError.class, AX::new);

        assertEquals("Index 0 out of bounds for length 0", exceptionInInitializerError.getCause().getMessage());
    }

    static class AX {
        static int[] x = new int[0];

        static {
            x[0] = 10;
        }
    }
}
