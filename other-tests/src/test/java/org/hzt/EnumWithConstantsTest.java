package org.hzt;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnumWithConstantsTest {

    @Test
    void testGetConstantValue() {
        assertEquals("camt.998.001.03", EnumWithConstants.INSERT_VALUE_OF_MINIMUM_RESERVE.getIdentifier(),
                "Enum with constants test");
    }

}
