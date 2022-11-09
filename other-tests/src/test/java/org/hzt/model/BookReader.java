package org.hzt.model;

import org.hzt.test.model.Book;

public class BookReader {

    private final Book book;

    public BookReader(Book book) {
        this.book = book;
    }

    public String getBookCategory() {
        return book.getCategory();
    }

    public String getDescription() {
        return book.getDescription();
    }
}
