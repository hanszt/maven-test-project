package hzt.preview.pattern_matching;

import java.time.LocalDate;

public sealed class Animal permits Cat, Dog {

    private final String name;
    private final LocalDate dateOdBirth;

    Animal(String name, LocalDate dateOdBirth) {
        this.name = name;
        this.dateOdBirth = dateOdBirth;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDateOdBirth() {
        return dateOdBirth;
    }
}
