package hzt.enthuware_tests;

import hzt.model.NumberEnum;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class EnthuwareTest1Tests {

    // Source: Enthuware Java11OCP Test 1 Q 22
    @Test
    void testBreakWithLabel() {
        int i = 0, j = 5;
        lab1:
        // 'i' is incremented at the end of the loop
        for (; ; ++i) {
            for (; ; --j) {
                if (i > j) {
                    break lab1;
                }
            }
        }
        System.out.println(" i = " + i + ", j = " + j);
        assertEquals(0, i);
        assertEquals(-1, j);
    }

    @Test
    void testMapMerge() {
        Map<String, Integer> map1 = new HashMap<>();
        map1.put("a", 1);
        map1.put("b", 1);
        final var b = map1.merge("b", 1, Integer::sum);
        final var c = map1.merge("c", 3, Integer::sum);
        System.out.println(map1);

        assertEquals(2, b);
        assertEquals(3, c);
    }

    static int wiggler(int x) {
        int y = x + 10;
        x++;
        System.out.println(x);
        assertEquals(6, x);
        return y;
    }

    @Test
    void testWrapperObjectsImmutable() {
        int dataWrapper = 5;
        int value = wiggler(dataWrapper);
        final var result = dataWrapper + value;

        System.out.println(result);

        assertEquals(20, result);
    }

    @Test
    void testWildCardMapConstructorCallWorksButPutAllNot() {
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put("Hallo", "Sophie");

        Map<?, ?> wildCardMap = new HashMap<>(stringMap);
//        wildCardMap.putAll(stringMap);

        assertTrue(wildCardMap.containsKey("Hallo"));
        assertTrue(wildCardMap.containsValue("Sophie"));
    }

    @Test
    void testWildCardSuperCanContainMore() {
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put("Hallo", "Sophie");

        Map<? super Comparable<?>, ? super Comparable<?>> wildCardMap = new HashMap<>(stringMap);
        wildCardMap.putAll(stringMap);
        wildCardMap.put(1, 5);
        wildCardMap.put(LocalDate.of(2002, Month.DECEMBER, 2), LocalDate.of(4, Month.JANUARY, 3));
        wildCardMap.put(NumberEnum.FOUR, NumberEnum.THREE);

        printMap(wildCardMap);

        assertEquals(4, wildCardMap.size());
    }

    private void printMap(Map<?, ?> map) {
        map.entrySet().forEach(System.out::println);
    }

    // Source: Enthuware Java11OCP Test 1 Q 26
    @Test
    void testIncrementAndGetInRangeOneToEightParallelStream() {
        IntStream.range(0, 1000).forEach(EnthuwareTest1Tests::testQ26AtomicIntegerNotDeterministic);
    }

    private static void testQ26AtomicIntegerNotDeterministic(int i) {
        AtomicInteger atomicInteger = new AtomicInteger();
        Stream<String> stream = Stream.of("old", "king", "cole", "was", "a", "merry", "old", "soul").parallel();

        //because allMatch is short-circuiting and the stream is parallel, the count is unpredictable
        final var allMatch = stream.filter(e -> {
            atomicInteger.incrementAndGet();
            return e.contains("o");
        }).allMatch(x -> x.indexOf("o") > 0);

        System.out.println("AI = " + atomicInteger);
        assertFalse(allMatch);
        final var value = atomicInteger.get();
        assertTrue(0 < value && value <= 8);
    }

    //  1. In the given code, stream refers to a parallel stream. This implies that the JVM is free to break up the
    //  original stream into multiple smaller streams, perform the operations on these pieces in parallel, and finally
    //  combine the results.
    //  2. Here, the stream consists of 8 elements. It is, therefore, possible for a JVM running on an eight core machine
    //  to split this stream into 8 streams (with one element each) and invoke the filter operation on each of them.
    //  If this happens, atomicInteger will be incremented 8 times.
    //  3. It is also possible that the JVM decides not to split the stream at all. In this case,
    //  it will invoke the filter predicate on the first element (which will return true)
    //  and then invoke the allMatch predicate (which will return false because "old".indexOf("o") is 0).
    //  Since allMatch is a short-circuiting terminal operation,
    //  it knows that there is no point in checking other elements because the result will be false anyway.
    //  Hence, in this scenario, atomicInteger will be incremented only once.
    //  4. The number of pieces that the original stream will be split into could be anything between 1 and 8
    //  and by applying the same logic as above, we can say that ai will be incremented any number of times between 1 and 8.

    @Test
    void testIncrementAndGetAlwaysEightParallelStream() {
        IntStream.range(0, 1000).forEach(EnthuwareTest1Tests::testQ26ModdedAtomicIntegerDeterministic);
    }

    static void testQ26ModdedAtomicIntegerDeterministic(int i) {
        AtomicInteger atomicInteger = new AtomicInteger();
        Stream<String> stream = Stream.of("old", "king", "cole", "was", "a", "merry", "old", "soul").parallel();

        final var count = stream.filter(e -> {
            atomicInteger.incrementAndGet();
            return e.contains("o");
        }).count();

        System.out.println("atomicInteger = " + atomicInteger);
        assertEquals(8, atomicInteger.get());
        assertEquals(4, count);
    }

    @Test
    void testListOfAndSetOfDoNotSupportNullElements() {
        //noinspection ConstantConditions,ResultOfMethodCallIgnored
        assertAll(
                () -> assertThrows(NullPointerException.class, () -> List.of(1, 2, null)),
                () -> assertThrows(NullPointerException.class, () -> Set.of(1, 2, 3, null))
        );
    }
}
