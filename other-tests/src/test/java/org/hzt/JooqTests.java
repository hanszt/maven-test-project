package org.hzt;

import org.jooq.lambda.Seq;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JooqTests {

    @Test
    void testSeqJooqMax() {
        List<String> names = List.of("Hi", "this", "is", "a", "test");

        Seq<String> sequence = Seq.cycle(names, 1);

        final Optional<String> max = sequence.max();

        assertEquals("this", max.orElse(""));
    }
}
