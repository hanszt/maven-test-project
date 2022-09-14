package org.hzt.sequences.functional_iterator_sequences;

import org.hzt.utils.It;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hzt.utils.It.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FunctionalSequenceTest {

    @Test
    void testFuncSequenceMapReduce() {
        final var strings = List.of("this", "is", "a", "test");

        final var sum = FunctionalSequence.of(strings)
                .onEach(It::println)
                .map(String::length)
                .reduce(0, Integer::sum);

        println("sum = " + sum);

        assertEquals(11, sum);
    }

    @Test
    void testFuncSequenceFromStreamAndReduce() {
        final var stream = IntStream.range(0, 1000).boxed();

        final var list = FunctionalSequence.of(stream)
                .onEach(It::println)
                .toList();

        println("list = " + list);

        assertEquals(1_000, list.size());
    }

    @Test
    void testFuncSequenceReduce() {
        final var strings = List.of(1, 2, 3, 4);

        final var sum = FunctionalSequence.of(strings)
                .onEach(It::println)
                .reduce(0, Integer::sum);

        println("sum = " + sum);

        assertEquals(10, sum);
    }

    @Test
    void testSequenceMapToStreamToSet() {
        final var strings = List.of(1, 2, 3, 4);

        final var set = FunctionalSequence.of(strings)
                .map(String::valueOf)
                .stream()
                .collect(Collectors.toSet());

        println("set = " + set);

        assertEquals(Set.of("1", "2", "3", "4"), set);
    }

    @Test
    void testSequenceMapToStreamParallel() {
        final var strings = List.of(1, 2, 3, 4, 4, 5, 6, 4);

        final var set = FunctionalSequence.of(strings)
                .map(String::valueOf)
                .stream()
                .parallel()
                .collect(Collectors.toSet());

        assertEquals(Set.of("1", "2", "3", "4", "5", "6"), set);
    }

    @Test
    void testSequenceFilter() {
        final var strings = List.of(1, 2, 3, 4, 4, 5, 6, 4);

        final var set = FunctionalSequence.of(strings)
                .filter(i -> i % 2 == 0)
                .toSet();

        assertEquals(Set.of(2, 4, 6), set);
    }

    @Test
    void testSequenceFilterMapToArray() {
        final var strings = List.of(1, 2, 12, 24342, 421, 23);

        final var array = FunctionalSequence.of(strings)
                .map(String::valueOf)
                .filter(s -> s.contains("1"))
                .toArray(String[]::new);

        assertArrayEquals(new String[]{"1", "12", "421"}, array);
    }

}
