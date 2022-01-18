package org.hzt;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;

public final class AgeCalculator {

    private AgeCalculator() {
    }

    public static long calculateDurationInDaysBetween(LocalDate from, LocalDate to) {
        return Duration.between(from.atStartOfDay(), to.atStartOfDay()).toDays();
    }

    public static long calculatePeriodInYearsBetween(LocalDate from, LocalDate to) {
        return Period.between(from, to).getYears();
    }

    public static LocalDate calculateDateWhenAgeInDaysReached(long ageInDays, LocalDate birthDate) {
        return birthDate.plusDays(ageInDays);
    }
}
