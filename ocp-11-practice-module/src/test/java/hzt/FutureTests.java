package hzt;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FutureTests {

    @Test
    void testGetOnFuture() throws ExecutionException, InterruptedException {
        var executorService = Executors.newSingleThreadExecutor();
        Future<String> future = executorService.submit(() -> waitAndReturnDone(5));
        //    Waits if necessary for the computation to complete, and then retrieves its result.
        //    Returns:
        //    the computed result
        final var string = future.get();
        System.out.println(string); //1
        final var runnables = executorService.shutdownNow();//2
        System.out.println("runnables = " + runnables);

        assertTrue(runnables.isEmpty());
    }

    @SuppressWarnings("squid:S2925")
    private String waitAndReturnDone(int i) throws InterruptedException {
        Thread.sleep(i * 1000L);
        return "DONE";
    }

}
