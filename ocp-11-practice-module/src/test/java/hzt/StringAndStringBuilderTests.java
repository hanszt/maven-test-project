package hzt;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringAndStringBuilderTests {

    @Test
    void testStringConcat() {
        String abc = "";
        var a = abc.concat("abc");
        abc.concat("def");

        System.out.println("abc = " + abc);

        System.out.println("a = " + a);

        assertTrue(abc.isEmpty());
    }

    //    q1 test 5
    private static final String fullPhoneNumber = "ddd-ddd-dddd";

    @Test
    void testStringBuilderInsert() {
        final var string = new StringBuilder("xxxx").insert(0, fullPhoneNumber, 0, 8).toString();
        assertEquals("ddd-ddd-xxxx", string);
    }

    @Test
    void testStringBuilderReplace() {
        final var string = new StringBuilder(fullPhoneNumber).replace(8, 12, "xxxx").toString();
        assertEquals("ddd-ddd-xxxx", string);
    }

    @Test
    void testStringBuilderSubString() {
        final var string = new StringBuilder(fullPhoneNumber).substring(0, 8) + "xxxx";
        assertEquals("ddd-ddd-xxxx", string);
    }

    @Test
    void testStringBuilderAppendWithIndexes() {
        final var string = new StringBuilder(fullPhoneNumber).append("xxxx", 0, 4).toString();
        assertEquals("ddd-ddd-ddddxxxx", string);
    }

    @Test
    void testStringBuilderAppendWithIndexes2() {
        final var string = new StringBuilder("xxxx").append(fullPhoneNumber, 0, 8).toString();
        assertEquals("xxxxddd-ddd-", string);
    }

    //Q46 test 5

//    The newLength argument must be greater than or equal to 0.
//    Parameters: newLength - the new length Throws: IndexOutOfBoundsException - if the newLength argument is negative.
    @Test
    void testStringBuilderSetLengthMethod() {
        StringBuilder sb = new StringBuilder("12345678");
        sb.setLength(5);
        sb.setLength(10);

        System.out.println("sb = " + sb);

        assertEquals(10 ,sb.length());
    }

    @Test
    void testStringBuilderWithSameContentDoNotEqual() {
        assertNotEquals(new StringBuilder("hallo"), new StringBuilder("hallo"));
    }

}
