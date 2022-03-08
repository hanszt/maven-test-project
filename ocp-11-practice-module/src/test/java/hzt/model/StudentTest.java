package hzt.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    @Test
    void testEqualityIsNotSymmetricWhenUsingInstanceOfInEquals() {
        final var person = new Person(1, "Hans");
        final var student = new Student(1, "Hans", 7.5);

        assertEquals(person, student);
        assertNotEquals(student, person);
    }

}
