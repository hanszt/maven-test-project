package hzt.model;

import java.util.Objects;

public class Person {

    private final long id;
    private final String name;

    public Person(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || (o instanceof Person &&
                id == ((Person) o).id &&
                Objects.equals(name, ((Person) o).name));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
