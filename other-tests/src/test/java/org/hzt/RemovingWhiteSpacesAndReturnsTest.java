package org.hzt;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;

import static org.hzt.utils.It.println;
import static org.junit.jupiter.api.Assertions.assertFalse;

class RemovingWhiteSpacesAndReturnsTest {

    private final RemovingWhiteSpacesAndReturns removingWhiteSpacesAndReturns = new RemovingWhiteSpacesAndReturns();

    @ParameterizedTest
    @ValueSource(strings = {
            "camt_004_CLM_ReturnAccount_bs100.xml",
            "camt_004_CLM_ReturnAccount_bs101.xml",
            "camt_004_CLM_ReturnAccount_RABO.xml", })
    void testRemoveSpacesFromLinesInTextFile(String fileName) throws IOException {
        String result = removingWhiteSpacesAndReturns.removeSpacesFromLinesInTextFile(fileName);
        println(result);
        assertFalse(result.isBlank());
    }

}
