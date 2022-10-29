package hzt;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FutureTests {

    private static final String DO_NOT_USE_THREAD_SLEEP_IN_TESTS = "squid:S2925";

    @Test
    @SuppressWarnings(DO_NOT_USE_THREAD_SLEEP_IN_TESTS)
    void testGetOnFuture() throws ExecutionException, InterruptedException {
        var executorService = Executors.newSingleThreadExecutor();
        Future<String> future = executorService.submit(() -> {
            TimeUnit.SECONDS.sleep(1);
            return "DONE";
        });
        //    Waits if necessary for the computation to complete, and then retrieves its result.
        //    Returns:
        //    the computed result
        final var string = future.get();
        System.out.println(string); //1
        final var runnables = executorService.shutdownNow();//2
        System.out.println("runnables = " + runnables);

        assertTrue(runnables.isEmpty());
    }

}
