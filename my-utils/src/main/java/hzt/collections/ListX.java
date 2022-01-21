package hzt.collections;

import hzt.strings.StringX;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * This class represents an immutable non-null list. When a list of this interface is created, all null values are filtered out
 * @param <T> the type of the elements
 *
 * @author Hans Zuidervaart
 */
public sealed interface ListX<T> extends CollectionX<T> permits MutableListX {

    static <T> ListX<T> empty() {
        return new ArrayListX<>();
    }

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
    static <T> ListX<T> of(T... values) {
        return new ArrayListX<>(values);
    }

    static ListX<Integer> ofInts(int... values) {
        var valueList = MutableListX.<Integer>empty();
        for (var value : values) {
            valueList.add(value);
        }
        return valueList;
    }

    static ListX<Long> ofLongs(long... values) {
        var valueList = MutableListX.<Long>empty();
        for (var value : values) {
            valueList.add(value);
        }
        return valueList;
    }

    static ListX<Double> ofDoubles(double... values) {
        var valueList = MutableListX.<Double>empty();
        for (var value : values) {
            valueList.add(value);
        }
        return valueList;
    }

    default MutableListX<T> toMutableList() {
       return MutableListX.of(this);
    }

    default List<T> toList() {
        return toListOf(Function.identity());
    }

    default SetX<T> toSetMutableSet() {
        return toMutableSetOf(Function.identity());
    }

    default SetX<T> toSetX() {
        return toMutableSetNotNullOf(Function.identity());
    }

    default Set<T> toSet() {
        return Set.copyOf(toMutableSetNotNullOf(Function.identity()));
    }

    @Override
    default <R> ListX<R> map(Function<T, R> mapper) {
        return toMutableListOf(mapper);
    }

    default ListX<StringX> toStringXList(Function<T, CharSequence> mapper) {
        return map(s -> StringX.of(mapper.apply(s)));
    }

    @Override
    default ListX<T> filter(Predicate<T> predicate) {
        return filterToMutableList(predicate);
    }

    @Override
    default ListX<T> filterNot(Predicate<T> predicate) {
        return filterToListX(predicate.negate());
    }

    @Override
    default ListX<T> takeWhile(Predicate<T> predicate) {
        return takeToListXWhile(predicate);
    }

    static <E> ListX<E> copyOf(Iterable<E> iterable) {
        return iterable instanceof ListX<E> listX ? listX : ListX.of(iterable);
    }

    default <R> R foldRight(@NotNull R initial, @NotNull BiFunction<T, R, R> operation) {
        List<T> list = getListOrElseCompute();
        var accumulator = initial;
        if (!list.isEmpty()) {
            final var listIterator = list.listIterator();
            while (listIterator.hasNext()) {
                listIterator.next();
            }
            while (listIterator.hasPrevious()) {
                accumulator = operation.apply(listIterator.previous(), accumulator);
            }
        }
        return accumulator;
    }

    default MutableListX<T> takeLastToMutableList(int n) {
        IterableX.requireGreaterThanZero(n);
        if (n == 0) {
            return MutableListX.empty();
        }
        var list = getListOrElseCompute();
        int size = list.size();
        if (n >= size) {
            return list;
        }
        if (n == 1) {
            return MutableListX.of(last());
        }
        var resultList = MutableListX.<T>withInitCapacity(n);
        for (int index = size - n; index < size; index++) {
            resultList.add(list.get(index));
        }
        return resultList;
    }

    default IterableX<T> takeLast(int n) {
        return IterableX.of(takeLastToMutableList(n));
    }

    @Override
    default <R> ListX<T> distinctBy(Function<T, R> selector) {
        return distinctToMutableListBy(selector);
    }

    int size();

    boolean isEmpty();

    default boolean isNotEmpty() {
        return !isEmpty();
    }

    boolean contains(Object o);

    @Override
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
