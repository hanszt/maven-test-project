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

    @Test
    void testPipeline() {
        final var integers = Seq.iterate(0, i -> ++i)
                .map(String::valueOf)
                .limit(1_000)
                .filter(s -> s.contains("1"))
                .map(Integer::parseInt)
                .toList();

        System.out.println("integers = " + integers);

        assertEquals(271, integers.size());
    }

    @Test
    void testSliding() {
        final var integers = Seq.of(1, 2, 3, 4, 4)
                .sliding(3)
                .peek(System.out::println);

        System.out.println("integers = " + integers);

        assertEquals(3, integers.count());
    }
}