package hzt.enthuware_tests.test4;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Test4Tests {

    //q18 test 4
    @Test
    void testMostSpecificMethodPossibleCalledFirst() {
        assertEquals(FileNotFoundException.class.getSimpleName(), method(null));
    }

    public String method(Object o) {
        return Object.class.getSimpleName();
    }

    public String method(FileNotFoundException s) {
        return FileNotFoundException.class.getSimpleName();
    }

    public String method(IOException s) {
        return IOException.class.getSimpleName();
    }

    //The reason is quite simple, the most specific method depending upon the argument is called.
    // Here, null can be passed to all the 3 methods but FileNotFoundException class is the subclass of IOException
    // which in turn is the subclass of Object. So, FileNotFoundException class is the most specific class.
    //
    // So, this method is called. Had there been two most specific methods,
    // it would not even compile as the compiler will not be able to determine which method to call.
    //
    // For example:
    //
    // Here,null can be passed as both StringBuffer and String and none is more specific than the other.So,it will not compile.
    //
    private static class TestClass {
        public void method(Object o) {
            System.out.println("Object Version");
        }

        public void method(String s) {
            System.out.println("String Version");
        }

        public void method(StringBuffer s) {
            System.out.println("StringBuffer Version");
        }

        public static void main(String args[]) {
            TestClass tc = new TestClass();
//            tc.method(null);
        }
    }
}
