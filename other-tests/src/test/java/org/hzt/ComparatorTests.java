package org.hzt;

import org.hzt.model.Person;
import org.hzt.utils.It;
import org.hzt.utils.sequences.Sequence;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static java.util.Comparator.comparing;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class ComparatorTests {

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
}
