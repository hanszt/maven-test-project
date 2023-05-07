package hzt.preview;

import org.hzt.utils.It;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.hzt.utils.It.println;

public class VirtualThreadSample {

    public static void main(String... args) {
        println("Virtual threads sample");
        printVirtualVsPlatformThreadState();
        spawnOneMillionVirtualThreads();
    }

    private static void spawnOneMillionVirtualThreads() {
        final var LENGTH = 1_000_000;
        final var counts = new int[LENGTH];
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < LENGTH; i++) {
            final int index = i;
            final var thread = Thread.ofVirtual().start(() -> incrementAndSleep(counts, index));
            thread.setName("Virtual thread " + i);
            threads.add(thread);
        }
        println("threads.size() = " + threads.size());
        IntStream.range(0, counts.length)
                .filter(i -> i % 10_000 == 0)
                .filter(i -> counts[i] != 0)
                .mapToObj(i -> "count: %d, name: %s".formatted(counts[i], threads.get(i).getName()))
                .forEach(It::println);
    }

    private static void incrementAndSleep(int[] counts, int index) {
        for (int j = 0; j < 100; j++) {
            try {
                counts[index] = counts[index] + 1;
                TimeUnit.NANOSECONDS.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static void printVirtualVsPlatformThreadState() {
        Thread.ofVirtual().start(() -> println("Virtual thread: " +  Thread.currentThread()));
        Thread.ofPlatform().start(() -> println("Platform thread: " + Thread.currentThread()));
    }
}
