package org.hzt;

import org.eclipse.collections.api.set.primitive.MutableIntSet;
import org.eclipse.collections.impl.list.mutable.primitive.IntArrayList;
import org.eclipse.collections.impl.map.mutable.primitive.IntIntHashMap;
import org.eclipse.collections.impl.set.mutable.primitive.IntHashSet;
import org.junit.jupiter.api.Test;

import static org.hzt.utils.It.println;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EclipseCollectionsTest {

    @Test
    void testIntHashSet() {
        final var set = new IntHashSet(1, 2, 3, 4, 5, 3, 6);
        assertEquals(6, set.size());
    }

    @Test
    void testIntListStream() {
        final var intArrayList = new IntArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);

        final var set = intArrayList.primitiveStream()
                .filter(i -> i % 3 == 0)
                .collect(IntHashSet::new, MutableIntSet::add, MutableIntSet::addAll);

        assertEquals(new IntHashSet(3, 6, 9, 12), set);
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

        assertArrayEquals(new int[]{12, 1, 2}, intIntHashMap.toArray());
    }
}
