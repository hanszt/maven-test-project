package hzt.stream.function;

import java.util.function.IntPredicate;

public interface IntPredicateX extends IntPredicate {

    static IntPredicateX by(IntPredicateX predicate) {
        return predicate;
    }

    static IntPredicateX noFilter() {
        return t -> true;
    }

    static IntPredicateX blockingFilter() {
        return t -> false;
    }
}
