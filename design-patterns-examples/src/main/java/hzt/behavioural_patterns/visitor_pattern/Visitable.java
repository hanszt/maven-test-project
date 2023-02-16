package hzt.behavioural_patterns.visitor_pattern;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

@FunctionalInterface
public interface Visitable {

    @SafeVarargs
    static <T> VisitableFactory<T> visiting(Function<T, Object>... functions) {
        return () -> Arrays.stream(functions);
    }

    Stream<Object> objectsToVisit();

    default <R> Stream<R> stream(Visitor<R> visitor) {
        return objectsToVisit()
                .map(visitor::visit)
                .filter(Objects::nonNull);
    }
}
