package org.hzt.model;

import org.hzt.custom_annotations.Initializable;
import org.hzt.custom_annotations.JsonElement;
import org.hzt.custom_annotations.JsonSerializable;
import org.hzt.utils.strings.StringX;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.Period;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
@JsonSerializable
public class Person implements Comparable<Person> {

    public static final String ZUIDERVAART = "Zuidervaart";
    @JsonElement
    private String firstName;

    @JsonElement
    private String lastName;

    @JsonElement(key = "birthDate")
    private LocalDate dateOfBirth;

    private String address;

    private final boolean playingPiano;

    public Person(String firstName, String lastName, LocalDate dateOfBirth, boolean playingPiano) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.playingPiano = playingPiano;
    }

    public Person(String firstName, String lastName, LocalDate dateOfBirth) {
        this(firstName, lastName, dateOfBirth, false);
    }

    public Person(String firstName) {
        this(firstName, "Undefinied", null, false);
    }

    public Person(String firstName, String lastName) {
        this(firstName, lastName, null);
    }

    @Initializable
    private void initNames() {
        this.firstName = StringX.capitalized(firstName);
        this.lastName = StringX.capitalized(lastName);
    }

    public MonthDay getMonthDayFromDateOfBirth() {
        return MonthDay.from(getDateOfBirth());
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Person withDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public int getAge() {
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    public String getAddress() {
        return address;
    }

    public boolean isPlayingPiano() {
        return playingPiano;
    }

    public static List<Person> createTestPersonList() {
        return List.of(new Person("Sophie", "Vullings", LocalDate.of(1994, 10, 20), true),
                new Person("Hans", ZUIDERVAART, LocalDate.of(1989, 10, 18), true),
                new Person("Huib", ZUIDERVAART, LocalDate.of(1951, 9, 23)),
                new Person("Nikolai", "Jacobs", LocalDate.of(1990, 2, 1), true),
                new Person("Ted", "Burgmeijer", LocalDate.of(1990, 3, 2)),
                new Person("Martijn", "Ruigrok", LocalDate.of(1940, 7, 3)),
                new Person("Henk", ZUIDERVAART, LocalDate.of(1940, 6, 3), true),
                new Person("Ben", "Bello", LocalDate.of(1970, 6, 3)));
    }

    @Override
    public int compareTo(Person o) {
        return this.lastName.compareTo(o.lastName);
    }

    public int compareFirstName(Person other) {
        return firstName.compareTo(other.getFirstName());
    }

    public String fullName() {
        return firstName + " " + lastName;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof Person other && Objects.equals(this.lastName, other.lastName));
    }

    @Override
    public int hashCode() {
        return Objects.hash(lastName);
    }

    @Override
    public String toString() {
        return "Person{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", address='" + address + '\'' +
                '}';
    }
}
