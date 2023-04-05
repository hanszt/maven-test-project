package hzt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;

class JavaDateTests {

    @BeforeEach
    void setup() {
        Locale.setDefault(Locale.GERMANY);
    }

    @Test
    void testFormatDate() {
        Date date = Date.from(LocalDate.of(2021, 11, 26)
                .atStartOfDay()
                .atZone(ZoneId.of("Europe/Amsterdam"))
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

    @Test
    void testDateToLocalTime() {
        final var localDate = JavaDate.toLocalTime(new java.sql.Time(2));
        System.out.println("localTime = " + localDate);
        assertEquals(LocalTime.of(1, 0), localDate);
    }

    @Nested
    class GregorianCalendarTests {

        @Test
        void testGregorianCalendarToZonedDateTime() {
            final var gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.set(2023, Calendar.MARCH, 28, 5, 0, 0);
            gregorianCalendar.set(Calendar.MILLISECOND, 0);
            gregorianCalendar.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Europe/Paris")));
            final var zonedDateTime = gregorianCalendar.toZonedDateTime();

            final var expected = ZonedDateTime.parse("2023-03-28T05:00:00.000+02:00[Europe/Paris]");

            assertAll(
                    () -> assertEquals(expected, zonedDateTime),
                    () -> assertNotEquals(GregorianCalendar.from(expected), gregorianCalendar)
            );
        }
    }
}
