package org.hzt.generators;

import org.hzt.utils.It;
import org.hzt.utils.iterables.Collectable;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.tuples.IndexedValue;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static org.hzt.generators.TowerOfHanoiGenerator.moveDisk;
import static org.junit.jupiter.api.Assertions.*;

/**
 * These tests test a Generator that behaves similarly to The generator implementation of a Kotlin sequence using coroutines
 * <p>
 * This java version uses a virtual thread as a producerThread
 */
class GeneratorTest {

    @Test
    <T> void testEmptyGenerator() {
        final var generatorBuilder = Generator.<T>yieldingFrom(scope -> {
        });
        try (final var emptyGenerator = generatorBuilder.generator()) {
            assertFalse(emptyGenerator.iterator().hasNext());
        }
    }

    @Test
    void testSingleElementGenerator() {
        var list = Generator.<Integer>yieldingFrom(scope -> scope.yieldNext(1))
                .useAsSequence(Sequence::toList);
        assertEquals(List.of(1), list);
    }

    private static <T> void yieldValues(GeneratorScope<T> scope, Iterable<T> elements) {
        for (T element : elements) {
            scope.yieldNext(element);
        }
    }

    private static <T> void nextInInfiniteLoop(GeneratorScope<T> scope, T initial, UnaryOperator<T> operator) {
        T acc = initial;
        //noinspection InfiniteLoopStatement
        while (true) {
            scope.yieldNext(acc);
            acc = operator.apply(acc);
        }
    }

    @Test
    void testTwoEltGenerator() {
        List<Integer> twoEltList = List.of(1, 2);
        List<Integer> result = Generator
                .<Integer>yieldingFrom(scope -> yieldValues(scope, twoEltList))
                .useAsStream(Stream::toList);
        assertEquals(twoEltList, result);
    }

    @Test
    void testInfiniteGenerator() {

        long NUM_ELEMENTS_TO_INSPECT = 1_000L;
        final var pair = Generator.<Integer>yieldingFrom(scope -> nextInInfiniteLoop(scope, 0, i -> i + 1))
                .useAsSequence(s -> s
                .filter(i -> i % 2 == 0)
                .take(NUM_ELEMENTS_TO_INSPECT)
                .withIndex()
                .onEach(e -> assertEquals(e.index() * 2, e.value()))
                .map(IndexedValue::value)
                .toTwo(Sequence::last, Sequence::toList));

        System.out.println("Thread.activeCount() = " + Thread.activeCount());

        assertAll(
                () -> assertEquals(NUM_ELEMENTS_TO_INSPECT, pair.second().size()),
                () -> assertEquals(1_998, pair.first())
        );
    }

    @Test
    void testInfiniteGeneratorIsLazy() {
        AtomicInteger integer = new AtomicInteger(0);
        final var build = Generator
                .<Integer>yieldingFrom(scope -> nextInInfiniteLoop(scope, 0, i -> i + 1))
                .useAsSequence(s -> s.onEach(e -> integer.incrementAndGet())
                        .onEach(It::println));

        assertAll(
                () -> assertTrue(isLambdaImpl(build.getClass())),
                () -> assertEquals(0, integer.get())
        );
    }

    private static boolean isLambdaImpl(Class<?> aClass) throws NoSuchMethodException {
        return aClass.isSynthetic() && !aClass.getDeclaredMethod("iterator").isSynthetic();
    }

    @Test
    void testInfiniteGeneratorCanNotBeCalledOutsideUseAsSequenceMethod() {
        AtomicInteger integer = new AtomicInteger(0);

        final var sequence = Generator
                .<Integer>yieldingFrom(scope -> nextInInfiniteLoop(scope, 0, i -> i + 1))
                .useAsSequence(seq -> seq.onEach(i -> integer.incrementAndGet())
                        .onEach(It::println));

        assertThrows(IllegalStateException.class, sequence::toList);
    }

    @Test
    void testInfiniteGeneratorLeavesNoRunningThreads() {
        final var generator = Generator.<Integer>yieldingFrom(scope -> nextInInfiniteLoop(scope, 1, i -> i)).generator();
        Iterator<Integer> iterator = generator.iterator();
        try (generator) {
            int NUM_ELEMENTS_TO_INSPECT = 1000;
            for (int i = 0; i < NUM_ELEMENTS_TO_INSPECT; i++) {
                assertTrue(iterator.hasNext());
                final var next = iterator.next();
                assertEquals(1, next);
            }
        }
        System.out.println("Thread.activeCount() = " + Thread.activeCount());
        assertEquals(Thread.State.TERMINATED, iterator instanceof GeneratorIterator<Integer> gi ? gi.getProducerState() : null);
    }

    private static class CustomRuntimeException extends RuntimeException {

        private static void throwIt() {
            throw new CustomRuntimeException();
        }
    }

    @Test
    <T> void testGeneratorRaisingExceptionHasNext() {
        try (Generator<T> generator = Generator.<T>yieldingFrom(scope -> CustomRuntimeException.throwIt()).generator()) {
            Iterator<T> iterator = generator.iterator();
            assertThrows(CustomRuntimeException.class, iterator::hasNext);
        }

    }

    @Test
    <T> void testGeneratorRaisingExceptionNext() {
        try (Generator<T> generator = Generator.<T>yieldingFrom(scope -> CustomRuntimeException.throwIt()).generator()) {
            Iterator<T> iterator = generator.iterator();
            assertThrows(CustomRuntimeException.class, iterator::next);
        }
    }

    @Nested
    class HanoiTest {

        @Test
        void testYieldSequenceFromTowerOfHanoiRecursiveFunction() {
            final var nrOfDisks = 3;
            final var expectedNrOfMoves = (int) (Math.pow(2, nrOfDisks) - 1);

            final var expected = List.of(
                    "Move disk  1 from rod a to rod c",
                    "Move disk  2 from rod a to rod b",
                    "Move disk  1 from rod c to rod b",
                    "Move disk  3 from rod a to rod c",
                    "Move disk  1 from rod b to rod a",
                    "Move disk  2 from rod b to rod c",
                    "Move disk  1 from rod a to rod c");

            List<String> instructions = Generator
                    .<String>yieldingFrom(scope -> moveDisk(scope, nrOfDisks, 'a', 'c', 'b'))
                    .useAsSequence(sequence -> sequence
                            .onEach(It::println)
                            .toList());

            assertAll(
                    () -> assertEquals(expectedNrOfMoves, instructions.size()),
                    () -> assertEquals(expected, instructions)
            );
        }

        @ParameterizedTest(name = "When starting with {0} disks, the nr of moves to complete the game should be: {1}")
        @CsvSource(value = {
                "15 -> 32767",
                "10 -> 1023",
                "1 -> 1",
                "2 -> 3",
                "3 -> 7"},
                delimiterString = " -> "
        )
        void testTowerOfHanoiNrOfMoves(int nrOfDisks, int expectedNrOfMoves) {
            List<String> instructions = Generator
                    .<String>yieldingFrom(scope -> moveDisk(scope, nrOfDisks, 'a', 'c', 'b'))
                    .useAsSequence(Collectable::toList);

            assertAll(
                    () -> assertEquals(expectedNrOfMoves, instructions.size()),
                    () -> assertEquals(expectedNrOfMoves, (Math.pow(2.0, nrOfDisks) - 1))
            );
        }
    }

    @Nested
    class FibonacciGeneratorTest {

        private final Generator.Builder<Long> fibonacciBuilder = Generator.yieldingFrom(scope -> {
            var cur = 0L;
            var next = 1L;
            //noinspection InfiniteLoopStatement
            while (true) {
                scope.yieldNext(next); // next Fibonacci number
                final var newNext = cur + next;
                cur = next;
                next = newNext;
            }
        });

        @Test
        void testFibonacciSequence() {
            final var fibNrs = fibonacciBuilder
                    .useAsStream(stream ->
                            stream.limit(10)
                                    .toList());

            fibonacciBuilder.consumeAsSequence(seq ->
                    seq.take(80)
                            .forEach(It::println));

            assertEquals(List.of(1L, 1L, 2L, 3L, 5L, 8L, 13L, 21L, 34L, 55L), fibNrs);
        }
    }
}