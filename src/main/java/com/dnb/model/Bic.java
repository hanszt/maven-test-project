package com.dnb.model;

import java.util.Objects;

public class Bic {

    private final String id;
    private final String name;

    public Bic(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Bic(String name) {
        this("", name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Bic bic = (Bic) o;
        return Objects.equals(name, bic.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Bic{" +
                "name='" + name + '\'' +
                '}';
    }
}
