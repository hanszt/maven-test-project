package org.hzt;

import org.hzt.utils.It;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CollectionsTest {

    @Test
    void testAddingEmptyMapToMap() {
        Map<String, String> map = new HashMap<>();
        map.put("Key", "Value");
        map.putAll(Collections.emptyMap());
        assertEquals(1, map.size());
    }

    @Test
    void testSortReverseOrder() {
        List<Integer> list = new ArrayList<>(List.of(1, 5, 4, 7, 2, 8, 9, 5, 7));
        list.sort(Collections.reverseOrder());
        list.forEach(It::println);
        assertEquals(9, list.get(0));
    }

    @Test
    @DisplayName("Test swap two elements")
    void testSwapTwoElements() {
        List<String> strings = new ArrayList<>(List.of("Hello", "this", "is", "a", "test"));
        Collections.swap(strings, 0, strings.size() - 1);
        assertEquals(List.of("test", "this", "is", "a", "Hello"), strings);
    }

    @Test
    void testCollectionsRotate() {
        final var list = new ArrayList<>(List.of("This", "is", "a", "test"));
        Collections.rotate(list, 2);
        assertEquals(List.of("a", "test", "This", "is"), list);
    }

    @Test
    void testCollectionsDisjoint() {
        final var list1 = new ArrayList<>(List.of("This", "is", "a", "test"));
        final var list2 = new ArrayList<>(List.of("some", "other", "stuff"));
        assertTrue(Collections.disjoint(list1, list2));
    }

    @Test
    void testNCopies() {
        final var strings = Collections.nCopies(100, "test");
        assertEquals(100, strings.size());
    }

    @Nested
    class BinarySearchTest {

        @Test
        void testBinarySearch() {
            final var strings = List.of("a", "b", "c", "d", "e", "f", "v", "x", "z");
            final var index = Collections.binarySearch(strings, "c");
            assertEquals(2, index);
        }

        @Test
        void testBinarySearchNotFound() {
            final var strings = List.of("a", "b", "c", "d", "e", "f", "v", "x", "z");
            final var index = Collections.binarySearch(strings, "y");
            assertEquals(-9, index);
        }
    }
}
