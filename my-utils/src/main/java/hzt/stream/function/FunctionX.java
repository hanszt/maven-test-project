package hzt.stream.function;

import java.util.function.Function;

public interface FunctionX<T, R> extends Function<T, R> {

    static <T, R> Function<T, R> apply(Function<T, R> function) {
        return function;
    }
}
