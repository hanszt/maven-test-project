package org.hzt.functional_iterator_sequences;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FunctionalIteratorTest {

    @Test
    void testFunctionalIteratorForEachRemaining() {
        List<Integer> list = new ArrayList<>();

        FunctionalSequence.of(IntStream.range(0, 1_000).boxed())
                .functionalIterator()
                .forEachRemaining(list::add);

        assertEquals(1_000, list.size());
    }
}
