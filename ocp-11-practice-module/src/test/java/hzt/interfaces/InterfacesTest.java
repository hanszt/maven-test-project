package hzt.interfaces;

import hzt.service_provider_modules.Adder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class InterfacesTest {

    @Test
    void testMultiAbstractMethodFunctionalInterface() {
        final var nrToAdd = 4;
        final int nr = 6;
        var adder = new FunctionalInterfaceContainingMoreThanOneAbstractMethod() {
            @Override
            public int apply(int i) {
                return i + nr;
            }
        };
        adder.printHello();

        final var result = apply(nrToAdd, i -> i + nr);
        System.out.println("result = " + result);
        assertEquals(adder.apply(nrToAdd), result);
    }

    @Test
    void testConstantOnlyReachableViaDirectInterface() {
        FunctionalInterfaceContainingMoreThanOneAbstractMethod.printFromStaticMethod();
        // Only accessible via Interface
//        Adder.printFromStaticMethod();
        assertEquals(4, Adder.CONSTANT_VAL);
    }

    @Test
    void testAccessibleViaChildClass() {
        final var add = AdderExtension.add(3, 5);
        assertEquals(8, add);
    }

    @SuppressWarnings("SameParameterValue")
    private int apply(int nr, FunctionalInterfaceContainingMoreThanOneAbstractMethod f) {
        return f.apply(nr);
    }

    @Test
    void testCasting() {
        // Source: Enthuware Java11OCP Test 1 Q 16
        //        Animal = I,
        //        Mammal = A,
        //        DogLike = B,
        //        Dog = C
        Mammal mammal = new Mammal();
        DogLike dogLike = new Dog();

        mammal = (DogLike) (Animal) dogLike;
        System.out.println(mammal);
        assertEquals(mammal, dogLike);
    }

    @Test
    void testInterfaceAndImplComparisonUsingIsOperator() {
        DogLike dogLikeFox = new Fox();
        Fox fox = new Fox();
        Dog dog = new Dog();
        if (dogLikeFox == fox) {
            fail("doglike should not equal fox");
        }
        assertTrue(dog.isAlive());
//        if (dog == fox) not allowed
    }

    @Test
    void testInterfaceOverriddenDefaultMethod() {
        assertTrue(new Dog().isAlive());
        final var entity = new Entity() {
        };
        assertFalse(entity.isAlive());
    }

}

interface Entity {

    private static void doSomeThing() {
    }

    private void doAnotherThing() {

    }


//    static boolean isAlive() {
//        return true;
//    }

    default boolean isAlive() {
        return false;
    }
}

//q 7 test 6
//You cannot override a non-static method with a static method and vice versa.
// (A default method is a non-static method.) You can, however, redeclare a static method of a super interface as a default
@FunctionalInterface
interface Animal extends Entity {


    @Override
    default boolean isAlive() {
        return true;
    }

    default void makeSound() {
        System.out.println("Making sound");
    }

    void eat(String foodName);
}

class Mammal implements Animal {

    // you can not define static and non static methods with same signature
//    public static void eat(String food) {
//
//    }

    @Override
    public void eat(String foodName) {
    }
}

class DogLike extends Mammal {
}

class Dog extends DogLike {
}

class Fox extends DogLike {
}
