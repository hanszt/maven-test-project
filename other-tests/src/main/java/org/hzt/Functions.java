package org.hzt;

import java.util.HashMap;
import java.util.Map;
import java.util.function.IntFunction;

public final class Functions {

    private Functions() {
    }

    @FunctionalInterface
    interface MemIntFunction<R> extends IntFunction<R>, Memoizable {

        static <R> MemIntFunction<R> of(IntFunction<R> function) {
            return function::apply;
        }

        default MemIntFunction<R> memoized() {
            if (isMemoized()) {
                return this;
            }
            final Map<Integer, R> cache = new HashMap<>();
            return (MemIntFunction<R> & Memoized) item -> {
                final var result = cache.get(item);
                if (result != null) {
                    return result;
                }
                final R value = apply(item);
                cache.put(item, value);
                return value;
            };
        }
    }

    interface Memoizable {

        Memoizable memoized();

        default boolean isMemoized() {
            return this instanceof Memoized;
        }
    }

    public interface Memoized {
    }
}
