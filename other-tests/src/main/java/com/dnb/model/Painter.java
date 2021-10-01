package com.dnb.model;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

public class Painter {

    private final String firstName;
    private final String lastname;
    private final LocalDate dateOfBirth;
    private LocalDate dateOfDeath;

    public Painter(String firstName, String lastname, LocalDate dateOfBirth) {
        this.firstName = firstName;
        this.lastname = lastname;
        this.dateOfBirth = dateOfBirth;
    }

    public Period age() {
        return Period.between(dateOfBirth, dateOfDeath != null ? dateOfDeath : LocalDate.now());
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastname() {
        return lastname;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Optional<LocalDate> getDateOfDeath() {
        return Optional.ofNullable(dateOfDeath);
    }

    public void setDateOfDeath(LocalDate dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
    }

    @Override
    public String toString() {
        return "Painter{" +
                "firstName='" + firstName + '\'' +
                ", lastname='" + lastname + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }
}
