package org.hzt;

import org.hzt.utils.It;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.MonthDay;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.stream.IntStream;

import static java.time.DayOfWeek.*;
import static java.time.Month.*;
import static java.util.stream.Collectors.partitioningBy;
import static org.awaitility.Awaitility.await;
import static org.hzt.utils.It.println;
import static org.junit.jupiter.api.Assertions.*;

class JavaTimeTest {

    @Test
    void testDateLocalDateEquivalent() {
        final var SECONDS_TO_ADD = 5;
        LocalDateTime dateTime = LocalDateTime.now().plusSeconds(SECONDS_TO_ADD);

        long wachtenMillis = System.currentTimeMillis() + (SECONDS_TO_ADD * 1000);
        Date startDate = new Date(wachtenMillis);
        LocalDateTime dateTimeFromOldDate = startDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        assertEquals(dateTime.toLocalDate(), dateTimeFromOldDate.toLocalDate());
    }

    @Test
    void testDateShouldNotBeMutable() {
        ZoneId.getAvailableZoneIds().forEach(System.out::println);

        final var instant = LocalDate.of(2022, FEBRUARY, 2)
                .atStartOfDay(ZoneId.of("Europe/Amsterdam"))
                .toInstant();

        final var event = new Event(Date.from(instant));
        final var date = event.date();
        assert date != null;
        date.setTime(2L);

        assertEquals(Date.from(instant), event.date());
    }

    private record Event(Date date) {

        Event(Date date) {
            this.date = date != null ? new Date(date.getTime()) : null;
        }

        @Override
        public Date date() {
            return date != null ? new Date(date.getTime()) : null;
        }
    }

    /**
     * @see <a href="https://www.baeldung.com/awaitlity-testing">Introduction to Awaitility</a>
     */
    @Test
    @Disabled("This test can be ignored")
    void testComparingDate() {
        final var now = new Date();
        final var date1 = new Date(1L);
        final var date2 = new Date(2L);
        final var date3 = new Date(3L);
        final var date4 = new Date(1L);

        assertAll(
                () -> assertEquals(-1, date1.compareTo(date2)),
                () -> assertEquals(1, date3.compareTo(date2)),
                () -> assertEquals(0, date4.compareTo(date1)),
                () -> await().atLeast(Duration.ofMillis(1)).until(() -> prevMomentComparedToNow(now))
        );
    }

    private boolean prevMomentComparedToNow(Date prevNow) {
        assertEquals(-1, prevNow.compareTo(new Date()));
        return true;
    }

    @Test
    void testIsLeapYear() {
        final var isLeapYearMap = IntStream.rangeClosed(0, 3000)
                .mapToObj(Year::of)
                .collect(partitioningBy(Year::isLeap));
        isLeapYearMap.entrySet().forEach(It::println);
        assertEquals(728, isLeapYearMap.get(true).size());
        assertEquals(2273, isLeapYearMap.get(false).size());
    }

    @Test
    void testMonthDay() {
        MonthDay monthDay = MonthDay.of(FEBRUARY, 29);
        assertTrue(monthDay.isValidYear(2000));
        assertFalse(monthDay.isValidYear(1900));
    }

    @Test
    void testYearMonth() {
        assertFalse(YearMonth.of(2100, FEBRUARY).isValidDay(29));
        assertTrue(YearMonth.of(2000, FEBRUARY).isValidDay(29));
    }

    @Test
    void testDayOfWeek() {
        assertAll(
                () -> assertEquals(WEDNESDAY, LocalDate.of(1989, OCTOBER, 18).getDayOfWeek()),
                () -> assertEquals(SUNDAY, LocalDate.of(1951, SEPTEMBER, 23).getDayOfWeek()),
                () -> assertEquals(SUNDAY, MONDAY.plus(34649))
        );
    }

    @Test
    void testLocalDateTimeFromEpochMilli() {
        //Epoch milli is the number of milliseconds since
        final var epochMilli = 1_631_531_699_081L;
        final var localDateTime = Instant.ofEpochMilli(epochMilli)
                .atZone(ZoneId.ofOffset("", ZoneOffset.UTC))
                .toLocalDateTime();

        println(localDateTime);

        final var toEpochMilli = localDateTime
                .toInstant(ZoneOffset.UTC)
                .toEpochMilli();

        println("LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli() = " + toEpochMilli);

        assertEquals(epochMilli, toEpochMilli);
    }

    @Nested
    class JodaTimeToJavaTimeTests {

        @Test
        @DisplayName("Joda time format vs java time format")
        void jodaTimeFormatVsJavaTimeFormat() {
            org.joda.time.LocalDate date = new org.joda.time.LocalDate(2022, 1, 1);
            LocalDate localDate = LocalDate.of(2022, JANUARY, 1);

            final var actual = localDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            final var expected = date.toString("dd-MM-yyyy");

            It.println(actual);

            assertEquals(expected, actual);
        }
    }

}
