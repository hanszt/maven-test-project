package org.hzt;

import java.time.LocalDate;
import java.time.Period;

import static java.time.temporal.ChronoUnit.DAYS;

public final class AgeCalculator {

    private AgeCalculator() {
    }

    public static long calculateDurationInDaysBetween(LocalDate from, LocalDate to) {
        return DAYS.between(from, to);
    }

    public static long calculatePeriodInYearsBetween(LocalDate from, LocalDate to) {
        return Period.between(from, to).getYears();
    }

    public static LocalDate calculateDateWhenAgeInDaysReached(long ageInDays, LocalDate birthDate) {
        return birthDate.plusDays(ageInDays);
    }
}
