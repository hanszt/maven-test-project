package org.hzt;

import org.hzt.utils.It;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        PropertiesSample.main();
        StringBufferVSStringBuilder.main();
        ThreadingSample.main();
        final var arguments = new String[6];
        ReturningResultViaStringArray.main(arguments);
        Arrays.stream(arguments).forEach(It::println);
    }
}
