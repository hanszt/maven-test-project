package org.hzt;

import org.eclipse.collections.impl.map.mutable.primitive.IntIntHashMap;
import org.eclipse.collections.impl.set.mutable.primitive.IntHashSet;
import org.junit.jupiter.api.Test;

import static org.hzt.utils.It.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EclipseCollectionsTest {

    @Test
    void testIntHashSet() {
        IntHashSet set = new IntHashSet(1, 2, 3, 4, 5, 3, 6);

        assertEquals(6, set.size());
    }

    @Test
    void testAddToValue() {
        final IntIntHashMap intIntHashMap = new IntIntHashMap();
        for (int i = 0; i < 12; i++) {
            intIntHashMap.addToValue(0, 1);
        }
        intIntHashMap.addToValue(1, 1);
        intIntHashMap.addToValue(4, 2);

        println("intIntHashMap = " + intIntHashMap);

        assertArrayEquals(new int[] {12, 1, 2}, intIntHashMap.toArray());
    }
}
