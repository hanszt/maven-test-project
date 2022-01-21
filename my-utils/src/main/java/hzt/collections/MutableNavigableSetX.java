package hzt.collections;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.function.Function;
import java.util.stream.Stream;

public interface MutableNavigableSetX<E> extends NavigableSet<E>, MutableSetX<E> {

    static <E, R extends Comparable<R>> MutableNavigableSetX<E> comparingBy(Function<E, R> selector) {
        return new TreeSetX<>(selector);
    }

    static <E> MutableNavigableSetX<E> of(NavigableSet<E> set) {
        return new TreeSetX<>(set);
    }

    static <E, R extends Comparable<R>> MutableNavigableSetX<E> of(Iterable<E> iterable, Function<? super E, ? extends R> selector) {
        return new TreeSetX<>(iterable, selector);
    }

    static <E, R extends Comparable<R>> MutableNavigableSetX<E> of(Collection<E> collection, Function<? super E, ? extends R> selector) {
        return new TreeSetX<>(collection, selector);
    }

    @SafeVarargs
    static <E, R extends Comparable<R>> MutableNavigableSetX<E> of(Function<E, R> selector, E first, E... others) {
        return new TreeSetX<>(selector, first, others);
    }

    @NotNull
    @Override
    default Iterator<E> iterator() {
        return iterable().iterator();
    }

    @Override
    default Stream<E> stream() {
        return MutableSetX.super.stream();
    }

    @Override
    @NotNull
    default E first() {
        return MutableSetX.super.first();
    }

    @Override
    default E last() {
        return MutableSetX.super.last();
    }

    default MutableNavigableSetX<E> toSet() {
        return getNavigableSetOrElseThrow();
    }
}
