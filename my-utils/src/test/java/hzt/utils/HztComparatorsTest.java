package hzt.utils;

import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Book;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class HztComparatorsTest {

    @Test
    void testComparingReversedOrNot() {
        final List<Book> bookList = TestSampleGenerator.createBookList();
        final List<Book> expectedAscending = bookList.stream()
                .sorted(Comparator.comparing(Book::getCategory))
                .collect(Collectors.toList());
        final List<Book> expectedDescending = bookList.stream()
                .sorted(Comparator.comparing(Book::getCategory).reversed())
                .collect(Collectors.toList());
        assertEquals(expectedAscending, bookList.stream()
                .sorted(HztComparators.comparing(Book::getCategory, true))
                .collect(Collectors.toList()));
        assertEquals(expectedDescending, bookList.stream()
                .sorted(HztComparators.comparing(Book::getCategory, false))
                .collect(Collectors.toList()));
    }
}
