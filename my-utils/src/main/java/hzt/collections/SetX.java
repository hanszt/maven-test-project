package hzt.collections;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

public sealed interface SetX<T> extends IterableX<T> permits MutableSetX {

    static <T> SetX<T> of(Iterable<T> iterable) {
        return new HashSetX<>(iterable);
    }

    static <T> SetX<T> copyOf(Iterable<T> iterable) {
        return iterable instanceof SetX<T> set ? set : SetX.of(iterable);
    }

    default Set<T> toSet() {
        return toSetOf(Function.identity());
    }

    int size();

    boolean contains(Object o);

    boolean containsAll(@NotNull Collection<?> c);

    default MutableListX<T> toMutableList() {
        return IterableX.super.getListOrElseCompute();
    }
}
