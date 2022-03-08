package hzt.model;

import java.util.Objects;

public class Student extends Person implements Comparable<Student> {

    private final double averageGrade;

    public Student(long id, String name, double averageGrade) {
        super(id, name);
        this.averageGrade = averageGrade;
    }

    public double getAverageGrade() {
        return averageGrade;
    }

    @Override
    public boolean equals(Object o) {
       return this == o || (o instanceof Student &&
               getId() == ((Student) o).getId() &&
               Objects.equals(getName(), ((Student) o).getName()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), averageGrade);
    }

    @Override
    public int compareTo(Student o) {
        return 0;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", averageGrade=" + averageGrade +
                '}';
    }
}
