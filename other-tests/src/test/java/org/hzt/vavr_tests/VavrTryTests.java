package org.hzt.vavr_tests;

import io.vavr.control.Try;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.ParseException;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Predicates.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class VavrTryTests {

    @Test
    void testTryThrowingException() {
        String other = "bunch of work through an exception";
        final var result = Try.of(this::bunchOfWorkThrowingIo).getOrElse(other);

        assertEquals(other, result);
    }

    private String bunchOfWorkThrowingIo() throws IOException {
        throw new IOException("This is an io exception");
    }

    @Test
    void testTrySucces() {
        String other = "bunch of work through an exception";
        final var result = Try.of(this::bunchOfWork).getOrElse(other);

        assertEquals("Done bunch of work", result);
    }

    private String bunchOfWork() {
        return "Done bunch of work";
    }

    @Test
    void testTryRecover() {
        String other = "bunch of work through an exception";
        String result = Try.of(this::bunchOfWorkThrowingIo)
                .recover(throwable -> Match(throwable).of(
                        Case($(instanceOf(IOException.class)), Throwable::getMessage),
                        Case($(instanceOf(IllegalArgumentException.class)), e -> "Ia"),
                        Case($(instanceOf(ParseException.class)), e -> "Parse")
                ))
                .getOrElse(other);

        assertEquals("This is an io exception", result);
    }

}
