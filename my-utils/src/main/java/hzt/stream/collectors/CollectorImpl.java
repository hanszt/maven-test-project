package hzt.stream.collectors;

import java.util.Collections;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Simple implementation class for {@code Collector}.
 *
 * @param <T> the type of elements to be collected
 * @param <R> the type of the result
 */
record CollectorImpl<T, A, R>(Supplier<A> supplier,
                              BiConsumer<A, T> accumulator,
                              BinaryOperator<A> combiner,
                              Function<A, R> finisher,
                              Set<Characteristics> characteristics) implements Collector<T, A, R> {
    CollectorImpl {
        characteristics = Collections.unmodifiableSet(characteristics);
    }
}
