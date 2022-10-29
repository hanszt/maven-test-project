package hzt.collections;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SetTests {

    @Test
    @Disabled("Takes to long")
    void testUnboundedSetAddThrowsOutOfMemoryError() {
        assertThrows(OutOfMemoryError.class, this::fillSetUntilOutOfMemory);
    }

    private void fillSetUntilOutOfMemory() {
        Set<String> set = new HashSet<>();
        try {
            int counter = 0;
            //noinspection InfiniteLoopStatement
            while (true) {
                set.add("hoi" + counter);
                counter++;
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            System.out.println(set.size());
        }
    }

    @Test
    void testSetOfArraysCanStoreMultipleEqualContentArrays() {
        Set<int[]> arraySet = new HashSet<>();
        arraySet.add(new int[]{1, 2, 3, 4});

        assertAll(
                () -> assertTrue(arraySet.add(new int[]{1, 2, 3, 4})),
                () -> assertEquals(2, arraySet.size())
        );
    }
}
