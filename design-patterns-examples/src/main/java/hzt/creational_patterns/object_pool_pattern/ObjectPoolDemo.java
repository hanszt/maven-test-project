package hzt.creational_patterns.object_pool_pattern;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class ObjectPoolDemo {

    private final ObjectPool<ExportingProcess> pool;
    private final AtomicLong processNo = new AtomicLong(0);

    public ObjectPoolDemo() {
        this.pool = createPool();
    }

    public ObjectPool<ExportingProcess> createPool() {
        // Create a pool of objects of type ExportingProcess.
           /* Parameters:
             1) Minimum number of special ExportingProcess instances residing in the pool = 4
             2) Maximum number of special ExportingProcess instances residing in the pool = 10
             3) Time in seconds for periodical checking of minObjects / maxObjects conditions
                in a separate thread = 5.
             --> When the number of ExportingProcess instances is less than minObjects,
                 missing instances will be created.
             --> When the number of ExportingProcess instances is greater than maxObjects,
                  instances will be removed un till the number of objects is equal to maxObjects.
             --> If the validation interval is negative, no periodical checking of
                  minObjects / maxObjects conditions in a separate thread takes place.
                  The boundaries will then be ignored.
           */
        return new ObjectPool<>(4, 10, 5) {

            @Override
            protected ExportingProcess createObject() {
                // create a test object which takes some time for creation
                return new ExportingProcess(processNo.incrementAndGet());
            }
        };
    }

    public void tearDown() {
        pool.shutdown();
    }

    public void testObjectPool() {
        ExecutorService executor = Executors.newFixedThreadPool(8);

        // execute 8 tasks in separate threads

        executor.execute(new ExportingTask(pool, 1));
        executor.execute(new ExportingTask(pool, 2));
        executor.execute(new ExportingTask(pool, 3));
        executor.execute(new ExportingTask(pool, 4));
        executor.execute(new ExportingTask(pool, 5));
        executor.execute(new ExportingTask(pool, 6));
        executor.execute(new ExportingTask(pool, 7));
        executor.execute(new ExportingTask(pool, 8));

        executor.shutdown();
        try {
            boolean result = executor.awaitTermination(30, TimeUnit.SECONDS);
            System.out.println("Result of await termination: " + result);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String... args) {
        ObjectPoolDemo objectPool = new ObjectPoolDemo();
        objectPool.tearDown();
        objectPool.testObjectPool();
    }
}
