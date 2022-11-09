package org.hzt.mockito;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MockitoTest {

    @Test
    void testStub() {
        @SuppressWarnings("unchecked") LinkedList<String> mockedList = mock(LinkedList.class);
        when(mockedList.get(0)).thenReturn("first");

        assertAll(
                () -> assertEquals("first", mockedList.get(0), "Testing first stub"),
                () -> assertNull(mockedList.get(1)));
    }

}
