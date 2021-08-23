package com.dnb.model;

import java.io.Serializable;

public class Book implements Serializable {

    private static final long serialVersionUID = 1;

    private String bookName;
    private transient String description;
    private transient int copies;
    private final transient String bookCategory;
    @SuppressWarnings("FieldCanBeLocal")
    private final transient String language = "Java";

    public Book(String bookCategory) {
        this.bookCategory = bookCategory;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    public String getBookCategory() {
        return bookCategory;
    }

    public String getLanguage() {
        return language;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookName='" + bookName + '\'' +
                ", description='" + description + '\'' +
                ", copies=" + copies +
                '}';
    }
}
