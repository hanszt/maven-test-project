package org.hzt;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ComparableTests {

    private static class Person implements Comparable<Person> {

        private final String name;

        private Person(String name) {
            this.name = name;
        }

        @Override
        public int compareTo(@NotNull Person o) {
            return name.compareTo(o.name);
        }

        public int compareByName(Person o) {
            return compareTo(o);
        }
    }

    @Test
    void testSortedListComparable() {
        List<Person> personList = new ArrayList<>();
        personList.add(new Person("Piet"));
        personList.add(new Person("Klaas"));
        personList.add(new Person("Jan"));
        personList.sort(Person::compareByName);

        assertEquals("Jan", personList.get(0).name);
    }
}
