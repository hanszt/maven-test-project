package org.hzt;

import org.hzt.model.Employee;
import org.hzt.model.Person;
import org.hzt.utils.It;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.hzt.utils.It.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GenericsTest {

    @Test
    void testBoundedWildCard() {
        final List<? super Person> personList = new ArrayList<>();
        personList.add(new Employee(""));
        final var hans = new Person("Hans");
        personList.add(hans);
        final List<String> strings = takeListAsWildCardAndGoToStringList(personList);
        println(strings);

        assertTrue(personList.contains(hans));
    }

    private static List<String> takeListAsWildCardAndGoToStringList(final List<? super Person> persons) {
        return persons.stream()
                .map(Object::toString)
                .toList();
    }

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    void testRawList() {
        final List list = new ArrayList();
        list.add(new Person("Hans"));
        list.add("Raar");
        list.add(1);
        list.forEach(It::println);

        final var collect = list.stream()
                .map(Object::getClass)
                .filter(String.class::equals)
                .toList();

        //noinspection RedundantOperationOnEmptyContainer
        for (final Object object : list) {
            if (object instanceof Person) {
                println("person.getAddress() = " + ((Person) object).getAddress());
            }
        }
        println(collect);
        assertNotNull(collect);
    }

    @Test
    void testGenericMethod() {
        final List<String> strings = Arrays.asList("Hans", "Hoe", "is", null);
        final List<String> list = filterNonNull(strings);
        assertEquals(3, list.size());
    }

    public <T> List<T> filterNonNull(final List<T> list) {
        return list.stream()
                .filter(Objects::nonNull)
                .toList();
    }
}
