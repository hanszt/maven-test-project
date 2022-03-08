package hzt.model;

import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EnumTest {

    @Test
    void testAbstractMethod() {
        assertEquals(8, NumberEnum.FOUR.add(4));
    }

    @Test
    void testOverriddenMethodInEnum() {
        assertEquals("THREE An enum test", NumberEnum.THREE.description());
        assertEquals("FOUR", NumberEnum.FOUR.description());
    }

    @Test
    void testEnumImplementsComparable() {
            var treeSet = new TreeSet<>();
            treeSet.add(NumberEnum.FOUR);
            treeSet.add(NumberEnum.THREE);
            treeSet.add(NumberEnum.ONE);
            //Every enum implements Comparable and the natural order of enums is the order in which they are defined.
            assertEquals(Set.of(NumberEnum.ONE, NumberEnum.THREE, NumberEnum.FOUR), treeSet);
    }
}
