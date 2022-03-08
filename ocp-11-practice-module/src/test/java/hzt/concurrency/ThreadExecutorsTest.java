package hzt.concurrency;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ThreadExecutorsTest {

    @Test
    void testSingleThreadExecutor() throws ExecutionException, InterruptedException, TimeoutException {
        runServiceAndTestResult(Executors.newSingleThreadExecutor());
    }

    @Test
    void testCashedThreadPool() throws ExecutionException, InterruptedException, TimeoutException {
        runServiceAndTestResult(Executors.newCachedThreadPool());
    }

    @Test
    void testUnconfigurableExecutorService() throws ExecutionException, InterruptedException, TimeoutException {
        runServiceAndTestResult(Executors.unconfigurableExecutorService(Executors.newCachedThreadPool()));
    }

    @Test
    void testFixedThreadPool() throws ExecutionException, InterruptedException, TimeoutException {
        runServiceAndTestResult(Executors.newFixedThreadPool(4));
    }

    @Test
    void testWorkSteelingPool() throws ExecutionException, InterruptedException, TimeoutException {
        runServiceAndTestResult(Executors.newWorkStealingPool());
    }

    private void runServiceAndTestResult(ExecutorService executorService)
            throws InterruptedException, ExecutionException, TimeoutException {
        final var future = executorService.submit(() -> "hallo");
        final var actual = future.get(3, TimeUnit.SECONDS);
        assertEquals("hallo", actual);
    }
}
