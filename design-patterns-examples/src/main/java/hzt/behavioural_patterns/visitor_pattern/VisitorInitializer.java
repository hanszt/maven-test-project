package hzt.behavioural_patterns.visitor_pattern;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface VisitorInitializer<R> {

    void build(VisitorBuilder<R> visitorBuilder);

    default Visitor<R> build() {
        Map<Class<?>, Function<Object, ? extends R>> registry = new HashMap<>();
        build(registry::put);
        return object -> registry.getOrDefault(object != null ? object.getClass() : null, o -> {
            final var type = o != null ? o.getClass().getSimpleName() : null;
            throw new IllegalArgumentException("No action registered for type: " + type);
        }).apply(object);
    }

    @FunctionalInterface
    interface ChainingInitializer<R> extends VisitorInitializer<R> {

        default ChainingInitializer<R> andThen(VisitorInitializer<R> after) {
            Objects.requireNonNull(after);
            return visitorBuilder -> {
                build(visitorBuilder);
                after.build(visitorBuilder);
            };
        }

        default <T> Chainer<T, R> whenVisitingType(Class<T> type) {
            return new Chainer<>() {
                @Override
                public Class<T> type() {
                    return type;
                }

                @Override
                public ChainingInitializer<R> previous() {
                    return ChainingInitializer.this;
                }
            };
        }
    }

    interface Chainer<T, R> {

        Class<T> type();
        ChainingInitializer<R> previous();

        default ChainingInitializer<R> then(Function<? super T, R> function) {
            final var type = type();
            return previous().andThen(builder -> builder.register(type, function.compose(type::cast)));
        }
    }
}
