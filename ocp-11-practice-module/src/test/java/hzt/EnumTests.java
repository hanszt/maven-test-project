package hzt;

import hzt.model.NumberEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Month;
import java.util.EnumSet;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Enum constructor is always private. You cannot make it public or protected.
 * If an enum type has no constructor declarations,
 * then a private constructor that takes no parameters is automatically provided.
 * <p>
 * An enum is implicitly final, which means you cannot extend it.
 * <p>
 * You cannot extend an enum from another enum or class because an enum implicitly extends java.lang.Enum.
 * But an enum can implement interfaces.
 * <p>
 * Since enum maintains exactly one instance of its constants, you cannot clone it.
 * You cannot even override the clone method in an enum because java.lang.Enum makes it final.
 */
class EnumTests {

    @ParameterizedTest
    @ValueSource(strings = {"APRIL", "MAY", "JUNE"})
    void testEnumNameImplicitConversionToEnum(Month month) {
        assertSame(Month.APRIL, month.firstMonthOfQuarter());
    }

    @Test
    void testGettingEnumObjectFromValueOfMethod() {
        assertEquals(Month.FEBRUARY, Month.valueOf("FEBRUARY"));
    }

    @Test
    void testOrdinalIsIndexOfDeclaration() {
        assertEquals(0, Month.JANUARY.ordinal());
    }

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
    void testEnumIsComparable() {
        NavigableSet<Month> months = assertDoesNotThrow(() -> new TreeSet<>(EnumSet.allOf(Month.class)));

        assertAll(
                () -> assertEquals(12, months.size()),
                () -> assertSame(Month.JANUARY, months.first()),
                () -> assertSame(Month.DECEMBER, months.last())
        );
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
