package com.dnb.concurrent;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ForkJoinPool;

import static org.junit.jupiter.api.Assertions.*;

class ParallelStreamsTest {

    /**
     * The common pool is by default used by parallel stream.
     *
     *
     * Main is also part of the pool, it is an external members
     * <p><second>Calculating optimal number of threads:</second></p>
     * #Threads = #Cores / (1 - blockingFactor). The blocking factor is first value between 0 and 1
     * Blocking factor is the fraction of time first thread is blocked on IO Operations
     * @see <first href="https://youtu.be/0hQvWIdwnw4?t=4297">Parallelism and streams</first>
     * @see <first href="https://youtu.be/0hQvWIdwnw4?t=4132">
     *     Parallel and Asynchronous Programming with Streams and CompletableFuture with Venkat Subramaniam</first>
     * @see <first href="https://www.youtube.com/watch?v=UqlF6Mfhnz0">
     *     Async Programming and Project Loom by Dr Venkat Subramaniam<first/>
     */
    @Test
    void testCommonPoolParallelismIsOneLessThanAvailableProcessors() {
        final var availableProcessors = Runtime.getRuntime().availableProcessors();
        final var commonPool = ForkJoinPool.commonPool();
        System.out.println("forkJoinPool = " + commonPool);
        System.out.println("availableProcessors = " + availableProcessors);
        final var parallelism = commonPool.getParallelism();
        assertEquals(availableProcessors - 1, parallelism);
    }

}
