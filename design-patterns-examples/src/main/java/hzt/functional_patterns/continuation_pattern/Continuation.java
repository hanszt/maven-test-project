package hzt.functional_patterns.continuation_pattern;

import java.util.function.Function;

/**
 * @see <a href="https://blog.marcinchwedczuk.pl/continuations-in-java">Continuations in Java</a>
 */
public interface Continuation<T> extends Function<T, Thunk> {

    @Override
    Thunk apply(T t);
}
