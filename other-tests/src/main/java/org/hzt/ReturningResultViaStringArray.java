package org.hzt;

import org.hzt.utils.It;

public class ReturningResultViaStringArray {

    public static void main(final String... args) {
        if (args.length >= 6) {
            args[0] = "Hello";
            args[1] = "World";
            args[2] = "This";
            args[3] = "is";
            args[4] = "a";
            args[5] = "test";
        }
        It.println("The length " + args.length + " is to short for the assigned values");
    }
}
