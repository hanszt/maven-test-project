package com.dnb.model;

import java.util.Objects;

public class Bic {

    private final String name;

    public Bic(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var bic = (Bic) o;
        return Objects.equals(name, bic.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Bic{" +
                "name='" + name + '\'' +
                '}';
    }
}
