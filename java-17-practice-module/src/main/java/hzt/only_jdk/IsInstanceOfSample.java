package hzt.only_jdk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IsInstanceOfSample {

    private static final Logger LOGGER = LoggerFactory.getLogger(IsInstanceOfSample.class);

    public static void main(String[] args) {
        try {
            testIsInstanceTest();
        } catch (ClassNotFoundException e) {
            LOGGER.error("", e);
        }
    }

    private static void testIsInstanceTest() throws ClassNotFoundException {
        Animal animal = Math.random() < .5 ? new Dog() : new Cat();
        LOGGER.info("{}", animal);
        if (isInstanceByClassname(animal, Dog.class.getName())) {
            LOGGER.info("it's a dog");
        } else {
            LOGGER.info("It's a cat");
        }
        if (animal instanceof Dog) {
            LOGGER.info("It's a dog");
        }
    }

    // This method tells us whether the object is an
    // instance of class whose name is passed as a
    // string 'c'.
    // use isInstance() method when needed dynamically
    // Source: https://www.geeksforgeeks.org/instanceof-operator-vs-isinstance-method-in-java/
    @SuppressWarnings("all")
    public static boolean isInstanceByClassname(Object obj, String classname) throws ClassNotFoundException {
        return Class.forName(classname).isInstance(obj);
    }

    static non-sealed class Cat extends Animal {

        public Cat() {
            super(4);
        }

        @Override
        public String toString() {
            return "Cat{}";
        }
    }

    static non-sealed class Dog extends Animal {

        public Dog() {
            super(4);
        }

        @Override
        public String toString() {
            return "Dog{}";
        }
    }

    static sealed class Animal {

        private final int nrOfLegs;

        Animal(int nrOfLegs) {
            this.nrOfLegs = nrOfLegs;
        }

        public int getNrOfLegs() {
            return nrOfLegs;
        }
    }
}
