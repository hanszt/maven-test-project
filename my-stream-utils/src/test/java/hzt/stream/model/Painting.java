package hzt.stream.model;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.Period;
import java.time.Year;

public class Painting {

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

    public String getName() {
        return name;
    }

    public Painter getPainter() {
        return painter;
    }

    public Year getYearOfCreation() {
        return yearOfCreation;
    }

    public boolean isInMuseum() {
        return isInMuseum;
    }

    @Override
    public String toString() {
        return "Painting{" +
                "name='" + name + '\'' +
                ", painter=" + painter +
                ", yearOfCreation=" + yearOfCreation +
                ", isInMuseum=" + isInMuseum +
                '}';
    }
}
