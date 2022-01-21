package hzt.collections;

public interface CollectionX<T> extends IterableX<T> {

    boolean isNotEmpty();

    boolean containsNot(T t);
}
