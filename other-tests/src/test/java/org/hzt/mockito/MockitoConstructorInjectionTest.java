package org.hzt.mockito;

import org.hzt.model.BookReader;
import org.hzt.test.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MockitoConstructorInjectionTest {

    final Book book;

    final BookReader bookReader;

    MockitoConstructorInjectionTest(@Mock Book book) {
        this.book = book;
        bookReader = new BookReader(book);
    }

    @BeforeEach
    void resetMocks() {
        Mockito.reset(book);
    }

    @Test
    void testStubbing() {
        when(book.getCategory()).thenReturn("Test category");
        assertEquals("Test category", bookReader.getBookCategory(), "Testing first stub");
    }

    @Test
    void testStubbing2() {
        when(book.getCategory()).thenReturn("Other test category");
        assertEquals("Other test category", bookReader.getBookCategory(), "Testing first stub");
    }

}
