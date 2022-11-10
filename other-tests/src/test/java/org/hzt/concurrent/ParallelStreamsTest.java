package org.hzt.concurrent;

import org.hzt.TimingUtils;
import org.hzt.utils.tuples.Pair;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

import static org.hzt.utils.It.printf;
import static org.hzt.utils.It.println;
import static org.junit.jupiter.api.Assertions.*;

class ParallelStreamsTest {

    /**
     * The common pool is by default used by parallel stream.
     * <p>
     * <p>
     * Main is also part of the pool, it is an external members
     * <p><second>Calculating optimal number of threads:</second></p>
     * #Threads = #Cores / (1 - blockingFactor). The blocking factor is a value between 0 and 1
     * Blocking factor is the fraction of time a thread is blocked on IO Operations
     *
     * @see <a href="https://youtu.be/0hQvWIdwnw4?t=4297">Parallelism and streams</a>
     * @see <a href="https://youtu.be/0hQvWIdwnw4?t=4132">
     * Parallel and Asynchronous Programming with Streams and CompletableFuture with Venkat Subramaniam</a>
     * @see <a href="https://www.youtube.com/watch?v=UqlF6Mfhnz0">
     * Async Programming and Project Loom by Dr Venkat Subramaniam<a/>
     */
    @Test
    void testCommonPoolParallelismIsOneLessThanAvailableProcessors() {
        final var availableProcessors = Runtime.getRuntime().availableProcessors();
        final var commonPool = ForkJoinPool.commonPool();

        println("forkJoinPool = " + commonPool);
        println("availableProcessors = " + availableProcessors);

        final var parallelism = commonPool.getParallelism();

        assertEquals(availableProcessors - 1, parallelism);
    }

    @Test
    void testParallelStreamExecutesFasterThanSequentialStream() {
        final int ITERATIONS = 100;
        println("Sequential:");
        final var resultSequential = executeAndTime(ITERATIONS, this::executeSequentialStream);
        println("Parallel:");
        final var resultParallel = executeAndTime(ITERATIONS, this::executeParallelStream);

        long durationSequential = resultSequential.first();
        long durationParallel = resultParallel.first();

        println("durationSequential = " + durationSequential / 1e9 + " s");
        println("durationParallel = " + durationParallel / 1e9 + " s");

        long timesFaster = durationSequential / durationParallel;

        final var ints = resultParallel.second();

        println("timesFaster = " + timesFaster);
        println("Array: " + Arrays.toString(ints));

        assertAll(
                () -> assertArrayEquals(ints, resultSequential.second()),
                () -> assertTrue(durationSequential > durationParallel)
        );
    }

    static <R> Pair<Long, R> executeAndTime(@SuppressWarnings("SameParameterValue") int times, IntFunction<R> intFunction) {
        final var start = System.nanoTime();
        final var result = intFunction.apply(times);
        final var duration = System.nanoTime() - start;
        println(ForkJoinPool.commonPool());
        return Pair.of(duration, result);
    }

    int[] executeSequentialStream(int times) {
        return IntStream.range(0, times)
                .filter(i -> i % 2 == 0)
                .map(ParallelStreamsTest::expensiveOperation)
                .toArray();
    }

    int[] executeParallelStream(int times) {
        return IntStream.range(0, times)
                .parallel()
                .filter(i -> i % 2 == 0)
                .map(ParallelStreamsTest::expensiveOperation)
                .toArray();
    }

    private static int expensiveOperation(int i) {
        TimingUtils.sleep(Duration.ofMillis(10));
        printf("This is call nr %s%n", i);
        return i;
    }

}
