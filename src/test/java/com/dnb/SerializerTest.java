package com.dnb;

import com.dnb.model.Book;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SerializerTest {

    @Test
    void testSerialisation() throws Exception {
        Book book = new Book("Fiction");
        String expectedBookName = "Java Reference";
        book.setBookName(expectedBookName);
        book.setDescription("will not be saved");
        book.setCopies(25);

        Serializer.serialize(book);
        Book deserializedBook = Serializer.deserialize();

        assertEquals(expectedBookName, deserializedBook.getBookName());
        assertNull(deserializedBook.getDescription());
        assertEquals(0, deserializedBook.getCopies());
        assertNull(deserializedBook.getBookCategory());
        assertEquals("Java", book.getLanguage());
    }
}