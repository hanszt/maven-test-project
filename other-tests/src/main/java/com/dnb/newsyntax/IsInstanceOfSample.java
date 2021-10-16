package com.dnb.newsyntax;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static java.lang.System.*;

public class IsInstanceOfSample {

    private static final Logger LOGGER = LogManager.getLogger(IsInstanceOfSample.class);

    public static void main(String[] args) {
        try {
            testIsInstanceTest();
        } catch (ClassNotFoundException e) {
            LOGGER.error(e);
        }
    }

    private static void testIsInstanceTest() throws ClassNotFoundException {
        Animal animal = Math.random() < .5 ? new Dog() : new Cat();
        out.println(animal);
        if (isInstanceByClassname(animal, "com.dnb.newsyntax.Dog")) {
            out.println("it's a dog");
        } else {
            out.println("It's a cat");
        }
        if (animal instanceof Dog) {
            out.println("It's a dog");
        }
    }

    // This method tells us whether the object is an
    // instance of class whose name is passed as a
    // string 'c'.
    // use isInstance() method when needed dynamically
    // Source: https://www.geeksforgeeks.org/instanceof-operator-vs-isinstance-method-in-java/
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
