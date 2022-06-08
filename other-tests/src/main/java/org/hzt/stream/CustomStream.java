package org.hzt.stream;

import org.hzt.utils.streams.StreamX;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

@FunctionalInterface
public interface CustomStream<T> extends StreamX<T> {


    static <T> CustomStream<T> of(@NotNull Iterable<T> iterable) {
        //noinspection NullableProblems
        return iterable::spliterator;
    }

    @Override
    default CustomStream<T> filter(Predicate<? super T> predicate) {
        return () -> StreamX.super.filter(predicate).spliterator();
    }

    default CustomStream<T> filterNot(Predicate<? super T> predicate) {
        return () -> StreamX.super.filter(predicate.negate()).spliterator();
    }
}
