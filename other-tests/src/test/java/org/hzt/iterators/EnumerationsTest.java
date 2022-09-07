package org.hzt.iterators;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EnumerationsTest {

    @Test
    void testGeneratorEnumerator() {
        final var integerEnumeration = Enumerations.generatorEnumeration(0, i -> i < 100, i -> i + 1);
        final var list = Collections.list(integerEnumeration);
        System.out.println("list = " + list);
        assertEquals(100, list.size());
    }

}
