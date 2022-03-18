package org.hzt.primitve_sequences;

import org.hzt.utils.sequences.Sequence;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;
import java.util.function.ToIntFunction;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

public interface IntSequence extends PrimitiveIterable.OfInt {

    static IntSequence of(int... array) {
        return () -> IntIterators.iteratorFromIntArray(array);
    }

    static <T> IntSequence of(Iterable<T> iterable, ToIntFunction<T> mapper) {
        return () -> IntIterators.toIntIterator(iterable.iterator(), mapper);
    }

    static IntSequence of(Iterable<Integer> iterable) {
        return () -> IntIterators.toIntIterator(iterable.iterator(), e -> e);
    }

    static IntSequence of(IntStream intStream) {
        return intStream::iterator;
    }

    default IntSequence map(IntUnaryOperator intUnaryOperator) {
        return () -> IntIterators.mappingIntIterator(intIterator(), intUnaryOperator);
    }

    default <R> Sequence<R> mapToObj(IntFunction<R> toObjMapper) {
        return Sequence.of(this).map(toObjMapper::apply);
    }

    default IntSequence filter(IntPredicate intPredicate) {
        return () -> IntFilteringIterator.of(intIterator(), intPredicate);
    }

    default List<Integer> toMutableList() {
        final var intIterator = intIterator();
        List<Integer> list = new ArrayList<>();
        while (intIterator.hasNext()) {
            list.add(intIterator.nextInt());
        }
        return list;
    }

    default List<Integer> toList() {
        return List.copyOf(toMutableList());
    }

    default int[] toArray() {
        return StreamSupport.intStream(spliterator(), false).toArray();
    }

    default long count() {
        long counter = 0;
        final var intIterator = intIterator();
        while (intIterator.hasNext()) {
            intIterator.nextInt();
            counter++;
        }
        return counter;
    }
}
