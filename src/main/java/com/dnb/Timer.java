package com.dnb;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.function.LongToDoubleFunction;

public final class Timer<R> {

    private final R result;
    private final long durationInNanos;

    private Timer(R result, long durationInNanos) {
        this.result = result;
        this.durationInNanos = durationInNanos;
    }

    private Timer(long durationInNanos) {
        this(null, durationInNanos);
    }

    public R getResult() {
        return result;
    }

    public double getTimeInMillis() {
        return durationInNanos / 1e6;
    }

    public Duration getTimeAsDuration() {
        return Duration.of(durationInNanos, ChronoUnit.NANOS);
    }

    /**
     * @param t the parameter the function is applied to
     * @param function the function that is applied to the parameter t
     * @param <T> The type of the input parameter
     * @param <R> The type of the output parameter
     * @return a Timer object that contains the time it took to execute the function and the result of the function
     */
    public static <T, R> Timer<R> timeAFunction(T t, Function<T, R> function) {
        long start = System.nanoTime();
        R r = function.apply(t);
        long time = System.nanoTime() - start;
        return new Timer<>(r, time);
    }

    public static <R> Timer<R> timeAFunction(long aLong, LongFunction<R> function) {
        long start = System.nanoTime();
        R r = function.apply(aLong);
        long time = System.nanoTime() - start;
        return new Timer<>(r, time);
    }

    /**
     * @param t the parameter consumed
     * @param consumer the consumer that consumes the parameter t
     * @param <T> The type of the parameter consumed
     * @return a Timer object that contains the time it took to execute the consumer
     */
    public static <T> Timer<Void> timeAConsumer(T t, Consumer<T> consumer) {
        long start = System.nanoTime();
        consumer.accept(t);
        long time = System.nanoTime() - start;
        return new Timer<>(time);
    }


}
