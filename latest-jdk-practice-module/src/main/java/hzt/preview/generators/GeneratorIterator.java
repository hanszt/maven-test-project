package hzt.preview.generators;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * This class allows specifying Python generator-like sequences. For examples,
 * see the JUnit test cases.
 * <p>
 * The implementation uses a separate Thread to produce the sequence items. This
 * is certainly not as fast as e.g. a for-loop, but not horribly slow either. On
 * a machine with a dual-core i5 CPU @ 2.67 GHz, 1000 items can be produced in
 * &lt; 0.03s.
 * <p>
 * By implementing the auto-closable interface, the class takes care not to leave any Threads
 * running longer than necessary.
 *
 * @see <a href="https://stackoverflow.com/questions/11570132/generator-functions-equivalent-in-java">Generator function java</a>
 * @see <a href="https://github.com/mherrmann/java-generator-functions/blob/master/src/main/java/io/herrmann/generator/Generator.java">
 * java-generator-functions</a>
 */
final class GeneratorIterator<T> implements Iterator<T>, GeneratorScope<T>, AutoCloseable {

    private Thread producerThread;
    private final Condition itemAvailableOrHasFinished = new Condition();
    private final Condition itemRequested = new Condition();
    private T nextItem = null;
    private boolean nextItemAvailable = false;
    private boolean hasFinished = false;
    private RuntimeException exceptionRaisedByProducer = null;

    private final Consumer<GeneratorScope<T>> scopeConsumer;

    GeneratorIterator(Consumer<GeneratorScope<T>> scopeConsumer) {
        this.scopeConsumer = scopeConsumer;
    }

    @Override
    public boolean hasNext() {
        return waitForNext();
    }

    @Override
    public T next() {
        if (!waitForNext()) {
            throw new NoSuchElementException();
        }
        nextItemAvailable = false;
        return nextItem;
    }

    private boolean waitForNext() {
        if (nextItemAvailable) {
            return true;
        }
        if (hasFinished) {
            return false;
        }
        if (producerThread == null) {
            producerThread = buildAndStartProducerThread();
        }
        itemRequested.setTrueAndNotifyAll();
        try {
            itemAvailableOrHasFinished.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            hasFinished = true;
        }
        if (exceptionRaisedByProducer != null) {
            throw exceptionRaisedByProducer;
        }
        return !hasFinished;
    }

    private Thread buildAndStartProducerThread() {
        //a virtual thread is always a daemon thread. See setDaemon(boolean) method
        return Thread.ofVirtual().start(this::setNext);
    }

    private void setNext() {
        try {
            itemRequested.await();
            scopeConsumer.accept(this);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            // No need to do anything here; Remaining steps in run()
            // will cleanly shut down the thread.
        } catch (RuntimeException e) {
            exceptionRaisedByProducer = e;
        } finally {
            hasFinished = true;
            itemAvailableOrHasFinished.setTrueAndNotifyAll();
        }
    }

    @Override
    public void yieldNext(T element) {
        nextItem = element;
        nextItemAvailable = true;
        itemAvailableOrHasFinished.setTrueAndNotifyAll();
        try {
            itemRequested.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            if (Thread.interrupted()) {
                throw new IllegalStateException(e);
            }
        }
    }

    public Thread.State getProducerState() {
        return producerThread.getState();
    }

    @Override
    public void close() {
        producerThread.interrupt();
        try {
            producerThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static class Condition {
        private static final String CALL_WAIT_ONLY_FROM_LOOPS = "java:S2274";
        private final AtomicBoolean isSet = new AtomicBoolean(false);

        public synchronized void setTrueAndNotifyAll() {
            isSet.set(true);
            notifyAll();
        }

        @SuppressWarnings(CALL_WAIT_ONLY_FROM_LOOPS)
        private synchronized void await() throws InterruptedException {
            try {
                if (isSet.get()) {
                    return;
                }
                wait();
            } finally {
                isSet.set(false);
            }
        }
    }
}
