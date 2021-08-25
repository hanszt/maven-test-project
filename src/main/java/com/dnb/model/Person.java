package com.dnb.model;

import com.dnb.common.HztStringUtils;
import com.dnb.custom_annotations.Initializable;
import com.dnb.custom_annotations.JsonElement;
import com.dnb.custom_annotations.JsonSerializable;

@JsonSerializable
public class Person {

    @JsonElement
    private String firstName;

    @JsonElement
    private String lastName;

    @JsonElement(key = "personAge")
    private String age;

    private String address;

    public Person(String firstName, String lastName, String age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public Person(String firstName) {
        this(firstName, "Undefinied", "Undefinied");
    }

    @Initializable
    private void initNames() {
        this.firstName = HztStringUtils.toOnlyFirstLetterUpperCase(firstName);
        this.lastName = HztStringUtils.toOnlyFirstLetterUpperCase(lastName);
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}