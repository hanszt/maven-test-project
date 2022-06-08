package org.hzt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        list.forEach(System.out::println);
        assertEquals(9, list.get(0));
    }

    @Test
    @DisplayName("Test swap two elements")
    void testSwapTwoElements() {
        List<String> strings = new ArrayList<>(List.of("Hello", "this", "is", "a", "test"));
        Collections.swap(strings, 0, strings.size() - 1);

        assertEquals(List.of("test", "this", "is", "a", "Hello"), strings);
    }
}
