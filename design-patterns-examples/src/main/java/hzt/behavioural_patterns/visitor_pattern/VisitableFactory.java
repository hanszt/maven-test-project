package hzt.behavioural_patterns.visitor_pattern;

import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@FunctionalInterface
public interface VisitableFactory<T> {

    Stream<Function<T, Object>> functions();

    default Visitable makeVisitable(T item) {
        return () -> functions().map(function -> function.apply(item));
    }

    default Visitable makeVisitable(Iterable<T> items) {
        return () -> functions().flatMap(function ->
                StreamSupport.stream(items.spliterator(), false)
                        .map(function));
    }

}
