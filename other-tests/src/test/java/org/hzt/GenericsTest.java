package org.hzt;

import org.hzt.model.Employee;
import org.hzt.model.Person;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GenericsTest {

    @Test
    void testBoundedWildCard() {
        List<? super Person> personList = new ArrayList<>();
        personList.add(new Employee(""));
        final var hans = new Person("Hans");
        personList.add(hans);
        List<String> strings = takeListAsWildCardAndGoToStringList(personList);
        System.out.println(strings);
        assertTrue(personList.contains(hans));
    }

    private static List<String> takeListAsWildCardAndGoToStringList(List<? super Person> persons) {
        return persons.stream()
                .map(Object::toString)
                .collect(Collectors.toUnmodifiableList());
    }

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    void testRawList() {
        List list = new ArrayList();
        list.add(new Person("Hans"));
        list.add("Raar");
        list.add(1);
        list.forEach(System.out::println);

        final Object collect = list.stream().map(Object::getClass)
                .filter(e -> e.equals(String.class))
                .collect(Collectors.toUnmodifiableList());

        for (Object object : list) {
            if (object instanceof Person) {
                System.out.println("person.getAddress() = " + ((Person) object).getAddress());
            }
        }
        System.out.println(collect);
        assertNotNull(collect);
    }

    @Test
    void testGenericMethod() {
        List<String> strings = Arrays.asList("Hans", "Hoe", "is", null);
        List<String> list = genericMethod(strings);
        assertEquals(3, list.size());
    }

    public <T> List<T> genericMethod(List<T> list) {
        return list.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableList());
    }
}
