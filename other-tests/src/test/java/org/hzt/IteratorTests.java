package org.hzt;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.PrimitiveIterator;
import java.util.Set;

import static org.hzt.utils.It.println;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IteratorTests {

    @Test
    void testIterator() {
        Iterator<Integer> iterator = new PrimitiveIterator.OfInt() {
            int i = 0;
            @Override
            public int nextInt() {
                return ++i;
            }
            @Override
            public boolean hasNext() {
                return i < 100;
            }
        };
        for (int i = 0; i < 40; i++) {
            iterator.next();
        }
        Set<Integer> set = new HashSet<>();
        iterator.forEachRemaining(set::add);
        assertEquals(60, set.size());
    }

    @Test
    void testIteratorOfInt() {
        class IntIterator implements PrimitiveIterator.OfInt {
            int i = 0;
            @Override
            public int nextInt() {
                return ++i;
            }
            @Override
            public boolean hasNext() {
                return i < 100;
            }
        }
        IntIterator iterator = new IntIterator();
        int result = 0;
        while (iterator.hasNext()) {
            result = iterator.nextInt();
            println("result = " + result);
        }
        iterator.i = 80;
        iterator.forEachRemaining((int i) -> println("i = " + i));
        assertEquals(100, result);
    }

}
