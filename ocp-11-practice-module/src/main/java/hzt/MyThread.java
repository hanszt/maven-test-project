package hzt;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static java.lang.System.*;

public class MyThread extends Thread {

    private static final String START_MESSAGE_FORMATTER = "Thread %s started%n";
    private static final String END_MESSAGE_FORMATTER = "Thread %s ended%n";

    @Override
    public void run() {
        out.printf(START_MESSAGE_FORMATTER, Thread.currentThread().getName());
        Duration duration = Duration.of(5, ChronoUnit.SECONDS);
        LocalTime startTime = LocalTime.now(); // seconds
        long counter = 0;
        boolean running = true;
        while (running) {
           counter++;
            running = startTime.plus(duration).isAfter(LocalTime.now());
        }
        out.printf("%d while loop cycles in %2d seconds%n", counter, duration.toSeconds());
        out.printf(END_MESSAGE_FORMATTER, Thread.currentThread().getName());
    }

    public static void main(String[] args) {
        out.printf(START_MESSAGE_FORMATTER, Thread.currentThread().getName());
        for (int i = 0; i < 100; i++) {
            Thread thread = new MyThread();
            thread.start();
        }
        out.printf(END_MESSAGE_FORMATTER, Thread.currentThread().getName());
    }

}
