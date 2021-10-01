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
     * <p><b>Calculating optimal number of threads:</b></p>
     * #Threads = #Cores / (1 - blockingFactor). The blocking factor is a value between 0 and 1
     * Blocking factor is the fraction of time a thread is blocked on IO Operations
     * @see <a href="https://youtu.be/0hQvWIdwnw4?t=4297">Parallelism and streams</a>
     * @see <a href="https://youtu.be/0hQvWIdwnw4?t=4132">
     *     Parallel and Asynchronous Programming with Streams and CompletableFuture with Venkat Subramaniam</a>
     * @see <a href="https://www.youtube.com/watch?v=UqlF6Mfhnz0">
     *     Async Programming and Project Loom by Dr Venkat Subramaniam<a/>
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
