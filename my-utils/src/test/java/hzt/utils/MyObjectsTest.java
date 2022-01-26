package hzt.utils;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MyObjectsTest {

    @Test
    void testRequireAllNonNull() {
        final int integer = 4;
        final double aDouble = 6.0;

        final BigInteger bigInteger = BigInteger.valueOf(4);
        Throwable throwable = assertThrows(NullPointerException.class, () -> MyObjects
                .requireAllNonNull(Number.class, integer, bigInteger, aDouble, null));

        final Set<?> treeSet = new TreeSet<>();
        final List<?> list = new ArrayList<>();
        final Set<?> hashSet = new HashSet<>();
        final Deque<?> arrayDeque = new ArrayDeque<>();
        Throwable throwable2 = assertThrows(NullPointerException.class, () -> MyObjects
                .requireAllNonNull(Iterable.class, list, hashSet, null, arrayDeque, treeSet, null));

        assertEquals("Number 4 is null", throwable.getMessage());
        assertEquals("Iterable 3 is null", throwable2.getMessage());
    }

    @Test
    void testObjectsRequireAllNonNull() {
        final int integer = 4;
        final double aDouble = 6.0;

        final BigInteger bigInteger = BigInteger.valueOf(4);
        Throwable throwable = assertThrows(IllegalArgumentException.class, () -> MyObjects.requireAllNonNull(
                integer, bigInteger, aDouble, null, "", null));

        throwable.printStackTrace();

        assertAll(
                () -> assertEquals("Some objects where null", throwable.getMessage()),
                () -> assertEquals(2, throwable.getSuppressed().length)
        );
    }

}
