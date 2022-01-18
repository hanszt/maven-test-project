package org.hzt.test.model;

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

    @Override
    public int compareTo(Painting o) {
        return name.compareTo(o.name);
    }
}
