package org.hzt.guava;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GuavaTests {

    @Test
    void test() {
        final var integers = Lists.newArrayList(3, 4, 5, 3, 2);
        assertEquals(List.of(3,4, 5, 3, 2), integers);
    }

    @Test
    void testArrayFromIterable() {
        Iterable<String> strings = () -> new Iterator<>() {
            int counter = 0;

            @Override
            public boolean hasNext() {
                return counter < 100;
            }

            @Override
            public String next() {
                return "hi" + counter++;
            }
        };
        final var stringsArray = Iterables.toArray(strings, String.class);

        assertEquals(100, stringsArray.length);
    }
}
