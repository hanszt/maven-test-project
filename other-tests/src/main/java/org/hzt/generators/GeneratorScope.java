package org.hzt.generators;

public interface GeneratorScope<T> {

    void yieldNext(T value);
}
