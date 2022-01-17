package com.dnb;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MockitoTest {

    @Test
    void testStub() {
        @SuppressWarnings("unchecked") LinkedList<String> mockedList = mock(LinkedList.class);
        when(mockedList.get(0)).thenReturn("first");
        assertEquals("first", mockedList.get(0), "Testing first stub");
    }

}
