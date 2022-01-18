package hzt.utils;

import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Book;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class HztComparatorsTest {

    @Test
    void testComparingReversedOrNot() {
        final var bookList = TestSampleGenerator.createBookList();
        final var expectedAscending = bookList.stream()
                .sorted(Comparator.comparing(Book::getCategory))
                .toList();
        final var expectedDescending = bookList.stream()
                .sorted(Comparator.comparing(Book::getCategory).reversed())
                .toList();
        assertEquals(expectedAscending, bookList.stream()
                .sorted(HztComparators.comparing(Book::getCategory, true))
                .toList());
        assertEquals(expectedDescending, bookList.stream()
                .sorted(HztComparators.comparing(Book::getCategory, false))
                .toList());
    }
}
