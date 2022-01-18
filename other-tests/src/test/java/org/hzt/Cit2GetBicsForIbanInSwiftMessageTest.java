package org.hzt;

import org.hzt.model.Bic;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class Cit2GetBicsForIbanInSwiftMessageTest {

    private static final String TEST_STRING = "  LENZDEMA    , Abn Amro   ";

    @Test
    void getBicsForIbanInSwiftMessageTest() {
        Set<Bic> bicSet = Set.of(
                new Bic("LENZDEMA"),
                new Bic("Abn Amro"));
        Set<Bic> bics = Cit2GetBicsForIbanInSwiftMessage.getBicsForIbanInSwiftMessage(TEST_STRING);
        assertTrue(bicSet.containsAll(bics));
    }

}
