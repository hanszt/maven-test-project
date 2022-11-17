package demo.sequences;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class WindowedIterator<T> extends AbstractIterator<List<T>> {
    private final Iterator<T> iterator;
    private final int size;
    private final int step;
    private final boolean partialWindows;
    private List<T> nextWindow = new ArrayList<>();
    private int skip = 0;

    WindowedIterator(
            Iterator<T> iterator,
            int size,
            int step,
            boolean partialWindows) {
        this.iterator = iterator;
        this.size = size;
        this.step = step;
        this.partialWindows = partialWindows;
    }

    private List<T> computeNextWindow() {
        int windowInitCapacity = Math.min(size, 1024);
        final int gap = step - size;
        if (gap >= 0) {
            computeNextForWindowedSequenceNoOverlap(windowInitCapacity, gap);
        } else {
            computeNextForWindowedSequenceOverlapping(windowInitCapacity);
        }
        return List.copyOf(nextWindow);
    }

    private void computeNextForWindowedSequenceOverlapping(int windowInitCapacity) {
        nextWindow = nextWindow.isEmpty() ? new ArrayList<>(windowInitCapacity) : new ArrayList<>(nextWindow);
        if (nextWindow.isEmpty()) {
            fillIfWindowEmpty();
        } else {
            calculateNextWindow();
        }
        if (!partialWindows && nextWindow.size() < size) {
            nextWindow.clear();
        }
    }

    private void fillIfWindowEmpty() {
        while (iterator.hasNext() && nextWindow.size() < size) {
            nextWindow.add(iterator.next());
        }
    }

    private void calculateNextWindow() {
        int stepCount = step;
        while (stepCount > 0) {
            if (!nextWindow.isEmpty()) {
                nextWindow.remove(0);
            }
            if (iterator.hasNext()) {
                nextWindow.add(iterator.next());
            }
            stepCount--;
        }
    }

    private void computeNextForWindowedSequenceNoOverlap(int bufferInitCapacity, int gap) {
        nextWindow = new ArrayList<>(bufferInitCapacity);
        while (iterator.hasNext()) {
            T item = iterator.next();
            if (skip > 0) {
                skip--;
                continue;
            }
            nextWindow.add(item);
            if (nextWindow.size() == size) {
                skip = gap;
                return;
            }
        }
        if (!nextWindow.isEmpty() && !partialWindows) {
            nextWindow.clear();
        }
    }

    @Override
    void computeNext() {
        final List<T> next = computeNextWindow();
        if (next.isEmpty()) {
            done();
        } else {
            setNext(next);
        }
    }
}
