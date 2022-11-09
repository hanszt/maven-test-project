package org.hzt.mockito;

import org.hzt.model.BookReader;
import org.hzt.test.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @see <a href="https://mincong.io/2020/04/19/mockito-junit5/">Mockito: 3 Ways to Init Mock in JUnit 5</a>
 * Requires Junit.jupiter.version of 5.7.0 or higher
 */
@ExtendWith(MockitoExtension.class)
class MockitoWithAnnotationsTest {

    @Mock
    private Book book;

    private BookReader bookReader;

    @BeforeEach
    void setup() {
        bookReader = new BookReader(book);
    }

    @Test
    void testStubbing2() {
        when(book.getCategory()).thenReturn("Test category");
        assertEquals("Test category", bookReader.getBookCategory(), "Testing first stub");
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    class ParameterMockTest {

        @Test
        void testMockAsParameter(@Mock Book book) {
            when(book.getDescription()).thenReturn("This is a mocked book");
            final var localBookReader = new BookReader(book);
            assertEquals("This is a mocked book", localBookReader.getDescription());
        }
    }
}
