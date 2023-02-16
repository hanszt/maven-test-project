package org.hzt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.time.Duration;

import static org.hzt.utils.It.println;

public class ThreadingSample {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadingSample.class);

    public static void main(String... args) {
        printActiveThreads();
        int counter = 0;
        while (counter < 10) {
            LOGGER.info("counter = {}", counter);
            TimingUtils.sleep(Duration.ofSeconds(1));
            counter++;
        }
        System.out.println("Thread.activeCount() = " + ManagementFactory.getThreadMXBean().getThreadCount());
    }

    /**
     * The nr of threads run in normal run is two and in debug mode is one
     * 
     * @see <a href="https://stackoverflow.com/questions/23140478/why-the-first-thread-activecount-return-1-but-another-return-2-in-my-code">
     *     Why the first Thread.activeCount() return 1 but another return 2 in my code?</a>
     */
    private static void printActiveThreads() {
        Thread thread = Thread.currentThread();
        thread.setName("Admin Thread");
        thread.setPriority(1);
        println("Thread = " + thread);
        int count = Thread.activeCount();
        println("currently active threads = " + count);

        Thread[] threads = new Thread[count];
        // returns the number of threads put into the array
        final var nr = Thread.enumerate(threads);
        println("nr = " + nr);
        // prints active threads
        for (int i = 0; i < count; i++) {
            println(i + ": " + threads[i]);
        }
    }
}
