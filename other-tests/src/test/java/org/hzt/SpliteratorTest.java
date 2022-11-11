package org.hzt;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NavigableSet;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.Spliterator;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SpliteratorTest {

    SpliteratorTest() {
        System.out.println("Constructing SpliteratorTests...");
    }

    @Test
    @Tag("UnitTest")
    void testEstimateSize() {
        List<Integer> list = List.of(1, 2, 3, 4, 5);

        final var estimateSize = list.spliterator().estimateSize();

        assertEquals(5, estimateSize);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Test
    void testTryAdvance() {
        final var inputList = List.of(1, 2, 3, 4, 5, 6, 7, 8);

        final var thisSpliterator = inputList.spliterator();
        final var otherSpliterator = thisSpliterator.trySplit();

        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        while (thisSpliterator.tryAdvance(list1::add)) ;
        while (otherSpliterator.tryAdvance(list2::add)) ;

        list2.addAll(list1);

        assertEquals(list2, inputList);
    }

    @Nested
    @Tag("UnitTest")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class TrySplitTests {

        private static final int TIMES_TO_SPLIT = 3;

        TrySplitTests() {
            System.out.println("Constructing TrySplitTests...");
        }

        @Test
        void testTrySplitFromArrayList() {
            final var spliterator = IntStream.range(0, 1_000_000)
                    .filter(i -> i % 3 == 0)
                    .map(i -> i * 2)
                    .boxed()
                    .toList()
                    .spliterator();

            List<Spliterator<Integer>> spliterators = createSpliteratorList(spliterator);
            printSpliteratorEstimatedSizes(spliterators);

            assertEquals(timesSplitRaisedToPowerOfTwo(), spliterators.size());
        }

        @Test
        void testTrySplitFromHashSet() {
            final var spliterator = IntStream.range(0, 1_000_000)
                    .filter(i -> i % 3 == 0)
                    .boxed()
                    .collect(Collectors.toUnmodifiableSet())
                    .spliterator();

            List<Spliterator<Integer>> spliterators = createSpliteratorList(spliterator);
            printSpliteratorEstimatedSizes(spliterators);

            assertEquals(timesSplitRaisedToPowerOfTwo(), spliterators.size());
        }

        @Test
        void testTrySplitFromTreeSet() {
            final NavigableSet<Integer> treeSet = IntStream.range(0, 1_000_000)
                    .filter(i -> i % 3 == 0)
                    .boxed()
                    .collect(Collectors.toCollection(TreeSet::new));

            final var spliterator = treeSet.spliterator();

            List<Spliterator<Integer>> spliterators = createSpliteratorList(spliterator);
            printSpliteratorEstimatedSizes(spliterators);

            assertEquals(timesSplitRaisedToPowerOfTwo(), spliterators.size());
        }

        @Test
        void testTrySplitFromQueue() {
            final Queue<Integer> deque = IntStream.range(0, 1_000_000)
                    .filter(i -> i % 3 == 0)
                    .boxed()
                    .collect(Collectors.toCollection(ArrayDeque::new));

            final var spliterator = deque.spliterator();

            List<Spliterator<Integer>> spliterators = createSpliteratorList(spliterator);
            printSpliteratorEstimatedSizes(spliterators);

            assertEquals(timesSplitRaisedToPowerOfTwo(), spliterators.size());
        }

        @Test
        void testTrySplitFromIntArray() {
            final var ints = IntStream.range(0, 100_000).toArray();
            final var ofInt = IntStream.of(ints).spliterator();

            List<Spliterator<Integer>> spliterators = createSpliteratorList(ofInt);
            printSpliteratorEstimatedSizes(spliterators);

            assertEquals(timesSplitRaisedToPowerOfTwo(), spliterators.size());
        }

        @Test
        void testTrySplitFromIntRangeWithFilter() {
            final var spliterator = IntStream.range(0, 1_000_000)
                    .filter(i -> i % 3 == 0)
                    .spliterator();

            List<Spliterator<Integer>> spliterators = createSpliteratorList(spliterator);
            printSpliteratorEstimatedSizes(spliterators);

            assertEquals(1, spliterators.size());
        }

        @Test
        void testTrySplitFromIntRange() {
            final var spliterator = IntStream.range(0, 1_000_000).spliterator();

            List<Spliterator<Integer>> spliterators = createSpliteratorList(spliterator);
            printSpliteratorEstimatedSizes(spliterators);

            assertEquals(timesSplitRaisedToPowerOfTwo(), spliterators.size());
        }

        @Test
        void testTrySplitFromGeneratedStream() {
            final var spliterator = IntStream.iterate(0, i -> i + 2)
                    .filter(i -> i % 3 == 0)
                    .takeWhile(i -> String.valueOf(i).length() < 100)
                    .spliterator();

            List<Spliterator<Integer>> spliterators = createSpliteratorList(spliterator);
            printSpliteratorEstimatedSizes(spliterators);

            assertEquals(1, spliterators.size());
        }

        private static void printSpliteratorEstimatedSizes(List<Spliterator<Integer>> spliterators) {
            final var longs = spliterators.stream()
                    .mapToLong(Spliterator::estimateSize)
                    .toArray();

            System.out.println(Arrays.toString(longs));
        }

        @NotNull
        private static <T> List<Spliterator<T>> createSpliteratorList(Spliterator<T> initSpliterator) {
            final List<Spliterator<T>> spliterators = new ArrayList<>(List.of(initSpliterator));
            for (int i = 0; i < TIMES_TO_SPLIT; i++) {
                final int spliteratorsSize = spliterators.size();
                for (int j = 0; j < spliteratorsSize; j++) {
                    Optional.ofNullable(spliterators.get(j).trySplit())
                            .ifPresent(spliterators::add);
                }
            }
            return spliterators;
        }

        private static int timesSplitRaisedToPowerOfTwo() {
            final var pow = (int) Math.pow(2, TIMES_TO_SPLIT);
            System.out.println("nrOfSpliterators = " + pow);
            return pow;
        }

    }

    @Nested
    class CharacteristicsTests {
        @Test
        void testCharacteristicsSpliteratorFromList() {
            final var spliterator = List.of(1, 2, 3, 4).spliterator();

            final var expected = Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED;

            System.out.println("expected = " + expected);

            assertAll(
                    () -> assertEquals(expected, spliterator.characteristics()),
                    () -> assertTrue(spliterator.hasCharacteristics(Spliterator.ORDERED)),
                    () -> assertTrue(spliterator.hasCharacteristics(Spliterator.SIZED)),
                    () -> assertTrue(spliterator.hasCharacteristics(Spliterator.SUBSIZED)),
                    () -> assertFalse(spliterator.hasCharacteristics(Spliterator.NONNULL)),
                    () -> assertFalse(spliterator.hasCharacteristics(Spliterator.IMMUTABLE)),
                    () -> assertFalse(spliterator.hasCharacteristics(Spliterator.DISTINCT)),
                    () -> assertFalse(spliterator.hasCharacteristics(Spliterator.SORTED)),
                    () -> assertFalse(spliterator.hasCharacteristics(Spliterator.CONCURRENT))
            );
        }

        @Test
        void testCharacteristicsSpliteratorFromSet() {
            final var spliterator = Set.of(1, 2, 3, 4).spliterator();

            final var expected = Spliterator.DISTINCT | Spliterator.SIZED | Spliterator.SUBSIZED;

            System.out.println("expected = " + expected);

            assertAll(
                    () -> assertEquals(expected, spliterator.characteristics()),
                    () -> assertTrue(spliterator.hasCharacteristics(Spliterator.DISTINCT)),
                    () -> assertTrue(spliterator.hasCharacteristics(Spliterator.SIZED)),
                    () -> assertTrue(spliterator.hasCharacteristics(Spliterator.SUBSIZED)),
                    () -> assertFalse(spliterator.hasCharacteristics(Spliterator.ORDERED)),
                    () -> assertFalse(spliterator.hasCharacteristics(Spliterator.NONNULL)),
                    () -> assertFalse(spliterator.hasCharacteristics(Spliterator.IMMUTABLE)),
                    () -> assertFalse(spliterator.hasCharacteristics(Spliterator.SORTED)),
                    () -> assertFalse(spliterator.hasCharacteristics(Spliterator.CONCURRENT))
            );
        }

        @Test
        void testCharacteristicsSpliteratorFromTreeSet() {
            final var spliterator = new TreeSet<>(Set.of(1, 2, 3, 4)).spliterator();

            final var expected = Spliterator.DISTINCT | Spliterator.SIZED | Spliterator.SORTED | Spliterator.ORDERED;

            System.out.println("expected = " + expected);

            assertAll(
                    () -> assertEquals(expected, spliterator.characteristics()),
                    () -> assertTrue(spliterator.hasCharacteristics(Spliterator.DISTINCT)),
                    () -> assertTrue(spliterator.hasCharacteristics(Spliterator.SIZED)),
                    () -> assertTrue(spliterator.hasCharacteristics(Spliterator.SORTED)),
                    () -> assertTrue(spliterator.hasCharacteristics(Spliterator.ORDERED)),
                    () -> assertFalse(spliterator.hasCharacteristics(Spliterator.SUBSIZED)),
                    () -> assertFalse(spliterator.hasCharacteristics(Spliterator.NONNULL)),
                    () -> assertFalse(spliterator.hasCharacteristics(Spliterator.IMMUTABLE)),
                    () -> assertFalse(spliterator.hasCharacteristics(Spliterator.CONCURRENT))
            );
        }

        @Test
        void testCharacteristicsSpliteratorFromConcurrentSkipListSet() {
            final var spliterator = new ConcurrentSkipListSet<>(Set.of(1, 2, 3, 4)).spliterator();

            final var expected = Spliterator.DISTINCT | Spliterator.ORDERED | Spliterator.NONNULL | Spliterator.SORTED | Spliterator.CONCURRENT;

            System.out.println("expected = " + expected);

            assertAll(
                    () -> assertEquals(expected, spliterator.characteristics()),
                    () -> assertTrue(spliterator.hasCharacteristics(Spliterator.DISTINCT)),
                    () -> assertTrue(spliterator.hasCharacteristics(Spliterator.ORDERED)),
                    () -> assertTrue(spliterator.hasCharacteristics(Spliterator.SORTED)),
                    () -> assertTrue(spliterator.hasCharacteristics(Spliterator.NONNULL)),
                    () -> assertTrue(spliterator.hasCharacteristics(Spliterator.CONCURRENT)),
                    () -> assertFalse(spliterator.hasCharacteristics(Spliterator.SIZED)),
                    () -> assertFalse(spliterator.hasCharacteristics(Spliterator.SUBSIZED)),
                    () -> assertFalse(spliterator.hasCharacteristics(Spliterator.IMMUTABLE))
            );
        }
    }
}
