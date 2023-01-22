package hzt.functional_patterns.continuation_pattern;

import java.util.function.Function;

public interface Continuation<T> extends Function<T, Thunk> {
}
