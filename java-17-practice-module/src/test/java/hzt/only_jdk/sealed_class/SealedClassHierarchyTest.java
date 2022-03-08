package hzt.only_jdk.sealed_class;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SealedClassHierarchyTest {

    @Test
    void testSealedClassHierarchy() {
        final var banana = new Banana("Afrika");
        final var apple = new Apple("Nederland");
        final var fruit = new Fruit("Nederland");
        final var citrusFruit = new CitrusFruit("Amerika");
        final var lemon = new Lemon("Italy");

        final var fruits = List.of(banana, apple, fruit, citrusFruit, lemon);

        fruits.forEach(System.out::println);

        final var noneMatchOrange = fruits.stream()
                .noneMatch(Orange.class::isInstance);

        assertTrue(noneMatchOrange);
    }

}
