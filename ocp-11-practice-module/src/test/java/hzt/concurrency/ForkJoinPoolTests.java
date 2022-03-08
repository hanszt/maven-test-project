package hzt.concurrency;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ForkJoinPool;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ForkJoinPoolTests {
    
    @Test
    void testCommonPool() {
        final var forkJoinPool = ForkJoinPool.commonPool();
        forkJoinPool.execute(() -> System.out.println("hallo"));
        final var activeThreadCount = forkJoinPool.getActiveThreadCount();
        System.out.println("activeThreadCount = " + activeThreadCount);
        assertTrue(activeThreadCount > 0);
    }
}
