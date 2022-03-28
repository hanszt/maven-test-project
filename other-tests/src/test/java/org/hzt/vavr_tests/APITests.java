package org.hzt.vavr_tests;

import io.vavr.API;
import io.vavr.NotImplementedError;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class APITests {

    @Test
    void testTODO() {
        assertThrows(NotImplementedError.class, API::TODO);
    }
}
