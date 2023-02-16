package hzt.enthuware_tests;

import java.io.Console;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;

public class FoundationTest2 {

    public static void main(String... args) {
        String s1 = "Hallo";
        String s = s1.intern();
        System.out.println(s);
        System.out.println("Creating employee");
        new Employee();
    }

    static void putInHashTable() {
        Console console = null;
        Map<String, Object> map = new Hashtable<>();
        map.put("key1", console);

        map.entrySet().forEach(System.out::println);
        System.out.println(staticPerson);
    }

    private static Person staticPerson = new Person("Statically called person") {
    };

    native float getVariance();

    private static class Person implements Serializable {

        private static final long serialVersionUID = 4;
        private final String name;


        private Person(String name) {
            this.name = name;
            System.out.println("Constructor of person " + name + " called");
        }
        private Person() {
            this("No name specified");
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    private static class Employee extends Person {

    }

    private interface Predicate<T> {

        public abstract boolean test(T t);
    }
}
