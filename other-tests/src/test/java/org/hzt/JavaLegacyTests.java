package org.hzt;

import org.hzt.utils.sequences.Sequence;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import static org.hzt.utils.It.println;
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

        for (int i : iterable) {
            println(i);
        }

        final List<Integer> lsw = Sequence.of(iterable).toList();
        final List<Integer> ld = Streams.stream(iterable).toList();
        final List<String> list = Collections.list(dictionary.keys());

        assertAll(
                () -> assertEquals(2, lsw.size()),
                () -> assertEquals(2, ld.size()),
                () -> assertEquals(2, list.size()),
                () -> assertEquals(Long.MAX_VALUE, l)
        );
    }

    @Test
    void testStringTokenizer() {
        final var inputString = "This,is,a,test";
        final var delimiter = ",";

        final var stringTokenizer = new StringTokenizer(inputString, delimiter);
        List<String> tokens = new ArrayList<>();
        while (stringTokenizer.hasMoreElements()) {
            tokens.add(stringTokenizer.nextToken());
        }

        final var list = Sequence.of(new StringTokenizer(inputString, delimiter)::asIterator)
                .castIfInstanceOf(String.class)
                .toList();

        final var strings = Pattern.compile(delimiter)
                .splitAsStream(inputString)
                .toList();

        assertAll(
                () -> assertEquals(List.of("This", "is", "a", "test"), tokens),
                () -> assertEquals(strings, tokens),
                () -> assertEquals(strings, list)
        );
    }
}
