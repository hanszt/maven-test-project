package hzt.utils;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@FunctionalInterface
public interface ObjectX<T> extends Supplier<T> {

    default <R> R let(Function<T, R> mapper) {
        return mapper.apply(get());
    }

    default T also(Consumer<T> block) {
        final var t = get();
        block.accept(t);
        return t;
    }

    default T when(Predicate<T> predicate, Consumer<T> block) {
        T t = get();
        if (predicate.test(t)) {
            block.accept(t);
        }
        return t;
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
