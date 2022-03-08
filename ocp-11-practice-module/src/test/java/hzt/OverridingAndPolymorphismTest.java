package hzt;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class OverridingAndPolymorphismTest {

    //q25 test 4
//    Note that method print() is overridden in class B. Due to polymorphism,
//    the method to be executed is selected depending on the class of the actual object.
//    Here, when an object of class B is created,
//    first B's default constructor (which is not visible in the code but is automatically provided
//    by the compiler because B does not define any constructor explicitly) is called.

//    The first line of this constructor is a call to super(), which invokes A's constructor.
//    A's constructor in turn calls print().
//
//    Now, print is a non-private instance method and is therefore polymorphic,
//    which means, the selection of the method to be executed depends on the class of actual object on which it is invoked.

    //    Here, since the class of actual object is B, B's print is selected instead of A's print.
//    At this point of time, variable i has not been initialized (because we are still in the middle of initializing A),
//    so its default value i.e. 0 is printed.
    @Test
    void testObjectCreationNotYetFinished() {
        Computer computer = new Laptop();
        final var value = computer.getValue();
        System.out.println("value = " + value);
        assertEquals(4, value);
    }

    private static class Computer {

        private Computer() {
            final var value = getValue();
            System.out.println("value during construction = " + value);
            assertEquals(0, value);
        }

        int getValue() {
            fail();
            return Integer.MAX_VALUE;
        }
    }

    private static class Laptop extends Computer {

        private int value = 4;

        @Override
        int getValue() {
            return value;
        }
    }

    // q 33 test 6
//    IllegalArgumentException extends from RuntimeException. So you don't have to worry about it at least at compile time. You may or may not declare it in the throws clause. The caller doesn't have to catch it anyway. The overriding method in the subclass is free to not throw any checked exception at all even if the overridden method throws a checked exception. No exception is a valid subset of exceptions thrown by the overridden method.
    @Test
    void testOverridingWithExceptions() {
        Great g = new Amazing();
        assertDoesNotThrow(() -> g.doStuff());
    }

    static class Great {
        public void doStuff() throws IOException {
        }
    }


    static class Amazing extends Great {
        public void doStuff() throws IOException, IllegalArgumentException {
        }
    }

}
