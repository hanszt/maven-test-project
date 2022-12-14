package org.hzt;

import org.junit.jupiter.api.Test;

import static org.hzt.utils.It.println;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StringManipulationTest {

    private static final String MESSAGE_ID_TEMPLATE = "ABNANL20000ddMMyy33mfi";

    @Test
    void testStartAndEndIndexTest() {
        int startindex = MESSAGE_ID_TEMPLATE.indexOf("mfi");
        int endIndexDate = MESSAGE_ID_TEMPLATE.lastIndexOf('y');

        assertAll(
                () -> assertEquals("mfi", MESSAGE_ID_TEMPLATE.substring(startindex)),
                () -> assertEquals("y33mfi", MESSAGE_ID_TEMPLATE.substring(endIndexDate))
        );
    }

    @Test
    void testInvertString() {
        String test = "This is first string that needs to be inverted";
        String inverted = new StringBuilder(test).reverse().toString();
        assertEquals("detrevni eb ot sdeen taht gnirts tsrif si sihT", inverted);
    }

    @Test
    void testRepeatStringPiece() {
        assertEquals(200, "12".repeat(100).length());
    }

    @Test
    void testString() {
        StringBuilder stringBuilder = new StringBuilder("323");
        assertTrue("323".contentEquals(stringBuilder));
    }

    @Test
    void testReplaceFirst() {
        final var replaceFirstResult = "Hallo dit is een teststring".replaceFirst("e", "");
        println("replaceFirst = " + replaceFirstResult);
        assertEquals("Hallo dit is en teststring", replaceFirstResult);
    }
}
