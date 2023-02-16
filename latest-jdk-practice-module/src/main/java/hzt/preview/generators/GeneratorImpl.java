package hzt.preview.generators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

final class GeneratorImpl<T> implements Generator<T> {

    private static final String CATCH_SPECIFIC_EXCEPTIONS = "java:S2221";
    private final List<GeneratorIterator<T>> iteratorList = new ArrayList<>();
    private final AtomicBoolean closed = new AtomicBoolean(false);
    private final Consumer<GeneratorScope<T>> scopeConsumer;

    GeneratorImpl(Consumer<GeneratorScope<T>> scopeConsumer) {
        this.scopeConsumer = scopeConsumer;
    }

    @Override
    public void close() {
        try {
            final var generatorException = new GeneratorException("Exception while closing generator...");
            for (var iterator : iteratorList) {
                close(generatorException, iterator);
            }
            if (generatorException.getSuppressed().length > 0) {
                throw generatorException;
            }
        } finally {
            closed.set(true);
        }
    }

    private static <T> void close(GeneratorException generatorException, GeneratorIterator<T> iterator) {
        try {
            iterator.close();
        } catch (@SuppressWarnings(CATCH_SPECIFIC_EXCEPTIONS) Exception e) {
            generatorException.addSuppressed(e);
        }
    }

    @Override
    public Iterator<T> iterator() {
        if (closed.get()) {
            throw new IllegalStateException("Generator is already closed");
        }
        final var generatorIterator = new GeneratorIterator<>(scopeConsumer);
        iteratorList.add(generatorIterator);
        return generatorIterator;
    }
}
