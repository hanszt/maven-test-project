package hzt.preview.pattern_matching;

import java.time.LocalDate;

public sealed class Animal permits Cat, Dog {

    private final String name;
    private final LocalDate dateOfBirth;

    Animal(String name, LocalDate dateOfBirth) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
}
