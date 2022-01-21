package hzt.utils;

import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Book;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class HztComparatorsTest {

    @Test
    void testComparingReversedOrNot() {
        final var bookList = TestSampleGenerator.createBookList();
        final var expectedAscending = bookList.stream()
                .sorted(Comparator.comparing(Book::getCategory))
                .collect(Collectors.toUnmodifiableList());
        final var expectedDescending = bookList.stream()
                .sorted(Comparator.comparing(Book::getCategory).reversed())
                .collect(Collectors.toUnmodifiableList());
        assertEquals(expectedAscending, bookList.stream()
                .sorted(HztComparators.comparing(Book::getCategory, true))
                .collect(Collectors.toUnmodifiableList()));
        assertEquals(expectedDescending, bookList.stream()
                .sorted(HztComparators.comparing(Book::getCategory, false))
                .collect(Collectors.toUnmodifiableList()));
    }
}
