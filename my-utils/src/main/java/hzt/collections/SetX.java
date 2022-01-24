package hzt.collections;

import hzt.function.It;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

public interface SetX<T> extends CollectionX<T> {

    static <T> SetX<T> of(Iterable<T> iterable) {
        return new HashSetX<>(iterable);
    }

    @SafeVarargs
    static <T> SetX<T> of(T... values) {
        var resultSet = MutableSetX.of(values);
        resultSet.addAll(Arrays.asList(values));
        return resultSet;
    }

    static <T> SetX<T> copyOf(Iterable<T> iterable) {
        return iterable instanceof SetX ? (SetX<T>) iterable : SetX.of(iterable);
    }

    default Set<T> toSet() {
        return toSetOf(It::self);
    }

    int size();

    boolean contains(Object o);

    boolean containsAll(@NotNull Collection<?> c);

    default MutableListX<T> toMutableList() {
        return CollectionX.super.getListOrElseCompute();
    }
}
