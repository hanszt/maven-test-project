package hzt.utils;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@FunctionalInterface
public interface ObjectX<T> extends Supplier<T> {

    static <T> ObjectX<T> of(T t) {
        return () -> t;
    }

    default <R> R let(Function<T, R> mapper) {
        return mapper.apply(get());
    }

    default <R> ObjectX<R> run(Function<T, R> mapper) {
        return ObjectX.of(let(mapper));
    }

    default T also(Consumer<T> block) {
        final T t = get();
        block.accept(t);
        return t;
    }

    default ObjectX<T> apply(Consumer<T> block) {
        return ObjectX.of(also(block));
    }

    default T when(Predicate<T> predicate, Consumer<T> block) {
        T t = get();
        if (predicate.test(t)) {
            block.accept(t);
        }
        return t;
    }

    default ObjectX<T> alsoWhen(Predicate<T> predicate, Consumer<T> block) {
        return ObjectX.of(when(predicate, block));
    }

    default Optional<T> takeIf(Predicate<T> predicate) {
        T t = get();
        return predicate.test(t) ? Optional.ofNullable(t) : Optional.empty();
    }

    default Optional<T> takeUnless(Predicate<T> predicate) {
        T t = get();
        return predicate.test(t) ? Optional.empty() : Optional.ofNullable(t);
    }

}
