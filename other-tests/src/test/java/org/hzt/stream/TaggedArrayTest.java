package org.hzt.stream;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaggedArrayTest {

    @Test
    void testTaggedArrayParallelTraversal() {
        Set<String> result = new HashSet<>();
        String[] strings = {"This", "is", "the", "actual", "data", "!"};
        Object[] tags = new Object[] {"These", "are", "the", "ignored", "tags", LocalDate.now()};

        final var stringTaggedArray = new TaggedArray<>(strings, tags);

        stringTaggedArray.parEach(result::add);

        assertEquals(Set.of("This", "is", "the", "actual", "data", "!"), result);
    }

}
