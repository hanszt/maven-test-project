package org.hzt;

import org.hzt.model.Person;
import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Museum;
import org.hzt.utils.It;
import org.hzt.utils.sequences.Sequence;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.Comparator.nullsFirst;
import static java.util.Comparator.nullsLast;
import static java.util.Comparator.reverseOrder;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class ComparatorTests {

    ComparatorTests() {
        System.out.printf("Constructing %s...%n", ComparatorTests.class.getSimpleName());
    }

    @Test
    void comparePersonByLasName() {
        final var firstNames = Sequence.of("Piet", "Klaas", "Else");
        final var lastNames = Sequence.of("Janssen", "Abdoulkariem", "Bayer");
        final var birthDays = Sequence.of("1989-10-18", "2000-03-23", "1964-06-01")
                .map(LocalDate::parse);

        final var persons = firstNames
                .zip(lastNames, Person::new)
                .zip(birthDays, Person::withDateOfBirth);

        final var expected = persons.sorted(comparing(Person::getFirstName));
        final var sorted1 = persons.sorted(Person::compareFirstName);
        final var sorted2 = persons.sortedBy(Person::getFirstName);

        sorted1.forEach(It::println);

        assertAll(
                () -> assertIterableEquals(expected, sorted1),
                () -> assertIterableEquals(expected, sorted2)
        );
    }

    @Test
    void testComparatorComparingListWithNullValues() {
        final var strings = Arrays
                .asList("This", "is", null, "a", null, "comparing", "data", "containing", null);

        final var stringList = strings.stream()
                .sorted(nullsFirst(String::compareToIgnoreCase))
                .toList();

        assertEquals(Arrays.asList(null, null, null, "a", "comparing", "containing", "data", "is", "This"), stringList);
    }

    @Test
    void testComparatorComparingByPossibleNullValues() {
        final var museumListContainingNulls = TestSampleGenerator.getMuseumListContainingNulls();

        final var museumNames = museumListContainingNulls.stream()
                .sorted(comparing(Museum::getName, nullsFirst(String::compareToIgnoreCase)))
                .map(Museum::getName)
                .toList();

        assertAll(
                () -> assertEquals(Arrays.asList(null, "Picasso Museum", "Van Gogh Museum", "Vermeer Museum"), museumNames),
                () -> assertEquals(museumListContainingNulls.size(), museumNames.size())
        );
    }

    @Test
    void testComposedComparator() {
        final List<Student> students = createStudents();

        final var sortedStudents = students.stream()
                .sorted(Student::compareByNameThenSurNameThenDateOfBirth)
                .map(Student::toString)
                .toList();

        assertAll(
                () -> validateSortedStudents(sortedStudents),
                () -> assertEquals(students.size(), sortedStudents.size())
        );
    }

    @NotNull
    private static List<Student> createStudents() {
        return List.of(
                new Student("Joep", "Leentjes", null),
                new Student("Joep", "Beentjes", LocalDate.parse("2000-10-12")),
                new Student("Lucy", "Leentjes", LocalDate.parse("2000-10-14")),
                new Student("Lucy", "Leentjes", LocalDate.parse("1960-01-23")),
                new Student(null, "Abdoel", LocalDate.parse("1970-01-23")));
    }

    private static void validateSortedStudents(List<String> sortedStudents) {
        assertEquals(
                "Student[name=null, surName=Abdoel, dateOfBirth=1970-01-23]" +
                        "Student[name=Joep, surName=Beentjes, dateOfBirth=2000-10-12]" +
                        "Student[name=Joep, surName=Leentjes, dateOfBirth=null]" +
                        "Student[name=Lucy, surName=Leentjes, dateOfBirth=2000-10-14]" +
                        "Student[name=Lucy, surName=Leentjes, dateOfBirth=1960-01-23]",
                String.join("", sortedStudents));
    }

    record Student(String name, String surName, LocalDate dateOfBirth) {

        private int compareByNameThenSurNameThenDateOfBirth(Student other) {
            return comparing(Student::name, nullsFirst(String::compareToIgnoreCase))
                    .thenComparing(Student::surName, String::compareToIgnoreCase)
                    .thenComparing(Student::dateOfBirth, nullsLast(reverseOrder()))
                    .compare(this, other);
        }
    }
}
