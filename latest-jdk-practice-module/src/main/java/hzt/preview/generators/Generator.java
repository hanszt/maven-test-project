package hzt.preview.generators;

import org.hzt.utils.sequences.Sequence;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface Generator<T> extends AutoCloseable {

    Iterator<T> iterator();

    void close();

    static <T> GeneratorBuilder<T> from(Consumer<GeneratorScope<T>> scopeConsumer) {
        return () -> new GeneratorImpl<>(scopeConsumer);
    }

    @FunctionalInterface
    interface GeneratorBuilder<T> {

        Generator<T> generator();

        default <R> R useAsSequence(Function<Sequence<T>, R> mapper) {
            try (final var generator = generator()) {
                return mapper.apply(Sequence.of(generator::iterator));
            }
        }

        default void consumeAsSequence(Consumer<Sequence<T>> consumer) {
            try (final var generator = generator()) {
                consumer.accept(Sequence.of(generator::iterator));
            }
        }

        default <R> R useAsStream(Function<Stream<T>, R> mapper) {
            try (final var generator = generator()) {
                return mapper.apply(StreamSupport
                        .stream(() -> spliterator(generator), 0, false));
            }
        }

        default void consumeAsStream(Consumer<Stream<T>> consumer) {
            try (final var generator = generator()) {
                consumer.accept(StreamSupport
                        .stream(() -> spliterator(generator), 0, false));
            }
        }

        private static <T> Spliterator<T> spliterator(Generator<T> generator) {
            return Spliterators.spliteratorUnknownSize(generator.iterator(), 0);
        }

    }
}
