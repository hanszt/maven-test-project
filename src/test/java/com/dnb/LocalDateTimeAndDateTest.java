package com.dnb;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.Temporal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

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
        LocalDate localDate = LocalDate.now();
        Temporal temporal = localDate.adjustInto(LocalDate.MAX);


        assertEquals(dateTime.toLocalDate(), dateTimeFromOldDate.toLocalDate());

    }

}