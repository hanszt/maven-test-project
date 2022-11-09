package org.hzt.mockito;

import org.hzt.model.BookReader;
import org.hzt.test.model.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class MockitoConstructorInjectionTest {

    final Book book;

    final BookReader bookReader;

    MockitoConstructorInjectionTest(@Mock Book book) {
        this.book = book;
        bookReader = new BookReader(book);
    }

    @Test
    void testStubbing2() {
        when(book.getCategory()).thenReturn("Test category");
        assertEquals("Test category", bookReader.getBookCategory(), "Testing first stub");
    }

}
