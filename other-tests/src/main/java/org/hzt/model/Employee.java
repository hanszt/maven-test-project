package org.hzt.model;

import java.time.LocalDate;

public class Employee extends Person {

    public Employee(String firstName, String lastName, LocalDate age) {
        super(firstName, lastName, age);
    }

    public Employee(String name) {
        super(name);
    }
}
