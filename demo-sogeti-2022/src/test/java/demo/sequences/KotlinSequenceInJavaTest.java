package demo.sequences;

import org.junit.jupiter.api.Test;

import java.util.List;

import static kotlin.sequences.SequencesKt.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class KotlinSequenceInJavaTest {

    @Test
    void testKotlinSequenceInJava() {
        List<Integer> integerList = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);

        final var integers = toList(filter(map(asSequence(integerList.iterator()),
                i -> i * 3),
                i -> i % 2 == 0));

        assertEquals(List.of(6, 12, 18, 24), integers);
    }
}
