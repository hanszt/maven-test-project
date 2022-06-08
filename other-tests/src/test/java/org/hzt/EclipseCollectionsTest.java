package org.hzt;

import org.eclipse.collections.impl.set.mutable.primitive.IntHashSet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EclipseCollectionsTest {

    @Test
    void testIntHashSet() {
        IntHashSet set = new IntHashSet(1, 2, 3, 4, 5, 3, 6);

        assertEquals(6, set.size());
    }
}
