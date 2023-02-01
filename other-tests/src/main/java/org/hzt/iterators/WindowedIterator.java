package org.hzt.iterators;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.IntUnaryOperator;

public final class WindowedIterator<T> extends AbstractIterator<List<T>> {

    private final Iterator<T> iterator;
    private final int initSize;
    private final IntUnaryOperator nextSizeSupplier;
    private final int initStep;
    private final IntUnaryOperator nextStepSupplier;
    private final boolean partialWindows;

    private int step = 0;
    private int size = 0;
    private List<T> nextWindow = new ArrayList<>();

    private WindowedIterator(
            @NotNull Iterator<T> iterator,
            int initSize,
            @NotNull IntUnaryOperator nextSizeSupplier,
            int initStep,
            @NotNull IntUnaryOperator nextStepSupplier,
            boolean partialWindows) {
        this.iterator = iterator;
        this.initSize = initSize;
        this.nextSizeSupplier = nextSizeSupplier;
        this.initStep = initStep;
        this.nextStepSupplier = nextStepSupplier;
        this.partialWindows = partialWindows;
    }

    public static <T> WindowedIterator<T> of(@NotNull Iterator<T> iterator,
                                             int initSize,
                                             @NotNull IntUnaryOperator nextSizeSupplier,
                                             int initStep,
                                             @NotNull IntUnaryOperator nextStepSupplier,
                                             boolean partialWindows) {
        return new WindowedIterator<>(iterator, initSize, nextSizeSupplier, initStep, nextStepSupplier, partialWindows);
    }

    private List<T> computeNextWindow() {
        int windowInitCapacity = Math.min(size, 1024);
        final int gap = step - size;
        size = calculateNextSize(size);
        if (gap >= 0) {
            computeNextWindowNoOverlap(windowInitCapacity, gap);
        } else {
            computeNextWindowOverlapping(windowInitCapacity);
        }
        step = calculateNextStep(step);
        return List.copyOf(nextWindow);
    }

    private int calculateNextSize(int cur) {
        int next = cur <= 0 ? initSize : nextSizeSupplier.applyAsInt(cur);
        return Math.max(next, 1);
    }

    private int calculateNextStep(int cur) {
        int next = cur <= 0 ? initStep : nextStepSupplier.applyAsInt(cur);
        return Math.max(next, 1);
    }

    private void computeNextWindowOverlapping(int windowInitCapacity) {
        nextWindow = nextWindow.isEmpty() ? new ArrayList<>(windowInitCapacity) : new ArrayList<>(nextWindow);
        calculateNextOverlappingWindow();
        if (!partialWindows && nextWindow.size() < size) {
            nextWindow.clear();
        }
    }

    private void calculateNextOverlappingWindow() {
        int stepCount = 0;
        while (stepCount < step && !nextWindow.isEmpty()) {
            nextWindow.remove(0);
            stepCount++;
        }
        while (iterator.hasNext() && nextWindow.size() < size) {
            nextWindow.add(iterator.next());
        }
    }

    private void computeNextWindowNoOverlap(int bufferInitCapacity, int gap) {
        int skip = gap;
        nextWindow = new ArrayList<>(bufferInitCapacity);
        while (iterator.hasNext()) {
            T item = iterator.next();
            if (skip > 0) {
                skip--;
                continue;
            }
            nextWindow.add(item);
            if (nextWindow.size() == size) {
                return;
            }
        }
        if (!nextWindow.isEmpty() && !partialWindows) {
            nextWindow.clear();
        }
    }

    @Override
    protected void computeNext() {
        final var next = computeNextWindow();
        if (!next.isEmpty()) {
            setNext(next);
        } else {
            done();
        }
    }
}
