package hzt.collections;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface SetX<T> extends IterableX<T> {

    static <T> SetX<T> of(Iterable<T> iterable) {
        return new HashSetX<>(iterable);
    }

    static <T> SetX<T> copyOf(Iterable<T> iterable) {
        return iterable instanceof SetX ? (SetX<T>) iterable : SetX.of(iterable);
    }

    int size();

    boolean contains(Object o);

    boolean containsAll(@NotNull Collection<?> c);

    default MutableListX<T> toMutableList() {
        return IterableX.super.getMutableListOrElseCompute();
    }
}
