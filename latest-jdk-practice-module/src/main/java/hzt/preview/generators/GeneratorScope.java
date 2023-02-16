package hzt.preview.generators;

public interface GeneratorScope<T> {

    void yieldNext(T value);
}
