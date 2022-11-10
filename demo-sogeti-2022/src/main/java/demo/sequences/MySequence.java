package demo.sequences;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

@FunctionalInterface
public interface MySequence<T> extends Iterable<T> {

    static <T> MySequence<T> generate(T initValue, UnaryOperator<T> nextValueSupplier) {
        return () -> new GeneratorIterator<>(() -> initValue, nextValueSupplier);
    }

    static <T> MySequence<T> of(Iterable<T> iterable) {
        return iterable::iterator;
    }

    @SafeVarargs
    static <T> MySequence<T> of(T... values) {
        return () -> new ArrayIterator<>(values.length, index -> values[index]);
    }

    default <R> MySequence<R> map(Function<T, R> mapper) {
        return () -> new MappingIterator<>(iterator(), mapper);
    }

    default <R> MySequence<R> mapIndexed(BiFunction<Integer, T, R> mapper) {
        return () -> new MappingIndexedIterator<>(iterator(), mapper);
    }

    default MySequence<T> filter(Predicate<T> predicate) {
        return () -> new FilteringIterator<>(iterator(), predicate);
    }

    default T first() {
        final var iterator = iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        }
        throw new NoSuchElementException();
    }

    default List<T> toList() {
        List<T> list = new ArrayList<>();
        for (T e : this) {
            list.add(e);
        }
        return List.copyOf(list);
    }
}
