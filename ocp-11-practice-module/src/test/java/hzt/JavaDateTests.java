package hzt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JavaDateTests {

    @BeforeEach
    void setup() {
        Locale.setDefault(Locale.GERMANY);
    }

    @Test
    void testFormatDate() {
        Date date = Date.from(LocalDate.of(2021, 11, 26)
                .atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());

        assertAll(
                () -> assertEquals("11", new SimpleDateFormat("M").format(date)),
                () -> assertEquals("11", new SimpleDateFormat("MM").format(date)),
                () -> assertEquals("Nov", new SimpleDateFormat("MMM").format(date)),
                () -> assertEquals("novembre", new SimpleDateFormat("MMMM", Locale.FRANCE).format(date))
        );
    }

    @Test
    void testSqlDateToLocalDate() {
        final var date = new java.sql.Date(0L);
        final var localDate = date.toLocalDate();

        assertEquals(LocalDate.EPOCH, localDate);
    }

    @Test
    void testSqlDateFromLocalDate() {
        final var date = java.sql.Date.valueOf(LocalDate.of(2022, Month.AUGUST, 2));
        assertTrue(new Date(1231L).before(date));
    }

    @Test
    void testThrowsUnsupportedOperationExceptionWhenToInstantCalled() {
        final var date = java.sql.Date.valueOf(LocalDate.of(2022, Month.AUGUST, 2));
        assertThrows(UnsupportedOperationException.class, date::toInstant);
    }

    @Test
    void testDateToLocalDate() {
        final var localDate = JavaDate.toLocalDate(new java.sql.Date(2));
        System.out.println("localDate = " + localDate);
        assertEquals(LocalDate.EPOCH, localDate);
    }
}
