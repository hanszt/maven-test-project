package org.hzt.vavr_tests;

import io.vavr.collection.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @see <a href="https://docs.vavr.io/">Vavr User Guide</a>
 */
class VavrCollectionsTests {

    @Test
    void testListMax() {
        final var max = List.of(1, 2, 3, 4, 5).max();

        assertEquals(5, max.getOrElse(Integer.MAX_VALUE));
    }

    @Test
    void testListCombinations() {
        final var combinations = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).combinations();

        combinations.forEach(System.out::println);

        assertEquals(1024, combinations.size());
    }

    @Test
    void testListPermutations() {
        final var permutations = List.of(1, 2, 3, 4, 5, 6).permutations();

        permutations.forEach(System.out::println);

        //factorial of the size of the input list
        assertEquals(720, permutations.size());
    }

    @Test
    void testListIntersperse() {
        final var interspersed = List.of(1, 2, 3, 4, 5, 6).intersperse(3);

        interspersed.forEach(System.out::println);

        //factorial of the size of the input list
        assertEquals(11, interspersed.size());
    }

    @Test
    void testListRotateLeft() {
        final var rotatedLeft = List.of(1, 2, 3, 4, 5, 6, 7).rotateLeft(3);

        assertEquals(List.of(4, 5, 6, 7, 1, 2, 3), rotatedLeft);
    }
}
