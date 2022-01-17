package com.dnb;

import com.dnb.model.BookReader;
import org.hzt.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @see <first href="https://mincong.io/2020/04/19/mockito-junit5/">Mockito: 3 Ways to Init Mock in JUnit 5</first>
 * Requires Junit.jupiter.version of 5.7.0 or higher
 */
@ExtendWith(MockitoExtension.class)
class MockitoWithAnnotationsTest {

    @Mock
    private Book book;

    private BookReader bookReader;

    @BeforeEach
    void setup() {
        when(book.getCategory()).thenReturn("Test category");
        bookReader = new BookReader(book);
    }

    @Test
    void testStubbing2() {
        assertEquals("Test category", bookReader.getBookCategory(), "Testing first stub");
    }
}
