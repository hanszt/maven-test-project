package com.dnb.model;

import com.dnb.common.HztStringUtils;
import com.dnb.custom_annotations.Initializable;
import com.dnb.custom_annotations.JsonElement;
import com.dnb.custom_annotations.JsonSerializable;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.Period;

@JsonSerializable
public class Person {

    @JsonElement
    private String firstName;

    @JsonElement
    private String lastName;

    @JsonElement(key = "birthDate")
    private LocalDate dateOfBirth;

    private String address;

    private boolean playingPiano;

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

    @Initializable
    private void initNames() {
        this.firstName = HztStringUtils.toOnlyFirstLetterUpperCase(firstName);
        this.lastName = HztStringUtils.toOnlyFirstLetterUpperCase(lastName);
    }

    public MonthDay getMonthDayFromDateOfBirth() {
        return MonthDay.from(getDateOfBirth());
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public int getAge() {
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isPlayingPiano() {
        return playingPiano;
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
