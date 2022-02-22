package org.hzt.primitve_sequences;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.function.IntUnaryOperator;
import java.util.function.ToIntFunction;

public final class IntIterators {

    private IntIterators() {
    }

    @NotNull
    public static <T> PrimitiveIterator.OfInt toIntIterator(Iterator<T> iterator, ToIntFunction<T> mapper) {
        return new PrimitiveIterator.OfInt() {
            @Override
            public int nextInt() {
                return mapper.applyAsInt(iterator.next());
            }
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
        };
    }

    @NotNull
    public static PrimitiveIterator.OfInt iteratorFromIntArray(int[] array) {
        return new PrimitiveIterator.OfInt() {
            int index = 0;
            @Override
            public int nextInt() {
                int prevIndex = index;
                if (prevIndex < 0 || prevIndex >= array.length) {
                    throw new NoSuchElementException("index out of bounds. (Index value: " + index + ")");
                }
                return array[index++];
            }
            @Override
            public boolean hasNext() {
                return index < array.length;
            }
        };
    }

    public static PrimitiveIterator.OfInt mappingIntIterator(PrimitiveIterator.OfInt intIterator, IntUnaryOperator intUnaryOperator) {
        return new PrimitiveIterator.OfInt() {
            @Override
            public int nextInt() {
                return intUnaryOperator.applyAsInt(intIterator.nextInt());
            }
            @Override
            public boolean hasNext() {
                return intIterator.hasNext();
            }
        };
    }
}
