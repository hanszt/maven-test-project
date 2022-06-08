package org.hzt.stream;

import java.io.Serial;
import java.util.Spliterator;
import java.util.concurrent.CountedCompleter;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;

/**
 * <p><b>Example.</b> Here is a class (not a very useful one, except
 *  for illustration) that maintains an array in which the actual data
 *  are held in even locations, and unrelated tag data are held in odd
 *  locations. Its Spliterator ignores the tags.
 *
 * @see java.util.Spliterator
 * @param <T> The type of the tagged array.
 */
class TaggedArray<T> {
    private final Object[] elements; // immutable after construction

    TaggedArray(T[] data, Object[] tags) {
        int size = data.length;
        if (tags.length != size) {
            throw new IllegalArgumentException();
        }
        this.elements = new Object[2 * size];
        int i = 0;
        int j = 0;
        while (i < size) {
            elements[j++] = data[i];
            elements[j++] = tags[i];
            ++i;
        }
    }

    public Spliterator<T> spliterator() {
        return new TaggedArraySpliterator<>(elements, 0, elements.length);
    }

    void parEach(Consumer<T> action) {
        Spliterator<T> s = spliterator();
        long targetBatchSize = s.estimateSize() / (ForkJoinPool.getCommonPoolParallelism() * 8L);
        new ParEach<>(null, s, action, targetBatchSize).invoke();
    }

    @SuppressWarnings("squid:S2972")
    static class TaggedArraySpliterator<T> implements Spliterator<T> {
        private final Object[] array;
        private int origin; // current index, advanced on split or traversal
        private final int fence; // one past the greatest index

        @SuppressWarnings("squid:S2384")
        TaggedArraySpliterator(Object[] array, int origin, int fence) {
            this.array = array;
            this.origin = origin;
            this.fence = fence;
        }

        @Override
        public void forEachRemaining(Consumer<? super T> action) {
            for (; origin < fence; origin += 2) {
                //noinspection unchecked
                action.accept((T) array[origin]);
            }
        }

        public boolean tryAdvance(Consumer<? super T> action) {
            if (origin < fence) {
                //noinspection unchecked
                action.accept((T) array[origin]);
                origin += 2;
                return true;
            }
            return false;
        }

        public Spliterator<T> trySplit() {
            int lo = origin; // divide range in half
            int mid = ((lo + fence) >>> 1) & ~1; // force midpoint to be even
            if (lo < mid) { // split out left half
                origin = mid; // reset this Spliterator's origin
                return new TaggedArraySpliterator<>(array, lo, mid);
            }
            // too small to split
            return null;
        }

        public long estimateSize() {
            return ((fence - origin) / 2);
        }

        public int characteristics() {
            return ORDERED | SIZED | IMMUTABLE | SUBSIZED;
        }
    }

    static class ParEach<T> extends CountedCompleter<Void> {

        @Serial
        private static final long serialVersionUID = 2L;
        final transient Spliterator<T> spliterator;
        final transient Consumer<T> action;
        final long targetBatchSize;

        ParEach(ParEach<T> parent, Spliterator<T> spliterator,
                Consumer<T> action, long targetBatchSize) {
            super(parent);
            this.spliterator = spliterator;
            this.action = action;
            this.targetBatchSize = targetBatchSize;
        }

        public void compute() {
            Spliterator<T> sub;
            while (spliterator.estimateSize() > targetBatchSize &&
                    (sub = spliterator.trySplit()) != null) {
                addToPendingCount(1);
                new ParEach<>(this, sub, action, targetBatchSize).fork();
            }
            spliterator.forEachRemaining(action);
            propagateCompletion();
        }
    }
}
