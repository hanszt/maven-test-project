package org.hzt.test.model;

import java.time.Clock;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.Period;
import java.time.Year;

public record Painting(String name, Painter painter, Year yearOfCreation, boolean isInMuseum)
        implements Comparable<Painting> {

    public Period age() {
        return Period.between(yearOfCreation.atMonthDay(MonthDay.now()), LocalDate.now());
    }

    public int ageInYears() {
        return age().getYears();
    }

    public Year getYearOfCreation() {
        return yearOfCreation;
    }

    public Period getMilleniumOfCreation() {
        if (yearOfCreation.isAfter(Year.of(1000)) && yearOfCreation.isBefore(Year.of(2000))) {
            return Period.between(LocalDate.of(1000, 1, 1), LocalDate.of(1999, 12, 31));
        } else if (yearOfCreation.isBefore(Year.of(1000))) {
            return Period.between(LocalDate.of(0, 1, 1), LocalDate.of(999, 12, 31));
        } else {
            return Period.between(LocalDate.of(2000, 1, 1), LocalDate.now(Clock.systemDefaultZone()));
        }
    }

    @Override
    public int compareTo(Painting o) {
        return name.compareTo(o.name);
    }
}
