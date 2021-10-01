package hzt.stream.predicates;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public final class CollectionPredicates {

    private CollectionPredicates() {
    }

    public static <E> Predicate<Collection<E>> containsAll(Collection<E> other) {
        return collection -> collection != null && other != null && collection.containsAll(other);
    }

    @SafeVarargs
    public static <E> Predicate<Collection<E>> containsAll(E... values) {
        return containsAll(List.of(values));
    }

    public static <E> Predicate<Collection<E>> containsAny(Collection<E> other) {
        return collection -> collection != null && other != null && other.stream().anyMatch(collection::contains);
    }

    @SafeVarargs
    public static <E> Predicate<Collection<E>> containsAny(E... values) {
        return containsAny(List.of(values));
    }

    public static <E> Predicate<Collection<E>> containsNone(Collection<E> other) {
        return collection -> collection != null && other != null && other.stream().noneMatch(collection::contains);
    }

    @SafeVarargs
    public static <E> Predicate<Collection<E>> containsNone(E... values) {
        return containsNone(List.of(values));
    }
}
