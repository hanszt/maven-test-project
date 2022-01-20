package hzt.stream.function;

import java.util.function.Consumer;

public interface ConsumerX<T> extends Consumer<T> {

    static <T> ConsumerX<T> of(ConsumerX<T> consumer) {
        return consumer;
    }
}
