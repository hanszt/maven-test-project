package org.hzt.test.model;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.Period;
import java.time.Year;
import java.util.Objects;

public final class Painting
        implements Comparable<Painting> {
    private final String name;
    private final Painter painter;
    private final Year yearOfCreation;
    private final boolean isInMuseum;

    public Painting(String name, Painter painter, Year yearOfCreation, boolean isInMuseum) {
        this.name = name;
        this.painter = painter;
        this.yearOfCreation = yearOfCreation;
        this.isInMuseum = isInMuseum;
    }

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

    public String name() {
        return name;
    }

    public Painter painter() {
        return painter;
    }

    public Year yearOfCreation() {
        return yearOfCreation;
    }

    public boolean isInMuseum() {
        return isInMuseum;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Painting) obj;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.painter, that.painter) &&
                Objects.equals(this.yearOfCreation, that.yearOfCreation) &&
                this.isInMuseum == that.isInMuseum;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, painter, yearOfCreation, isInMuseum);
    }

    @Override
    public String toString() {
        return "Painting[" +
                "name=" + name + ", " +
                "painter=" + painter + ", " +
                "yearOfCreation=" + yearOfCreation + ", " +
                "isInMuseum=" + isInMuseum + ']';
    }

}
