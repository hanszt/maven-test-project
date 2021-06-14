package com.dnb;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OtherTests {

    @Test
    void testChars() {
        IntStream.range(33, 900).mapToObj(this::toCharacter).forEach(System.out::println);
        System.out.println();
        "Hello".chars().mapToObj(this::toCharacter).forEach(System.out::println);
        String.format("Hello,%nI'm Hans").lines().forEach(System.out::println);
        char c = 'ͽ';
        assertEquals(893, c);
    }

    private Character toCharacter(int i) {
        return (char) i;
    }

}
