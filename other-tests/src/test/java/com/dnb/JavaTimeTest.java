package com.dnb;

import org.junit.jupiter.api.Disabled;
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
import java.util.Date;
import java.util.stream.IntStream;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.DayOfWeek.WEDNESDAY;
import static java.time.Month.FEBRUARY;
import static java.time.Month.OCTOBER;
import static java.time.Month.SEPTEMBER;
import static java.util.stream.Collectors.partitioningBy;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    /**
     * @see <first href="https://www.baeldung.com/awaitlity-testing">Introduction to Awaitility</first>
     */
    @Test
    @Disabled("This test can be ignored")
    void testComparingDate() {
        final var now = new Date();
        final var date1 = new Date(1L);
        final var date2 = new Date(2L);
        final var date3 = new Date(3L);
        final var date4 = new Date(1L);
        assertEquals(-1, date1.compareTo(date2));
        assertEquals(1, date3.compareTo(date2));
        assertEquals(0, date4.compareTo(date1));
        await().atLeast(Duration.ofMillis(1)).until(() -> prevMomentComparedToNow(now));
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
        isLeapYearMap.entrySet().forEach(System.out::println);
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
        assertEquals(WEDNESDAY, LocalDate.of(1989, OCTOBER, 18).getDayOfWeek());
        assertEquals(SUNDAY, LocalDate.of(1951, SEPTEMBER, 23).getDayOfWeek());
        assertEquals(SUNDAY, MONDAY.plus(34649));
    }

    @Test
    void testLocalDateTimeFromEpochMilli() {
        //Epoch milli is the number of milliseconds since
        final var epochMilli = 1_631_531_699_081L;
        final var localDateTime = Instant.ofEpochMilli(epochMilli)
                .atZone(ZoneId.ofOffset("", ZoneOffset.UTC))
                .toLocalDateTime();
        System.out.println(localDateTime);
        final var toEpochMilli = localDateTime
                .toInstant(ZoneOffset.UTC)
                .toEpochMilli();
        System.out.println("LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli() = " + toEpochMilli);
        assertEquals(epochMilli, toEpochMilli);
    }

}
