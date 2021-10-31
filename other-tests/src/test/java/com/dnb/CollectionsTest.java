package com.dnb;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CollectionsTest {

    @Test
    void testAddingEmptyMapToMap() {
        Map<String, String> map = new HashMap<>();
        map.putAll(Map.of("Key", "Value"));
        map.putAll(Collections.emptyMap());
        assertEquals(1, map.size());
    }
}
