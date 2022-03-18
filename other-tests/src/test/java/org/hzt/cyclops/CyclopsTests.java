package org.hzt.cyclops;

import cyclops.data.NonEmptyList;
import org.hzt.utils.collections.MutableListX;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class CyclopsTests {

    @Test
    void testCyclopsList() {
        var cyclopsList = NonEmptyList.of("Hallo", "Dit", "is", "een", "test");
        var myList = MutableListX.of("Hallo", "Dit", "is", "een", "test");

        final var lengths = myList.map(String::length);
        final var integers = cyclopsList.map(String::length).toList();

        assertIterableEquals(lengths, integers);
    }
}
