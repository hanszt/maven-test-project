package org.hzt.text;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.Normalizer;

import static org.junit.jupiter.api.Assertions.*;

class JavaTextTest {

    @Test
    @DisplayName("Normalize a word using NFC Form")
    void test1() {
        final String text = "'schön' is a  word that can be normalized";
        final String alreadyNormalized = "Normal english ascii text";

        final Normalizer.Form nfc = Normalizer.Form.NFC;
        final String normalized = Normalizer.normalize(text, nfc);

        System.out.println(normalized);

        assertAll(
                () -> assertTrue(Normalizer.isNormalized(alreadyNormalized, nfc)),
                () -> assertFalse(Normalizer.isNormalized(text, nfc)),
                () -> assertTrue(Normalizer.isNormalized(normalized, nfc))
        );
    }
}
