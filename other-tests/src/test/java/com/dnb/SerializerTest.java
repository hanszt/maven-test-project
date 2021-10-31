package com.dnb;

import org.hzt.model.Book;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SerializerTest {

    @Test
    void testSerialisation() throws Exception {
        Book book = new Book("Fiction");
        String expectedBookName = "Java Reference";
        book.setTitle(expectedBookName);
        book.setDescription("will not be saved");
        book.setCopies(25);

        Serializer.serialize(book, "serialization_test");
        Book deserializedBook = Serializer.deserialize("serialization_test");

        assertEquals(expectedBookName, deserializedBook.getTitle());
        assertNull(deserializedBook.getDescription());
        assertEquals(0, deserializedBook.getCopies());
        assertNull(deserializedBook.getCategory());
        assertEquals("Java", book.getLanguage());
    }
}
