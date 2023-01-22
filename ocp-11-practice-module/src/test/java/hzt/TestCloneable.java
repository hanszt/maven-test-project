package hzt;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TestCloneable {

    @Test
    void testCloningAnArray() {
        int[] array = {1, 4, 56, 3, 5, 4, 3, 8};
        final var clone = array.clone();

        assertNotEquals(clone, array);
        assertArrayEquals(clone, array);
        final var i = 4;
        clone[i] = 100;
        assertNotEquals(clone[i], array[i]);
    }
}
