package com.dnb;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.function.LongToDoubleFunction;

public class Timer<R> {

    private final R result;
    private final long durationInNanos;

    public Timer(R result, long durationInNanos) {
        this.result = result;
        this.durationInNanos = durationInNanos;
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

    public static <T, R> Timer<R> timeFunction(T t, Function<T, R> function) {
        long start = System.nanoTime();
        R r = function.apply(t);
        long time = System.nanoTime() - start;
        return new Timer<>(r, time);
    }

    public static <R> Timer<R> timeFunction(long t, LongFunction<R> function) {
        long start = System.nanoTime();
        R r = function.apply(t);
        long time = System.nanoTime() - start;
        return new Timer<>(r, time);
    }

    public static Timer<Double> timeFunction(long t, LongToDoubleFunction function) {
        long start = System.nanoTime();
        double r = function.applyAsDouble(t);
        long time = System.nanoTime() - start;
        return new Timer<>(r, time);
    }
}
