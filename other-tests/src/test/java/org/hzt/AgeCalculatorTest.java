package org.hzt;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.hzt.utils.It.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AgeCalculatorTest {

    @Test
    void testCalculateAgeInDays() {
        final long ageInDays = AgeCalculator
                .calculateDurationInDaysBetween(
                        LocalDate.of(1994, Month.OCTOBER, 20),
                        LocalDate.of(2022, Month.MARCH, 7));

        println("ageInDays = " + ageInDays);

        assertEquals(10_000, ageInDays);
    }

    @Test
    void testCalculateLocalDateWhenAgeInDaysReached() {
        final LocalDate dateWhenAgeSophieInDaysIs10_000 = AgeCalculator
                .calculateDateWhenAgeInDaysReached(10_000, LocalDate.of(1994, Month.OCTOBER, 20));

        final LocalDate dateWhenAgeHansInDaysIs15_000 = AgeCalculator
                .calculateDateWhenAgeInDaysReached(15_000, LocalDate.of(1989, Month.OCTOBER, 18));

        final LocalDate dateWhenAgeHuibInDaysIs30_000 = AgeCalculator
                .calculateDateWhenAgeInDaysReached(30_000, LocalDate.of(1954, Month.SEPTEMBER, 23));

        println("dateWhenAgeSophieInDaysIs10_000 = " + dateWhenAgeSophieInDaysIs10_000);
        println("dateWhenAgeHansInDaysIs15_000 = " + dateWhenAgeHansInDaysIs15_000);
        println("dateWhenAgeHuibInDaysIs30_000 = " + dateWhenAgeHuibInDaysIs30_000);

        assertEquals(LocalDate.of(2022, Month.MARCH, 7), dateWhenAgeSophieInDaysIs10_000);
    }

    @Test
    void testCalculateAgeInYears() {
        final LocalDate BIRTHDATE_HANS = LocalDate.of(1989, Month.OCTOBER, 18);
        final LocalDate dateWhenAgeHansInDaysIs20_000 = AgeCalculator
                .calculateDateWhenAgeInDaysReached(20_000, BIRTHDATE_HANS);

        final LocalDate BIRTHDATE_HUIB = LocalDate.of(1954, Month.SEPTEMBER, 23);
        final LocalDate dateWhenAgeHuibInDaysIs30_000 = AgeCalculator
                .calculateDateWhenAgeInDaysReached(30_000, BIRTHDATE_HUIB);

        final long ageInYears = AgeCalculator.calculatePeriodInYearsBetween(
                BIRTHDATE_HANS,
                dateWhenAgeHansInDaysIs20_000);

        final long ageInYearsHuibWhen30_000Days = AgeCalculator.calculatePeriodInYearsBetween(
                BIRTHDATE_HUIB,
                dateWhenAgeHuibInDaysIs30_000);

        println("ageInYears = " + ageInYears);
        println("ageInYearsHuibWhen30_000Days = " + ageInYearsHuibWhen30_000Days);

        assertEquals(54, ageInYears);
    }

}
