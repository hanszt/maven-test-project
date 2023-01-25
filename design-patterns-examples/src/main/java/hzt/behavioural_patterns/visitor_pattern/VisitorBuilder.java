package hzt.behavioural_patterns.visitor_pattern;

import java.util.function.Function;

@FunctionalInterface
public interface VisitorBuilder<R> {

    void register(Class<?> type, Function<Object, R> function);
}
