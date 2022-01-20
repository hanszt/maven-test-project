package hzt.collections;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Stream;

public sealed interface MutableSetX<E> extends Set<E>, SetX<E> permits HashSetX, MutableLinkedSetX, MutableNavigableSetX {

    static <E> MutableSetX<E> empty() {
        return new HashSetX<>();
    }

    static <E> MutableSetX<E> of(Set<E> set) {
        return new HashSetX<>(set);
    }

    static <E> MutableSetX<E> of(Iterable<E> set) {
        return new HashSetX<>(set);
    }

    static <E> MutableSetX<E> of(Collection<E> collection) {
        return new HashSetX<>(collection);
    }

    @SafeVarargs
    static <E> MutableSetX<E> of(E first, E... others) {
        return new HashSetX<>(first, others);
    }

    @NotNull
    @Override
    default Iterator<E> iterator() {
        return iterable().iterator();
    }

    @Override
    default Stream<E> stream() {
        return Set.super.stream();
    }

}
