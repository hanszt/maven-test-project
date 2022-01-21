package hzt.collections;

import hzt.strings.StringX;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * This class represents an immutable non-null list. When a list of this interface is created, all null values are filtered out
 * @param <T> the type of the elements
 *
 * @author Hans Zuidervaart
 */
public sealed interface ListX<T> extends IterableX<T> permits MutableListX {

    static <T> ListX<T> of(Iterable<T> iterable) {
        return new ArrayListX<>(iterable);
    }

    static <T> ListX<T> of(Collection<T> iterable) {
        return new ArrayListX<>(iterable);
    }

    static <T> ListX<T> of(List<T> list) {
        return new ArrayListX<>(list);
    }

    @SafeVarargs
    static <T> ListX<T> of(T first, T... values) {
        return new ArrayListX<>(first).plus(Arrays.asList(values));
    }

    default MutableListX<T> toMutableList() {
       return MutableListX.of(this);
    }

    default List<T> toList() {
        return toListOf(Function.identity());
    }

    @Override
    default <R> ListX<R> map(Function<T, R> mapper) {
        return toMutableListOf(mapper);
    }

    default ListX<StringX> toStringXList(Function<T, CharSequence> mapper) {
        return map(s -> StringX.of(mapper.apply(s)));
    }

    @Override
    default ListX<T> filterNot(Predicate<T> predicate) {
        return filterToList(predicate.negate());
    }

    @Override
    default ListX<T> takeWhile(Predicate<T> predicate) {
        return takeToListWhile(predicate);
    }

    static <E> ListX<E> copyOf(Iterable<E> iterable) {
        return iterable instanceof ListX<E> listX ? listX : ListX.of(iterable);
    }

    int size();

    boolean isEmpty();

    default boolean isNotEmpty() {
        return !isEmpty();
    }

    boolean contains(Object o);

    default boolean containsNot(T t) {
        return !contains(t);
    }

    boolean containsAll(Collection<?> c);

    default boolean containsNoneOf(Collection<?> collection) {
        return !containsAll(collection);
    }

    T get(int index);

    int indexOf(Object o);

    int lastIndexOf(Object o);

    ListIterator<T> listIterator();

    ListIterator<T> listIterator(int index);

    ListX<T> subList(int fromIndex, int toIndex);
}
