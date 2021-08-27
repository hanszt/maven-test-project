package com.dnb;

import org.awaitility.Duration;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LocalDateTimeAndDateTest {

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
     * @see <a href="https://www.baeldung.com/awaitlity-testing">Introduction to Awaitility</a>
     */
    @Test
    void testComparingDate() {
        final var now = new Date();
        final var date1 = new Date(1L);
        final var date2 = new Date(2L);
        final var date3 = new Date(3L);
        final var date4 = new Date(1L);
        assertEquals(-1, date1.compareTo(date2));
        assertEquals(1, date3.compareTo(date2));
        assertEquals(0, date4.compareTo(date1));
        await().atLeast(Duration.ONE_MILLISECOND).until(() -> prevMomentComparedToNow(now));
    }

    private boolean prevMomentComparedToNow(Date prevNow) {
        assertEquals(-1, prevNow.compareTo(new Date()));
        return true;
    }

}
