package com.dnb;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StringManipulationTest {

    private static final String MESSAGE_ID_TEMPLATE = "ABNANL20000ddMMyy33mfi";

    private final StringManipulation stringManipulation = new StringManipulation();
    @Test
    void startAndEndIndexTest() {
        int startindex = stringManipulation.startIndex(MESSAGE_ID_TEMPLATE, "mfi");
        int endIndexDate = stringManipulation.endIndex(MESSAGE_ID_TEMPLATE, 'y');
        Assertions.assertEquals(16, endIndexDate);
        Assertions.assertEquals(19, startindex);
    }
}