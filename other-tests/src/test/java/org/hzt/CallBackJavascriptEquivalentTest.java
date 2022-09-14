package org.hzt;

import org.junit.jupiter.api.Test;

import static org.hzt.utils.It.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CallBackJavascriptEquivalentTest {

    private static final String NO_LAST_NAME_PROVIDED = "No last name provided";

    @Test
    void testHelloCallbackNonNullBackName() {
        final var result = CallBackJavascriptEquivalent
                .hello("Piet", "Klaassen", () -> NO_LAST_NAME_PROVIDED);

        assertEquals("Piet Klaassen", result);
    }

    @Test
    void testHelloCallbackNullBackName() {
        final var result = CallBackJavascriptEquivalent
                .hello("Piet", null, () -> NO_LAST_NAME_PROVIDED);

        assertEquals("Piet, No last name provided", result);
    }

    @Test
    void testJavascriptHello() throws Exception {
        final var s = CallBackJavascriptEquivalent.setupEngine(e ->
                CallBackJavascriptEquivalent.runHelloScript(() -> NO_LAST_NAME_PROVIDED, e));

        final var expectedWhenEngineWorking = "Hello script run";
        final var expectedWhenEngineNotWorking = "No working engine";

        final var expected = s.equals(expectedWhenEngineNotWorking) ? expectedWhenEngineNotWorking : expectedWhenEngineWorking;

        assertEquals(expected, s);
    }

    @Test
    void testJavascriptEngineHelloWorld() throws Exception {
        final var s = CallBackJavascriptEquivalent.setupEngine(CallBackJavascriptEquivalent::runHelloWorldScript);
        final var expectedWhenEngineWorking = "Hello world script run";
        final var expectedWhenEngineNotWorking = "No working engine";

        final var expected = s.equals(expectedWhenEngineNotWorking) ? expectedWhenEngineNotWorking : expectedWhenEngineWorking;

        println("expected = " + expected);

        assertEquals(expected, s);
    }

    @Test
    void testJavascriptEngineWithParams() throws Exception {
        final var s = CallBackJavascriptEquivalent.setupEngine(CallBackJavascriptEquivalent::runScriptWithParams);
        final var expectedWhenEngineWorking = "Hello baeldung baeldung baeldung";
        final var expectedWhenEngineNotWorking = "No working engine";
        final var expected = s.equals(expectedWhenEngineNotWorking) ? expectedWhenEngineNotWorking : expectedWhenEngineWorking;

        println("expected = " + expected);

        assertEquals(expected, s.trim());
    }
}
