package org.hzt;

import hzt.collections.MutableListX;
import hzt.sequences.Sequence;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JavaLegacyTests {

    @Test
    void testHashTable() {
        Dictionary<String, Integer> dictionary = new Hashtable<>();
        dictionary.put("test", 1);
        dictionary.put("test 2", 2);

        final Iterable<Integer> iterable = () -> dictionary.elements().asIterator();
        final var l = iterable.spliterator().estimateSize();

        for (Integer s : iterable) {
            System.out.println(s);
        }

        final List<Integer> lsw = Sequence.of(iterable).toList();
        final List<Integer> ld = StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toUnmodifiableList());
        final List<String> list = Collections.list(dictionary.keys());

        assertAll(
                () -> assertEquals(2, lsw.size()),
                () -> assertEquals(2, ld.size()),
                () -> assertEquals(2, list.size()),
                () -> assertEquals(Long.MAX_VALUE, l)
        );
    }
}
