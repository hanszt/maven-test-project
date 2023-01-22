package hzt.functional_patterns.continuation_pattern;

import java.util.function.Supplier;

public interface Thunk extends Supplier<Thunk> {
}
