package com.dnb.model;

import org.hzt.model.Book;

public class BookReader {

    private final Book book;

    public BookReader(Book book) {
        this.book = book;
    }

    public String getBookCategory() {
        return book.getCategory();
    }
}
