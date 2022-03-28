package org.hzt.sequences.primitve_sequences;

import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;

/**
 * @param <T> The boxed version of the primitive type
 * @param <C> The primitive consumer type
 */
public interface PrimitiveIterable<T, C> extends Iterable<T> {

    void forEach(C action);

    @FunctionalInterface
    interface OfInt extends PrimitiveIterable<Integer, IntConsumer> {

        PrimitiveIterator.OfInt intIterator();

        @Override
        default PrimitiveIterator.OfInt iterator() {
            return intIterator();
        }

        @Override
        default void forEach(@NotNull IntConsumer action) {
            PrimitiveIterator.OfInt intIterator = intIterator();
            while (intIterator.hasNext()) {
                action.accept(intIterator.nextInt());
            }
        }

        @Override
        default Spliterator.OfInt spliterator() {
            return Spliterators.spliteratorUnknownSize(intIterator(), 0);
        }
    }

    @FunctionalInterface
    interface OfLong extends PrimitiveIterable<Long, LongConsumer> {

        PrimitiveIterator.OfLong longIterator();

        @Override
        default PrimitiveIterator.OfLong iterator() {
            return longIterator();
        }

        @Override
        default void forEach(@NotNull LongConsumer action) {
            PrimitiveIterator.OfLong intIterator = longIterator();
            while (intIterator.hasNext()) {
                action.accept(intIterator.nextLong());
            }
        }

        @Override
        default Spliterator.OfLong spliterator() {
            return Spliterators.spliteratorUnknownSize(longIterator(), 0);
        }
    }

    @FunctionalInterface
    interface OfDouble extends PrimitiveIterable<Double, DoubleConsumer> {

        PrimitiveIterator.OfDouble doubleIterator();

        @Override
        default PrimitiveIterator.OfDouble iterator() {
            return doubleIterator();
        }

        @Override
        default void forEach(@NotNull DoubleConsumer action) {
            PrimitiveIterator.OfDouble intIterator = doubleIterator();
            while (intIterator.hasNext()) {
                action.accept(intIterator.nextDouble());
            }
        }

        @Override
        default Spliterator.OfDouble spliterator() {
            return Spliterators.spliteratorUnknownSize(doubleIterator(), 0);
        }
    }
}
