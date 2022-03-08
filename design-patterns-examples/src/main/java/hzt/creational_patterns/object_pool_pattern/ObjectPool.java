package hzt.creational_patterns.object_pool_pattern;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
  pool implementation is based on ConcurrentLinkedQueue from the java.util.concurrent package.
  ConcurrentLinkedQueue is a thread-safe queue based on linked nodes.
   Because the queue follows FIFO technique (first-in-first-out).
 */
public abstract class ObjectPool<T> {

    private final ConcurrentLinkedQueue<T> pool;

    /**
      ScheduledExecutorService starts a special task in a separate thread and observes
      the minimum and maximum number of objects in the pool periodically in a specified
       time (with parameter validationInterval).
      When the number of objects is less than the minimum, missing instances will be created.
      When the number of objects is greater than the maximum, too many instances will be removed.
      This is sometimes useful for the balance of memory consuming objects in the pool.
   */
    private final ScheduledExecutorService executorService;

    /**
      Creates the pool.
      @param minObjects:   minimum number of objects residing in the pool.
      @param maxObjects:   maximum number of objects residing in the pool.
      @param validationInterval: time in seconds for periodical checking of
         minObjects / maxObjects conditions in a separate thread.
      When the number of objects is less than minObjects, missing instances will be created.
      When the number of objects is greater than maxObjects, too many instances will be removed.
    */
    protected ObjectPool(final int minObjects, final int maxObjects, final long validationInterval) {
        // initialize pool
        pool = initializePool(minObjects);
        // check pool conditions in a separate thread
        executorService = Executors.newSingleThreadScheduledExecutor();
        // anonymous class
        executorService.scheduleWithFixedDelay(() -> executeScheduledTask(minObjects, maxObjects),
                validationInterval, validationInterval, TimeUnit.SECONDS);
    }

    private void executeScheduledTask(int minObjects, int maxObjects) {
        int size = pool.size();

        if (size < minObjects) {
            int sizeToBeAdded = minObjects + size;
            IntStream.range(0, sizeToBeAdded).mapToObj(i -> createObject()).forEach(pool::add);
        } else if (size > maxObjects) {
            int sizeToBeRemoved = size - maxObjects;
            IntStream.range(0, sizeToBeRemoved).forEach(i -> pool.poll());
        }
    }

    /**
        Gets the next free object from the pool. If the pool doesn't contain any objects,
        a new object will be created and given to the caller of this method back.

        @return T borrowed object
    */
    public T borrowObject() {
        T object = pool.poll();
        if (object == null) {
            object = createObject();
        }
        return object;
    }

    /**
         Returns object back to the pool.
         @param object object to be returned
     */
    public void returnObject(T object) {
        if (object == null) return;
        this.pool.offer(object);
    }

    /**
         Shutdown this pool.
     */
    public void shutdown() {
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    /**
        Creates a new object.
         @return T new object
     */
    protected abstract T createObject();

    private ConcurrentLinkedQueue<T> initializePool(final int minObjects) {
        return IntStream.range(0, minObjects)
                .mapToObj(i -> createObject())
                .collect(Collectors.toCollection(ConcurrentLinkedQueue::new));
    }
}
