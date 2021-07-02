package com.dnb;

import com.dnb.model.Bic;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReflectionSampleTest {

    @Test
    void testSetFinalFieldNameToNewValue() throws NoSuchFieldException, IllegalAccessException {
        var bic = new Bic("BicName");
        ReflectionSample.setFinalFieldNameToNewValue(bic, "Hans");
        assertEquals("Hans", bic.getName());
    }
}