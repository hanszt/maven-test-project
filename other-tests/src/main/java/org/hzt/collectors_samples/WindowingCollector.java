package org.hzt.collectors_samples;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

// TODO: 26-4-2023 implement
final class WindowingCollector<T, A, R> implements Collector<T, A, R> {

    private final int windowSize;
    private final int step;
    private final boolean partialWindows;
    private final Collector<T, A, R> downstream;

    WindowingCollector(int windowSize, int step, boolean partialWindows, Collector<T, A, R> downstream) {
        this.windowSize = windowSize;
        this.step = step;
        this.partialWindows = partialWindows;
        this.downstream = downstream;
    }

    @Override
    public Supplier<A> supplier() {
        return downstream.supplier();
    }

    @Override
    public BiConsumer<A, T> accumulator() {
        return (a, t) -> {
            downstream.accumulator().accept(a, t);
        };
    }

    @Override
    public BinaryOperator<A> combiner() {
        throw new UnsupportedOperationException("Windowing collector not supported in parallel");
    }

    @Override
    public Function<A, R> finisher() {
        return downstream.finisher();
    }

    @Override
    public Set<Characteristics> characteristics() {
        return downstream.characteristics();
    }
}
