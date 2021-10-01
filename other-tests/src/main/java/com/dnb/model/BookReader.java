package com.dnb.model;

public class BookReader {

    private final Book book;

    public BookReader(Book book) {
        this.book = book;
    }

    public String getBookCategory() {
        return book.getCategory();
    }
}
