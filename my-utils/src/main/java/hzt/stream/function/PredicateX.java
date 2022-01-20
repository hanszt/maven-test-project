package hzt.stream.function;

import java.util.function.Predicate;

public interface PredicateX<T> extends Predicate<T> {

    static <T> PredicateX<T> by(PredicateX<T> predicate) {
        return predicate;
    }

    static <T> PredicateX<T> noFilter() {
        return t -> true;
    }

    static <T> PredicateX<T> blockingFilter() {
        return t -> false;
    }
}
