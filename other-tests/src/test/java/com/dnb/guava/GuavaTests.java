package com.dnb.guava;

import com.google.common.base.Functions;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GuavaTests {

    @Test
    void test() {
        final var integers = Lists.newArrayList(3, 4, 5, 3, 2);
        assertEquals(List.of(3,4, 5, 3, 2), integers);
    }
}
