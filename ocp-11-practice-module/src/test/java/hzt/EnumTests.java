package hzt;

import org.junit.jupiter.api.Test;

import java.time.Month;
import java.util.EnumSet;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Enum constructor is always private. You cannot make it public or protected.
 * If an enum type has no constructor declarations,
 * then a private constructor that takes no parameters is automatically provided.
 *
 * An enum is implicitly final, which means you cannot extend it.
 *
 * You cannot extend an enum from another enum or class because an enum implicitly extends java.lang.Enum.
 * But an enum can implement interfaces.
 *
 * Since enum maintains exactly one instance of its constants, you cannot clone it.
 * You cannot even override the clone method in an enum because java.lang.Enum makes it final.
 */
class EnumTests {

    @Test
    void testEnumIsComparable() {
        Set<Month> months = assertDoesNotThrow(() -> new TreeSet<>(EnumSet.allOf(Month.class)));
        assertEquals(12, months.size());
    }

    @Test
    void testGettingEnumObjectFromValueOfMethod() {
        assertEquals(Month.FEBRUARY, Month.valueOf("FEBRUARY"));
    }

    @Test
    void testOrdinalIsIndexOfDeclaration() {
        assertEquals(0, Month.JANUARY.ordinal());
    }

}
