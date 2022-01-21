package hzt.collections;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface MutableListX<E> extends List<E>, ListX<E> {

    static <E> MutableListX<E> empty() {
        return new ArrayListX<>();
    }

    static <E> MutableListX<E> withInitCapacity(int capacity) {
        return new ArrayListX<>(capacity);
    }

    static <E> MutableListX<E> of(@NotNull Iterable<E> iterable) {
        return new ArrayListX<>(iterable);
    }

    static <E> MutableListX<E> of(@NotNull Collection<E> collection) {
        return new ArrayListX<>(collection);
    }

    static <E> MutableListX<E> of(@NotNull List<E> list) {
        return new ArrayListX<>(list);
    }

    @SafeVarargs
    static <E> MutableListX<E> of(@NotNull E first, E... values) {
        return new ArrayListX<>(first, values);
    }

    @Override
    default MutableListX<E> plus(@NotNull Iterable<E> values) {
        return toMutableListPlus(values);
    }

    @Override
    default MutableListX<E> plus(E value) {
        return toMutableListPlus(value);
    }

    @Override
    default <R> MutableListX<R> map(Function<E, R> mapper) {
        return toMutableListOf(mapper);
    }

    @Override
    default MutableListX<E> filter(Predicate<E> predicate) {
        return filterToMutableList(predicate);
    }

    default <R> MutableListX<E> filterNotNullBy(Function<E, R> function, Predicate<R> predicate) {
        return filterNotNullToMutableListBy(function, predicate);
    }

    @Override
    default MutableListX<E> filterNot(Predicate<E> predicate) {return filter(predicate.negate());
    }

    @Override
    default MutableListX<E> takeWhile(Predicate<E> predicate) {
        return takeToMutableListWhile(predicate);
    }

    /**
     * Returns an iterator over the elements in this list in proper sequence.
     *
     * @return an iterator over the elements in this list in proper sequence
     */
    @NotNull
    @Override
    default Iterator<E> iterator() {
        return iterable().iterator();
    }

    @Override
    default Stream<E> stream() {
        return List.super.stream();
    }

    ListX<E> toListX();

    MutableListX<E> subList(int fromIndex, int toIndex);
}
