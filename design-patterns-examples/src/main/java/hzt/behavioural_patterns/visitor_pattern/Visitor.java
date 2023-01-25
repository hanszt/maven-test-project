package hzt.behavioural_patterns.visitor_pattern;

/**
 * This implementation of the visitor pattern was inspired by this video:
 *
 * @see <a href="https://www.youtube.com/watch?v=gq23w9nycBs">
 * Designing functional and fluent API: example of the Visitor Pattern by José Paumard</a>
 */
@FunctionalInterface
public interface Visitor<R> {

    R visit(Object object);

    static <R> VisitorInitializer.ChainingInitializer<R> ofType(Class<R> type) {
        return builder -> builder.register(type, type::cast);
    }

}
