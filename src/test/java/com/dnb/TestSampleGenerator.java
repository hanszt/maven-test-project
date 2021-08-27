package com.dnb;

import com.dnb.model.Book;
import com.dnb.model.Person;

import java.time.LocalDate;
import java.util.List;

public final class TestSampleGenerator {

    private TestSampleGenerator() {
    }

    public static List<Book> createBookList() {
        return List.of(new Book("Harry Potter", "Fiction"),
                new Book("Lord of the Rings", "Fiction"),
                new Book("Pragmatic Programmer", "Programming"),
                new Book("OCP 11 Volume 1", "Programming"),
                new Book("Homo Deus", "Educational"),
                new Book("The da Vinci Code", "Fiction"),
                new Book("The da Vinci Code", "Fiction"));
    }

    public static List<Person> createTestPersonList() {
        return List.of(new Person("Sophie", "Vullings", LocalDate.of(1994, 10, 20), true),
                new Person("Hans", "Zuidervaart", LocalDate.of(1989, 10, 18), true),
                new Person("Huib", "Zuidervaart", LocalDate.of(1951, 9, 23)),
                new Person("Nikolai", "Jacobs", LocalDate.of(1990, 2, 1), true),
                new Person("Ted", "Burgmeijer", LocalDate.of(1990, 3, 2)),
                new Person("Martijn", "Ruigrok", LocalDate.of(1940, 7, 3)),
                new Person("Henk", "Zuidervaart", LocalDate.of(1938, 6, 3), true),
                new Person("Ben", "Bello", LocalDate.of(1970, 6, 3)));
    }

}
